package com.hand.handtruck.ui.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.home.BaseFragment;


/**
 *  个人用户
 * @author hxz
 */

public class UserInfoFragment extends BaseFragment implements OnClickListener{

	Context mContext;
	private RelativeLayout rl_about_us,rl_out;
	private TextView tv_name;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_info, container, false);
		mContext=getActivity();
		sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
		String uname = (String) sp.getString("uname", null);
		tv_name=(TextView)view.findViewById(R.id.tv_name);
		tv_name.setText(uname+"");

		rl_about_us=(RelativeLayout)view.findViewById(R.id.rl_about_us);
		rl_about_us.setOnClickListener(this);

		rl_out=(RelativeLayout)view.findViewById(R.id.rl_out);
		rl_out.setOnClickListener(this);

		return view;
	}


	protected void inIt() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//关于我们
			case R.id.rl_about_us:
				AboutUsAct.start(mContext);
				break;
				//退出
			case R.id.rl_out:
				LoginActivity.start(mContext);
				getActivity().finish();
				break;
		}
	}

}
