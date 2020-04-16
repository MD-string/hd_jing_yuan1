package com.hand.handtruck.ui.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GetCompanyTruckTask;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.utils.Tools;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by wcf on 2018/3/12
 * Describe: 车辆信息详情页
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class FormBasicDetailsActivity extends BaseActivity implements View.OnClickListener {

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
    private FormBean bean;
    private TextView mTranpTime;
    private TextView mTranposrtLoadTime;
    private TextView mUnloadEndTime,mUnloadTime;
    private TextView mFormNumber,mStuff;

    public  static void start(Context context, FormBean bean){
        Intent i=new Intent(context, FormBasicDetailsActivity.class);
        i.putExtra("tansport_bean",(Serializable)bean);
        context.startActivity(i);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_basic_details;
    }

    @Override
    protected void findViews() {
        mContext = FormBasicDetailsActivity.this;
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("车辆信息");
        mTvOperator = (TextView) findViewById(R.id.tv_operator);
        mTvOperator.setVisibility(View.GONE);


        mIvItem = (ImageView) findViewById(R.id.iv_item);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mTvDetailTruckNumber = (TextView) findViewById(R.id.tv_detail_truck_number); //车牌号

        mTvDetailDeviceNumber = (TextView) findViewById(R.id.tv_detail_bussiner);  //经销商
        mFormNumber=(TextView)findViewById(R.id.tv_form_number);         //发货单号
        mStuff=(TextView)findViewById(R.id.tv_stuff);//货物
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
        bean= (FormBean)getIntent().getSerializableExtra("tansport_bean");
        sp=getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token=sp.getString("token","");
        userName= sp.getString("uname","hzyx");

        String carNumber=bean.getCarNumber();
        mTvDetailTruckNumber.setText(carNumber);
        mTvDetailDeviceNumber.setText(bean.getCustName());
        mFormNumber.setText(bean.getOrderCode());
        mStuff.setText(bean.getProdName());
        mTvDetailWeight.setText(bean.getPackType());
        String weight=bean.getSendWeight();
        String str=mineformat.format(Float.parseFloat(weight));
        mTvDetailWeightTime.setText(str+"吨");
        String loc=bean.getSaleAreaName();
        if(Tools.isEmpty(loc)){
            loc="--";
        }
        mTvDetailLng.setText(loc);
        String loc1=bean.getSaleAreaName();
        mTvDetailLat.setText(loc1);

        String date=bean.getFullLoadTime();
        if(!Tools.isEmpty(date)){
            mTvDetailLocationTime.setText(date);
        }else{
            mTvDetailLocationTime.setText("--");
        }

        String leaveDate=bean.getLeaveTime();
        mTranpTime.setText(leaveDate);

        String tranTime=bean.getFullLoadTime();
        if(!Tools.isEmpty(tranTime)){
            mTranposrtLoadTime.setText(tranTime);
        }else{
            mTranposrtLoadTime.setText("--");
        }

        String unTime=bean.getDeviceOrder().getLastDeviceTime();
        if(!Tools.isEmpty(tranTime)){

            mUnloadTime.setText(unTime);
        }else{
            mUnloadTime.setText("--");
        }

        String endTime=bean.getUnloadEndTime();

        final String status=bean.getRailCheckStatus();//订单状态  0-未审核  1-正常卸货  2异常卸货
        if ("0".equals(status)){ //
            endTime="运输中";
        }
        mUnloadEndTime.setText(endTime);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                setResult(1010);
                finish();
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
