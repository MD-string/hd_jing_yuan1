package com.hand.handtruck.ui.TruckInfo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.TruckInfo.adapter.CompanyAdapter;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.prestener.CarListTask;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆
 */
public class CompanyTruckFragment extends BaseFragment implements View.OnClickListener,ICarListView,XListView.IXListViewListener  {


    private XListView list_1;
    private static final String TAG = "TruckInfoActivity";
    private  List<CarInfo> mGroupList=new ArrayList<>();
    private CompanyAdapter madapter;
    private  CompanyTruckGroupBean bean;
    private TextView mTvBack;
    private Context mContext;
    private CarListTask cTask;
    private String token;
    private TextView tv_truck_number,tv_online_truck_number;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_car_list_1, container, false);
        mContext=getActivity();
        SharedPreferences sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);

        list_1=(XListView)view.findViewById(R.id.list_1);
        list_1.setPullRefreshEnable(false);
        list_1.setPullLoadEnable(false);
        list_1.setXListViewListener(this);
        RelativeLayout  rl_et=(RelativeLayout)view.findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("search_list",(Serializable)mGroupList);
                CommonKitUtil.startActivity(getActivity(), SearchCarActivity.class, bund, false);
                return false;
            }
        });
        View mView= LayoutInflater.from(mContext).inflate(R.layout.lvitem_company_1, null);
        tv_truck_number=(TextView)mView.findViewById(R.id.tv_truck_number);
        tv_online_truck_number=(TextView)mView.findViewById(R.id.tv_online_truck_number);
        list_1.addHeaderView(mView);
        madapter=new CompanyAdapter(mContext,mGroupList);
        list_1.setAdapter(madapter);


        cTask=new CarListTask(mContext,this);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        cTask.getCarList(map);
        return view;
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
    public void doSuccess(List<CarInfo> list) {
        mGroupList=doHandlerData(list);
        madapter.updateListView(mGroupList);

    }


    private  List<CarInfo> doHandlerData(List<CarInfo> list) {
        List<CarInfo> flist = new ArrayList<>(); // 集团
        for(int g=0;g<list.size();g++){
            CarInfo info2 = list.get( g);
            String remark2 = info2.getRemark();
            String id2 = info2.getId();
            String parent2 = info2.getParent();

            if ("company".equals(remark2) && "#".equals(parent2)) { //吉林水泥集团
                String name=info2.getText();
                String companyName=name.substring(0,name.indexOf("司")+1);
                CarInfo finfo=new CarInfo();
                finfo.setId(id2);
                finfo.setText(companyName);
                finfo.setRemark(remark2);
                flist.add(finfo);
            }

        }
        for(int i=0;i<flist.size();i++) {
            CarInfo c1 = flist.get(i);
            dohaveCarList(c1,list);
        }

        return flist;
    }

    public int dohaveCarList( CarInfo cinfo,List<CarInfo> list){
        CarInfo c1=cinfo;
        String pId=c1.getId();
        int number=0;
        int count=0;
//        List<CarInfo> newlist=new ArrayList<>();
//        for(int k=0;k<list.size();k++){
//            CarInfo info0 = list.get(k);
//            String remark0 = info0.getRemark();
//            String parent0 = info0.getParent();
//            String  status=info0.getIcon();
//            if("car".equals(remark0) && parent0.equals(pId)){
//                newlist.add(info0);
//                if(status.contains("online")){
//                    number++;
//                }
//            }else if("company".equals(remark0) && parent0.equals(pId)){
//                String name=info0.getText();
//                String companyName=name.substring(0,name.indexOf("司")+1);
//                info0.setText(companyName);
//                count=count+ dohaveCarList(info0,list);
//            }
//
//        }
//        c1.setOnlineNumeber(number+"");
//        c1.setList(newlist);

       return count;
    }

    @Override
    public void doError(String msg) {
        showTips(msg);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
