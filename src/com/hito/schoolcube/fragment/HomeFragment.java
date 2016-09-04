package com.hito.schoolcube.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hito.schoolcube.R;
import com.hito.schoolcube.api.API;
import com.hito.schoolcube.entity.News;
import com.hito.schoolcube.utils.BaseOperate.AsyncRequestCallBack;
import com.hito.schoolcube.utils.NewsOperate;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class HomeFragment extends Fragment implements OnClickListener {
	protected static final String TAG = "HomeFragment";
	private Button btn_club_info;
	private Button btn_school_info;
	private ListView listView;
	private NewsOperate newsOperate;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		initView(view);
		initNews();

		return view;
	}

	private void initNews() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "1");
		newsOperate.asyncRequest(params, API.API10006,
				new AsyncRequestCallBack() {

					@Override
					public void callBack() {
						List<News> newss = newsOperate.getNs();
						MyAdapter adapter = new MyAdapter(newss);
						listView.setAdapter(adapter);
					}
				});
	}

	public void initView(View view) {
		newsOperate = new NewsOperate();
		listView = (ListView) view.findViewById(R.id.fragment_home_list);
		btn_club_info = (Button) view.findViewById(R.id.btn_club_info);
		btn_school_info = (Button) view.findViewById(R.id.btn_school_info);

		btn_club_info.setOnClickListener(this);
		btn_school_info.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_club_info:
			btn_club_info
					.setBackgroundResource(R.drawable.bottom_border_style_checked);
			btn_school_info
					.setBackgroundResource(R.drawable.bottom_border_style);
			break;
		case R.id.btn_school_info:
			btn_club_info.setBackgroundResource(R.drawable.bottom_border_style);
			btn_school_info
					.setBackgroundResource(R.drawable.bottom_border_style_checked);
			break;
		}
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_desc;
	}

	class MyAdapter extends BaseAdapter {

		private List<News> data;

		public MyAdapter(List<News> newss) {
			this.data = newss;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.homefragment_news,
						null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_news);
				holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
				holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
				view.setTag(holder);
			}
			final News news = data.get(position);
			holder.tv_title.setText(news.getTitle());
			holder.tv_desc.setText(news.getContent());

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("newsId", news.getId());
					intent.setClass(getActivity(), ShowNewsActivity.class);
					startActivity(intent);
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
