package com.hand.handtruck.ui.TruckInfo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.sortlistviewdemo.PinyinUtils;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.ui.TruckInfo.adapter.CompanyTruckAdapter2;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.ui.TruckInfo.bean.CompanyInfo;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:搜索
 */
public class SearchTruckActivity extends BaseActivity implements View.OnClickListener {


    private ExpandableListView list_1;
    private EditText et_search;
    private TextView tv_cancle,tv_search;
    private List<CompanyInfo> mGroupList=new ArrayList<>();
    private List<CompanyInfo> mNewList=new ArrayList<>();
    private CompanyTruckAdapter2 madapter;
    private ACache acache;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_truck_1;
    }

    @Override
    protected void findViews() {
        mContext=this;
        acache= ACache.get(mContext,"CompanyTruckFragment");
        Bundle b=getIntent().getExtras();
        if(mGroupList!=null ||mGroupList.size() >0){
            mGroupList.clear();
        }
//        mFirst=(List<FirstCompanyInfo>)b.getSerializable("truck_list");  ???
//        for(int i=0;i<mFirst.size();i++){
//            List<SecondCompanyInfo> newlist=mFirst.get(i).getChildren();
//            mGroupList.addAll(newlist);
//        }
        mGroupList = (  List<CompanyInfo>)acache.getAsObject("company_truck_list");

        et_search=(EditText)findViewById(R.id.et_search);
        tv_cancle=(TextView)findViewById(R.id.tv_cancle);
        tv_cancle.setVisibility(View.VISIBLE);
        tv_search=(TextView)findViewById(R.id.tv_search);
        tv_search.setVisibility(View.GONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str=s.toString();
                if(str !=null && !str.equals("")){
                    tv_cancle.setVisibility(View.GONE);
                    tv_search.setVisibility(View.VISIBLE);
                }else{
                    tv_cancle.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.GONE);
                }

                if(!Tools.isEmpty(s.toString())){
                    List<CompanyInfo>list= doSearch(s.toString());
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);
                        //遍历所有group,将所有项设置成默认展开
                        int groupCount =list.size();
                        for (int i=0; i<groupCount; i++)
                        {
                            list_1.expandGroup(i);
                        }

                    }else{
                        List<CompanyInfo>list1=new ArrayList<>();
                        madapter.updateListView(list1);
                    }
                }else{
                    List<CompanyInfo>list2=new ArrayList<>();
                    madapter.updateListView(list2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str1=s.toString();
                if(str1 !=null && !str1.equals("")){
                    tv_cancle.setVisibility(View.GONE);
                    tv_search.setVisibility(View.VISIBLE);
                }else{
                    tv_cancle.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.GONE);
                }
            }
        });
        list_1=(ExpandableListView)findViewById(R.id.list_1);
        madapter=new CompanyTruckAdapter2(mContext,mNewList);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount =mNewList.size();

        for (int i=0; i<groupCount; i++) {

            list_1.expandGroup(i);

        }
        list_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard(mContext,et_search);
                return false;
            }
        });
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
    protected void setListeners() {
        tv_cancle.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    protected void inIt() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_cancle:
                finish();
                break;
            //搜索
            case R.id.tv_search:
                String etStr=et_search.getText().toString().trim();
                if(!Tools.isEmpty(etStr)){
                    List<CompanyInfo>list= doSearch(etStr);
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);
                        //遍历所有group,将所有项设置成默认展开
                        int groupCount =list.size();
                        for (int i=0; i<groupCount; i++)
                        {
                            list_1.expandGroup(i);
                        }

                    }else{
                        List<CompanyInfo>list3=new ArrayList<>();
                        madapter.updateListView(list3);
                        showTips("搜索不到数据");
                    }
                }else{
                    List<CompanyInfo>list4=new ArrayList<>();
                    madapter.updateListView(list4);
                    showTips("搜索关键字为空");
                }
                break;
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }

    //搜索匹配首字母
    public List<CompanyInfo> doSearch(String etstr){
        List<CompanyInfo> list=new ArrayList<>();
        boolean isChinese= PinyinUtils.isContainChinese(etstr);
        if(isChinese){
            String str=etstr;//获取第一个字首字母
            for(int i=0;i<mGroupList.size();i++){
                boolean ishave=false;
                CompanyInfo comybean=mGroupList.get(i);
                String companyName=comybean.getName();//公司名称
                if(companyName.contains(etstr)){
                    list.add(comybean);
                }else{
                    CompanyInfo b=new CompanyInfo();
                    List<CarInfo> tlist=  mGroupList.get(i).getCarList(); //车辆名称
                    List<CarInfo> chlist= new ArrayList<>();
                    if(tlist !=null && tlist.size() > 0){
                        for(int j=0;j<tlist.size();j++){
                            CarInfo bean=tlist.get(j);
                            String Chname=bean.getText();
                            if(Chname.contains(str)){
                                chlist.add(bean);
                                ishave=true;
                            }

                        }
                    }
                    if(ishave){
                        b.setText(mGroupList.get(i).getText());
                        b.setOnLineNumber(mGroupList.get(i).getOnLineNumber());
                        b.setName(mGroupList.get(i).getName());
                        b.setCarList(chlist);
                        list.add(b);
                    }
                }
            }
        }else{
            String str=etstr;//
            for(int i=0;i<mGroupList.size();i++){
                boolean ishave=false;
                CompanyInfo b=new CompanyInfo();
                List<CarInfo> tlist=  mGroupList.get(i).getCarList();
                List<CarInfo> chlist= new ArrayList<>();
                if(tlist !=null && tlist.size() > 0){
                    for(int j=0;j<tlist.size();j++){
                        CarInfo bean=tlist.get(j);
                        String Chname=bean.getText();
                        String Enname= PinyinUtils.converfirst(Chname)+Chname;//获取第一个字首字母
                        if(Enname.contains(str)){
                            chlist.add(bean);
                            ishave=true;
                        }

                    }
                }
                if(ishave){
                    b.setText(mGroupList.get(i).getText());
                    b.setOnLineNumber(mGroupList.get(i).getOnLineNumber());
                    b.setName(mGroupList.get(i).getName());
                    b.setCarList(chlist);
                    list.add(b);
                }
            }
        }
        return list;

    }
    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

}
