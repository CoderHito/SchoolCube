package com.hito.schoolcube.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hito.schoolcube.entity.User;

public class UserOperate extends BaseOperate {

	private int s;
	private String activateCode;
	private String vCode;
	private User user;
	private int concernTimes;
	private int concernedTimes;
	private List<User> users;

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
			concernTimes = response.optInt("concernTimes", 0);
			concernedTimes = response.optInt("concernedTimes", 0);
			user = parseUser();
			users = parseUsers();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<User> parseUsers() {
		JSONObject response = getResponse();
		JSONArray arr = response.optJSONArray("users");
		if (arr == null || arr.length() == 0)
			return null;
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject userInfo = arr.optJSONObject(i);
			User u = new User();
			u.setOpenId(userInfo.optInt("openId", -1));
			u.setUsername(userInfo.optString("username", ""));
			u.setName(userInfo.optString("name", ""));
			u.setHeaderImg(userInfo.optString("headerImg", ""));
			users.add(u);
		}
		return users;
	}

	private User parseUser() {
		User u = new User();
		JSONObject response = getResponse();
		JSONObject userInfo = response.optJSONObject("userInfo");
		if (userInfo == null)
			return null;
		u.setOpenId(userInfo.optInt("openId", -1));
		u.setUsername(userInfo.optString("username", ""));
		u.setName(userInfo.optString("name", ""));
		u.setHeaderImg(userInfo.optString("headerImg", ""));
		u.setSignature(userInfo.optString("signature", ""));
		u.setSex(userInfo.optInt("sex", -1));
		u.setSchoolId(userInfo.optInt("schoolId", -1));
		u.setProfessionId(userInfo.optInt("professionId", -1));
		u.setHobby(userInfo.optString("hobby", ""));
		u.setLevel(userInfo.optInt("level"));
		u.setScore(userInfo.optInt("score"));

		JSONObject jschool = userInfo.optJSONObject("s");
		JSONObject jpro = userInfo.optJSONObject("p");
		u.setSchool(jschool.optString("name", ""));
		u.setProfession(jpro.optString("name", ""));
		return u;
	}

	public int getS() {
		return s;
	}

	public String getActivateCode() {
		return activateCode;
	}

	public User getUser() {
		return user;
	}

	public List<User> getUsers() {
		return users;
	}

	public int getConcernTimes() {
		return concernTimes;
	}

	public int getConcernedTimes() {
		return concernedTimes;
	}

}
