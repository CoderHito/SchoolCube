package com.hito.schoolcube.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * �첽����ͼƬ
 * 
 * @author hito
 * 
 */
public class AsyncImageLoader {
	// ����߳���
	private static final int THREAD_COUNT = 40;
	// �첽����ͼƬ��
	private static AsyncImageLoader asyncImageLoader = null;
	// �̳߳�
	private ExecutorService mService = null;

	private AsyncImageLoader() {
		mService = Executors.newFixedThreadPool(THREAD_COUNT);
	}

	/**
	 * ��ʼ��AsyncImageLoader
	 * 
	 * @return
	 */
	public static AsyncImageLoader getInstance() {
		if (asyncImageLoader == null) {
			asyncImageLoader = new AsyncImageLoader();
		}
		return asyncImageLoader;
	}

	/**
	 * ͼƬ���������û���
	 */
	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

	/**
	 * ����ͼƬ
	 * 
	 * @param context
	 *            ������
	 * @param imageUrl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�Ļ���·��
	 * @param callback
	 *            �ص�����
	 * @return
	 */
	public Drawable loadDrawable(final Context context, final String imageUrl,
			final String path, final ImageCallback callback) {
		return loadDrawable(context, imageUrl, path, false, callback);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param context
	 *            ������
	 * @param imageUrl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�Ļ���·��
	 * @param isUseCache
	 *            �Ƿ񻺴浽�ڴ�
	 * @param callback
	 *            �ص�����
	 * @return
	 */
	@SuppressLint("HandlerLeak")
	public Drawable loadDrawable(final Context context, final String imageUrl,
			final String path, final boolean isUseCache,
			final ImageCallback callback) {
		if (StringUtils.isBlank(imageUrl) || !imageUrl.startsWith("http"))
			return null;
		if (isUseCache && imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				callback.imageLoaded(softReference.get(), imageUrl);
				return softReference.get();
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null)
					callback.imageLoaded((Drawable) msg.obj, imageUrl);
			}
		};
		mService.submit(new Runnable() {
			public void run() {
				Drawable drawable = getDrawableFromCache(context, imageUrl,
						path);
				if (drawable == null) {
					try {
						saveInputStream(context, imageUrl, path);
						drawable = getDrawableFromCache(context, imageUrl, path);
						if (drawable == null) {
							// һ�������
							saveInputStream(context, imageUrl, path);
							drawable = getDrawableFromCache(context, imageUrl,
									path);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						drawable = null;
					}
				}
				if (isUseCache && drawable != null) {
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
				}
				handler.sendMessage(handler.obtainMessage(0, drawable));
				if (drawable != null)
					drawable = null;
			};
		});
		return null;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param context
	 *            ������
	 * @param imageUrl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�Ļ���·��
	 * @param isUseCache
	 *            �Ƿ񻺴浽�ڴ�
	 * @param callback
	 *            �ص�����
	 * @return bitmap��ʽ�ĈDƬ
	 */
	@SuppressLint("HandlerLeak")
	public Bitmap loadBitmap(final Context context, final String imageUrl,
			final String path, final ImageCallback callback) {
		if (StringUtils.isBlank(imageUrl) || !imageUrl.startsWith("http"))
			return null;
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null)
					callback.imageLoaded((Drawable) msg.obj, imageUrl);
			}
		};
		mService.submit(new Runnable() {
			public void run() {
				Bitmap bitmap = getBitmapFromCache(context, imageUrl, path);
				if (bitmap == null) {
					try {
						saveInputStream(context, imageUrl, path);
						bitmap = getBitmapFromCache(context, imageUrl, path);
						if (bitmap == null) {
							// һ�������
							Thread.sleep(500);
							saveInputStream(context, imageUrl, path);
							bitmap = getBitmapFromCache(context, imageUrl, path);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						bitmap = null;
					}
					// �������ͼƬʧ�ܣ�������һ��
					catch (InterruptedException e) {
						e.printStackTrace();
						bitmap = null;
					}
				}
				handler.sendMessage(handler.obtainMessage(0, bitmap));
				if (bitmap != null)
					bitmap = null;
			};
		});
		return null;
	}

	/**
	 * ���汣��������
	 * 
	 * @param context
	 *            ������
	 * @param imageUrl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�Ĵ洢·��
	 * @throws IOException
	 *             io�쳣
	 */
	public void saveInputStream(Context context, String imageUrl, String path)
			throws IOException {
		InputStream is = null;
		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		File file = new File(path);
		if (!file.exists()) {
			try {
				conn = (HttpURLConnection) new URL(imageUrl).openConnection();
				conn.setConnectTimeout(20000);
				conn.setReadTimeout(20000);
				conn.setRequestMethod("GET");
				conn.connect();
				is = conn.getInputStream();
				fos = new FileOutputStream(file);
				int size = 0;
				byte[] buffer = new byte[1024];
				while ((size = is.read(buffer)) != -1)
					fos.write(buffer, 0, size);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
						fos = null;
					}
					if (file != null)
						file = null;
					if (conn != null)
						conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �ӻ����л�ȡͼƬ
	 * 
	 * @param context
	 *            ������
	 * @param imageurl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�洢��·��
	 * @return
	 */
	public Drawable getDrawableFromCache(Context context, String imageurl,
			String path) {
		if (imageurl == null || imageurl.equals(""))
			return null;
		File file = new File(path);
		if (!file.exists())
			return null;
		try {
			file.setLastModified(System.currentTimeMillis());
			return (Drawable) (new BitmapDrawable(getBitmapFromCache(context,
					imageurl, path)));// Drawable.createFromPath(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ
	 * 
	 * @param context
	 *            ������
	 * @param imageurl
	 *            ͼƬ��url��ַ
	 * @param path
	 *            ͼƬ�洢��·��
	 * @return
	 */
	public Bitmap getBitmapFromCache(Context context, String imageurl,
			String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		int size = 1;
		int width = opt.outWidth;
		int height = opt.outHeight;
		while (width / 2 > getWidth(context) && height / 2 > getHeight(context)) {
			width /= 2;
			height /= 2;
			size *= 2;
		}
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = size;
		opt.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists())
			bitmap = BitmapFactory.decodeFile(path, opt);
		else
			bitmap = null;
		return bitmap;
	}

	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

	/**
	 * ��ȡ��Ļ�Ŀ�
	 * 
	 * @param context
	 * @return
	 */
	private int getWidth(Context context) {
		if (mScreenWidth > 0)
			return mScreenWidth;
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService("window");
		windowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		return dm.widthPixels;
	}

	/**
	 * ��ȡ��Ļ�ĸ�
	 * 
	 * @param context
	 * @return
	 */
	private int getHeight(Context context) {
		if (mScreenHeight > 0)
			return mScreenHeight;
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService("window");
		windowManager.getDefaultDisplay().getMetrics(dm);
		mScreenHeight = dm.heightPixels;
		return mScreenHeight;
	}

	/**
	 * ͼƬ������ɺ�ִ�еĲ��� ��Ҫ��дiamgeLoaded(Drawable imageDrawable,String imageUrl)����
	 * 
	 * @author hito
	 * 
	 */
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
