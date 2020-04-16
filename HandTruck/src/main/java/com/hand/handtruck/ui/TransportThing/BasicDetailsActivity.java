package com.hand.handtruck.ui.TransportThing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.activity.UpdateTruckInfoActivity;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GetCompanyTruckTask;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.ui.TransportThing.bean.TransportBean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by wcf on 2018/3/12
 * Describe: 车辆信息详情页
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class BasicDetailsActivity extends BaseActivity implements View.OnClickListener {

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
    private DecimalFormat mineformat= new DecimalFormat("0.00");
    private Map<String, String> mapParams;
    private GetCompanyTruckTask companyTruckTask;
    private String token;
    private String  userName;
    private SharedPreferences sp = null;
    private String deviceId;
    private CompanyTruckBean truckModel;
    private TransportBean bean;
    private TextView mTranpTime;
    private TextView mTranposrtLoadTime;
    private TextView mUnloadEndTime,mUnloadTime;

    public  static void start(Context context, TransportBean bean){
        Intent i=new Intent(context, BasicDetailsActivity.class);
        i.putExtra("tansport_bean",(Serializable)bean);
        context.startActivity(i);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_basic_details;
    }

    @Override
    protected void findViews() {
        mContext = BasicDetailsActivity.this;
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("基本信息");
        mTvOperator = (TextView) findViewById(R.id.tv_operator);
        mTvOperator.setVisibility(View.GONE);


        mIvItem = (ImageView) findViewById(R.id.iv_item);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mTvDetailTruckNumber = (TextView) findViewById(R.id.tv_detail_truck_number); //车牌号

        mTvDetailDeviceNumber = (TextView) findViewById(R.id.tv_detail_bussiner);  //经销商
        mTvDetailWeight = (TextView) findViewById(R.id.tv_stuff_type);  //货物类型
        mTvDetailWeightTime = (TextView) findViewById(R.id.tv_weight_purse); //净重
        mTvDetailLng = (TextView) findViewById(R.id.tv_detail_loc); //目的地
        mTvDetailLat = (TextView) findViewById(R.id.tv_detail_lat);//卸货地点
        mTvDetailLocationTime = (TextView) findViewById(R.id.tv_detail_location_time);//装载耗时

        mTranpTime = (TextView) findViewById(R.id.tv_start_car_date);//发车时间
        mTranposrtLoadTime=(TextView)findViewById(R.id.tv_transport_date);//运输时长
        mUnloadTime=(TextView)findViewById(R.id.tv_unload_date);//卸货耗时
        mUnloadEndTime=(TextView)findViewById(R.id.tv_unload_end_time);//卸货完成时间
    }

    @Override
    protected void setListeners() {
        mTvBack.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        bean= (TransportBean)getIntent().getSerializableExtra("tansport_bean");
        sp=getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token=sp.getString("token","");
        userName= sp.getString("uname","hzyx");

        String carNumber=bean.getCarNumber();
        mTvDetailTruckNumber.setText(carNumber);
        String bussicer="--";
        mTvDetailDeviceNumber.setText(bussicer);

        mTvDetailWeight.setText("--");
//        String weight=bean.getWeight();
//        String str=mineformat.format(Float.parseFloat(weight));
//        mTvDetailWeightTime.setText(str+"吨");
//        String loc=bean.getUnloadArea();
//        if(Tools.isEmpty(loc)){
//            loc="--";
//        }
//        mTvDetailLng.setText(loc);
//        String loc1=bean.getUnloadAddress();
//        mTvDetailLat.setText(loc1);
//
//        String date=bean.getLoadTime();
//        String time1= DateUtil.timeParse(date);
//        if(time1.contains(":")){
//            time1=time1.replace(":","分");
//        }
//        mTvDetailLocationTime.setText(time1+"秒");
//
//        String leaveDate=bean.getLeaveDate();
//        mTranpTime.setText(leaveDate);
//
//        String tranTime=bean.getTransportTime();
//        String tranTime1= DateUtil.timeParse(tranTime);
//        if(tranTime1.contains(":")){
//            tranTime1=tranTime1.replace(":","分");
//        }
//        mTranposrtLoadTime.setText(tranTime1+"秒");
//
//        String unTime=bean.getUnloadTime();
//        String unTime1= DateUtil.timeParse(unTime);
//        if(unTime1.contains(":")){
//            unTime1=unTime1.replace(":","分");
//        }
//        mUnloadTime.setText(unTime1+"秒");
//
//        String endTime=bean.getUnloadEndDate();
//        mUnloadEndTime.setText(endTime);
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
