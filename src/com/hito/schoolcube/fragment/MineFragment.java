package com.hito.schoolcube.fragment;

import com.hito.schoolcube.LoginActivity;
import com.hito.schoolcube.R;
import com.hito.schoolcube.utils.App;
import com.hito.schoolcube.view.CircleImageView;

import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MineFragment extends Fragment implements OnClickListener {

	private CircleImageView ibtn_head;
	private TextView tv_name;
	private ImageView iv_sex;
	private TextView tv_sign;
	/**
	 * 基本信息
	 */
	private View iv_info;
	/**
	 * 个性签名
	 */
	private View iv_sign;
	/**
	 * 用户等级
	 */
	private View iv_level;
	/**
	 * 设置
	 */
	private View iv_setting;
	private View iv_logout;
	private View contentView;
	private Button btn_concern;
	private Button btn_fans;

	// 选择摄像头，相册弹框
	private PopupWindow mPopupwindow;
	private View view_pop;
	private TextView tv_cancel;
	private TextView tv_takePhoto;
	private TextView tv_takeImage;
	private View view_writeupload;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		App app = (App) getActivity().getApplication();

		if (app == null || app.getUser() == null) {
			Toast.makeText(getActivity(), "还未登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
			return view;
		}
		this.contentView = view;

		initView();
		initData();
		return view;
	}

	private void initView() {
		ibtn_head = (CircleImageView) contentView.findViewById(R.id.mine_head);
		tv_name = (TextView) contentView.findViewById(R.id.mine_userName);
		iv_sex = (ImageView) contentView.findViewById(R.id.mine_sex);
		tv_sign = (TextView) contentView.findViewById(R.id.mine_sign);
		iv_info = (RelativeLayout) contentView
				.findViewById(R.id.mine_Relay_first);
		iv_sign = (RelativeLayout) contentView
				.findViewById(R.id.mine_Relay_second);
		iv_level = (RelativeLayout) contentView
				.findViewById(R.id.mine_Relay_third);
		iv_setting = (RelativeLayout) contentView
				.findViewById(R.id.mine_Relay_four);
		iv_logout = (RelativeLayout) contentView
				.findViewById(R.id.mine_Relay_six);
		btn_concern = (Button) contentView.findViewById(R.id.mine_concerns);
		btn_fans = (Button) contentView.findViewById(R.id.mine_fans);

		// 拍照上传 相册选择
		view_pop = LayoutInflater.from(getActivity()).inflate(
				R.layout.mine_change_head, null);

		tv_cancel = (TextView) view_pop
				.findViewById(R.id.mine_change_head_cancel);
		tv_takePhoto = (TextView) view_pop
				.findViewById(R.id.mine_change_head_takePhoto);
		tv_takeImage = (TextView) view_pop
				.findViewById(R.id.mine_change_head_takeImage);

		view_writeupload = LayoutInflater.from(getActivity()).inflate(
				R.layout.writeupload, null);

		iv_info.setOnClickListener(this);
		iv_sign.setOnClickListener(this);
		iv_level.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
		iv_logout.setOnClickListener(this);
		ibtn_head.setOnClickListener(this);

		btn_concern.setOnClickListener(this);
		btn_fans.setOnClickListener(this);

		tv_cancel.setOnClickListener(this);
		tv_takePhoto.setOnClickListener(this);
		tv_takeImage.setOnClickListener(this);

	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {

	}
}
