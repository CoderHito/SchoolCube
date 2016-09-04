package com.hito.schoolcube;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hito.schoolcube.api.API;
import com.hito.schoolcube.utils.DialogUtils;
import com.hito.schoolcube.utils.PostUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private EditText et_account;
	private EditText et_pwd;
	private Button btn_register;
	private Button btn_back_to_login;
	private CheckBox cb_remreber;
	private String account;
	private PostUtils postUtils;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int s = postUtils.getS();
			if (s == -1) {
				DialogUtils.showToastShort(RegisterActivity.this, "该号码已被注册");
			} else if (s == 1) {
				Intent intent = new Intent(RegisterActivity.this,
						RegisterSendSMSActivity.class);
				String activateCode = postUtils.getActivateCode();
				intent.putExtra("account", account);
				intent.putExtra("pwd", pwd);
				intent.putExtra("activateCode", activateCode);
				Toast.makeText(RegisterActivity.this, "验证码是" + activateCode, 0)
						.show();
				startActivity(intent);
			}
		};
	};
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		postUtils = new PostUtils();
		cb_remreber = (CheckBox) findViewById(R.id.cb_remreber);
		btn_back_to_login = (Button) findViewById(R.id.btn_back_to_login);
		et_account = (EditText) findViewById(R.id.et_account);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		btn_register = (Button) findViewById(R.id.btn_register);

		btn_back_to_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_to_login:
			finish();
			break;
		case R.id.btn_register:
			account = et_account.getText().toString();
			pwd = et_pwd.getText().toString();

			if (cb_remreber.isChecked()) {
				new Thread() {
					public void run() {
						Map<String, String> pairsMap = new HashMap<>();
						pairsMap.put("phone", account);
						postUtils.request2(pairsMap, API.API10002);
						handler.sendEmptyMessage(0);
					};
				}.start();
			}

			break;
		default:
			break;
		}
	}
}
