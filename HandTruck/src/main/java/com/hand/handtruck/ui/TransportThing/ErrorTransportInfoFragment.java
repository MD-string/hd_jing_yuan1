package com.hand.handtruck.ui.TransportThing;

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
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.bean.PagerBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.TransportThing.adapter.TransportErrorLoadAdapter;
import com.hand.handtruck.ui.TransportThing.bean.TransportBean;
import com.hand.handtruck.ui.TransportThing.presenter.TransportErrorTask;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.SearchBean;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ACache;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 异常卸货
 * @author hxz
 */

public class ErrorTransportInfoFragment extends BaseFragment implements OnClickListener, XListView.IXListViewListener {


	private XListView list_error;
	private Context mcontext;
	private TransportErrorLoadAdapter madapter;
	private List<TransportBean> list=new ArrayList<>();
	private TransportErrorTask tranTask;
	private SharedPreferences sp;
	private HashMap<String, String> mapParams;
	private String token;
	private int currentPage;
	private List<TransportBean> tlist=new ArrayList<>();
	private TextView tv_current_page,tv_total_page;
	private int mpos;
	private BroadcastReceiver receiver;
	private boolean isSearch;
	private boolean isAllRead;
	private static final String TAG = "ErrorTransportInfoFragment";
	private ACache acache;
	private List<String> strlist=new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.frag_trp_error, container, false);
		initView(view);
		registerBrodcat();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private void initView(View view) {
		mcontext=getActivity();
		acache= ACache.get(mcontext,TAG);
		list_error=(XListView)view.findViewById(R.id.list_error);
		//		View headView = View.inflate(getActivity(), R.layout.frg_error_outproduct, null);
		//		list_error.addHeaderView(headView);
		list_error.setPullRefreshEnable(true);
		list_error.setPullLoadEnable(true);
		list_error.setXListViewListener(this);
		list_error.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(tlist !=null && tlist.size() >0 ){
					mpos=position;
					TransportBean bean=	tlist.get(position-1);
					String code=bean.getOrderCode()+"";


					if(Tools.isEmpty(code)){
						ToastUtil.getInstance().showCenterMessage(mcontext, "订单编号为空");
						return;
					}
					HashMap	mapParams1 = new HashMap<>();
					mapParams1.put("token", token);
					mapParams1.put("orderCode",code);
					tranTask.getOrderInfo(mapParams1);

					strlist.add(code);
					acache.put("ordercode_list",(Serializable) strlist);
				}else{
					ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
				}

			}
		});

		madapter=new TransportErrorLoadAdapter(mcontext,list);
		list_error.setAdapter(madapter);

		tv_current_page=(TextView)view.findViewById(R.id.tv_current_page);//当前页面
		tv_total_page=(TextView)view.findViewById(R.id.tv_total_page); //总页码

		tranTask = TransportErrorTask.getInstance(mcontext, mHandler);
		sp = mcontext.getSharedPreferences(ConstantsCode.FILE_NAME, 0);
		initData(0);
	}
	private void initData(int first){
		mapParams = new HashMap<>();
		token = (String) sp.getString("token", null);
		mapParams.put("token", token);
		mapParams.put("nowPage","1");
		mapParams.put("pageSize","10");
		mapParams.put("type","5");
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
				mapParams.put("type","5");
				tranTask.loadMoreData(mapParams);
				onLoad();
			}
		}, 300);

	}

	private void onLoad() {
		list_error.stopRefresh();
		list_error.stopLoadMore();
	}
	@SuppressLint("HandlerLeak")
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try{
				switch (msg.what) {
					case ConstantsCode.MSG_REQUEST_SUCCESS:
						tlist.clear();
						currentPage = 1;
						PagerBean transportbean = (PagerBean) msg.obj;
						tlist = transportbean.getContent();
						String totalPage = transportbean.getTotalPage();
						String totalElement = transportbean.getTotalElement();

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
						if (tlist != null && tlist.size() > 0) {

							if (!Tools.isEmpty(totalPage)) {
								int tPage = Integer.parseInt(totalPage);
								if (tPage <= 1) {
									list_error.setPullLoadEnable(false);
								} else {
									list_error.setPullLoadEnable(true);
								}
							}

							tv_current_page.setText(currentPage + "");
							tv_total_page.setText("/" + totalPage);
							if (!isAllRead) {
								Intent erIntent = new Intent(ConstantsCode.TRANSPORT_ERROR_TRANS);
								erIntent.putExtra("error_trans", totalElement);
								mcontext.sendBroadcast(erIntent);
							}

							for (int w = 0; w < tlist.size(); w++) {
								TransportBean tbean = tlist.get(w);
								String ordCode = tbean.getOrderCode();
								List<String> stlist = (List<String>) acache.getAsObject("ordercode_list");
								if (stlist != null && stlist.size() > 0) {
									DLog.e(TAG, "MSG_REQUEST_SUCCESS=" + stlist.size());

									for (int k2 = 0; k2 < stlist.size(); k2++) {
										String ocode = stlist.get(k2);
										if (!Tools.isEmpty(ordCode) && ordCode.equals(ocode)) {
											tbean.setRead(true);
											Intent readIntent = new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
											mcontext.sendBroadcast(readIntent);
										}
									}
								}

							}

							madapter.updateListView(tlist, isAllRead);
						} else {
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
						tlist = transportbean1.getContent();
						String totalPage1 = transportbean1.getTotalPage();
						String totalElement1 = transportbean1.getTotalElement();
						if (tlist != null && tlist.size() > 0) {

							madapter.updateListView(tlist, isAllRead);
							if (!Tools.isEmpty(totalPage1)) {
								int tPage1 = Integer.parseInt(totalPage1);
								if (tPage1 <= 1) {
									list_error.setPullLoadEnable(false);
								} else {
									list_error.setPullLoadEnable(true);
								}
							}


							tv_current_page.setText(currentPage + "");
							tv_total_page.setText("/" + totalPage1);
							if (!isAllRead) {
								Intent erIntent = new Intent(ConstantsCode.TRANSPORT_ERROR_TRANS);
								erIntent.putExtra("error_trans", totalElement1);
								mcontext.sendBroadcast(erIntent);
							}

							for (int p = 0; p < tlist.size(); p++) {
								TransportBean tbean = tlist.get(p);
								String ordCode = tbean.getOrderCode();
								List<String> stlist1 = (List<String>) acache.getAsObject("ordercode_list");
								if (stlist1 != null && stlist1.size() > 0) {
									DLog.e(TAG, "MSG_REQUEST_SUCCESS1=" + stlist1.size());
									for (int k1 = 0; k1 < stlist1.size(); k1++) {
										String ocode = stlist1.get(k1);
										if (!Tools.isEmpty(ordCode) && ordCode.equals(ocode)) {
											tbean.setRead(true);
											Intent readIntent = new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
											mcontext.sendBroadcast(readIntent);
										}
									}
								}

							}

							madapter.updateListView(tlist, isAllRead);

						} else {
							ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
						}
						break;

					case ConstantsCode.MSG_REQUEST_FAIL1:
						ToastUtil.getInstance().showCenterMessage(mcontext, "数据刷新失败");
						break;
					case ConstantsCode.MSG_REQUEST_SUCCESS2:
						currentPage++;
						PagerBean transportbean2 = (PagerBean) msg.obj;
						String tpage2 = transportbean2.getTotalPage();
						String totalElement2 = transportbean2.getTotalElement();
						List<TransportBean> tlist2 = transportbean2.getContent();
						if (tlist2 != null && tlist2.size() > 0) {

							for (int j = 0; j < tlist2.size(); j++) {
								TransportBean bean = tlist2.get(j);

								String ordCode = bean.getOrderCode();
								List<String> stlist2 = (List<String>) acache.getAsObject("ordercode_list");
								if (stlist2 != null && stlist2.size() > 0) {
									DLog.e(TAG, "MSG_REQUEST_SUCCESS2=" + stlist2.size());
									for (int k = 0; k < stlist2.size(); k++) {
										String ocode = stlist2.get(k);
										if (!Tools.isEmpty(ordCode) && ordCode.equals(ocode)) {
											bean.setRead(true);
											Intent readIntent = new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
											mcontext.sendBroadcast(readIntent);
										}
									}
								}

								tlist.add(bean);
							}

						}
						madapter.updateListView(tlist, isAllRead);
						if (!Tools.isEmpty(tpage2)) {
							int tp2 = Integer.parseInt(tpage2);
							if (currentPage < tp2) {
								list_error.setPullLoadEnable(true);
							} else {
								list_error.setPullLoadEnable(false);
							}

						}

						tv_current_page.setText(currentPage + "");
						tv_total_page.setText("/" + tpage2);

						if (!isAllRead) {

							Intent erIntent = new Intent(ConstantsCode.TRANSPORT_ERROR_TRANS);
							erIntent.putExtra("error_trans", totalElement2);
							mcontext.sendBroadcast(erIntent);
						}
						break;

					case ConstantsCode.MSG_REQUEST_FAIL2:
						loadMoreError();
						ToastUtil.getInstance().showCenterMessage(mcontext, "数据加载失败");
						break;
					case ConstantsCode.MSG_REQUEST_EMPTY:
						ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
						break;
					case ConstantsCode.MSG_REQUEST_SUCCESS3:
						FormBean formBean = (FormBean) msg.obj;
						AlramContentActivity.start(mcontext, formBean); //报警信息详情页面

						if (!isAllRead) {

							TransportBean bean = tlist.get(mpos - 1);
							bean.setRead(true);
							madapter.updateListView(tlist, isAllRead);
							Intent readIntent = new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
							mcontext.sendBroadcast(readIntent);
						}
						break;

					case ConstantsCode.MSG_REQUEST_SUCCESS4:

						tlist.clear();
						currentPage = 1;
						PagerBean transportbean4 = (PagerBean) msg.obj;
						tlist = transportbean4.getContent();
						String totalPage4 = transportbean4.getTotalPage();
						String totalElement4 = transportbean4.getTotalElement();
						if (tlist != null && tlist.size() > 0) {

							if (!Tools.isEmpty(totalPage4)) {
								int tPage4 = Integer.parseInt(totalPage4);
								if (tPage4 <= 1) {
									list_error.setPullLoadEnable(false);
								} else {
									list_error.setPullLoadEnable(true);
								}
							}

							tv_current_page.setText(currentPage + "");
							tv_total_page.setText("/" + totalPage4);
							if (!isAllRead) {
								Intent erIntent = new Intent(ConstantsCode.TRANSPORT_ERROR_TRANS);
								erIntent.putExtra("error_trans", totalElement4);
								mcontext.sendBroadcast(erIntent);
							}

							for (int i = 0; i < tlist.size(); i++) {
								TransportBean tbean = tlist.get(i);
								String ordCode = tbean.getOrderCode();
								List<String> stlist4 = (List<String>) acache.getAsObject("ordercode_list");
								if (stlist4 != null && stlist4.size() > 0) {
									DLog.e(TAG, "MSG_REQUEST_SUCCESS4=" + stlist4.size());
									for (int k = 0; k < stlist4.size(); k++) {
										String ocode = stlist4.get(k);
										if (!Tools.isEmpty(ordCode) && ordCode.equals(ocode)) {
											tbean.setRead(true);
											Intent readIntent = new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
											mcontext.sendBroadcast(readIntent);
										}
									}
								}

							}

							madapter.updateListView(tlist, isAllRead);

						} else {
							ToastUtil.getInstance().showCenterMessage(mcontext, "数据为空");
						}

						break;
					default:

						break;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	};



	/**
	 * 上拉加载异常
	 */
	private void loadMoreError() {
		list_error.showErrorTip();
	}

	/**
	 * 上拉加载异常
	 */
	private void loadMoreNotData() {
		list_error.disFooterView();
	}
	/**
	 * 下拉刷新异常
	 */
	private void loadRefreshError() {
		ToastUtil.getInstance().showCenterMessage(mcontext, "加载失败");
		onLoad();
	}


	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();
				if(action.equals(ConstantsCode.BRO_INFORMATION_SEARCH)){
					SearchBean sbean=(SearchBean)intent.getSerializableExtra("search_bean");
					isSearch=true;
					//                    if(isHidden) {
					HashMap hap = new HashMap<>();
					hap.put("token", token);
					hap.put("nowPage", "1");
					hap.put("pageSize", "50");
					hap.put("type","5");
					hap.put("startTime", sbean.getStartTime());
					hap.put("endTime", sbean.getEndTime());
					hap.put("custName", sbean.getCustName());
					hap.put("address", sbean.getAddress());
					hap.put("carNumber", sbean.getCarNumber());
					tranTask.getTransportList(hap);
					//                    }
				}else if(action.equals(ConstantsCode.TRANS_ERROR_ALL_READED)){
					for(int i=0;i<tlist.size();i++){
						TransportBean bean=	tlist.get(i);
						bean.setRead(true);
					}
					isAllRead=true;
					madapter.updateListView(tlist,isAllRead);
					strlist.clear();
					acache.put("ordercode_list",(Serializable) strlist);
				}
				else if(action.equals(ConstantsCode.DISCOVER_GET_EXAMINE)){
					//不延迟请求总报Token无效
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							//调网络接口 请求数据
							mapParams = new HashMap<>();
							token = (String) sp.getString("token", null);
							mapParams.put("token", token);
							mapParams.put("nowPage","1");
							mapParams.put("pageSize","10");
							mapParams.put("type","5");
							tranTask.dopullListData(mapParams);

							onLoad();
							//				isRefresh=true;
						}
					}, 300);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantsCode.BRO_INFORMATION_SEARCH);
		filter.addAction(ConstantsCode.TRANS_ERROR_ALL_READED);
		filter.addAction(ConstantsCode.DISCOVER_GET_EXAMINE);
		getActivity().registerReceiver(receiver, filter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		}
	}
}
