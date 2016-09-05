package com.hito.schoolcube.api;

public class API {
	private static final String BASE_URL = "http://172.7.29.84:9999/bookApi/";
	public static final String API001 = BASE_URL + "book";
	public static final String API002 = BASE_URL + "bookList";

	private static final String BASE_URL1 = "http://120.55.190.176:8080/sc/";
	// 登录
	public static final String API10001 = BASE_URL1
			+ "appjs/appUser!doLogin.action";
	// 注册之前短信码发放
	public static final String API10002 = BASE_URL1
			+ "appjs/appUser!beforeReg.action";
	// 注册
	public static final String API10003 = BASE_URL1
			+ "appjs/appUser!doReg.action";
	// 获取轮播图片列表
	public static final String API10004 = BASE_URL1
			+ "appjs/appNews!downListImage.action";
	// 下载图片
	public static final String API10005 = BASE_URL1
			+ "app/appDownload!downloadImg.action";
	// 获取社团数据
	public static final String API10006 = BASE_URL1
			+ "appjs/appNews!doGetNews4Asso.action";
	// 获取新闻内容
	public static final String API10007 = BASE_URL1
			+ "appjs/appNews!doGetNewsById.action";
	// 获取HTML
	public static final String API10008 = BASE_URL1
			+ "app/appNews!doGetNewsHtml.action";
	// 检查新版本
	public static final String API00001 = BASE_URL1
			+ "appjs/data!version.action";
	// 根据用户编号获取用户的基本信息
	public static final String API10011 = BASE_URL1
			+ "appjs/appUser!doGetUserByOpenID.action";
	// 上传头像
	public static final String API10012 = BASE_URL1
			+ "app/appUser!doupHeadImage.action";
	// 获取用户关注的人
	public static final String API10013 = BASE_URL1
			+ "appjs/appUser!showAllIConcernUser.action";
	// 更改用户名
	public static final String API10014 = BASE_URL1
			+ "appjs/appUser!updateNameByOpenID.action";
	// 加入社团
	public static final String API10015 = BASE_URL1
			+ "appjs/appUser!doaddBoardUser.action";
	// 获取操场帖子信息
	public static final String API20001 = BASE_URL1
			+ "appjs/appPost!doGetPosts.action";
	// 获取所有的社团信息
	public static final String API20002 = BASE_URL1
			+ "appjs/appBoard!doGetBoard.action";
}
