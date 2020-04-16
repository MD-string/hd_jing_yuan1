package com.hand.handtruck.ui.form;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.ui.form.adapter.FormSearchAdapter;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.PagerOrderBean;
import com.hand.handtruck.ui.form.presenter.FormSearchTask;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * describe:搜索
 */
public class SearchForActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener   {


    private XListView list_1;
    private EditText et_search;
    private TextView tv_cancle,tv_search;
    private static final String TAG = "TruckInfoActivity";
    private ACache acache;
    private List<FormBean> mGroupList=new ArrayList<>();
    private List<FormBean> mlist=new ArrayList<>();
    private FormSearchAdapter madapter;
    private FormSearchTask tranTask;
    private SharedPreferences sp;
    private String token;
    private int currentPage=1;
    private HashMap<String, String> mapParams;
    private String etStr;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_form;
    }

    @Override
    protected void findViews() {
        mContext=this;
        acache= ACache.get(mContext,TAG);
        //        mGroupList=(List<FormBean>)acache.getAsObject("truck_list");//缓存
//        Bundle b=getIntent().getExtras();
//        mGroupList=(List<FormBean>)b.getSerializable("form_list");

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
                    etStr=str;
                    initData(0,str);
                }else{
                    List<FormBean>list2=new ArrayList<>();
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
        list_1.setPullRefreshEnable(true);
        list_1.setPullLoadEnable(true);
        list_1.setXListViewListener(this);
        madapter=new FormSearchAdapter(mContext,mlist);
        list_1.setAdapter(madapter);
        list_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard(mContext,et_search);
                return false;
            }
        });
        sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);
        tranTask = FormSearchTask.getInstance(mContext, mHandler);
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    mGroupList.clear();
                    currentPage = 1;
                    PagerOrderBean transportbean = (PagerOrderBean) msg.obj;
                    String totalPage=transportbean.getTotalPage();
                    mGroupList = transportbean.getContent();
                    //					List<TransportBean> mlist =new ArrayList<>();
                    //					if(tlist!=null && tlist.size() > 0){
                    //						for(int i=0;i<tlist.size();i++){
                    //							TransportBean bean =tlist.get(i);
                    //							String status=bean.getStatus();
                    //							if("-1".equals(status)){
                    //								mlist.add(bean);
                    //							}
                    //
                    //						}
                    //					}
                    if(mGroupList !=null && mGroupList.size() >0){

                        madapter.updateListView(mGroupList);

                        if(!Tools.isEmpty(totalPage)){
                            int tPage=Integer.parseInt(totalPage);
                            if(tPage <=1){
                                list_1.setPullLoadEnable(false);
                            }else{
                                list_1.setPullLoadEnable(true);
                            }
                        }
                    }else{
                        ToastUtil.getInstance().showCenterMessage(mContext, "数据为空");
                    }
                    break;


                case ConstantsCode.MSG_REQUEST_FAIL:
                    ToastUtil.getInstance().showCenterMessage(mContext, "获取数据失败");
                    break;
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    mGroupList.clear();
                    currentPage = 1;
                    PagerOrderBean transportbean1 = (PagerOrderBean) msg.obj;
                    String tp1=transportbean1.getTotalPage();
                    mGroupList= transportbean1.getContent();
                    if(mGroupList !=null && mGroupList.size() >0){

                        madapter.updateListView(mGroupList);

                        if(!Tools.isEmpty(tp1)){
                            int tPage1=Integer.parseInt(tp1);
                            if(tPage1 <=1){
                                list_1.setPullLoadEnable(false);
                            }else{
                                list_1.setPullLoadEnable(true);
                            }
                        }
                    }else{
                        ToastUtil.getInstance().showCenterMessage(mContext, "数据为空");
                    }
                    break;

                case ConstantsCode.MSG_REQUEST_FAIL1:
                    ToastUtil.getInstance().showCenterMessage(mContext, "数据刷新失败");
                    break;
                case ConstantsCode.MSG_REQUEST_SUCCESS2:
                    currentPage ++;
                    PagerOrderBean transportbean2 = (PagerOrderBean) msg.obj;
                    String tpage2=transportbean2.getTotalPage();
                    List<FormBean> tlist2= transportbean2.getContent();
                    if(tlist2 !=null && tlist2.size() >0){

                        for(int j=0;j<tlist2.size();j++){
                            FormBean bean=tlist2.get(j);
                            mGroupList.add(bean);
                        }

                    }
                    madapter.updateListView(mGroupList);
                    if(!Tools.isEmpty(tpage2)){
                        int tp2=Integer.parseInt(tpage2);
                        if(currentPage <tp2){
                            list_1.setPullLoadEnable(true);
                        }else{
                            list_1.setPullLoadEnable(false);
                        }

                    }
                    break;

                case ConstantsCode.MSG_REQUEST_FAIL2:
                    loadMoreError();
                    ToastUtil.getInstance().showCenterMessage(mContext, "数据加载失败");
                    break;
                case ConstantsCode.MSG_REQUEST_EMPTY:
                    ToastUtil.getInstance().showCenterMessage(mContext, "数据为空");
                    break;
                default:

                    break;

            }
        }
    };

    /**
     * 获取焦点,并隐藏或显示软键盘
     */
    public static void hideKeyBoard(Context context, View view) {
        /*隐藏或显示软键盘*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    /**
     * 上拉加载异常
     */
    private void loadMoreError() {
        list_1.showErrorTip();
    }

    /**
     * 上拉加载异常
     */
    private void loadMoreNotData() {
        list_1.disFooterView();
    }
    /**
     * 下拉刷新异常
     */
    private void loadRefreshError() {
        ToastUtil.getInstance().showCenterMessage(mContext, "加载失败");
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
                 etStr=et_search.getText().toString().trim();
                if(!Tools.isEmpty(etStr)){
//                    List<FormBean>list= doSearch(etStr);  //搜索匹配工作
//                    if(list !=null && list.size() >0){
//                        madapter.updateListView(list);
//
//                    }else{
//                        List<FormBean>list3=new ArrayList<>();
//                        madapter.updateListView(list3);
//                        showTips("搜索不到数据");
//                    }
//                    mapParams = new HashMap<>();
//                    mapParams.put("token", token);
//                    mapParams.put("nowPage",String.valueOf(1));
//                    mapParams.put("pageSize","10");
//                    mapParams.put("searchKey",etStr);
//                    tranTask.getTransportList(mapParams);
                    initData(0,etStr);
                }else{
                    List<FormBean>list4=new ArrayList<>();
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
    public List<FormBean> doSearch(String etstr){
        List<FormBean> list=new ArrayList<>();
        String str=etstr;
        for(int i=0;i<mGroupList.size();i++){
            boolean isExist=false;
            FormBean  info=mGroupList.get(i);
            String carNumber=  info.getCarNumber(); //车辆名称
            String  companyName=info.getCustName(); //客户名称
            if(!isExist && carNumber !=null  ){
                if(carNumber.contains(str)){
                    list.add(info);
                    isExist=true;
                }
            }
            if(!isExist &&companyName !=null){
                if(companyName.contains(str)){
                    list.add(info);
                    isExist=true;
                }
            }

        }
        return list;

    }
    private void initData(int first,String ss){
        mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("nowPage","1");
        mapParams.put("pageSize","10");
        mapParams.put("searchKey",ss);
        if(0 == first){
            tranTask.getTransportList(mapParams);
        }else{
            tranTask.pullListData(mapParams);
        }
    }
    @Override
    public void onRefresh() {
        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调网络接口 请求数据
                initData(1,etStr);
                onLoad();
                //				isRefresh=true;
            }
        }, 300);
    }
    private void onLoad() {
        list_1.stopRefresh();
        list_1.stopLoadMore();
    }
    @Override
    public void onLoadMore() {
        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapParams = new HashMap<>();
                token = (String) sp.getString("token", null);
                mapParams.put("token", token);
                mapParams.put("nowPage",String.valueOf(currentPage+1));
                mapParams.put("pageSize","10");
                mapParams.put("searchKey",etStr);
                tranTask.loadMoreData(mapParams);
                onLoad();
            }
        }, 300);
    }
}
