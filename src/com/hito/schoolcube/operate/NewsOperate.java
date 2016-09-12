package com.hito.schoolcube.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hito.schoolcube.entity.Image;
import com.hito.schoolcube.entity.News;

public class NewsOperate extends BaseOperate {
	private static final String TAG = "NewsOperate";
	private int s;
	private List<Image> imagePath;
	public List<Image> getImagePath() {
		return imagePath;
	}

	private List<News> ns;
	private News news;

	@Override
	public void asyncRequest(Map<String, String> paris, String url,
			AsyncRequestCallBack callBack) {
		super.asyncRequest(paris, url, callBack);
	}

	public List<News> getNs() {
		return ns;
	}

	public News getNews() {
		return news;
	}

	@Override
	public void handleSuccess(JSONObject response) {
		s = response.optInt("s", -999);
		Log.e(TAG, "newsOperate------handleSuccess");
		imagePath = parseNewsImgs();
		ns = parseNews();
		news = parseNews1();
	}

	private News parseNews1() {
		JSONObject response = getResponse();
		JSONObject newsInfo = response.optJSONObject("news");
		if (newsInfo == null)
			return null;
		News n = new News();
		n.setId(newsInfo.optInt("id", -1));
		n.setTitle(newsInfo.optString("title", ""));
		n.setContent(newsInfo.optString("content", ""));
		n.setOpenId(newsInfo.optInt("openId", -1));
		n.setUserName(newsInfo.optString("userName", ""));
		n.setCreateTime(newsInfo.optString("createTime", "").replace("T", " "));
		return n;
	}

	private List<News> parseNews() {
		List<News> ns = null;
		try {
			ns = new ArrayList<News>();
			JSONObject response = getResponse();
			JSONArray arr = response.getJSONArray("newss");
			if (arr == null || arr.length() == 0)
				return null;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				News n = new News();
				n.setId(obj.getInt("id"));
				n.setTitle(obj.optString("title", ""));
				n.setContent(obj.optString("content", ""));
				n.setOpenId(obj.optInt("openId", -1));
				n.setCreateTime(obj.optString("createTime").replace("T", " "));
				JSONObject b = obj.optJSONObject("b");
				if (b != null) {
					n.setBoardId(b.optInt("id", -1));
					n.setBoardName(b.optString("name", ""));
					n.setBoardImgUrl(b.optString("headImg", ""));
				}
				ns.add(n);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ns;
	}

	private List<Image> parseNewsImgs() {
		List<Image> images = null;
		try {
			JSONArray array = getResponse().getJSONArray("imageList");
			if (array == null || array.length() == 0) {
				return null;
			}
			images = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Image image = new Image(object.getInt("id"),
						object.getString("path") + object.getString("name"));
				images.add(image);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return images;
	}

}
