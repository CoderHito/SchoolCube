package com.hito.schoolcube.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import android.os.Environment;

/**
 * �ļ�������
 * 
 * @author hito
 * 
 */
public class FileManager {

	private static FileManager manager;

	/**
	 * ��Ч����ʱ����3��
	 */
	private static final long maxCacheTime = 1000 * 60 * 60 * 24 * 15;

	/**
	 * ���캯��
	 * 
	 * @param context
	 */
	public FileManager() {
	}

	/**
	 * ��������ȡFileManagerʵ��
	 * 
	 * @param context
	 * @return
	 */
	public static FileManager getInitialize() {
		if (manager == null)
			manager = new FileManager();
		return manager;
	}

	/**
	 * ��ȡ����cacheĿ¼·��
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public String getCacheDir(Context context, String path) {
		StringBuffer dir = new StringBuffer();
		dir.append(context.getFilesDir().getAbsolutePath());
		if (!path.startsWith("/"))
			dir.append("/");
		dir.append(path);
		File file = new File(dir.toString());
		if (!file.exists())
			file.mkdirs();
		return dir.toString();
	}

	/**
	 * ��ȡsd������·��
	 * 
	 * @param path
	 *            ·��
	 * @return ��sd���ķ���sd�ϵ�·�� û��sd������null
	 */
	public String getSDDir(String path) {
		if (!isSdCardAvailable())
			return null;
		StringBuffer dir = new StringBuffer();
		dir.append(Environment.getExternalStorageDirectory());
		if (!path.startsWith("/"))
			dir.append("/");
		dir.append(path);
		File file = new File(dir.toString());
		if (!file.exists())
			file.mkdirs();
		return dir.toString();
	}

	/**
	 * ��ȡ�����ļ���·��
	 * 
	 * @param path
	 *            �����ļ���·��
	 * @param url
	 *            ������ļ���url
	 * @return
	 */
	public String getCacheFileUrl(Context context, String path, String url) {
		return getSDOrCache(context, path) + formatPath(url);
	}

	/**
	 * ��ȡ����·��
	 * 
	 * @param context
	 *            ������
	 * @param path
	 *            ·��
	 * @return ��sd������sd�ϵ�·�� û��sd������cach�е�·��
	 */
	public String getSDOrCache(Context context, String path) {
		String url = null;
		if (isSdCardAvailable())
			url = getSDDir(path);
		else
			url = getCacheDir(context, path);
		return url;
	}

	/**
	 * ����ڴ濨�Ƿ����
	 * 
	 * @return �����򷵻� true
	 */
	public boolean isSdCardAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * ��ʽ��url��ַ �Ա����url�������ļ�
	 * 
	 * @param name
	 *            ͼƬurl
	 * @return
	 */
	public String formatPath(String name) {
		String path = name;
		path = path.replace(":", "_");
		path = path.replace("/", "_");
		return path;
	}

	/**
	 * �жϸ�·�����ļ��Ƿ����
	 * 
	 * @param path
	 *            �ļ�·������ȫ·����
	 * @return
	 */
	public boolean isExists(String path) {
		File file = new File(path);
		if (file.exists())
			return true;
		return false;
	}

	/**
	 * �жϵ�ǰĿ¼�Ƿ�����ļ�
	 * 
	 * @param path
	 * @return
	 */
	public boolean hasMoreFile(String path) {
		File file = new File(path);
		if (!file.exists())
			return false;
		String[] files = file.list();
		if (files == null || files.length == 0)
			return false;
		return true;
	}

	/**
	 * ���sd���й��ڵĻ���
	 * 
	 * @param basePath
	 *            ·��(Ҫ����ļ���·��)
	 */
	public void cleanInvalidCache(String basePath) {
		File file = new File(basePath);
		if (!file.exists())
			return;
		File[] caches = file.listFiles();
		if (caches == null || caches.length == 0)
			return;
		long now = System.currentTimeMillis();
		try {
			for (int i = 0; i < caches.length; i++) {
				if ((now - caches[i].lastModified()) < maxCacheTime) {
					continue;
				}
				caches[i].delete();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					// ÿɾ��һ��ͼƬ��ͣ��10���룬��ֹcpuռ���ʹ��ߣ���ɳ�������Ӧ
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���汣��������
	 * 
	 * @param path
	 *            Ҫ�������ȫ·��
	 * @param is
	 *            �����ȡ��inputstream��
	 */
	public void saveInputStream(String path, InputStream is) {
		if (!isExists(path)) {
			File file = new File(path);
			FileOutputStream fos = null;
			try {
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
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ���ļ��ж�ȡ��
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return
	 */
	public InputStream loadInputStream(String path) {
		InputStream is = null;
		if (isExists(path)) {
			File file = new File(path);
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return is;
	}

	/**
	 * �Ѷ���д���ļ�
	 * 
	 * @throws Exception
	 */
	public void WriteObject(Object obj, Context context, String path)
			throws Exception {
		FileOutputStream fis = context.openFileOutput(path,
				Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fis);
		oos.writeObject(obj);
		oos.close();
		fis.close();
	}

	/**
	 * ���ļ���ȡ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object readObject(Context context, String path) throws Exception {
		Object obj = new Object();
		FileInputStream fis = context.openFileInput(path);
		ObjectInputStream oos = new ObjectInputStream(fis);
		obj = oos.readObject();
		oos.close();
		fis.close();
		return obj;
	}

	/**
	 * ɾ�������ļ�
	 * 
	 * @param path
	 *            �ļ�������·��
	 */
	public void delete(String path) {
		File file = new File(path);
		if (file.exists())
			file.delete();
	}
}
