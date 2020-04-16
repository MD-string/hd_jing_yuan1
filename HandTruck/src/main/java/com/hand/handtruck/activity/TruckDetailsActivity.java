package com.hand.handtruck.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.OnLineTruckResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GetCompanyTruckTask;
import com.hand.handtruck.model.OnLineTruckBean;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;

import java.util.Map;

/**
 * Created by wcf on 2018/3/12
 * Describe: 车辆信息详情页
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class TruckDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvBack;
    private TextView mTvTitle;
    private TextView mTvOperator;
    private ImageView mIvItem;
    private RelativeLayout mRlTitle;
    private TextView mTvDetailTruckNumber;
    private TextView mTvDetailDeviceNumber;
    private TextView mTvDetailWeight;
    private TextView mTvDetailWeightTime;
    private TextView mTvDetailLng;
    private TextView mTvDetailLat;
    private TextView mTvDetailLocationTime;
    private Activity mContext;
    private Map<String, String> mapParams;
    private GetCompanyTruckTask companyTruckTask;
    private String token;
    private String  userName;
    private SharedPreferences sp = null;
    private String deviceId;
    private CarInfo truckModel;
    private String sourceKey;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /*获取在线车辆信息*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    OnLineTruckResultBean onLineTruckBean = (OnLineTruckResultBean) msg.obj;
                    OnLineTruckBean onLineTruck= onLineTruckBean.getResult();
                    mTvDetailTruckNumber.setText(onLineTruck.getCarNumber());
                    mTvDetailDeviceNumber.setText(onLineTruck.getDeviceId());
                    mTvDetailWeight.setText(onLineTruck.getWeight());
                    mTvDetailWeightTime.setText(onLineTruck.getCarUploadDate());
                    mTvDetailLng.setText(onLineTruck.getX());
                    mTvDetailLat.setText(onLineTruck.getY());
                    mTvDetailLocationTime.setText(onLineTruck.getGpsUploadDate());
                    break;
                //失败
                case ConstantsCode.MSG_REQUEST_FAIL:

                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_truck_details;
    }

    @Override
    protected void findViews() {
        mContext = TruckDetailsActivity.this;

        sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        sourceKey = (String) sp.getString("sourceKey", null);//资源权限标示
        //编辑车辆 car:edit 判断是否存在


        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvOperator = (TextView) findViewById(R.id.tv_operator);
        mTvOperator.setVisibility(View.GONE);

        mIvItem = (ImageView) findViewById(R.id.iv_item);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mTvDetailTruckNumber = (TextView) findViewById(R.id.tv_detail_truck_number);
        mTvDetailDeviceNumber = (TextView) findViewById(R.id.tv_detail_device_number);
        mTvDetailWeight = (TextView) findViewById(R.id.tv_detail_weight);
        mTvDetailWeightTime = (TextView) findViewById(R.id.tv_detail_weight_time);
        mTvDetailLng = (TextView) findViewById(R.id.tv_detail_lng);
        mTvDetailLat = (TextView) findViewById(R.id.tv_detail_lat);
        mTvDetailLocationTime = (TextView) findViewById(R.id.tv_detail_location_time);
        mTvTitle.setText("车辆在线信息");


    }

    @Override
    protected void setListeners() {
        mTvBack.setOnClickListener(this);
        mTvOperator.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        sp=getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token=sp.getString("token","");
        userName= sp.getString("uname","hzyx");
        mTvOperator.setVisibility(View.GONE);
//        if (userName.equals("hzyx")){
//            mTvOperator.setVisibility(View.GONE);
//        }else {
//            mTvOperator.setVisibility(View.VISIBLE);
//            mTvOperator.setText("修改");
//        }
        Bundle bundle = getIntent().getExtras();
        truckModel = (CarInfo) bundle.getSerializable("truckModel");
        deviceId= truckModel.getDeviceId();
//        companyTruckTask = GetCompanyTruckTask.getInstance(mContext, mHandler);
//        mapParams=new HashMap<>();
//        mapParams.put("token",token);
//        mapParams.put("deviceId",deviceId);
//        companyTruckTask.getOnlineTruckInfo(mapParams);

        mTvDetailTruckNumber.setText(truckModel.getCarNumber());
        mTvDetailDeviceNumber.setText(truckModel.getDeviceId());
        mTvDetailWeight.setText(truckModel.getWeight());
        mTvDetailWeightTime.setText(truckModel.getDate());
        mTvDetailLng.setText(truckModel.getX());
        mTvDetailLat.setText(truckModel.getY());
        mTvDetailLocationTime.setText(truckModel.getDate());
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                setResult(1010);
                finish();
                break;
            //修改
            case R.id.tv_operator:
                Bundle bundle = new Bundle();
                bundle.putSerializable("truckModel", truckModel);
                CommonKitUtil.startActivity(mContext, UpdateTruckInfoActivity.class, bundle, false);
                break;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1010);
        }
        return super.onKeyDown(keyCode, event);

    }
}
