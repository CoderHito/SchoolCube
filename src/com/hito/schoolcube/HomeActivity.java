package com.hito.schoolcube;

import com.hito.schoolcube.fragment.AssnFragment;
import com.hito.schoolcube.fragment.HomeFragment;
import com.hito.schoolcube.fragment.MineFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class HomeActivity extends Activity implements OnClickListener {
	private ImageButton ibtn_home;
	private ImageButton ibtn_assn;
	private ImageButton ibtn_mine;
	private int[] allButtons = { R.id.ibtn_home, R.id.ibtn_assn, R.id.ibtn_mine };
	private int[] allSeeImg = { R.drawable.btnhomechecked,
			R.drawable.btnassnchecked, R.drawable.btnmimechecked };
	private int[] allUnSeeImg = { R.drawable.btnhome, R.drawable.btnassn,
			R.drawable.btnmime };
	private LinearLayout ll_content;
	private FragmentManager fm;
	private HomeFragment homeFragment;
	private MineFragment mineFragment;
	private AssnFragment assnFragment;
	private FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
	}

	private void initView() {
		fm = getFragmentManager();
		transaction = fm.beginTransaction();
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ibtn_home = (ImageButton) findViewById(R.id.ibtn_home);
		ibtn_assn = (ImageButton) findViewById(R.id.ibtn_assn);
		ibtn_mine = (ImageButton) findViewById(R.id.ibtn_mine);

		ibtn_assn.setOnClickListener(this);
		ibtn_home.setOnClickListener(this);
		ibtn_mine.setOnClickListener(this);
		
		homeFragment = new HomeFragment();
		transaction.replace(R.id.ll_content, homeFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		showView(v.getId());
	}

	private void showView(int id) {
		/**
		 * 将标题栏内的图片全都变为不选中
		 * 
		 * 设置当前页面的按钮为选中状态
		 */
		for (int i = 0; i < allButtons.length; i++) {
			ImageButton btn = (ImageButton) findViewById(allButtons[i]);
			// btn.setBackgroundResource(allUnSeeImg[i]);
			btn.setImageResource(allUnSeeImg[i]);
		}

		for (int i = 0; i < allButtons.length; i++) {
			if (allButtons[i] == id) {
				ImageButton btn = (ImageButton) findViewById(allButtons[i]);
				// btn.setBackground(getResources().getDrawable(allSeeImg[i]));
				btn.setImageResource(allSeeImg[i]);
			}
		}
		transaction = fm.beginTransaction();
		switch (id) {
		case R.id.ibtn_home:
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
			}
			transaction.replace(R.id.ll_content, homeFragment);
			transaction.commit();
			break;
		case R.id.ibtn_assn:
			if (assnFragment == null) {
				assnFragment = new AssnFragment();
			}
			transaction.replace(R.id.ll_content, assnFragment);
			transaction.commit();
			break;
		case R.id.ibtn_mine:
			if (mineFragment == null) {
				mineFragment = new MineFragment();
			}
			transaction.replace(R.id.ll_content, mineFragment);
			transaction.commit();
			break;
		}
	}
}
