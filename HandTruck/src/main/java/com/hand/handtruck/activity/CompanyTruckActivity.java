package com.hand.handtruck.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.adapter.CompanyTruckAdapter;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆
 */
public class CompanyTruckActivity extends BaseActivity implements View.OnClickListener {


    private ExpandableListView list_1;
    private static final String TAG = "TruckInfoActivity";
    private ACache acache;
    private  List<CompanyTruckGroupBean> mGroupList;
    private CompanyTruckAdapter madapter;
    private  CompanyTruckGroupBean bean;
    private TextView mTvBack;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_truck;
    }

    @Override
    protected void findViews() {
        mContext=this;
        Bundle b=getIntent().getExtras();
        int position=b.getInt("click_position");
        int ptrue=position-2;//有2个位置是头部
        acache= ACache.get(mContext,TAG);
        mGroupList=new ArrayList<>();
        List<CompanyTruckGroupBean> clist=(List<CompanyTruckGroupBean>)acache.getAsObject("truck_list");//缓存
        if(clist !=null && clist.size() >0){
             bean=clist.get(ptrue);
        }else{
            bean=new CompanyTruckGroupBean();
        }
        mGroupList.add(bean);


        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);


        list_1=(ExpandableListView)findViewById(R.id.list_1);
        View v= LayoutInflater.from(this).inflate(R.layout.car_list_header, null);
        RelativeLayout  rl_et=(RelativeLayout)v.findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("search_list",(Serializable)mGroupList);
                CommonKitUtil.startActivity(CompanyTruckActivity.this, SearchTruckActivity.class, bund, false);
                return false;
            }
        });
        list_1.addHeaderView(v);
        madapter=new CompanyTruckAdapter(mContext,mGroupList);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount = list_1.getCount()-1;//减去头部
        for (int i=0; i<groupCount; i++)
        {
            list_1.expandGroup(i);
        }
    }


    @Override
    protected void setListeners() {
        mTvBack.setOnClickListener(this);
    }

    @Override
    protected void inIt() {

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
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }



}
