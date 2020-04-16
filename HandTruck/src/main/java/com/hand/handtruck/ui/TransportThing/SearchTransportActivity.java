package com.hand.handtruck.ui.TransportThing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDatePicker;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.bean.TruckChildBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Describe:搜索运输任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class SearchTransportActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_title,tv_back;
    private static final String TAG = "TruckInfoActivity";
    private ACache acache;
    private List<TruckChildBean> list=new ArrayList<>();
    private Spinner tv_car_number;
    private TextView mTvTrendStartTime,mTvTrendEndTime;

    private CustomDatePicker customDatePickerS;
    private CustomDatePicker customDatePickerE;
    private LinearLayout rl_all;
    Context context;
    private RelativeLayout rl_carNo;
    private Button btn_query_1;
    private ArrayAdapter<String> adapter;
    private String devId;
    private  TextView mTvTrendStartTime_0,mTvTrendEndTime_0;
    private boolean isStartFirst=false;
    private boolean isEndTime=false;

    public  static void start(Context context){
        Intent i=new Intent(context, SearchTransportActivity.class);
        context.startActivity(i);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /*获取在线车辆信息*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_transport;
    }

    @Override
    protected void findViews() {
        context=SearchTransportActivity.this;
        acache= ACache.get(context,TAG);
        rl_all=(LinearLayout)findViewById(R.id.rl_all);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText("搜索");
        tv_back=(TextView)findViewById(R.id.tv_back);
        rl_carNo=(RelativeLayout)findViewById(R.id.rl_carNo);
        tv_car_number=(Spinner)findViewById(R.id.tv_car_number);

        mTvTrendStartTime_0 = (TextView)findViewById(R.id.tv_trend_start_time_0); //开始时间
        mTvTrendStartTime_0.setVisibility(View.VISIBLE);
        mTvTrendEndTime_0 = (TextView)findViewById(R.id.tv_trend_end_time_0);//终止时间
        mTvTrendEndTime_0.setVisibility(View.VISIBLE);

        mTvTrendStartTime = (TextView)findViewById(R.id.tv_trend_start_time_1); //开始时间
        mTvTrendStartTime.setVisibility(View.GONE);
        mTvTrendEndTime = (TextView)findViewById(R.id.tv_trend_end_time_1);//终止时间
        mTvTrendEndTime.setVisibility(View.GONE);

        btn_query_1=(Button)findViewById(R.id.btn_query_1);

        List<CompanyTruckGroupBean> clist=(List<CompanyTruckGroupBean>)acache.getAsObject("truck_list");//缓存
        if(clist !=null && clist.size() > 0){
            for(int i=0;i<clist.size();i++){
                CompanyTruckGroupBean bean=clist.get(i);
                String compName=bean.getName();
                if(compName.contains("株洲蓝眼科技")){
                    List<TruckChildBean> mlist = bean.getChildren();
                    if(mlist !=null && mlist.size() > 0 ){
                        list  =    mlist;
                    }
                }
            }
        }



    }

    @Override
    protected void setListeners() {
        tv_back.setOnClickListener(this);
        rl_carNo.setOnClickListener(this);
        mTvTrendStartTime_0.setOnClickListener(this);
        mTvTrendEndTime_0.setOnClickListener(this);

        mTvTrendStartTime.setOnClickListener(this);
        mTvTrendEndTime.setOnClickListener(this);
        btn_query_1.setOnClickListener(this);

        tv_car_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >0){
                    TruckChildBean item=list.get(position-1);
                    devId=item.getTruckModel().getDeviceId();

                }else{
                    devId="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void inIt() {
        List<String> mlist=new ArrayList<>();
        if(list !=null && list.size() >0){
            for(int i=0;i<list.size();i++){
                mlist.add(list.get(i).getTruckModel().getCarNumber());
            }
            mlist.add(0,"请选择");
            adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,mlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            tv_car_number.setAdapter(adapter);

        }

        SimpleDateFormat sdf = getDate();
        Date date = new Date();
        String now = sdf.format(date);
        String end =mTvTrendEndTime.getText().toString();
        customDatePickerS = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                isStartFirst=false;
                mTvTrendStartTime.setText(time);
            }

            @Override
            public void cancle() {
                if(isStartFirst){
                    mTvTrendStartTime_0.setVisibility(View.VISIBLE);
                    mTvTrendStartTime.setVisibility(View.GONE);
                    mTvTrendStartTime_0.setText("");
                }
            }
        }, "2010-01-01 00:00", end); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePickerS.showSpecificTime(true); // 显示时和分
        customDatePickerS.setIsLoop(true); // 允许循环滚动
        customDatePickerE = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                isEndTime=false;
                mTvTrendEndTime.setText(time);
            }

            @Override
            public void cancle() {
                if(isEndTime){
                    mTvTrendEndTime_0.setVisibility(View.VISIBLE);
                    mTvTrendEndTime.setVisibility(View.GONE);
                    mTvTrendEndTime_0.setText("");
                }

            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePickerE.showSpecificTime(true); // 显示时和分
        customDatePickerE.setIsLoop(true); // 允许循环滚动

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_trend_start_time_0:
                isStartFirst=true;
                mTvTrendStartTime_0.setVisibility(View.GONE);
                mTvTrendStartTime.setVisibility(View.VISIBLE);
                customDatePickerS.show(mTvTrendStartTime.getText().toString());
                break;
            case R.id.tv_trend_end_time_0:
                isEndTime=true;
                mTvTrendEndTime_0.setVisibility(View.GONE);
                mTvTrendEndTime.setVisibility(View.VISIBLE);
                customDatePickerE.show(mTvTrendEndTime.getText().toString());
                break;
            case R.id.tv_trend_start_time_1:
                customDatePickerS.show(mTvTrendStartTime.getText().toString());

                break;
            case R.id.tv_trend_end_time_1:

                customDatePickerE.show(mTvTrendEndTime.getText().toString());
                break;
            case R.id.btn_query_1:
                String eTime="";
                String sTime="";
                if(mTvTrendEndTime_0.getVisibility()==View.VISIBLE){
                    eTime="";
                }else{
                    eTime=mTvTrendEndTime.getText().toString()+":00";
                }
                if(mTvTrendStartTime_0.getVisibility()==View.VISIBLE){
                     sTime="";
                }else{

                    sTime=mTvTrendStartTime.getText().toString()+":00";
                }
                SearchTransportSencondActivity.start(context,devId,eTime,sTime);
                break;
        }

    }
    private SimpleDateFormat getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(date);
        //        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date date = calendar.getTime();


        String now = sdf.format(date);
        mTvTrendEndTime.setText(now);


        Date date1 = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        date1 = calendar1.getTime();
        String yesterday = sdf.format(date1);
        mTvTrendStartTime.setText(yesterday);
        return sdf;

    }

    public void  showTips(String tip){
        com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(context,tip);
    }
}
