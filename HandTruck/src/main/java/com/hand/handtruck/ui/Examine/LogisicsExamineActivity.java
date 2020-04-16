package com.hand.handtruck.ui.Examine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomScrollViewPager;
import com.hand.handtruck.adapter.WFragmentAdapter;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.utils.ACache;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * 销售部审核
 * 2018-12-3
 * */
public class LogisicsExamineActivity extends FragmentActivity implements View.OnClickListener {

    private TextView mTvBack, mTvTitle;
    private Activity mContext;
    private String token;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private SharedPreferences sp = null;
    Context context;

    //tab数量
    private int tabCount = 2;
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    private FormBean formBean;
    private LinearLayout ll_et;
    private ImageView img_2,img_1;


    public static void start(Context context, FormBean bean){
        Intent i=new Intent(context, LogisicsExamineActivity.class);
        i.putExtra("FormBean",(Serializable) bean);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        context =this;
        //透明状态栏
        CommonKitUtil.windowTranslucentStatus(LogisicsExamineActivity.this);
        formBean= (FormBean)getIntent().getSerializableExtra("FormBean");
        findViews();
        initTabLineWidth();
        inIt();
        setListeners();

    }

    protected void findViews() {
        mContext = LogisicsExamineActivity.this;
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(this);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("审核");
        ll_et=(LinearLayout)findViewById(R.id.ll_et);
        ll_et.setVisibility(View.GONE);

        ll_tab_1=(LinearLayout)findViewById(R.id.ll_tab_1);
        tv_tab_1=(TextView)findViewById(R.id.tv_tab_1);
        tv_tab_1.setText("订单详情");
        img_1=(ImageView)findViewById(R.id.img_1);
        img_2=(ImageView)findViewById(R.id.img_2);
        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_check_market));
        ll_tab_2=(LinearLayout)findViewById(R.id.ll_tab_2);
        tv_tab_2=(TextView)findViewById(R.id.tv_tab_2);
        tv_tab_2.setText("销售部审核");
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

        sp = getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");


        FormInfoFrgment moreFrg=new FormInfoFrgment();
        Bundle bundle = new Bundle();
        /*往bundle中添加数据*/
        bundle.putSerializable("formBean", (Serializable)formBean);
        /*把数据设置到Fragment中*/
        moreFrg.setArguments(bundle);

        LogisicsExamineFragemtn logFrgment=new LogisicsExamineFragemtn();
        Bundle bundle1 = new Bundle();
        /*往bundle中添加数据*/
        bundle1.putSerializable("formBean", (Serializable)formBean);
        /*把数据设置到Fragment中*/
        logFrgment.setArguments(bundle1);

        mFragmentList.add(moreFrg);  //订单详情
        mFragmentList.add(logFrgment);  //取证处理

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


                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth / tabCount));


                } else if (currentIndex == 1 && position == 0) // 1->0
                {

                    lp.leftMargin = (int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex *(screenWidth/tabCount));

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
                        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_detail_white));
                        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_check_market));
                        tv_tab_1.setTextColor(getResources().getColor(R.color.white));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        ll_tab_1.setBackground(mContext.getResources().getDrawable(R.color.gh_blue));
                        ll_tab_2.setBackground(mContext.getResources().getDrawable(R.color.had_bg));
                        break;
                    case 1:
                        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_detail));
                        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_check_market_white));
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
        leftStartMargin = (int)((screenWidth /tabCount));
        lp.width = leftStartMargin;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        img_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_detail));
        img_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.order_check_market));
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
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void  showTips(String tip){
        com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }





}
