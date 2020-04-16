package com.hand.handtruck.ui.TransportThing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.adapter.WFragmentAdapter;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:搜索运输任务结果页面
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class SearchTransportSencondActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "TruckInfoActivity";
    private ACache acache;
    Context context;
    private TextView tv_title;
    private LinearLayout ll_tab_title;
    private TextView mTabLineIv;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ViewPager mPageVp;
    private LinearLayout ll_tab_1,ll_tab_2,ll_tab_3;
    private TextView tv_tab_1,tv_tab_2,tv_tab_3;
    private WFragmentAdapter mFragmentAdapter;
    private int currentIndex;
    private int screenWidth;
    private int tabCount=3;
    private TextView tv_back;
    private String deviceId,endTime,startTime;

    public  static void start(Context context,String deviceId,String endTime,String startTime){
        Intent i=new Intent(context, SearchTransportSencondActivity.class);
        i.putExtra("dev_id",deviceId);
        i.putExtra("end_time",endTime);
        i.putExtra("start_time",startTime);
        context.startActivity(i);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transport_thing;
    }

    @Override
    protected void findViews() {
        context= this;
        deviceId=getIntent().getStringExtra("dev_id");
        endTime=getIntent().getStringExtra("end_time");
        startTime=getIntent().getStringExtra("start_time");

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText("搜索");
        tv_back=(TextView)findViewById(R.id.tv_back);
        ll_tab_title=(LinearLayout)findViewById(R.id.ll_tab_title);
        ll_tab_title.setVisibility(View.VISIBLE);
        mTabLineIv=(TextView)findViewById(R.id.id_tab_line_iv);
        mPageVp=(ViewPager)findViewById(R.id.id_page_vp);
        ll_tab_1=(LinearLayout)findViewById(R.id.ll_tab_1);
        ll_tab_2=(LinearLayout)findViewById(R.id.ll_tab_2);
        ll_tab_3=(LinearLayout)findViewById(R.id.ll_tab_3);
        tv_tab_1=(TextView)findViewById(R.id.tv_tab_1);
        tv_tab_2=(TextView)findViewById(R.id.tv_tab_2);
        tv_tab_3=(TextView)findViewById(R.id.tv_tab_3);

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
        ll_tab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(2,true);
            }
        });

    }

    @Override
    protected void setListeners() {
        tv_back.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        initTabLineWidth();
        TotalTransportSencondFragment totalFragment=new TotalTransportSencondFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dev_id",deviceId);
        bundle.putString("end_time",endTime);
        bundle.putString("start_time",startTime);
        totalFragment.setArguments(bundle);//数据传递到fragment中


        NormalTransportSencondFragment norlFragment=new NormalTransportSencondFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("dev_id",deviceId);
        bundle1.putString("end_time",endTime);
        bundle1.putString("start_time",startTime);
        norlFragment.setArguments(bundle1);//数据传递到fragment中


        ErrorTransportSencondFragment erFragment=new ErrorTransportSencondFragment();

        Bundle bundle2 = new Bundle();
        bundle2.putString("dev_id",deviceId);
        bundle2.putString("end_time",endTime);
        bundle2.putString("start_time",startTime);
        erFragment.setArguments(bundle2);//数据传递到fragment中

        mFragmentList.add(totalFragment);
        mFragmentList.add(norlFragment);
        mFragmentList.add(erFragment);

        mFragmentAdapter = new WFragmentAdapter(
                getSupportFragmentManager(), mFragmentList);
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

                int tabWidth = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(mContext, 20))/ tabCount);
                //

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));


                } else if (currentIndex == 1 && position == 0) // 1->0
                {

                    lp.leftMargin = (int)(-(1-offset) *(screenWidth*1.0/3)+currentIndex *(screenWidth/3));

                }else if (currentIndex == 1&& position == 1) // 1-2
                {

                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                }
                else if (currentIndex == 2 && position == 1) // 1-2
                {

                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/3)+currentIndex*(screenWidth/3));

                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
//                switchBorderTab(position);
                resetTextView() ;
                switch (position) {
                    case 0:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        break;
                    case 1:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        break;
                    case 2:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.hand_blue));
                        break;

                }
                currentIndex = position;
            }
        });

        mPageVp.setCurrentItem(0);
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
        lp.width = screenWidth/3;
        mTabLineIv.setLayoutParams(lp);
    }


    /**
     * 重置颜色
     */
    private void resetTextView() {
        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
    }
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
        }

    }

    public void  showTips(String tip){
        com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(context,tip);
    }
}
