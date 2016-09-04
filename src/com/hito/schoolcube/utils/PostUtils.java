package com.hito.schoolcube.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;

public class PostUtils {
	private JSONObject response = null;
	private String msg;
	private int s;
	private String activateCode;
	private String body;
	private boolean isSuccess = false;;

	/**
	 * 是否执行成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	public PostUtils() {
		isSuccess = false;
	}

	public String getBody() {
		return body;
	}

	/**
	 * 返回验证码
	 * 
	 * @return
	 */
	public String getActivateCode() {
		return activateCode;
	}

	/**
	 * 返回请求结果 json格式
	 * 
	 * @return
	 */
	public JSONObject getResponse() {
		return response;
	}

	public String getMsg() {
		return msg;
	}

	public int getS() {
		return s;
	}

	public void request2(Map<String, String> paris, String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			if (paris != null && !paris.isEmpty()) {
				System.err.println("1111111111111111111");
				Set<Entry<String, String>> entrySet = paris.entrySet();
				List<BasicNameValuePair> pairList = new ArrayList<>();
				Iterator<Entry<String, String>> iterator = entrySet.iterator();
				boolean isFirst = true;
				StringBuffer request = new StringBuffer();
				request.append(url).append("?");
				while (iterator.hasNext()) {
					Entry<String, String> next = iterator.next();
					String key = next.getKey();
					String value = next.getValue();
					BasicNameValuePair pair = new BasicNameValuePair(key, value);
					pairList.add(pair);
					if (!isFirst)
						request.append("&");
					request.append(next.getKey())
							.append("=")
							.append(URLEncoder.encode(next.getValue(), "UTF-8"));
					isFirst = false;
					System.out.println("22222222222222222222");
				}
				System.out.println(pairList.size());
				System.out.println(request);
				post.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
				HttpResponse httpResponse = client.execute(post);
				int code = httpResponse.getStatusLine().getStatusCode();
				System.out.println("-----------" + code);
				if (code == 200) {
					body = EntityUtils.toString(httpResponse.getEntity(),
							"utf-8");
					isSuccess = true;
					JSONObject response = new JSONObject(body);
					analyJson(response);
				}
			}
		} catch (JSONException ex) {
			msg = "内容解析异常！";
		} catch (HttpHostConnectException ex) {
			msg = "无法连接到服务器，请确认当前已连接的网络！";
		} catch (ConnectTimeoutException ex) {
			msg = "连接服务器超时！";
		} catch (Exception ex) {
			msg = "操作失败！";
		}
	}

	// // 增加一些日志输出，便于项目调试
	// public void request(Map<String, String> params, String url) {
	// // 设置超时
	// BasicHttpParams httpParams = new BasicHttpParams();
	// HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
	// HttpConnectionParams.setSoTimeout(httpParams, 30000);
	// HttpClient client = new DefaultHttpClient(httpParams);
	// HttpPost req = new HttpPost(url);
	// StringBuffer request = new StringBuffer();
	// request.append(url).append("?");
	// try {
	// if (params != null && !params.isEmpty()) {
	// Set<Entry<String, String>> entites = params.entrySet();
	// boolean isFirst = true;
	// List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	// for (Entry<String, String> entry : entites) {
	// BasicNameValuePair pair = new BasicNameValuePair(
	// entry.getKey(), entry.getValue());
	// pairs.add(pair);
	// if (!isFirst)
	// request.append("&");
	// request.append(entry.getKey())
	// .append("=")
	// .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	// isFirst = false;
	// }
	// req.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
	// }
	// // if (null != JSESSIONID) {
	// // req.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
	// // }
	// // Log.d(Setting.TAG, request.toString());
	// HttpResponse resp = client.execute(req);
	// int responseCode = resp.getStatusLine().getStatusCode();
	// String body = EntityUtils.toString(resp.getEntity(), "utf-8");
	// if (responseCode != 200) {
	// msg = "服务器繁忙，请稍后重试！";
	// } else {
	// CookieStore cookieStore = ((DefaultHttpClient) client)
	// .getCookieStore();
	// List<Cookie> cookies = cookieStore.getCookies();
	// for (int i = 0; i < cookies.size(); i++) {
	// if ("JSESSIONID".equals(cookies.get(i).getName())) {
	// String JSESSIONID = cookies.get(i).getValue();
	// break;
	// }
	// }
	// System.out.println("--------------" + responseCode);
	// response = new JSONObject(body);
	// analyJson(response);
	// // handleResponse(response);
	// }
	// // Log.d(Setting.TAG, "response: "+response);
	// resp.getEntity().consumeContent();
	// } catch (JSONException ex) {
	// // Log.d(Setting.TAG, "",ex);
	// msg = "内容解析异常！";
	// } catch (HttpHostConnectException ex) {
	// // Log.d(Setting.TAG, "",ex);
	// msg = "无法连接到服务器，请确认当前已连接的网络！";
	// } catch (ConnectTimeoutException ex) {
	// // Log.d(Setting.TAG, "",ex);
	// msg = "连接服务器超时！";
	// } catch (Exception ex) {
	// // Log.d(Setting.TAG, "",ex);
	// msg = "操作失败！";
	// }
	// }

	public void analyJson(JSONObject obj) throws JSONException {
		s = obj.getInt("s");
		activateCode = obj.getString("activate");
	}

}
