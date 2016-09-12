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
	 * һ�������Ĺ��캯�����ڴ�����ʵ������ʱ��ʹ�õ�
	 * 
	 * @param context
	 */
	public myViewPager(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	/**
	 * ���������Ĺ��캯���� view���ص�ʱ��ʹ�õ�
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
	 * ���������Ĺ��캯���� �����Զ�����ʽ��ʱ��ʹ��
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
	 * ����ͼƬ
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
	 * ��ʼ��imageLoder
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
			// ����ע����LinearLayuot���ؼ��ĸ�����ʲô�͵���˭�İ�
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

			// ��ѡ����ĳ��ҳ���ʱ��ص�
			// position ѡ���ҳ��
			@Override
			public void onPageSelected(int position) {
				int myindex = position % imageViews.size();
				String msg = desc[myindex];
				tv_title.setText(msg);
				// ��ǰָʾ�����ø���
				ll_point.getChildAt(myindex).setEnabled(true);
				// ��һ�ε�ָʾ��
				ll_point.getChildAt(lastPosition).setEnabled(false);

				lastPosition = myindex;
			}

			// ��ҳ������˻ص�
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			// ��ҳ��״̬�����仯��ʱ��ص�
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		// ����������
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * instantiate��ʵ����
		 * 
		 * ��baseAdapter��getView����
		 * 
		 * container:viewPager
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// �õ�ͼƬ
			ImageView imageView = imageViews.get(position % imageViews.size());
			// ��ӵ�������
			container.addView(imageView);
			// ����ʵ�����������ֱ�ӷ���ʵ��
			return imageView;
		}

		/**
		 * view:��ʾ�е�ĳһ��ҳ��
		 * 
		 * object:����instantiateItem�ķ���ֵ
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
		 * ����ĳ������ �տ�ʼһ������������ҳ�棬�������
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			// ���� һ��view�������ù�ϵ�Ƴ�
			container.removeView((View) object);
			// super.destroyItem(container, position, object);
		}

	}

}
