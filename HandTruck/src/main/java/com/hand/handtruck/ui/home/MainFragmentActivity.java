package com.hand.handtruck.ui.home;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.NavigationConfig;
import com.hand.handtruck.constant.ConString;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.BaseActivity;
import com.hand.handtruck.ui.Examine.ExamineOrderFragment;
import com.hand.handtruck.ui.TransportThing.TransportFragment;
import com.hand.handtruck.ui.TruckInfo.CompanyTruckFragment1;
import com.hand.handtruck.ui.UserInfo.UserInfoFragment;
import com.hand.handtruck.ui.form.FormFragment;
import com.hand.handtruck.utils.CommonUtils;
import com.tencent.bugly.beta.Beta;


/**
 * 主窗口
 * @date  2018.12.5
 */
@SuppressLint({ "HandlerLeak", "NewApi" })
@NavigationConfig(isShow = false, titleId = R.string.main_title, showLeft = false)
public class MainFragmentActivity extends BaseActivity {


	private int[] mTabIconIdother = { R.id.tab_icon01, R.id.tab_icon02, R.id.tab_icon03,R.id.tab_icon04,R.id.tab_icon05 };
	private int[] mTabBtnIdother = { R.id.tab_menu01,R.id.tab_menu02,R.id.tab_menu03,R.id.tab_menu04,R.id.tab_menu05};
	private int[] mTablayIdother = { R.id.tab_lay01,  R.id.tab_lay02,R.id.tab_lay03,R.id.tab_lay04,R.id.tab_lay05};
	private int[] iconReother = { R.mipmap.icon_main_no,R.mipmap.icon_order_no,R.mipmap.icon_msg_no, R.mipmap.icon_person_check, R.mipmap.icon_person_no};
	private int[] iconResCheckedother = { R.mipmap.icon_main_ed,R.mipmap.icon_order_ed,R.mipmap.icon_msg_ed, R.mipmap.icon_person_check_ed, R.mipmap.icon_person_ed};



	private int[] mTabIconIdother4 = { R.id.tab_icon01, R.id.tab_icon02, R.id.tab_icon03,R.id.tab_icon05 };
	private int[] mTabBtnIdother4 = { R.id.tab_menu01,R.id.tab_menu02,R.id.tab_menu03,R.id.tab_menu05};
	private int[] mTablayIdother4 = { R.id.tab_lay01,  R.id.tab_lay02,R.id.tab_lay03,R.id.tab_lay05};
	private int[] iconReother4 = { R.mipmap.icon_main_no,R.mipmap.icon_order_no,R.mipmap.icon_msg_no,  R.mipmap.icon_person_no};
	private int[] iconResCheckedother4 = { R.mipmap.icon_main_ed, R.mipmap.icon_order_ed,R.mipmap.icon_msg_ed, R.mipmap.icon_person_ed};

	private TextView[] mTabBtn;//

	private ImageView[] mTabIcon;


	private RelativeLayout[] mTablay;

	private BaseFragment[] fragments;

	private BaseFragment mCurFragment;

	private FragmentManager mFragmentManager;

	static private Context mContext;
	private SharedPreferences sp;
	private String sourceKey;
	private boolean isAllHave;
	private RelativeLayout tab_lay04;


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		jumpManager();
	}
	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(isFinishing()){
				return;
			}
			switch (msg.what) {

				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		//		Window window = this.getWindow();
		//		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		//		window.setStatusBarColor(this.getResources().getColor(R.color.tranlent));

		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.layout_cust_main);

		sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
		sourceKey = (String) sp.getString("sourceKey", null);//资源权限标示
		//运输任务列表接口 transportTask:listTask 判断是否存在 运输tab


		init();// 初始化
		initData();// 初始化基础数据

		jumpManager();

		Beta.checkUpgrade(false,false);//自动检测更新
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
	}

	public int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * 公共跳转处理
	 */
	private void jumpManager() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String route = extras.getString("route");

			//			if(!Tools.isEmpty(tag)){
			//				PreviewFiveBean bean=(PreviewFiveBean) extras.getSerializable("mbean");
			//				String url=bean.getArticleUrl();
			//				if(Tools.isEmpty(url)){
			//					
			//					artUrl=UrlConstant.GET_DISCOVER_DD_INFO+"id="+bean.getArticleId()+"&"+Tools.getWebUrlParam("1");
			//					shareUrl=UrlConstant.GET_DISCOVER_DD_INFO+"id="+bean.getArticleId()+"&"+Tools.getWebUrlParam("3");
			//				}else{
			//					
			//					artUrl=url+"?"+Tools.getWebUrlParam("1");
			//					shareUrl=url+"?"+Tools.getWebUrlParam("3");
			//				}
			//				GrouponWebActivity.launchHandpickPage(mContext, artUrl, bean.getTitle(), shareUrl, bean.getIntroduction()); 
			//			}

			int tabNum = extras.getInt("tab_num", -1);
			if (tabNum != -1) {
				showTab(tabNum,isAllHave);
			}
		}
	}


	//	}

	private void initData() {

	}

	/**
	 * 初始化 控件
	 */
	@SuppressLint("RestrictedApi")
	private void init() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//不锁屏
		mFragmentManager = getSupportFragmentManager();

		DLog.i("mainfrag", "Fragments:" + mFragmentManager.getFragments());
		if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() != 0) {
			DLog.i("mainfrag", "Fragments size:" + mFragmentManager.getFragments().size());
			FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
			for (Fragment iterable_element : mFragmentManager.getFragments()) {
				DLog.i("mainfrag", "Fragment" + iterable_element);
				if (iterable_element != null) {
					mFragmentTransaction.remove(iterable_element);
				}

			}
			mFragmentTransaction.commitAllowingStateLoss();
			mFragmentManager.executePendingTransactions();
		}
		tab_lay04=(RelativeLayout)findViewById(R.id.tab_lay04);
		//fragment 首页
		if(sourceKey.contains(ConString.DataStrng.DATA_CHECK_HAND) ||sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_MARKET) || sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_SALE) ||sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_FINANCE)
				||sourceKey.contains(ConString.DataStrng.DATA_TOTAL_EXAMINE)){
			fragments = new BaseFragment[mTabIconIdother.length];
			fragments[0] = new CompanyTruckFragment1();
			fragments[1] = new FormFragment();
			fragments[2] = new TransportFragment();
			fragments[3]=new ExamineOrderFragment();
			fragments[4] = new UserInfoFragment();

			mTabIcon = new ImageView[mTabIconIdother.length];
			mTabBtn = new TextView[mTabIconIdother.length];
			mTablay = new RelativeLayout[mTabIconIdother.length];
			tab_lay04.setVisibility(View.VISIBLE);
			isAllHave=false;
		}else{
			fragments = new BaseFragment[mTabIconIdother4.length];
			fragments[0] = new CompanyTruckFragment1();
			fragments[1] = new FormFragment();
			fragments[2] = new TransportFragment();
			fragments[3] = new UserInfoFragment();
			mTabIcon = new ImageView[mTabIconIdother4.length];
			mTabBtn = new TextView[mTabIconIdother4.length];
			mTablay = new RelativeLayout[mTabIconIdother4.length];
			tab_lay04.setVisibility(View.GONE);
			isAllHave=true;
		}


		initTab(isAllHave);
	}

	//初始化 tab
	private void initTab(boolean isAll){
		if(isAll){
			for (int i = 0; i < mTabIconIdother4.length; i++) {
				mTabIcon[i] = getViewById(mTabIconIdother4[i]);
				RelativeLayout.LayoutParams 	params = new RelativeLayout.LayoutParams(mTabIcon[i].getLayoutParams());
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				if(i ==0 ){
					params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
					params.height=CommonUtils.dip2px(mContext,20.2f);
					params.width =CommonUtils.dip2px(mContext, 22f);
				}else if (i== 1){
					params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
					params.height=CommonUtils.dip2px(mContext,20.2f);
					params.width =CommonUtils.dip2px(mContext, 22f);
				}else if(i== 2) {
					params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
					params.height = CommonUtils.dip2px(mContext, 20.2f);
					params.width = CommonUtils.dip2px(mContext, 22f);
				}else if(i== 3) {
					params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
					params.height = CommonUtils.dip2px(mContext, 19.7f);
					params.width = CommonUtils.dip2px(mContext, 20.5f);
				}
				mTabIcon[i] .setLayoutParams(params);
				mTabBtn[i] = getViewById(mTabBtnIdother4[i]);
				mTablay[i] = getViewById(mTablayIdother4[i]);
				mTablay[i].setOnClickListener(new TabListener(i));
			}
		}else{
			for (int i = 0; i < mTabIconIdother.length; i++) {
				mTabIcon[i] = getViewById(mTabIconIdother[i]);
				RelativeLayout.LayoutParams 	params = new RelativeLayout.LayoutParams(mTabIcon[i].getLayoutParams());
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				if(i ==0 ){
					params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
					params.height=CommonUtils.dip2px(mContext,20.2f);
					params.width =CommonUtils.dip2px(mContext, 22f);
				}else if (i== 1){
					params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
					params.height=CommonUtils.dip2px(mContext,20.2f);
					params.width =CommonUtils.dip2px(mContext, 22f);
				}else if(i== 2) {
					params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
					params.height = CommonUtils.dip2px(mContext, 20.2f);
					params.width = CommonUtils.dip2px(mContext, 22f);
				}else if(i== 3) {
					params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
					params.height = CommonUtils.dip2px(mContext, 20.2f);
					params.width = CommonUtils.dip2px(mContext, 22f);
				}else if(i== 4) {
					params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
					params.height = CommonUtils.dip2px(mContext, 19.7f);
					params.width = CommonUtils.dip2px(mContext, 20.5f);
				}
				mTabIcon[i] .setLayoutParams(params);
				mTabBtn[i] = getViewById(mTabBtnIdother[i]);
				mTablay[i] = getViewById(mTablayIdother[i]);
				mTablay[i].setOnClickListener(new TabListener(i));
			}
		}



		showTab(0,isAll); // Show Table "1"
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private int previousTabIndex = -1;


	/**
	 * Show Table
	 *
	 * @param index
	 */
	private void showTab(int index,boolean isAll) {
		if( index != 5){

			showTabAnim(index);
		}
		if(isAll){
			for (int i = 0; i < mTabIconIdother4.length; i++) {
				if (i == index) {// 当前选中的
					showFragment(fragments[i]);
					mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_titile));
					mTabIcon[i].setImageResource(iconResCheckedother4[i]);
				} else {
					hideFragment(fragments[i]);
					mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_99));
					mTabIcon[i].setImageResource(iconReother4[i]);
				}
			}
		}else {
			for (int i = 0; i < mTabIconIdother.length; i++) {
				if (i == index) {// 当前选中的
					showFragment(fragments[i]);
					mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_titile));
					mTabIcon[i].setImageResource(iconResCheckedother[i]);
				} else {
					hideFragment(fragments[i]);
					mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_99));
					mTabIcon[i].setImageResource(iconReother[i]);
				}
			}
		}

		switch (index) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				break;
		}

		previousTabIndex = index;
	}

	@SuppressLint("NewApi") private void showTabAnim(int index) {
		//		//正常切换了
		//		if (previousTabIndex >= 0 && previousTabIndex != index) {
		//			AnimatorSet animatorSet = new AnimatorSet();
		//			animatorSet.setDuration(200);
		//			animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleX", 1.1f, 1.0f), ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleY", 1.1f, 1.0f));
		//			animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleX", 1.0f, 1.1f), ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleY", 1.0f, 1.1f));
		//			animatorSet.start();
		//
		//		}
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(200);
		animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[index], "scaleX", 1.0f, 1.1f), ObjectAnimator.ofFloat(mTablay[index], "scaleY", 1.0f,1.1f));
		animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[index], "scaleX", 1.1f, 1.0f), ObjectAnimator.ofFloat(mTablay[index], "scaleY", 1.1f, 1.0f));
		animatorSet.start();
	}

	class TabListener implements OnClickListener {
		int index;

		public TabListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			showTab(this.index,isAllHave); // Show Table
		}
	}


	/**
	 * 隐藏当前fragment
	 *
	 * @param fragment
	 */
	private void hideFragment(BaseFragment fragment) {
		if (!fragment.isAdded()) {
			return;
		}
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		//		mFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		if (!fragment.isHidden()) {
			mFragmentTransaction.hide(fragment);
		}
		mFragmentTransaction.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
	}

	/**
	 * 显示当前fragment
	 *
	 * @param fragment
	 */
	private void showFragment(BaseFragment fragment) {
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		if (!fragment.isAdded()) {
			mFragmentTransaction.add(R.id.fragment_lay, fragment);
		}
		if (fragment.isHidden()) {
			mFragmentTransaction.show(fragment);
			fragment.onBaseRefresh(); // 用于fragment的更新
		}
		mFragmentTransaction.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();

		mCurFragment = fragment;
		initNavigationByConfig(fragment.getClass());
	}

	/**
	 * 以下两个方法实现主界面时点击返回键实现与点击home键同样效果
	 */
	public void back() {
		moveTaskToBack(true);
	}

	//	@Override
	//	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	//		super.onActivityResult(requestCode, resultCode, intent);
	//		if (requestCode == ConsTantsCode.REQUEST_CODE_EMAIL) {//在HDSaveFragment邮件发送，在所依赖的Activity回调,回调结果再传递给HDSaveFragment
	//			if (NotNull.isNotNull(fragments[1]) && fragments[1].isVisible()) {
	//				fragments[1].onActivityResult(requestCode, resultCode, intent);
	//			}
	//		}
	//	}

	private  void mayRequestLocation(int REQUEST_COARSE_LOCATION) {
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				//判断是否需要 向用户解释，为什么要申请该权限
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
					Toast.makeText(mContext, "动态请求权限", Toast.LENGTH_LONG).show();
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
				return;
			} else {

			}
		} else {

		}
	}


	//系统方法,从requestPermissions()方法回调结果
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//确保是我们的请求
		if (requestCode == 1001) {
			if (grantResults !=null && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mContext, "权限被授予", Toast.LENGTH_SHORT).show();
			} else if (grantResults !=null && grantResults.length >0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mContext, "权限被拒绝", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
