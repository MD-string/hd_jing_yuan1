package com.hand.handtruck.ui.TruckInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomScrollViewPager;
import com.hand.handtruck.activity.TruckDetailsActivity;
import com.hand.handtruck.adapter.WFragmentAdapter;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.utils.ACache;
import com.hand.handtruck.utils.Tools;
import com.hand.handtruck.view.MapContainer;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * hxz
 * 重量趋势  主页面
 * 2018-12-3
 * */
public class WeightTrendMainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView mTvBack, mTvTitle, mTvOperator;
    private ImageView mIvItem;
    private RelativeLayout mRlTitle;
    private MapContainer mMapContainer;
    private TextView  mTvTrendStartTime, mTvTrendEndTime;
    private EditText mTvTrendTimeInterval, mTvTrendDataCount;
    private Activity mContext;
    private String token;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private SharedPreferences sp = null;
    private CarInfo truckModel;
    Context context;

    private String sign;
    //tab数量
    private int tabCount = 2;

    private DecimalFormat myformat;
    private CustomScrollViewPager mPageVp;
    private LinearLayout ll_tab_1,ll_tab_2;
    private TextView tv_tab_1,tv_tab_2;
    private TextView mTabLineIv;
    private WFragmentAdapter mFragmentAdapter;
    private int currentIndex=0;
    private int screenWidth;
    private int leftStartMargin;
    private ACache acache;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取重量趋势成功
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    break;


            }
        }
    };
    private ImageView img_1,img_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_trend);
        context =this;
        //透明状态栏
        CommonKitUtil.windowTranslucentStatus(WeightTrendMainActivity.this);
        acache=ACache.get(context);

        sp = context.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        //车辆状态数据接口 car:realTime 判断是否存在 运输tab

        findViews();
        initTabLineWidth();
        inIt();
        setListeners();

    }

    protected void findViews() {
        mContext = WeightTrendMainActivity.this;
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvOperator = (TextView) findViewById(R.id.tv_operator);//详细信息
        mTvOperator.setVisibility(View.GONE);
        mIvItem = (ImageView) findViewById(R.id.iv_item);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mTvOperator.setText("详细信息");
        mTvOperator.setOnClickListener(this);

        ll_tab_1=(LinearLayout)findViewById(R.id.ll_tab_1);
        tv_tab_1=(TextView)findViewById(R.id.tv_tab_1);
        ll_tab_2=(LinearLayout)findViewById(R.id.ll_tab_2);
        tv_tab_2=(TextView)findViewById(R.id.tv_tab_2);

        img_1=(ImageView)findViewById(R.id.img_1);
        img_2=(ImageView)findViewById(R.id.img_2);
        mPageVp=(CustomScrollViewPager)findViewById(R.id.id_page_vp);
        mTabLineIv=(TextView)findViewById(R.id.id_tab_line_iv);//引导线

        ll_tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(0,true);
            }
        });
        ll_tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(1,true);
            }
        });

    }



    protected void inIt() {
        Bundle bundle = getIntent().getExtras();
        truckModel = (CarInfo) bundle.getSerializable("truckModel");
        sign = bundle.getString("sign");
        mTvTitle.setText(truckModel.getCarNumber());

        if(!Tools.isEmpty(sign)){
            acache.put("thruck_sign",sign);
        }else{
            acache.put("thruck_sign","");
        }
        if( truckModel != null){
            acache.put("thruck_truckModel",(Serializable)truckModel);
        }else{
            CompanyTruckBean    cp=new CompanyTruckBean();
            acache.put("thruck_truckModel",(Serializable)cp);
        }


        sp = getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        myformat = new DecimalFormat("0.00");


        LocusFragment locusFrg=new LocusFragment();
        RTSLFragment rtslFrg=new RTSLFragment();
        mFragmentList.add(locusFrg);  //实时定位
        mFragmentList.add(rtslFrg);  //历史查询

        mFragmentAdapter = new WFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                DLog.d("offset1:", offset+ "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记x个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                int tabWidth = (int)((screenWidth /2.0));
                //
                //				int leftMarginToLeft = (int) (offset * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(mContext, 60)) / 2));
                //				int leftMarginToRight = (int) (-(1 - offset) * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(mContext, 60)) / 2));
                int leftMarginToLeft  =(int) (offset * (screenWidth * 1.0 / 2)+currentIndex * tabWidth );
                int leftMarginToRight= (int) (-(1 - offset)  * (screenWidth * 1.0 /2) +currentIndex * tabWidth);

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = leftMarginToLeft;

                } else if (currentIndex == 1 && position == 0) // 1->0
                {

                    lp.leftMargin = leftMarginToRight;

                }
//                else if (currentIndex == 1 && position == 1) // 1->2
//                {
//
//                    lp.leftMargin = leftMarginToLeft-3;
//
//                } else if (currentIndex == 2 && position == 1) // 2->1
//                {
//                    lp.leftMargin = leftMarginToRight-3;
//
//                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
//                switchBorderTab(position);
                mTabLineIv.setVisibility(View.VISIBLE);
                resetTextView() ;
                switch (position) {
                    case 0:
                        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_location_white));
                        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_gui_ji));
                        tv_tab_1.setTextColor(getResources().getColor(R.color.white));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        ll_tab_1.setBackground(mContext.getResources().getDrawable(R.color.gh_blue));
                        ll_tab_2.setBackground(mContext.getResources().getDrawable(R.color.had_bg));
                        break;
                    case 1:
                        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_location));
                        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_gui_ji_white));
                        tv_tab_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.white));
                        ll_tab_1.setBackground(mContext.getResources().getDrawable(R.color.had_bg));
                        ll_tab_2.setBackground(mContext.getResources().getDrawable(R.color.gh_blue));
                        break;

                }
                currentIndex = position;
            }
        });

        mPageVp.setCurrentItem(currentIndex,true);

    }
    /**
     * 设置滑动条的宽度为屏幕的1/x(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        leftStartMargin = (int)((screenWidth /2.0));
        lp.width = leftStartMargin;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_location));
        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_gui_ji));
        tv_tab_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        tv_tab_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        ll_tab_1.setBackground(mContext.getResources().getDrawable(R.color.had_bg));
        ll_tab_2.setBackground(mContext.getResources().getDrawable(R.color.had_bg));
    }

    protected void setListeners() {
        mTvBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                //取消当前网络请求
                finish();
                break;
            //详细信息
            case R.id.tv_operator:
                Bundle bundle = new Bundle();
                bundle.putSerializable("truckModel", truckModel);
                CommonKitUtil.startActivityForResult(mContext, TruckDetailsActivity.class, bundle, ConstantsCode.MSG_REQUEST_CODE);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void  showTips(String tip){
        com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }


    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


}
