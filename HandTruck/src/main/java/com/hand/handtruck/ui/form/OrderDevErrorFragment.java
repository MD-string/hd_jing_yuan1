package com.hand.handtruck.ui.form;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.form.adapter.FormAdapter;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.PagerOrderBean;
import com.hand.handtruck.ui.form.bean.SearchBean;
import com.hand.handtruck.ui.form.presenter.FormDevErrorTask;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 设备异常
 * @author hxz
 */

public class OrderDevErrorFragment extends BaseFragment implements OnClickListener,XListView.IXListViewListener{


    private XListView list_1;
    private Context mcontext;
    private List<FormBean> list=new ArrayList<>();
    private FormAdapter madapter;
    private FormDevErrorTask tranTask;
    private HashMap<String, String> mapParams;
    private String token;
    private int currentPage=1;
    private SharedPreferences sp;
    private List<FormBean> tlist=new ArrayList<>();
    private ImageView tv_search;
    private BroadcastReceiver receiver;
    private boolean isSearch;
    private SearchBean sbean;
    private boolean isHidden;
    private TextView tv_current_page,tv_total_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=getActivity();
        View view = inflater.inflate(R.layout.fragment_form_list, container, false);
        initView(view);
        registerBrodcat();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        list_1=(XListView)view.findViewById(R.id.list_1);
        list_1.setPullRefreshEnable(true);
        list_1.setPullLoadEnable(true);
        list_1.setXListViewListener(this);
        list_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(tlist !=null && tlist.size() >0 ){
                    FormBean bean=	tlist.get(position-1);
                    String status=bean.getRailCheckStatus();

//                    if ("0".equals(status)){ //
//                        ToastUtil.getInstance().showCenterMessage(mcontext, "该车辆处于在途状态，暂不提供数据查询");
//                    }else{
                        FormInfoActivity.start(mcontext,bean);
//                    }

                }else{
                    ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
                }
            }
        });
        madapter=new FormAdapter(mcontext,list);
        list_1.setAdapter(madapter);

        tv_search=(ImageView)view.findViewById(R.id.tv_search); //搜索
        tv_search.setVisibility(View.VISIBLE);
        tv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bund=new Bundle();
                bund.putSerializable("form_list",(Serializable)tlist);
                CommonKitUtil.startActivity(getActivity(), SearchForActivity.class, bund, false);
            }
        });

        tv_current_page=(TextView)view.findViewById(R.id.tv_current_page);//当前页面
        tv_total_page=(TextView)view.findViewById(R.id.tv_total_page); //总页码

        tranTask = FormDevErrorTask.getInstance(mcontext, mHandler);
        sp = mcontext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        initData(0);

    }

    private void initData(int first){
        mapParams = new HashMap<>();
        token = (String) sp.getString("token", null);
        mapParams.put("token", token);
        mapParams.put("nowPage","1");
        mapParams.put("pageSize","10");
        mapParams.put("railCheckStatus","-1");
//        if(isSearch  && sbean !=null){
//            mapParams.put("startTime",sbean.getStartTime());
//            mapParams.put("endTime",sbean.getEndTime());
//            mapParams.put("custName",sbean.getCustName());
//            mapParams.put("address",sbean.getAddress());
//            mapParams.put("carNumber",sbean.getCarNumber());
//        }
        if(0 == first){
            tranTask.getTransportList(mapParams);
        }else{
            tranTask.pullListData(mapParams);
        }
    }


    @Override
    public void onClick(View v) {
    }

    //刷新
    @Override
    public void onRefresh() {  //方法无回调？
        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调网络接口 请求数据
                initData(1);
                onLoad();
                //				isRefresh=true;
            }
        }, 300);
    }
    //加载更多
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
                mapParams.put("railCheckStatus","-1");
//                if(isSearch  && sbean !=null){
//                    mapParams.put("startTime",sbean.getStartTime());
//                    mapParams.put("endTime",sbean.getEndTime());
//                    mapParams.put("custName",sbean.getCustName());
//                    mapParams.put("address",sbean.getAddress());
//                    mapParams.put("carNumber",sbean.getCarNumber());
//                }
                tranTask.loadMoreData(mapParams);
                onLoad();
            }
        }, 300);

    }

    private void onLoad() {
        list_1.stopRefresh();
        list_1.stopLoadMore();
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    tlist.clear();
                    currentPage = 1;
                    PagerOrderBean transportbean = (PagerOrderBean) msg.obj;
                    String totalPage=transportbean.getTotalPage();
                    String totalElement=transportbean.getTotalElement();
                    tlist = transportbean.getContent();
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
                    if(tlist !=null && tlist.size() >0){

                        madapter.updateListView(tlist);

                        if(!Tools.isEmpty(totalPage)){
                            int tPage=Integer.parseInt(totalPage);
                            if(tPage <=1){
                                list_1.setPullLoadEnable(false);
                            }else{
                                list_1.setPullLoadEnable(true);
                            }
                        }
                        tv_current_page.setText(currentPage+"");
                        tv_total_page.setText("/"+totalPage);

                        Intent erIntent=new Intent(ConstantsCode.COMON_BRO_DEV_ERROR);
                        erIntent.putExtra("dev_error",totalElement);
                        mcontext.sendBroadcast(erIntent);
                    }else{
                        ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
                    }
                    break;


                case ConstantsCode.MSG_REQUEST_FAIL:
                    ToastUtil.getInstance().showCenterMessage(mcontext, "获取数据失败");
                    break;
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    tlist.clear();
                    currentPage = 1;
                    PagerOrderBean transportbean1 = (PagerOrderBean) msg.obj;
                    String tp1=transportbean1.getTotalPage();
                    String totalElement1=transportbean1.getTotalElement();
                    tlist= transportbean1.getContent();
                    if(tlist !=null && tlist.size() >0){

                        madapter.updateListView(tlist);

                        if(!Tools.isEmpty(tp1)){
                            int tPage1=Integer.parseInt(tp1);
                            if(tPage1 <=1){
                                list_1.setPullLoadEnable(false);
                            }else{
                                list_1.setPullLoadEnable(true);
                            }
                        }
                        tv_current_page.setText(currentPage+"");
                        tv_total_page.setText("/"+tp1);

                        Intent erIntent=new Intent(ConstantsCode.COMON_BRO_DEV_ERROR);
                        erIntent.putExtra("dev_error",totalElement1);
                        mcontext.sendBroadcast(erIntent);
                    }else{
                        ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
                    }
                    break;

                case ConstantsCode.MSG_REQUEST_FAIL1:
                    ToastUtil.getInstance().showCenterMessage(mcontext, "数据刷新失败");
                    break;
                case ConstantsCode.MSG_REQUEST_SUCCESS2:
                    currentPage ++;
                    PagerOrderBean transportbean2 = (PagerOrderBean) msg.obj;
                    String tpage2=transportbean2.getTotalPage();
                    String totalElement2=transportbean2.getTotalElement();
                    List<FormBean> tlist2= transportbean2.getContent();
                    if(tlist2 !=null && tlist2.size() >0){

                        for(int j=0;j<tlist2.size();j++){
                            FormBean bean=tlist2.get(j);
                            tlist.add(bean);
                        }

                    }
                    madapter.updateListView(tlist);
                    if(!Tools.isEmpty(tpage2)){
                        int tp2=Integer.parseInt(tpage2);
                        if(currentPage <tp2){
                            list_1.setPullLoadEnable(true);
                        }else{
                            list_1.setPullLoadEnable(false);
                        }

                    }
                    tv_current_page.setText(currentPage+"");
                    tv_total_page.setText("/"+tpage2);

                    Intent erIntent=new Intent(ConstantsCode.COMON_BRO_DEV_ERROR);
                    erIntent.putExtra("dev_error",totalElement2);
                    mcontext.sendBroadcast(erIntent);
                    break;

                case ConstantsCode.MSG_REQUEST_FAIL2:
                    loadMoreError();
                    ToastUtil.getInstance().showCenterMessage(mcontext, "数据加载失败");
                    break;
                case ConstantsCode.MSG_REQUEST_EMPTY:
                    ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
                    break;
                default:

                    break;

            }
        }
    };


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
        ToastUtil.getInstance().showCenterMessage(mcontext, "加载失败");
        onLoad();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            isHidden=true;
        }else{
            isHidden=false;
        }
    }
    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(ConstantsCode.COMON_BRO_DATA)){
                    SearchBean   sbean=(SearchBean)intent.getSerializableExtra("search_bean");
                    isSearch=true;
//                    if(isHidden) {
                        HashMap hap = new HashMap<>();
                        hap.put("token", token);
                        hap.put("nowPage", "1");
                        hap.put("pageSize", "50");
                         hap.put("railCheckStatus","-1");
                        hap.put("startTime", sbean.getStartTime());
                        hap.put("endTime", sbean.getEndTime());
                        hap.put("custName", sbean.getCustName());
                        hap.put("address", sbean.getAddress());
                        hap.put("carNumber", sbean.getCarNumber());
                        tranTask.getTransportList(hap);
//                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsCode.COMON_BRO_DATA);
        getActivity().registerReceiver(receiver, filter);
    }
//    @Override
//    public void onBaseRefresh() {
//        isSearch=false;
//        sbean=null;
//        super.onBaseRefresh();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
