package com.hito.schoolcube.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hito.schoolcube.R;
import com.hito.schoolcube.api.API;
import com.hito.schoolcube.entity.Image;
import com.hito.schoolcube.utils.BaseOperate.AsyncRequestCallBack;
import com.hito.schoolcube.utils.NewsOperate;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class SlideShowView extends FrameLayout {

	private Context contex;
	private NewsOperate newsOperate = new NewsOperate();
	private List<Image> images;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	/**
	 * ���ͼƬ��Դ��list
	 */
	private List<String> imageUrls = new ArrayList<>();
	/**
	 * �ֲ�ͼƬ
	 */
	private List<ImageView> imageViewList;
	/**
	 * Բ��
	 */
	private List<View> dotViewList;
	private ViewPager viewPager;

	/**
	 * ��ʱ����
	 */
	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem;
	// �Զ��ֲ����ÿ���
	private final static boolean isAutoPlay = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);
		};
	};

	public SlideShowView(Context context) {
		this(context,null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.contex = context;
		System.out.println("SlideShowView -----------����������");
		// 1.��ʼ��ImageLoader
		initImageLoader(context);
		// 2.
		initData();
		if (isAutoPlay) {
			startPlay();
		}
	}

	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��һ������ Runnable�ӿڵ�command,�Ƴ�1�룬���4��
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	class SlideShowTask implements Runnable {
		// 4
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	private void initData() {
		System.out.println("initData---");
		imageViewList = new ArrayList<ImageView>();
		dotViewList = new ArrayList<View>();

		downlaodAds();
	}

	/**
	 * �����ֲ�ͼƬ
	 */
	private void downlaodAds() {
		System.out.println("download images");
		Map<String, String> params = new HashMap<>();
		newsOperate.asyncRequest(params, API.API10004,
				new AsyncRequestCallBack() {

					@Override
					public void callBack() {
						images = newsOperate.getImagePath();
						if (images == null) {
							return;
						}
						new GetListTask().execute("");
					}
				});
	}

	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				for (Image temp : images) {
					imageUrls.add(API.API10005 + "?imgUrl=" + temp.getPath());
				}
				System.out.println("111");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			System.out.println("222");
			if (result) {
				initUI(contex);
			}
		}

	}

	public void initUI(Context context) {
		if (imageUrls == null || imageUrls.size() == 0) {
			return;
		}
		LayoutInflater.from(context).inflate(R.layout.activity_slide_show_view,
				this, true);
		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();

		for (int i = 0; i < imageUrls.size(); i++) {
			ImageView view = new ImageView(context);
			view.setTag(imageUrls.get(i));
			if (i == 0) {
				view.setBackgroundResource(R.drawable.temp_inset);
			}
			// ������������ͼƬ��Ŀ���ǰ�ͼƬ��������View��
			view.setScaleType(ScaleType.FIT_XY);
			imageViewList.add(view);

			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewList.add(dotView);
		}
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 1:// ���ƻ�����������
				isAutoPlay = false;
				break;
			case 2:// �����л���
				isAutoPlay = true;
				break;
			case 0:// �������������л���ϻ��߼������
					// ��ǰΪ���һ�ţ���ʱ�������󻬣����л�����һ��
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// ��ǰΪ��һ�ţ���ʱ�������һ������л������һ��
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub

			currentItem = pos;
			for (int i = 0; i < dotViewList.size(); i++) {
				if (i == pos) {
					((View) dotViewList.get(pos))
							.setBackgroundResource(R.drawable.dot_focus);
				} else {
					((View) dotViewList.get(i))
							.setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}

	}

	/**
	 * ViewPager��������
	 * 
	 * @author Hito
	 * 
	 */
	class MyPagerAdapter extends PagerAdapter {

		// ����һ��Item--�Ƴ�
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// ((ViewPag.er)container).removeView((View)object);
			((ViewPager) container).removeView(imageViewList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView imageView = imageViewList.get(position);

			imageLoader.displayImage(imageView.getTag() + "", imageView);

			((ViewPager) container).addView(imageViewList.get(position));
			return imageViewList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * ImageLoader�����ʼ��
	 * 
	 * @param context
	 */
	private void initImageLoader(Context context) {
		System.out.println("ImageLoader ��ʼ��");
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove
				// for
				// release
				// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
