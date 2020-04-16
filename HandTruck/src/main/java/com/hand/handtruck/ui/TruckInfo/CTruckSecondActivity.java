package com.hand.handtruck.ui.TruckInfo;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.TruckInfo.adapter.CarNumberAdapter;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.bean.CompanyInfo;
import com.hand.handtruck.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆详细页面
 */
public class CTruckSecondActivity extends Activity implements View.OnClickListener, XListView.IXListViewListener  {


    private XListView list_1;
    private static final String TAG = "TruckInfoActivity";
    private  List<CarInfo> mGroupList=new ArrayList<>();
    private CarNumberAdapter madapter;
    private CompanyTruckGroupBean bean;
    private TextView mTvBack;
    private Context mContext;
    private String token;
    private CompanyInfo mCinfo;
    private TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_car_list_1);
        mContext=this;
        SharedPreferences sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);

        Bundle b=getIntent().getExtras();
        mCinfo=(CompanyInfo)b.getSerializable("sencond_info");
        mGroupList=mCinfo.getCarList();

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText(mCinfo.getName()+"");
        TextView tv_back=(TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_1=(XListView)findViewById(R.id.list_1);
        list_1.setPullRefreshEnable(false);
        list_1.setPullLoadEnable(false);
        list_1.setXListViewListener(this);
        RelativeLayout  rl_et=(RelativeLayout)findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("search_list",(Serializable)mGroupList);
                CommonKitUtil.startActivity(CTruckSecondActivity.this, SearchCarActivity.class, bund, false);
                return false;
            }
        });
//        View mView= LayoutInflater.from(mContext).inflate(R.layout.lvitem_company_1, null);
//        TextView tv_company=(TextView)mView.findViewById(R.id.tv_company);
//        tv_company.setText(mCinfo.getName()+"");
//        list_1.addHeaderView(mView);
        madapter=new CarNumberAdapter(mContext,mGroupList);
        list_1.setAdapter(madapter);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                break;
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }




    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
