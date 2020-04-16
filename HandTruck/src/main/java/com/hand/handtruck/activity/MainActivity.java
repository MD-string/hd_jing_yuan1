package com.hand.handtruck.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.application.MyApplication;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Sha;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvBack;
    private TextView mTvTitle;
    private TextView mTvOperator;
    private ImageView mIvItem;
    private RelativeLayout mRlTitle;
    private LinearLayout mLlTruckInfo;
    private LinearLayout mLlTruckAdd;
    /*再点一次退出*/
    private boolean isPressedBackOnce = false;
    private long firstPressedTime = 0;
    private long secondPressedTime = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvOperator = (TextView) findViewById(R.id.tv_operator);
        mIvItem = (ImageView) findViewById(R.id.iv_item);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mLlTruckInfo = (LinearLayout) findViewById(R.id.ll_truck_info);
        mLlTruckAdd = (LinearLayout) findViewById(R.id.ll_truck_add);
    }

    @Override
    protected void setListeners() {
        mLlTruckInfo.setOnClickListener(this);
        mLlTruckAdd.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        mTvBack.setVisibility(View.GONE);
        mTvTitle.setText("车载信息管理系统");
        LogUtil.e("sha值=="+ Sha.sHA1(MainActivity.this));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //车辆信息
            case R.id.ll_truck_info:
                CommonKitUtil.startActivity(MainActivity.this, TruckInfoActivity.class, new Bundle(), false);
                break;
            //车辆添加
            case R.id.ll_truck_add:
                CommonKitUtil.startActivity(MainActivity.this, AddTruckActivity.class, new Bundle(), false);
                break;
        }

    }
    //提示“再点击一次退出程序”
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (isPressedBackOnce) {
                secondPressedTime = System.currentTimeMillis();
                if (secondPressedTime - firstPressedTime > 2000) {
                    isPressedBackOnce = true;
                    // Toast.makeText(this, getResources().getString(R.string.exit_click_again), Toast.LENGTH_SHORT).show();
                    firstPressedTime = System.currentTimeMillis();
                } else {
                    firstPressedTime = 0;
                    secondPressedTime = 0;
                    isPressedBackOnce = false;
                    MyApplication.getInstance().exit();
                }
            } else {
                isPressedBackOnce = true;
                // Toast.makeText(this, getResources().getString(R.string.exit_click_again), Toast.LENGTH_SHORT).show();
                firstPressedTime = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }
}
