package com.hito.schoolcube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class myWebView extends WebView {

	private Context context;
	private onScrollChangedCallBack mOnScrollChangedCallBack;

	
	public onScrollChangedCallBack getOnScrollChangedCallBack() {
		return mOnScrollChangedCallBack;
	}

	public void setOnScrollChangedCallBack(
			onScrollChangedCallBack mOnScrollChangedCallBack) {
		this.mOnScrollChangedCallBack = mOnScrollChangedCallBack;
	}


	public myWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public myWebView(Context context) {
		super(context);
	}

	public myWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedCallBack != null) {
			mOnScrollChangedCallBack.callBack();
		}
	}
	public interface onScrollChangedCallBack {
		public void callBack();
	}

}
