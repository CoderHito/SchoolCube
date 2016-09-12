package com.hito.schoolcube.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class myViewPager extends FrameLayout {
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private NewsOperate newsOperate;
	private ViewPager viewPager;
	private TextView tv_title;
	private LinearLayout ll_point;

	private int ids[] = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e };
	private String desc[] = { "asdasdsahkoiuytrqw123",
			"6786786sdsahkoiuytrqw123", "123123eqasdasdahkoiuytrqw123",
			"345657689asdsahkoiuytrqw123", "789345890456890dsahkoiuytrqw123" };
	private List<ImageView> imageViews;

	private int lastPosition;
	private boolean isRunning;
	private ArrayList<ImageView> imageViewsList;
	private ArrayList<View> dotViewsList;
	private List<Image> imgs;
	private List<String> imgUrls;
	private List<ImageView> downloadImageViews;

	private Context context;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			if (isRunning) {
				handler.sendEmptyMessageDelayed(0, 2000);
			}
		};
	};

	/**
	 * 一个参数的构造函数是在代码中实例化的时候使用的
	 * 
	 * @param context
	 */
	public myViewPager(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	/**
	 * 两个参数的构造函数是 view加载的时候使用的
	 * 
	 * @param context
	 * @param attrs
	 */
	public myViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	/**
	 * 三个参数的构造函数是 设置自定义样式的时候使用
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public myViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		downloadImageViews = new ArrayList<>();
		imgUrls = new ArrayList<>();
//		downloadImage();
		View view = View.inflate(getContext(), R.layout.myviewpager, this);
		initImageLoader(context);

		initView(view);

		isRunning = true;
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	/**
	 * 下载图片
	 */
	private void downloadImage() {
		Map<String, String> paris = new HashMap<>();
		newsOperate.asyncRequest(paris, API.API10004,
				new AsyncRequestCallBack() {

					@Override
					public void callBack() {
						imgs = newsOperate.getImagePath();
						if (imgs == null) {
							return;
						}
						new GetListTask();
					}
				});
	}

	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			for (Image temp : imgs) {
				imgUrls.add(API.API10005 + "?imgUrl=" + temp.getPath());
			}
			return true;
		}

	}

	/**
	 * 初始化imageLoder
	 * 
	 * @param context
	 */
	private void initImageLoader(Context context) {
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

	@SuppressWarnings("deprecation")
	public void initView(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		ll_point = (LinearLayout) view.findViewById(R.id.ll_point);

		imageViews = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(ids[i]);
			imageViews.add(imageView);
			ImageView point = new ImageView(context);
			point.setBackgroundResource(R.drawable.point_selector);
			// 导包注意是LinearLayuot，控件的父类是什么就导入谁的包
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 15;
			point.setLayoutParams(params);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			ll_point.addView(point);
		}

		viewPager.setAdapter(new MyPagerAdapter());
		tv_title.setText(desc[0]);
		int index = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% imageViews.size();
		viewPager.setCurrentItem(index);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// 当选择了某个页面的时候回调
			// position 选择的页面
			@Override
			public void onPageSelected(int position) {
				int myindex = position % imageViews.size();
				String msg = desc[myindex];
				tv_title.setText(msg);
				// 当前指示点设置高亮
				ll_point.getChildAt(myindex).setEnabled(true);
				// 上一次的指示点
				ll_point.getChildAt(lastPosition).setEnabled(false);

				lastPosition = myindex;
			}

			// 当页面滚动了回调
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			// 当页面状态发生变化的时候回调
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		// 返回总条数
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * instantiate：实例化
		 * 
		 * 和baseAdapter的getView类似
		 * 
		 * container:viewPager
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 得到图片
			ImageView imageView = imageViews.get(position % imageViews.size());
			// 添加到容器中
			container.addView(imageView);
			// 返回实例化对象，最好直接返回实例
			return imageView;
		}

		/**
		 * view:显示中的某一个页面
		 * 
		 * object:上面instantiateItem的返回值
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// if (view == object) {
			// return true;
			// } else {
			// return false;
			// }
			return (view == object);
		}

		/**
		 * 销毁某条数据 刚开始一进来创建两个页面，最多三个
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			// 销毁 一个view，将对用关系移除
			container.removeView((View) object);
			// super.destroyItem(container, position, object);
		}

	}

}
