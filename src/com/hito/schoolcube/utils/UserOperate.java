package com.hito.schoolcube.utils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hito.schoolcube.utils.BaseOperate.AsyncRequestCallBack;

public class UserOperate extends BaseOperate {

	private int s;
	private String activateCode;

	@Override
	public void asyncRequest(Map<String, String> paris, String url,
			AsyncRequestCallBack callBack) {
		super.asyncRequest(paris, url, callBack);
	}
	
	@Override
	public void handleSuccess(JSONObject response) {
		try {
			s = response.getInt("s");
			activateCode = response.getString("activate");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getS() {
		return s;
	}

	public String getActivateCode() {
		return activateCode;
	}
	
}
