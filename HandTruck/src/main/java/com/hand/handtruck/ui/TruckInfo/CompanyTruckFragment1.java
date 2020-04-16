package com.hand.handtruck.ui.TruckInfo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.ui.TruckInfo.adapter.CompanyTruckAdapter0;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.bean.CompanyInfo;
import com.hand.handtruck.ui.TruckInfo.bean.FirstCompanyInfo;
import com.hand.handtruck.ui.TruckInfo.bean.SecondCompanyInfo;
import com.hand.handtruck.ui.TruckInfo.prestener.CarListTask;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆
 */
public class CompanyTruckFragment1 extends BaseFragment implements View.OnClickListener, ICarListView, XListView.IXListViewListener  {


    private ExpandableListView list_1;
    private static final String TAG = "TruckInfoActivity";
    private  List<FirstCompanyInfo> mGroupList=new ArrayList<>();
    private List<CarInfo> mcarlist=new ArrayList<>();
    private CompanyTruckAdapter0 madapter;
    private CompanyTruckGroupBean bean;
    private Context mContext;
    private CarListTask cTask;
    private String token;
    private List<FirstCompanyInfo> pGList;
    private RelativeLayout rl_et;
    private ACache acache;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        mContext=getActivity();
        SharedPreferences sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);
        acache= ACache.get(mContext,"CompanyTruckFragment");

        list_1=(ExpandableListView)view.findViewById(R.id.list_1);
        rl_et=(RelativeLayout)view.findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("truck_list",(Serializable)pGList);
                CommonKitUtil.startActivity(getActivity(), SearchTruckActivity.class, bund, false);
                return false;
            }
        });
        madapter=new CompanyTruckAdapter0(mContext,mGroupList,mcarlist);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount =mGroupList.size();

        for (int i=0; i<groupCount; i++) {

            list_1.expandGroup(i);

        }
        list_1.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        list_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard(mContext,rl_et);
                return false;
            }
        });

        cTask=new CarListTask(mContext,this);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        cTask.getCarList(map);
        return view;
    }

    /**
     * 获取焦点,并隐藏或显示软键盘
     */
    public static void hideKeyBoard(Context context, View view) {
        /*隐藏或显示软键盘*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }


    @Override
    public void doSuccess(List<CarInfo> list) {
        pGList=doHandlerData(list);


        List<CarInfo> ncarlist = new ArrayList<>();
        for(int g=0;g<list.size();g++){
            CarInfo info2 = list.get( g);
            String remark2 = info2.getRemark();
            String id2 = info2.getId();
            String parent2 = info2.getParent();
            String text=info2.getText().trim();

            if ("car".equals(remark2) ) { //分公司
                CarInfo info22 = new CarInfo();
                info22.setText(text);
                info22.setId(id2);
                info22.setParent(parent2);
                info22.setRemark(remark2);
                info22.setIcon(info2.getIcon());
                ncarlist.add(info22);
            }
        }
        madapter.updateListView(pGList,ncarlist);
        for (int i=0; i<pGList.size(); i++) {

            list_1.expandGroup(i);

        }
        //        acache.put("company_truck_list",(Serializable) pGList);
        acache.put("car_list",(Serializable) ncarlist);
    }

    @Override
    public void doError(String msg) {

    }

    private  List<FirstCompanyInfo> doHandlerData(List<CarInfo> list) {
        List<FirstCompanyInfo> flist = new ArrayList<>(); //总公司
        List<CompanyInfo> compList = new ArrayList<>(); //分公司
        List<CarInfo> carlist = new ArrayList<>();   //车辆
        for(int g=0;g<list.size();g++){
            CarInfo info2 = list.get( g);
            String remark2 = info2.getRemark();
            String id2 = info2.getId();
            String parent2 = info2.getParent();
            String text=info2.getText().trim();
            String icon=info2.getIcon();

            if ("company".equals(remark2) && "#".equals(parent2)) { //吉林水泥集团
                FirstCompanyInfo finfo=new FirstCompanyInfo();
                finfo.setId(id2);
                String name=info2.getText();
                String companyName="";
                if(name.contains("公司")){
                    companyName=name.substring(0,name.indexOf("司")+1);
                }else if(name.contains("[")){
                    companyName=name.substring(0,name.indexOf("["));
                }

                finfo.setName(companyName);
                finfo.setRemark(remark2);
                flist.add(finfo);
            }

            if ("company".equals(remark2) ) { //分公司
                String name1=info2.getText();
                String companyName1="";
                if(name1.contains("公司")){
                    companyName1=name1.substring(0,name1.indexOf("司")+1);
                }else if(name1.contains("[")){
                    companyName1=name1.substring(0,name1.indexOf("["));
                }

                CompanyInfo info = new CompanyInfo();
                info.setName(companyName1);
                info.setId(id2);
                info.setIcon(icon);
                info.setRemark(remark2);
                info.setParent(parent2);
                compList.add(info);

            }

            if ("car".equals(remark2) ) { //车辆
                CarInfo info22 = new CarInfo();
                info22.setText(text);
                info22.setId(id2);
                info22.setIcon(icon);
                info22.setParent(parent2);
                info22.setRemark(remark2);
                carlist.add(info22);

            }
        }
        if(compList !=null && compList.size() >0){
            for(int k=0;k<compList.size();k++){
                CompanyInfo cin=compList.get(k);
                String kid=cin.getId();
                List<CarInfo> carinflist=new ArrayList<>();
                int number=0;
                for(int j=0;j<list.size();j++){
                    CarInfo info0 = list.get(j);
                    String remark0 = info0.getRemark();
                    String parent0 = info0.getParent();
                    String  status=info0.getIcon();
                    info0.setCarNumber(info0.getText()+"");
                    info0.setDeviceId(info0.getId()+"");
                    if("car".equals(remark0) && parent0.equals(kid)){
                        carinflist.add(info0);
                        if(status.contains("online")){
                            number++;
                        }
                    }

                }
                cin.setOnLineNumber(number);
                cin.setCarList(carinflist);
                compList.set(k,cin);

            }
        }
        acache.put("company_truck_list",(Serializable) compList);

        if(flist !=null && flist.size() >0){
            for(int u=0;u<flist.size();u++){
                FirstCompanyInfo finfo1=flist.get(u);
                String id1 = finfo1.getId();
                List<SecondCompanyInfo> secondList=new ArrayList<>();//二级 分公司
                for(int p=0;p<compList.size();p++){
                    //                    CompanyInfo cin=compList.get(p);
                    //                    String parentId=cin.getParent();
                    //                    if(id1.equals(parentId)){
                    //                        newList.add(cin);
                    //                    }

                    CompanyInfo cin1=compList.get(p);
                    String parentId=cin1.getParent();
                    String nid=cin1.getId();

                    if(id1.equals(parentId)){    //找出二级 分公司
                        SecondCompanyInfo info=new SecondCompanyInfo();
                        info.setId(cin1.getId());
                        info.setIcon(cin1.getIcon());
                        info.setParent(cin1.getParent());
                        info.setRemark(cin1.getRemark());
                        info.setName(cin1.getName());
                        info.setText(cin1.getText());
                        List<CompanyInfo> otherList = new ArrayList<>();
                        int totalNumber=0;
                        int number1=0;
                        for(int t=0;t<compList.size();t++){
                            CompanyInfo cint=compList.get(t);
                            String parentIdt=cint.getParent();

                            if(nid.equals(parentIdt)){
                                otherList.add(cint);

                                number1=number1+cint.getOnLineNumber();
                                List<CarInfo>cclist=cint.getCarList();
                                if(cclist !=null && cclist.size() >0){
                                    totalNumber=totalNumber+cclist.size();
                                }
                            }

                        }
                        int otherNumber=0;
                        int ohterOnline=0;
                        for(int k=0;k<carlist.size();k++){
                            CarInfo  infoc1=   carlist.get(k);
                            String status=infoc1.getIcon();
                            if(infoc1.getParent().equals(nid)){
                                otherNumber++;
                                if(!Tools.isEmpty(status) && status.contains("online")){
                                    ohterOnline++;
                                }
                            }
                        }

                        info.setCountNumber(totalNumber+otherNumber);
                        info.setSencondOnline(number1+ohterOnline);
                        info.setChildren(otherList);
                        secondList.add(info);

                    }

                }
                if(carlist !=null && carlist.size() >0){      // 一级车辆
                    for(int k=0;k<carlist.size();k++){
                        CarInfo  infoc=   carlist.get(k);
                        if(infoc.getParent().equals(id1)){
                            SecondCompanyInfo info1=new SecondCompanyInfo();
                            info1.setId(infoc.getId());
                            info1.setIcon(infoc.getIcon());
                            info1.setRemark(infoc.getRemark());
                            info1.setParent(infoc.getParent());
                            info1.setName(infoc.getText());
                            info1.setText(infoc.getText());
                            secondList.add(info1);
                        }
                    }
                }

                finfo1.setChildren(secondList);
            }

        }

        return flist;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
