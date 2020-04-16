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
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.ui.TruckInfo.adapter.CarNumberAdapter;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:搜索
 */
public class SearchCarActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener   {


    private XListView list_1;
    private EditText et_search;
    private TextView tv_cancle,tv_search;
    private static final String TAG = "TruckInfoActivity";
    private ACache acache;
    private List<CarInfo> mGroupList;
    private List<CarInfo> mlist=new ArrayList<>();
    private CarNumberAdapter madapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_truck;
    }

    @Override
    protected void findViews() {
        mContext=this;
        acache= ACache.get(mContext,TAG);
        //        mGroupList=(List<CarInfo>)acache.getAsObject("truck_list");//缓存
        Bundle b=getIntent().getExtras();
        mGroupList=(List<CarInfo>)b.getSerializable("search_list");

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
                    List<CarInfo>list= doSearch(s.toString());
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);

                    }else{
                        List<CarInfo>list1=new ArrayList<>();
                        madapter.updateListView(list1);
                    }
                }else{
                    List<CarInfo>list2=new ArrayList<>();
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
        list_1=(XListView)findViewById(R.id.list_1);
        list_1.setPullRefreshEnable(false);
        list_1.setPullLoadEnable(false);
        list_1.setXListViewListener(this);
        madapter=new CarNumberAdapter(mContext,mlist);
        list_1.setAdapter(madapter);
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
                    List<CarInfo>list= doSearch(etStr);
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);

                    }else{
                        List<CarInfo>list3=new ArrayList<>();
                        madapter.updateListView(list3);
                        showTips("搜索不到数据");
                    }
                }else{
                    List<CarInfo>list4=new ArrayList<>();
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
    public List<CarInfo> doSearch(String etstr){
        List<CarInfo> list=new ArrayList<>();
            String str=etstr;
            for(int i=0;i<mGroupList.size();i++){
                CarInfo  info=mGroupList.get(i);
                String carNumber=  info.getCarNumber(); //车辆名称
                if(carNumber !=null ){
                    if(carNumber.contains(str)){
                        list.add(info);
                    }

                }

            }
        return list;

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
