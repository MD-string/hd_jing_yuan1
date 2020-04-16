package com.hand.handtruck.ui.Examine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.Examine.adapter.ReExamineAdapter;
import com.hand.handtruck.ui.Examine.presenter.ReAndColTask;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.PagerOrderBean;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 调研取证
 * @author hxz
 */

public class ResearchAndColFragment extends BaseFragment implements OnClickListener,XListView.IXListViewListener{


    private XListView list_res;
    private Context mcontext;
    private List<FormBean> list=new ArrayList<>();
    private ReExamineAdapter madapter;
    private ReAndColTask tranTask;
    private HashMap<String, String> mapParams;
    private String token;
    private int currentPage=1;
    private SharedPreferences sp;
    private List<FormBean> tlist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=getActivity();
        View view = inflater.inflate(R.layout.fragment_res_ex, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        list_res=(XListView)view.findViewById(R.id.list_res);
        list_res.setPullRefreshEnable(true);
        list_res.setPullLoadEnable(true);
        list_res.setXListViewListener(this);
        list_res.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                if(tlist !=null && tlist.size() >0 ){
//                    FormBean bean=	tlist.get(position-1);
//                    String status=bean.getOrderStatus().getRailCheckStatus();
//
//                        FormInfoActivity.start(mcontext,bean);
//
//                }else{
//                    ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
//                }
                FormBean bean=	tlist.get(position-1);
                ResearchExamineActivity.startReaAndColAct(mcontext,bean);
            }
        });
        madapter=new ReExamineAdapter(mcontext,list);
        list_res.setAdapter(madapter);


        tranTask = ReAndColTask.getInstance(mcontext, mHandler);
        sp = mcontext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        initData(0);

    }

    private void initData(int first){
        mapParams = new HashMap<>();
        token = (String) sp.getString("token", null);
        mapParams.put("token", token);
        mapParams.put("nowPage","1");
        mapParams.put("pageSize","10");
        mapParams.put("railCheckStatus","-2");
        mapParams.put("detailStatus","0");
        mapParams.put("orderCheckStatus","1");

        if(0 == first){
            tranTask.getTransportList(mapParams);
        }else{
            tranTask.pullListData(mapParams);
        }
    }


    @Override
    public void onClick(View v) {
    }
    @Override
    public void onBaseRefresh() {
        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调网络接口 请求数据
                initData(0);
                onLoad();
                //				isRefresh=true;
            }
        }, 50);
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
                mapParams.put("railCheckStatus","-2");
                mapParams.put("detailStatus","0");
                mapParams.put("orderCheckStatus","1");
                tranTask.loadMoreData(mapParams);
                onLoad();
            }
        }, 300);

    }

    private void onLoad() {
        list_res.stopRefresh();
        list_res.stopLoadMore();
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
                                list_res.setPullLoadEnable(false);
                            }else{
                                list_res.setPullLoadEnable(true);
                            }
                        }
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
                    tlist= transportbean1.getContent();
                    if(tlist !=null && tlist.size() >0){

                        madapter.updateListView(tlist);

                        if(!Tools.isEmpty(tp1)){
                            int tPage1=Integer.parseInt(tp1);
                            if(tPage1 <=1){
                                list_res.setPullLoadEnable(false);
                            }else{
                                list_res.setPullLoadEnable(true);
                            }
                        }
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
                            list_res.setPullLoadEnable(true);
                        }else{
                            list_res.setPullLoadEnable(false);
                        }

                    }
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
        list_res.showErrorTip();
    }

    /**
     * 上拉加载异常
     */
    private void loadMoreNotData() {
        list_res.disFooterView();
    }
    /**
     * 下拉刷新异常
     */
    private void loadRefreshError() {
        ToastUtil.getInstance().showCenterMessage(mcontext, "加载失败");
        onLoad();
    }
}
