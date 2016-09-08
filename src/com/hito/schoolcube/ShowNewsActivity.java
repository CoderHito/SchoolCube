package com.hito.schoolcube;

import java.util.HashMap;
import java.util.Map;

import com.hito.schoolcube.R;
import com.hito.schoolcube.api.API;
import com.hito.schoolcube.entity.News;
import com.hito.schoolcube.utils.BaseOperate.AsyncRequestCallBack;
import com.hito.schoolcube.utils.NewsOperate;
import com.hito.schoolcube.view.myWebView;
import com.hito.schoolcube.view.myWebView.onScrollChangedCallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowNewsActivity extends Activity implements OnClickListener {
	protected static final String TAG = "ShowNewsActivity";
	private myWebView wv_content;
	private ImageButton ibtn_back;
	private ImageView iv_icon;
	private TextView tv_title;
	private TextView tv_time;
	private int newsId;
	private NewsOperate newsOperate;
	private RelativeLayout rl_desc;
	boolean isShow = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_news);
		Intent intent = getIntent();
		newsId = intent.getIntExtra("newsId", -1);
		initView();
		initData();

	}

	private void initData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(newsId));
		// getNewsByid
		newsOperate.asyncRequest(params, API.API10007,
				new AsyncRequestCallBack() {
					@Override
					public void callBack() {
						News n = newsOperate.getNews();
						if (n == null)
							return;
						tv_title.setText(n.getTitle());
						tv_time.setText(n.getCreateTime());
						wv_content.loadUrl(API.API10008 + "?id=" + n.getId());
					}
				});
	}

	public void initView() {
		rl_desc = (RelativeLayout) findViewById(R.id.rl_desc);
		ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
		wv_content = (myWebView) findViewById(R.id.wv_content);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		newsOperate = new NewsOperate();

		ibtn_back.setOnClickListener(this);
		wv_content.setOnScrollChangedCallBack(new onScrollChangedCallBack() {

			@Override
			public void callBack() {
				rl_desc.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_back:
			finish();
			break;

		default:
			break;
		}
	}
}
