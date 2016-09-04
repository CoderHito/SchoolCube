package com.hito.schoolcube.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

public class BaseOperate {
	private JSONObject response = null;
	private String msg;
	private String body;
	private boolean isSuccess = false;
	private Handler handler;

	/**
	 * 是否执行成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	public BaseOperate() {
		isSuccess = false;
	}

	public String getBody() {
		return body;
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

	public void asyncRequest(final Map<String, String> paris, final String url,
			final AsyncRequestCallBack callBack) {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (callBack != null){
					callBack.callBack();
				}
			}
		};
		new Thread() {
			public void run() {
				request2(paris, url);
				handler.sendEmptyMessage(0);
			};
		}.start();
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
					response = new JSONObject(body);
					handleSuccess(response);

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

	/**
	 * 网络请求成功的时候调用
	 * 
	 * @param response
	 */
	public void handleSuccess(JSONObject response) {

	}

	// 定义回调接口
	public interface AsyncRequestCallBack {
		public void callBack();
	}

}