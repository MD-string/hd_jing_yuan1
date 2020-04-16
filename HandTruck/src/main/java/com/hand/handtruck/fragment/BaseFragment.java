package com.hand.handtruck.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hand.handtruck.log.DLog;


/**
 * 公共fragment父类
 * @date 2015.9.20
 *
 */
public abstract class BaseFragment extends Fragment  {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void back() {
		getActivity().finish();
	}

	/**
	 * 如果需要fragment刷新数据，可以调用base的这个方法，在具体的fragmnet处理
	 */
	public void onBaseRefresh() {
		// TODO Auto-generated method stub

	}

	public void left() {
		DLog.i("BaseFragment", "left~~~~~~~~~~~");
	}

	public void right() {
		DLog.i("BaseFragment", "right~~~~~~~~~~~");
	}



	public boolean clickTab(int position) {
		DLog.i("BaseFragment", "clickTab~~~~~~~~~~~position:" + position);
		return true;
	}

}
