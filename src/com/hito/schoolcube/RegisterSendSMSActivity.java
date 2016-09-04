package com.hito.schoolcube;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.hito.schoolcube.api.API;
import com.hito.schoolcube.utils.DialogUtils;
import com.hito.schoolcube.utils.PostUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterSendSMSActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "RegisterSendSMSActivity";
	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
	private TextView et_your_phone;
	private Button btn_reg;
	private TextView tv_resend;
	private EditText et_code;
	private String activateCode;
	private String account;
	private String pwd;
	private PostUtils postUtils;
	private String errorMsg;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				String body = postUtils.getBody();
				JSONObject response = postUtils.getResponse();
				Log.e(TAG, response.toString());
				break;
			case ERROR:
				Toast.makeText(RegisterSendSMSActivity.this, "错误信息："+errorMsg, 0).show();
				break;
			default:
				break;
			}
			
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_sendsms);
		initView();
		getData();
	}

	public void getData() {
		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		pwd = intent.getStringExtra("pwd");
		activateCode = intent.getStringExtra("activateCode");
		et_your_phone.setText(account);
		Log.e(TAG, activateCode);
	}

	private void initView() {
		postUtils = new PostUtils();
		et_code = (EditText) findViewById(R.id.et_code);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		tv_resend = (TextView) findViewById(R.id.tv_resend);
		et_your_phone = (TextView) findViewById(R.id.et_your_phone);

		btn_reg.setOnClickListener(this);
		tv_resend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reg:
			String code = et_code.getText().toString();
			if (TextUtils.isEmpty(code)) {
				DialogUtils.showToastShort(RegisterSendSMSActivity.this,
						"验证码不能为空");
			} else if (!code.equals(activateCode)) {
				DialogUtils.showToastShort(RegisterSendSMSActivity.this,
						"验证码不正确");
			} else {
				DialogUtils.showToastShort(RegisterSendSMSActivity.this,
						"验证码正确");
			}
			new Thread() {
				

				public void run() {
					Map<String, String> paris = new HashMap<>();
					paris.put("account", account);
					paris.put("pwd", pwd);
					paris.put("activateCode", activateCode);
					postUtils.request2(paris, API.API10003);
					if (postUtils.isSuccess()) {
						handler.sendEmptyMessage(SUCCESS);
					} else {
						errorMsg = postUtils.getMsg();
						handler.sendEmptyMessage(ERROR);
					}
				};
			}.start();

			break;
		case R.id.tv_resend:

			break;
		default:
			break;
		}

	}
}
