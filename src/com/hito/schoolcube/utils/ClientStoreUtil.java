package com.hito.schoolcube.utils;

import com.hito.schoolcube.entity.User;

import android.content.Context;
import android.content.SharedPreferences;


public class ClientStoreUtil {
	public static final String STORE = "user";
	
	public static User getUser(Context context){
		SharedPreferences sp = context.getSharedPreferences(STORE, Context.MODE_PRIVATE);
		User user = new User();
		user.setOpenId(sp.getInt("openId", -1));
		user.setMobile(sp.getString("mobile", ""));
		user.setUsername(sp.getString("username", ""));
		user.setPwd(sp.getString("pwd", ""));
		user.setName(sp.getString("name", ""));
		user.setHeaderImg(sp.getString("headerImg", ""));
		user.setSignature(sp.getString("signature", ""));
		user.setSex(sp.getInt("sex", -1));
		user.setSchoolId(sp.getInt("schoolId", -1));
		user.setSchool(sp.getString("school", ""));
		user.setProfessionId(sp.getInt("professionId", -1));
		user.setProfession(sp.getString("profession", ""));
		user.setHobby(sp.getString("hobby", ""));
		user.setRemberMe(sp.getInt("remberMe", 0));
		user.setBackgroundColor(sp.getInt("color", 0));
		user.setReceive(sp.getInt("receive", 0));
		user.setLevel(sp.getInt("level", 0));
		user.setScore(sp.getInt("score", 0));
		return user;
	}
	public static void setUser(Context context,User user){
		SharedPreferences.Editor sp = context.
				getSharedPreferences(STORE, Context.MODE_PRIVATE).edit();
		sp.putInt("openId", user.getOpenId());
		sp.putString("mobile", user.getMobile());
		sp.putString("username", user.getUsername());
		sp.putString("pwd", user.getPwd());
		sp.putString("name", user.getName());
		sp.putString("headerImg", user.getHeaderImg());
		sp.putString("signature", user.getSignature());
		sp.putInt("sex", user.getSex());
		sp.putInt("schoolId", user.getSchoolId());
		sp.putString("school", user.getSchool());
		sp.putInt("professionId", user.getProfessionId());
		sp.putString("profession", user.getProfession());
		sp.putString("hobby", user.getHobby());
		sp.putInt("remberMe", user.getRemberMe());
		sp.putInt("color", user.getBackgroundColor());
		sp.putInt("receive", user.getReceive());
		sp.putInt("level", user.getLevel());
		sp.putInt("score", user.getScore());
		sp.commit();
	}
	public static void deleteUser(Context context){
		SharedPreferences.Editor sp = context.
				getSharedPreferences(STORE, Context.MODE_PRIVATE).edit();
		sp.remove("pwd");
		sp.remove("openid");
		sp.remove("username");
		sp.remove("name");
		sp.remove("headerImg");
		sp.remove("signature");
		sp.remove("sex");
		sp.remove("schoolId");
		sp.remove("school");
		sp.remove("professionId");
		sp.remove("profession");
		sp.remove("hobby");
		sp.remove("remberMe");
		sp.remove("level");
		sp.remove("score");
		sp.commit();
	}
}
