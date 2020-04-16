package com.hand.handtruck.ui.TruckInfo;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.ui.TruckInfo.adapter.CompanyFirstAdapter;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.bean.CompanyInfo;
import com.hand.handtruck.ui.TruckInfo.bean.SecondCompanyInfo;
import com.hand.handtruck.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆详细页面
 */
public class CTruckFirstActivity extends Activity implements View.OnClickListener, XListView.IXListViewListener  {


    private ExpandableListView list_1;
    private static final String TAG = "TruckInfoActivity";
//    private  List<CarInfo> mGroupList=new ArrayList<>();
    private CompanyFirstAdapter madapter;
    private CompanyTruckGroupBean bean;
    private TextView mTvBack;
    private Context mContext;
    private String token;
    private SecondCompanyInfo mCinfo;
    private TextView tv_title;
    private List<CarInfo> mcarlist;
    private List<CompanyInfo> comList;
    private ACache acache;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_car_list_f);
        mContext=this;
        SharedPreferences sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);
        acache= ACache.get(mContext,"CompanyTruckFragment");

        Bundle b=getIntent().getExtras();
        mCinfo=(SecondCompanyInfo)b.getSerializable("first_info");
        comList= mCinfo.getChildren();
        if(comList ==null || comList.size() <=0){
            comList=new ArrayList<>();
        }
        mcarlist= (List<CarInfo>)acache.getAsObject("car_list");
//        mcarlist=(List<CarInfo>)b.getSerializable("first_list");
        if(mcarlist !=null && mcarlist.size() >0){
            String id=mCinfo.getId();
            for(int k=0;k<mcarlist.size();k++){
                CarInfo info=mcarlist.get(k);
                String parentId=info.getParent();
                String icon=info.getIcon();
                if(id.equals(parentId)){
                    CompanyInfo cin=new CompanyInfo();
                    cin.setId(info.getId());
                    cin.setParent(parentId);
                    cin.setName(info.getText());
                    cin.setIcon(icon);
                    cin.setTag("car");
                    comList.add(cin);
                }

            }

        }
//        mGroupList=mCinfo.getCarList();

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText(mCinfo.getName()+"");
        TextView tv_back=(TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_1=(ExpandableListView)findViewById(R.id.list_1);
        View v= LayoutInflater.from(this).inflate(R.layout.car_list_header, null);
        RelativeLayout  rl_et=(RelativeLayout)v.findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("search_list",(Serializable)comList);
                CommonKitUtil.startActivity(CTruckFirstActivity.this, SearchTruckActivity.class, bund, false);
                return false;
            }
        });
        list_1.addHeaderView(v);
        madapter=new CompanyFirstAdapter(mContext,comList);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount = list_1.getCount()-1;//减去头部
        for (int i=0; i<groupCount; i++)
        {
            list_1.collapseGroup(i);
        }

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
