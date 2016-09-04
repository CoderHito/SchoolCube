package com.hito.schoolcube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 显示Logo,延迟两秒进入主页面，判断是否更新应用
 * 
 * @author Hito
 * 
 */
public class SplashActivity extends Activity {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 2000);
	}

}
