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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hito.schoolcube.api.API;
import com.hito.schoolcube.operate.UserOperate;
import com.hito.schoolcube.operate.BaseOperate.AsyncRequestCallBack;
import com.hito.schoolcube.utils.App;
import com.hito.schoolcube.utils.ClientStoreUtil;
import com.hito.schoolcube.utils.DialogUtils;
import com.hito.schoolcube.utils.PostUtils;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText et_account;
	private EditText et_pwd;
	private TextView tv_register;
	private CheckBox cb_remreber;
	private Button btn_login;
	private SharedPreferences sp;
	private String username;
	private String pwd;
	private UserOperate userOperate;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			int s = userOperate.getS();
			if (s == 1) {
				DialogUtils.showToastShort(LoginActivity.this, "登录成功");
				if (cb_remreber.isChecked()) {
					App app = (App) getApplication();
					userOperate.getUser().setPwd(pwd);
					userOperate.getUser().setMobile(username);
					// 保存用户到sharedpreference
					ClientStoreUtil.setUser(getApplicationContext(),
							userOperate.getUser());
					// Editor editor = sp.edit();
					// editor.putString("username", username);
					// editor.putString("pwd", pwd);
					// editor.commit();
				}
				enterHome();
			} else {
				DialogUtils.showToastShort(LoginActivity.this, "登录失败");
			}
		}
	};

	public void enterHome() {
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		startActivity(intent);
	};

	/**
	 * 解析Json
	 * 
	 * @param body
	 */
	protected String analyJson(String body) {
		// TODO
		try {
			JSONObject root = new JSONObject(body);
			String s = (String) root.getString("s");
			return s;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		userOperate = new UserOperate();
		btn_login = (Button) findViewById(R.id.btn_login);
		et_account = (EditText) findViewById(R.id.et_account);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		cb_remreber = (CheckBox) findViewById(R.id.cb_remreber);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_register = (TextView) findViewById(R.id.tv_register);

		// 获得已保存的用户名和 密码
		String name = sp.getString("username", "");
		String pwd = sp.getString("pwd", "");
		if (name != "" || pwd != "") {
			et_account.setText(name);
			et_pwd.setText(pwd);
		}

		btn_login.setOnClickListener(this);
		tv_register.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			username = et_account.getText().toString();
			pwd = et_pwd.getText().toString();
			boolean isChecked = cb_remreber.isChecked();

			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
				DialogUtils.showMessage(this, "校立方", "用户名或密码不能为空");
				return;
			}

			Map<String, String> params = new HashMap<>();
			params.put("username", username);
			params.put("pwd", pwd);
			// API.API10001
			userOperate.asyncRequest(params, API.API10001,
					new AsyncRequestCallBack() {

						@Override
						public void callBack() {
							handler.sendEmptyMessage(0);
							// userOperate.getUser();

						}
					});

			break;
		case R.id.tv_register:
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void logIn() {
		username = et_account.getText().toString();
		pwd = et_pwd.getText().toString();
		boolean isChecked = cb_remreber.isChecked();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
			// TODO 弹出对话框，显示 用户名密码不能为空
			DialogUtils.showMessage(this, "校立方", "用户名或密码不能为空");
			return;
		}

		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = null;
				try {
					post = new HttpPost(new URI(API.API10001));
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<BasicNameValuePair> pairs = new ArrayList<>();
				BasicNameValuePair pair1 = new BasicNameValuePair("username",
						username);
				BasicNameValuePair pair2 = new BasicNameValuePair("pwd", pwd);
				pairs.add(pair1);
				pairs.add(pair2);
				try {
					post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
					HttpResponse response = client.execute(post);
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						String body = EntityUtils.toString(
								response.getEntity(), "utf-8");
						Message msg = Message.obtain();
						msg.obj = body;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
