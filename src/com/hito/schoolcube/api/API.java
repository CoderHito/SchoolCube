package com.hito.schoolcube.api;

public class API {
	private static final String BASE_URL = "http://172.7.29.84:9999/bookApi/";
	public static final String API001 = BASE_URL + "book";
	public static final String API002 = BASE_URL + "bookList";

	private static final String BASE_URL1 = "http://120.55.190.176:8080/sc/";
	// ��¼
	public static final String API10001 = BASE_URL1
			+ "appjs/appUser!doLogin.action";
	// ע��֮ǰ�����뷢��
	public static final String API10002 = BASE_URL1
			+ "appjs/appUser!beforeReg.action";
	// ע��
	public static final String API10003 = BASE_URL1
			+ "appjs/appUser!doReg.action";
	// ��ȡ�ֲ�ͼƬ�б�
	public static final String API10004 = BASE_URL1
			+ "appjs/appNews!downListImage.action";
	// ����ͼƬ
	public static final String API10005 = BASE_URL1
			+ "app/appDownload!downloadImg.action";
	// ��ȡ��������
	public static final String API10006 = BASE_URL1
			+ "appjs/appNews!doGetNews4Asso.action";
	// ��ȡ��������
	public static final String API10007 = BASE_URL1
			+ "appjs/appNews!doGetNewsById.action";
	// ��ȡHTML
	public static final String API10008 = BASE_URL1
			+ "app/appNews!doGetNewsHtml.action";
}
