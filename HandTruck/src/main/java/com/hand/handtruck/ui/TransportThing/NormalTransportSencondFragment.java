package com.hand.handtruck.ui.TransportThing;

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
import com.hand.handtruck.bean.PagerBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.TransportThing.adapter.XlistbAdapter;
import com.hand.handtruck.ui.TransportThing.bean.TransportBean;
import com.hand.handtruck.ui.TransportThing.presenter.OutLineTask;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 正常运输信息
 * @author hxz
 */

public class NormalTransportSencondFragment extends BaseFragment implements OnClickListener, XListView.IXListViewListener {


	private XListView list_normal;
	private Context mcontext;
	private XlistbAdapter madapter;
	private List<TransportBean> list=new ArrayList<>();
	private OutLineTask tranTask;
	private SharedPreferences sp;
	private HashMap<String, String> mapParams;
	private String token;
	private int currentPage;
	private List<TransportBean> tlist =new ArrayList<>();
	private String deviceId,endTime,startTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext=getActivity();
		 View view = inflater.inflate(R.layout.frag_trp_normal, container, false);
		deviceId= getArguments().getString("dev_id");
		endTime= getArguments().getString("end_time");
		startTime= getArguments().getString("start_time");
		initView(view);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView(View view) {
		list_normal=(XListView)view.findViewById(R.id.list_normal);
		list_normal.setPullRefreshEnable(true);
		list_normal.setPullLoadEnable(true);
		list_normal.setXListViewListener(this);
		list_normal.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(tlist !=null && tlist.size() >0 ){
//					TransportBean bean=	tlist.get(position-1);
//					String status=bean.getStatus();
//					if("2".equals(status)){
//						ToastUtil.getInstance().showCenterMessage(mcontext, "该车辆处于在途状态，暂不提供数据查询");
//					}else if("3".equals(status)){
//						ToastUtil.getInstance().showCenterMessage(mcontext, "该车辆处于在场状态，暂不提供数据查询");
//					}else{
//						MoreDetailsActivity.start(mcontext,bean);
//					}

				}else{
					ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
				}

			}
		});

		madapter=new XlistbAdapter(mcontext,list);
		list_normal.setAdapter(madapter);

		tranTask = OutLineTask.getInstance(mcontext, mHandler);
		sp = mcontext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
		initData(0);
	}
	private void initData(int first){
		mapParams = new HashMap<>();
		token = (String) sp.getString("token", null);
		mapParams.put("token", token);
		mapParams.put("nowPage","1");
		mapParams.put("pageSize","10");
		mapParams.put("status","1");

		if(!Tools.isEmpty(deviceId)){
			mapParams.put("deviceId",deviceId);
		}
		if(!Tools.isEmpty(startTime)){
			mapParams.put("startTime",startTime);
		}
		if(!Tools.isEmpty(endTime)){
			mapParams.put("endTime",endTime);
		}

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
				mapParams.put("status","1");
				if(!Tools.isEmpty(deviceId)){
					mapParams.put("deviceId",deviceId);
				}
				if(!Tools.isEmpty(startTime)){
					mapParams.put("startTime",startTime);
				}
				if(!Tools.isEmpty(endTime)){
					mapParams.put("endTime",endTime);
				}

				tranTask.loadMoreData(mapParams);
				onLoad();
			}
		}, 300);

	}

	private void onLoad() {
		list_normal.stopRefresh();
		list_normal.stopLoadMore();
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
					PagerBean transportbean = (PagerBean) msg.obj;
					tlist = transportbean.getContent();
					String totalPage=transportbean.getTotalPage();
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
								list_normal.setPullLoadEnable(false);
							}else{
								list_normal.setPullLoadEnable(true);
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
					PagerBean transportbean1 = (PagerBean) msg.obj;
					tlist= transportbean1.getContent();
					String t1=transportbean1.getTotalPage();
					if(tlist !=null && tlist.size() >0){

						madapter.updateListView(tlist);


						if(!Tools.isEmpty(t1)){
							int tPage1=Integer.parseInt(t1);
							if(tPage1 <=1){
								list_normal.setPullLoadEnable(false);
							}else{
								list_normal.setPullLoadEnable(true);
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
					PagerBean transportbean2 = (PagerBean) msg.obj;
					String tpage2=transportbean2.getTotalPage();
					List<TransportBean> tlist2= transportbean2.getContent();
					if(tlist2 !=null && tlist2.size() >0){

						for(int j=0;j<tlist2.size();j++){
							TransportBean bean=tlist2.get(j);
							tlist.add(bean);
						}

					}
					madapter.updateListView(tlist);
					if(!Tools.isEmpty(tpage2)){
						int tp2=Integer.parseInt(tpage2);
						if(currentPage <tp2){
							list_normal.setPullLoadEnable(true);
						}else{
							list_normal.setPullLoadEnable(false);
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
		list_normal.showErrorTip();
	}

	/**
	 * 上拉加载异常
	 */
	private void loadMoreNotData() {
		list_normal.disFooterView();
	}
	/**
	 * 下拉刷新异常
	 */
	private void loadRefreshError() {
		ToastUtil.getInstance().showCenterMessage(mcontext, "加载失败");
		onLoad();
	}
}
