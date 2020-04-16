package com.hand.handtruck.ui.TruckInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hand.handlibray.util.NotNull;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDatePicker;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.Widget.RSTLOneMarkerView;
import com.hand.handtruck.bean.RealTimeTruckResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GPSDataTask;
import com.hand.handtruck.domain.TruckStatusTask;
import com.hand.handtruck.fragment.BaseFragment;
import com.hand.handtruck.model.GPSDataBean;
import com.hand.handtruck.model.OnLineTruckBean;
import com.hand.handtruck.model.RealTimeTruckBean;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.utils.DateUtil;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;
import com.hand.handtruck.view.MapContainer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * hxz
 * 历史查询
 * 2018-5-3
 * */
public class RTSLFragment extends BaseFragment implements View.OnClickListener {

	private ImageView mIvItem;
	private RelativeLayout mRlTitle;
	private MapContainer mMapContainer;
	private TextView  mTvTrendStartTime, mTvTrendEndTime;
	private Activity mContext;
	private TextView tv_no_weightData;
	private TextView mTvLandscape, mTvPortrait, mTvLocationUpdate;
	private Button mBtnQuery;
	private LineChart chart;
	private CustomDatePicker customDatePickerS;
	private CustomDatePicker customDatePickerE;
	private String token;
	private SharedPreferences sp = null;
	private CarInfo truckModel;
	//地图相关
	private MapView mMapTracking;
	private AMap mAMap;
	private PolylineOptions mPolylineOptions;
	private String sign;
	private List<GPSDataBean> gpsDataList;
	private MarkerOptions markerOption;
	private CoordinateConverter converter;
	private LatLngBounds.Builder newbounds;
	private Marker addMarker;
	private MarkerOptions markerOptions;
	private MarkerOptions markerOption1,markerOption2;
	private DecimalFormat mineformat= new DecimalFormat("0.0");
	private DecimalFormat myformat;
	private String speed = "0.00";
	private String statusWeight = "0.00";
	private String gpsWeightStart = "0.00";
	private String gpsWeightEnd = "0.00";
	private String gpsWeight = "0.00";
	private  CustomDialog dialog;


	private RelativeLayout ll_head;
	private TextView tv_1;

	private RealTimeTruckBean mRealWeight;

	private RelativeLayout rl_rooter,ll_root;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				//获取重量趋势成功
				case ConstantsCode.MSG_REQUEST_SUCCESS:
					//                    WeightTrendResultBean weightTrendResult = (WeightTrendResultBean) msg.obj;
					//                    List<WeightTrendBean> weightTrendList = weightTrendResult.getResult();
					//                    showResult(weightTrendList);
					break;
				//获取重量趋势失败
				case ConstantsCode.MSG_REQUEST_FAIL:


					break;
				//获取GPS成功
				case ConstantsCode.MSG_REQUEST_SUCCESS1:
					final	List<GPSDataBean> gDataList = ( List<GPSDataBean>) msg.obj;
					new Thread(new Runnable() {
						@Override
						public void run() {

							try {
								//去重
								gpsDataList=doDelDouble(gDataList);
								setMapTracking(gpsDataList,mRealWeight); //gps曲线

							}catch (Exception e){
								e.printStackTrace();
							}
						}
					}).start();

					break;
				//获取GPS失败
				case ConstantsCode.MSG_REQUEST_FAIL1:
					gpsDataList = new ArrayList<>();
					showTips("未获取到历史数据");
					closeProgressDialog();
					break;
				//获取实时重量成功
				case ConstantsCode.MSG_REQUEST_SUCCESS2:
					LogUtil.e("在重量趋势界面获取实时重量成功");
					RealTimeTruckResultBean realTimeTruckResult = (RealTimeTruckResultBean) msg.obj;
					RealTimeTruckBean timeTruck = realTimeTruckResult.getResult();

					mRealWeight=timeTruck;

					if (markerOptions == null) {
						markerOptions = new MarkerOptions();
					}
					if (markerOptions != null && !isDidffentDay(timeTruck)) {
						addNewAddress(timeTruck);
						//                        mAMap.invalidate();
					}
					break;
				//获取实时重量失败
				case ConstantsCode.MSG_REQUEST_FAIL2:
					LogUtil.e("在重量趋势界面获取实时重量失败");
					break;
				/*获取车辆实时及最后状态成功*/
				case ConstantsCode.MSG_REQUEST_SUCCESS3:
					LogUtil.e("在重量趋势界面获取车辆时及最后状态成功");
					//                    OnLineTruckResultBean onLineTruckBean = (OnLineTruckResultBean) msg.obj;
					//                    truckStatus = onLineTruckBean.getResult();

					break;
				//获取车辆实时及最后状态失败
				case ConstantsCode.MSG_REQUEST_FAIL3:
					LogUtil.e("在重量趋势界面获取车辆实时及最后状态失败");
					break;

			}
		}
	};
	private TextView mTvTrendDeviceId,mTvTrendWeight,mTvUnit;
	private RelativeLayout mLlTrendWeight;
	private TextView tv_weight_time,mTvTruckWeight,mTvWeightDate;
	private boolean isRootShow=false;
	private TextView tv_start_time,tv_end_time;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.activity_rstl_location, container, false);
		mContext=getActivity();
		findViews(view);
		setListeners();
		inIt(savedInstanceState);


		return view;
	}



	protected void findViews(View view) {
		//		mTvBack = (TextView)view.findViewById(R.id.tv_back);
		//		mTvBack.setVisibility(View.VISIBLE);
		//		mTvTitle = (TextView)view.findViewById(R.id.tv_title);
		//		mTvOperator = (TextView)view.findViewById(R.id.tv_operator);
		//		mIvItem = (ImageView)view.findViewById(R.id.iv_item);
		//		mRlTitle = (RelativeLayout)view.findViewById(R.id.rl_title);
		//		mTvOperator.setVisibility(View.VISIBLE);
		//		mTvOperator.setText("详细信息");
		inithead(view);
		initRoot(view);

		mLlTrendWeight = (RelativeLayout)view.findViewById(R.id.ll_trend_weight);
		mLlTrendWeight.setVisibility(View.GONE);
		mTvTrendWeight = (TextView)view.findViewById(R.id.tv_trend_weight);

		mTvUnit = (TextView)view.findViewById(R.id.tv_unit);
		mTvTrendDeviceId = (TextView)view.findViewById(R.id.tv_trend_device_id);
		tv_weight_time=(TextView)view.findViewById(R.id.tv_weight_time);
		//

		//        mBtnMapTrack = (Button) findViewById(R.id.btn_map_track);

		mMapTracking = (MapView) view.findViewById(R.id.map_tracking);
		mMapContainer = (MapContainer) view.findViewById(R.id.map_container);
		mMapContainer.setVisibility(View.VISIBLE);
		mTvLandscape = (TextView) view.findViewById(R.id.tv_landscape);
		mTvPortrait = (TextView) view.findViewById(R.id.tv_portrait);
		mTvLocationUpdate = (TextView) view.findViewById(R.id.tv_truck_location_update);

		//		tv_current_location=(ImageView)view.findViewById(R.id.tv_current_location);
		//		tv_current_location.setOnClickListener(this);

	}

	private void inithead(View view) {

		ll_head=(RelativeLayout)view.findViewById(R.id.ll_head);//头部
		tv_1=(TextView)view.findViewById(R.id.tv_1);

		mTvTrendStartTime = (TextView)view.findViewById(R.id.tv_trend_start_time_1); //开始时间
		mTvTrendEndTime = (TextView)view.findViewById(R.id.tv_trend_end_time_1);//终止时间
		mBtnQuery = (Button)view.findViewById(R.id.btn_query_1);//查询
	}
	private void initRoot(View view) {
		ll_root=(RelativeLayout)view.findViewById(R.id.ll_root);
		rl_rooter=(RelativeLayout)view.findViewById(R.id.rl_rooter);
		rl_rooter.setVisibility(View.GONE);
		chart = (LineChart)view.findViewById(R.id.chart);
		chart.setVisibility(View.GONE);
		ll_root.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isRootShow){
					rl_rooter.setVisibility(View.VISIBLE);
					isRootShow=true;
				}else{
					rl_rooter.setVisibility(View.GONE);
					isRootShow=false;
				}
			}
		});
		tv_no_weightData = (TextView)view.findViewById(R.id.tv_no_weightData);
		tv_no_weightData.setVisibility(View.VISIBLE);
		mTvTruckWeight=(TextView)view.findViewById(R.id.tv_quxian_weight);
		mTvWeightDate=(TextView)view.findViewById(R.id.tv_quxian_date);

		tv_start_time=(TextView)view.findViewById(R.id.tv_start_time);
		tv_end_time=(TextView)view.findViewById(R.id.tv_end_time);
	}

	protected void inIt(Bundle savedInstanceState) {
		Bundle bundle = mContext.getIntent().getExtras();
		truckModel = (CarInfo) bundle.getSerializable("truckModel");
		sign = bundle.getString("sign");
		//		mTvTitle.setText(truckModel.getCarNumber());

		mTvTrendDeviceId.setText(truckModel.getDeviceId());
		tv_weight_time.setText(truckModel.getDate());

		sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
		token = sp.getString("token", "");
		myformat = new DecimalFormat("0.00");
		mMapTracking.onCreate(savedInstanceState);// 此方法须覆写。
		//地图
		if (mAMap == null) {
			mAMap = mMapTracking.getMap();
		}
//		mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
//			@Override
//			public void onTouch(MotionEvent motionEvent) {
//				if(isRootShow){
//					rl_rooter.setVisibility(View.GONE);
//					isRootShow=false;
//				}
//			}
//		});
		UiSettings mUiSettings = mAMap.getUiSettings();
		mUiSettings.setZoomGesturesEnabled(true);//设置地图是否可以手势缩放大小
		mUiSettings.setScrollGesturesEnabled(true);//设置地图是否可以手势滑动
		mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mUiSettings.setLogoBottomMargin(-100);//隐藏logo
		mUiSettings.setZoomControlsEnabled(false);//设置地图默认的缩放按钮是否显示
//		mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));
		//		mPolylineOptions = new PolylineOptions();
		//		mPolylineOptions.width(10);
		//		mPolylineOptions.zIndex(10f);
		//		mPolylineOptions.color(getResources().getColor(R.color.blue));

		/*将GPS定位坐标类型转换为高德*/
		converter = new CoordinateConverter(mContext);
		// CoordType.GPS 待转换坐标类型
		converter.from(CoordinateConverter.CoordType.GPS);
		newbounds = new LatLngBounds.Builder();

		//每隔30秒请求重量
		//		timerTaskSpace30s();

		//        //每隔60秒左右请求车辆的状态
		//        mHandler.postDelayed(new Runnable() {
		//            @Override
		//            public void run() {
		//                timerTaskSpace58s();
		//            }
		//        }, 300);

		SimpleDateFormat sdf = getDate();
		Date date = new Date();
		String now = sdf.format(date);
		String end =mTvTrendEndTime.getText().toString();
		customDatePickerS = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
			@Override
			public void handle(String time) { // 回调接口，获得选中的时间
				mTvTrendStartTime.setText(time);
			}

			@Override
			public void cancle() {

			}
		}, "2010-01-01 00:00", end); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
		customDatePickerS.showSpecificTime(true); // 显示时和分
		customDatePickerS.setIsLoop(true); // 允许循环滚动
		customDatePickerE = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
			@Override
			public void handle(String time) { // 回调接口，获得选中的时间
				mTvTrendEndTime.setText(time);
			}

			@Override
			public void cancle() {

			}
		}, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
		customDatePickerE.showSpecificTime(true); // 显示时和分
		customDatePickerE.setIsLoop(true); // 允许循环滚动


		mBtnQuery.performClick();

	}

	protected void setListeners() {
		mTvTrendStartTime.setOnClickListener(this);
		mTvTrendEndTime.setOnClickListener(this);
		mBtnQuery.setOnClickListener(this);
		mTvLandscape.setOnClickListener(this);
		mTvPortrait.setOnClickListener(this);
		mTvLocationUpdate.setOnClickListener(this);
	}

	//GPS数据  除去 经纬度一样的数据
	private List<GPSDataBean> doDelDouble(List<GPSDataBean> gList){
		if(gList ==null || gList.size() <=0){
			return new ArrayList<>();
		}
		for(int i=0;i<gList.size();i++){
			GPSDataBean b1=gList.get(i);
			String x1=b1.getX();
			String y1=b1.getY();
			if(Tools.isEmpty(x1) || Tools.isEmpty(y1)){ //经纬度为空 去掉
				gList.remove(i);
			}else{
				if( Float.parseFloat(x1) ==0 || Float.parseFloat(y1)==0){ //经纬度 为0 去掉
					gList.remove(i);
				}
//				else{
//					for  ( int j=i+1; j<gList.size(); j++)  {
//						GPSDataBean b2=gList.get(j);
//						String x2=b2.getX();
//						String y2=b2.getY();
//						if  (x1.equals(x2) && y1.equals(y2))  {//经纬度 一样去掉
//							gList.remove(i);
//						}
//					}
//				}
			}

		}
		return gList;
	}


	private SimpleDateFormat getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

		//        Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		//        calendar.setTime(date);
		//        calendar.add(Calendar.DAY_OF_MONTH, 0);
		Date date = calendar.getTime();


		String now = sdf.format(date);
		mTvTrendEndTime.setText(now);


		Date date1 = new Date();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar1.add(Calendar.DAY_OF_MONTH, -1);
		date1 = calendar1.getTime();
		String yesterday = sdf.format(date1);
		mTvTrendStartTime.setText(yesterday);
		return sdf;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//起始时间
			case R.id.tv_trend_start_time_1:
				customDatePickerS.show(mTvTrendStartTime.getText().toString());
				break;
			//结束时间
			case R.id.tv_trend_end_time_1:
				customDatePickerE.show(mTvTrendEndTime.getText().toString());
				break;
			//            //查询车辆行驶轨迹
			//            case R.id.btn_map_track:
			//                mMapContainer.setVisibility(View.VISIBLE);
			//                mHandler.postDelayed(new Runnable() {
			//                    @Override
			//                    public void run() {
			////                        mScrollWeightTrend.fullScroll(ScrollView.FOCUS_DOWN);
			//                    }
			//                }, 300);
			//                GPSDataTask gpsDataTask = GPSDataTask.getInstance(mContext, mHandler);
			//                Map<String, String> mapParam = initMapParams();
			//                gpsDataTask.getGPSDataTask(mapParam);
			//                break;
			//查询重量折线图
			case R.id.btn_query_1:
				//                String count= mTvTrendDataCount.getText().toString();
				//                if(6000< Integer.valueOf(count)){
				//                    showTips("每次查询最多能返回6000条数据");
				//                    return;
				//                }

				//  mScrollWeightTrend.scrollTo(0, 800);// 改变滚动条的位置
				try{
					String startTime=mTvTrendStartTime.getText().toString();
					String endTime=mTvTrendEndTime.getText().toString();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
					Date date1 = sdf.parse(startTime);long start=date1.getTime();
					Date date2 = sdf.parse(endTime);long end=date2.getTime();
					boolean isSame=DateUtil.compareTwoDay(start,end);
					if(isSame){
						showTips("时间差不能大于2天");
						return;
					}

				}catch (Exception e){
					e.printStackTrace();
				}


				if(mAMap != null){
					mAMap.clear();
				}
				showProgressDialog("正在加载");

				//                WeightDataTask weightDataTask = WeightDataTask.getInstance(mContext, mHandler);
				//                Map<String, String> mapParams = initMapParams();
				//                weightDataTask.getWeightDataTask(mapParams);

				new Thread(new Runnable() {
					@Override
					public void run() {
						GPSDataTask gpsDataTask = GPSDataTask.getInstance(mContext, mHandler);
						Map<String, String> mapParam = initMapParams();
						gpsDataTask.getGPSDataTask(mapParam);  //试试异步
					}
				}).start();

				break;
			//			/*竖屏*/
			//			case R.id.tv_landscape:
			//				//取消当前网络请求
			//				cancelTimeNetTask();
			//				Bundle bundleL = new Bundle();
			//				bundleL.putSerializable("gpsList", (Serializable) gpsDataList);
			//				bundleL.putString("config", "portrait");
			//				bundleL.putString("token", token);
			//				bundleL.putString("deviceid", truckModel.getDeviceId());
			//				bundleL.putString("truckNum", truckModel.getCarNumber());
			//				bundleL.putString("sign", sign);
			//				CommonKitUtil.startActivityForResult(mContext, MapFullScreenActivity.class, bundleL, ConstantsCode.MSG_REQUEST_CODE);
			//
			//				break;
			//			/*横屏*/
			//			case R.id.tv_portrait:
			//				//取消当前网络请求
			//				cancelTimeNetTask();
			//				Bundle bundleP = new Bundle();
			//				bundleP.putSerializable("gpsList", (Serializable) gpsDataList);
			//				bundleP.putString("config", "landscape");
			//				bundleP.putString("token", token);
			//				bundleP.putString("deviceid", truckModel.getDeviceId());
			//				bundleP.putString("truckNum", truckModel.getCarNumber());
			//				bundleP.putString("sign", sign);
			//				CommonKitUtil.startActivityForResult(mContext, MapFullScreenActivity.class, bundleP, ConstantsCode.MSG_REQUEST_CODE);
			//				break;
			/*车辆实时及最后的位置刷新*/
			case R.id.tv_truck_location_update:
				Map<String, String> mapParamL = new HashMap<>();
				mapParamL.put("token", token);
				mapParamL.put("deviceId", truckModel.getDeviceId());
				TruckStatusTask.getInstance(mContext, mHandler).getTruckStatusInfo(mapParamL);

				break;
			//			/*车辆实时定位*/
			//			case R.id.tv_current_location:
			//				if(CommonUtils.isFastDoubleClick()){//连续点击算一次
			//					return;
			//				}
			//				mAMap.clear();
			////				timerTaskSpace30s();
			//
			//				rl_rooter.setVisibility(View.GONE);
			//				isRootShow=false;
			//				ll_head_content.setVisibility(View.GONE);
			//				tv_1.setTextColor(getResources().getColor(R.color.black));
			//				isHeadShow=false;
			//				break;

		}

	}



	//	/*取消定时*/
	//	private void cancelTimeNetTask() {
	//		OkHttpUtils.getInstance().cancelTag(this);
	//		if (timer != null) {
	//			timer.cancel();
	//			timer = null;
	//		}
	//		mHandler.removeCallbacksAndMessages(null);
	//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case ConstantsCode.MSG_REQUEST_CODE:
				//				timerTaskSpace30s();
				//每隔60秒左右请求车辆的状态
				//                mHandler.postDelayed(new Runnable() {
				//                    @Override
				//                    public void run() {
				//                        timerTaskSpace58s();
				//                    }
				//                }, 300);
				//                break;

		}
	}



	@NonNull
	private Map<String, String> initMapParams() {
		Map<String, String> mapParams = new HashMap<>();
		mapParams.put("token", token);
		mapParams.put("deviceId", truckModel.getDeviceId());
		mapParams.put("carNumber", truckModel.getCarNumber());
		mapParams.put("startTime", mTvTrendStartTime.getText().toString() + ":00");
		mapParams.put("endTime", mTvTrendEndTime.getText().toString() + ":00");
		mapParams.put("spaceTime", "30");
		//        mapParams.put("total", mTvTrendDataCount.getText().toString());
		return mapParams;
	}

	private void showResult(final List<GPSDataBean> glist) {
		mTvTruckWeight.setText(getString(R.string.truck_weight));
		mTvWeightDate.setText(getString(R.string.weight_inquire_date));
		chart.setVisibility(View.GONE);
		tv_no_weightData.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					int weightSize = glist.size();
					if (weightSize != 0) {
						final ArrayList<Entry> values = new ArrayList<Entry>();
						final ArrayList<String> dateValues = new ArrayList<String>();
						if (weightSize < 3) {
							return;
						}
						List<Float> weilist = new ArrayList<Float>();
						for (int i = 0; i < weightSize ; i++) {
							String date = glist.get(i).getDate();
							String deviceId = glist.get(i).getDeviceId();
//							String weight = glist.get(i).getWeight();
							String speed = glist.get(i).getSpeed();
							float weight1 = Float.valueOf(speed);
							//获取重量
							values.add(new Entry(i, weight1));
							dateValues.add(date);

							weilist.add(weight1);

						}
						DecimalFormat df = new DecimalFormat("0.00");//
						float  maxWei= Collections.max(weilist);//取最大值
						maxWei= Float.parseFloat(df.format(Math.ceil(maxWei)));//向上取整
						int  weiYU=(int)(maxWei/5);
						maxWei=5*(weiYU+1);
//						if(maxWei <50){
//							maxWei=50f;
//						}
						float midWei=maxWei/10f; //平均值
						midWei=Float.parseFloat(df.format(midWei));
						int size =weightSize;
						if(size <200){
							size=200;
						}
						float asize=Float.valueOf(size+"");

						Description description = new Description();
						description.setText("");
						description.setTextColor(Color.RED);
						description.setTextSize(15);
						chart.setDescription(description);//设置图表描述信息
						chart.setNoDataText("暂无数据");//没有数据时显示的文字
						chart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
						chart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
						chart.setDrawBorders(false);//禁止绘制图表边框的线
						//chart.setBorderColor(); //设置 chart 边框线的颜色。
						//chart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
						//chart.setLogEnabled(true);//打印日志
						//                        chart.notifyDataSetChanged();//刷新数据
						//                        chart.invalidate();//重绘


						final LineDataSet set1;
						//设置数据1  参数1：数据源 参数2：图例名称
						set1 = new LineDataSet(values, "");
						set1.setColor(Color.parseColor("#2066bd"));
						set1.setCircleColor(Color.RED);
						set1.setLineWidth(2f);//设置线宽
						set1.setCircleRadius(3f);//设置焦点圆心的大小
						set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
						//                            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
						set1.setHighlightEnabled(true);//是否禁用点击高亮线
						set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
						set1.setValueTextSize(9f);//设置显示值的文字大小
						set1.setDrawValues(false);//设置是否显示数据文字
						set1.setDrawFilled(false);//设置禁用范围背景填充
						set1.setDrawCircles(false);//设置是否显示点
						set1.setCubicIntensity(2f);//折线弯曲程度


						//获取此图表的x轴
						final XAxis xAxis = chart.getXAxis();
						xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
						xAxis.setDrawAxisLine(true);//是否绘制轴线
						xAxis.setDrawGridLines(false);//是否显示网格线(x轴上每个点对应的线)
						xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
						xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置

						xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
						xAxis.setLabelRotationAngle(10f);//设置x轴标签的旋转角度
						xAxis.setDrawLabels(false);//是否显示标签

						xAxis.setGranularity(1f);//设置X轴坐标之间的最小间隔


						//Y轴默认显示左右两个轴线
						//获取右边的轴线
						YAxis rightAxis = chart.getAxisRight();
						//设置图表右边的y轴禁用
						rightAxis.setEnabled(false);
						//获取左边的轴线
						final YAxis leftAxis = chart.getAxisLeft();
						leftAxis.setEnabled(true);
						//设置网格线为虚线效果
						leftAxis.enableGridDashedLine(10f, 10f, 0f);
						leftAxis.setGridColor(R.color.color3);
						leftAxis.setDrawGridLines(true);//是否显示网格线
						leftAxis.setAxisMinimum(0f);
						leftAxis.setAxisMaximum(maxWei);

						//是否绘制0所在的网格线
						leftAxis.setDrawZeroLine(false);
						leftAxis.setTextColor(R.color.back2);
						leftAxis.setGranularity(10f);//设置Y轴坐标之间的最小间隔
						leftAxis.setLabelCount(5,true);
						//                                                leftAxis.setXOffset(1);//设置Y轴偏移量
						leftAxis.setValueFormatter(new ValueFormatter() {
							@Override
							public String getFormattedValue(float value, AxisBase axis) {
								String str=mineformat.format((double)value);
								return  str ;
							}
						});

						chart.setTouchEnabled(true); // 设置是否可以触摸
						chart.setDragEnabled(true);// 是否可以拖拽
						chart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
						chart.setScaleXEnabled(true); //是否可以缩放 仅x轴
						chart.setScaleYEnabled(false); //是否可以缩放 仅y轴
						chart.setPinchZoom(false);  //设置x轴和y轴能否同时缩放。默认是否
						chart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
						chart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
						chart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
						chart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。


						Legend l = chart.getLegend();//图例
						l.setTextSize(10f);//设置文字大小
						l.setForm(Legend.LegendForm.CIRCLE);//正方形，圆形或线
						l.setFormSize(10f); // 设置Form的大小
						l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
						l.setFormLineWidth(10f);//设置Form的宽度
						l.setEnabled(false);


						final ArrayList<ILineDataSet> dataSets = new ArrayList<>();
						dataSets.add(set1);
						final LineData lineData = new LineData(dataSets);
						chart.setData(lineData);
						chart.setVisibleXRangeMaximum(asize);//设置在曲线图中显示的最大数量
						chart.invalidate();


					    mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setVisibility(View.VISIBLE);
                                tv_no_weightData.setVisibility(View.GONE);

								String startTime=mTvTrendStartTime.getText().toString();
								String endTime=mTvTrendEndTime.getText().toString();

								tv_start_time.setText(startTime);
								tv_end_time.setText(endTime);

                            }
                        });
						//点击图表时，执行语句
						chart.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//LogUtil.e("点击onClick: " + v.getScaleX());
								if (v.getScaleX() > 35) {
									set1.setDrawCircles(true);//设置是否显示点
								} else {
									set1.setDrawCircles(false);//设置是否显示点
								}
							}
						});

						chart.setOnChartGestureListener(new OnChartGestureListener() {
							@Override
							public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

							}

							@Override
							public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

							}

							@Override
							public void onChartLongPressed(MotionEvent me) {

							}

							@Override
							public void onChartDoubleTapped(MotionEvent me) {

							}

							@Override
							public void onChartSingleTapped(MotionEvent me) {

							}

							@Override
							public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
								LogUtil.e("onChartFling");
							}

							@Override
							public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
								LogUtil.e("onChartScale");
							}

							@Override
							public void onChartTranslate(MotionEvent me, float dX, float dY) {
								LogUtil.e("onChartTranslate");
							}
						});

						//当值被选中的时候，执行语句
						chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
							@Override
							public void onValueSelected(Entry e, Highlight h) {
								LogUtil.e("onValueSelected");
								String weight  = myformat.format(e.getY());
								mTvTruckWeight.setText("车速:" + weight + "km/h");
								mTvWeightDate.setText("日期:" + dateValues.get((int) e.getX()));
								int dex=(int)e.getX();
								//提示当前重量
								RSTLOneMarkerView markview=new RSTLOneMarkerView(mContext,R.layout.pub_current_wei_1,weight);
								chart.setMarker(markview);

								if(glist !=  null && glist.size() >dex){
									GPSDataBean bean= glist.get(dex);   //显示当时车辆位置
									OnLineTruckBean obean=new OnLineTruckBean();
									obean.setDeviceId(bean.getDeviceId());
									obean.setWeight(bean.getWeight());
									obean.setGpsUploadDate(bean.getDate());
									obean.setX(bean.getX());
									obean.setY(bean.getY());
									if (markerOption1 == null) {
										markerOption1 = new MarkerOptions();
									}
									//                                    if (markerOption1 != null) {//刷新策略：重量差值小于5，5秒刷新一次，大于5，立马刷新
									//                                        if(dex > 2){
									//                                            GPSDataBean b1= glist.get(dex-2);
									//                                            float weight1=Float.parseFloat(String.valueOf(b1.getWeight()));
									//                                            float weight2=Float.parseFloat(String.valueOf(bean.getWeight()));
									//                                            float cha=Math.abs(weight1-weight2);
									//                                            if(cha >= 2){
									//                                                addAddr(markerOption1,obean);
									//                                            }
									//
									//                                        }else{
									addAddr(markerOption1,obean,bean.getSpeed());
									//                                        }
									//                                    }
								}

							}

							@Override
							public void onNothingSelected() {
							}
						});

					} else {
						chart.setVisibility(View.GONE);
						tv_no_weightData.setVisibility(View.VISIBLE);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/*车辆行驶轨迹*/
	private void setMapTracking(final List<GPSDataBean> gpsDataList,RealTimeTruckBean bean) {
		List<Integer> colorList=new ArrayList<>();
		List<LatLng> latList=new ArrayList<>();
		mPolylineOptions = new PolylineOptions();
		mPolylineOptions.width(10);
		mPolylineOptions.zIndex(10f);
		markerOption = new MarkerOptions();
		if (NotNull.isNotNull(bean)) {
			//			addTruckOnTimeMarker(bean);
		}
//		mAMap.setOnPolylineClickListener(new AMap.OnPolylineClickListener() {
//			@Override
//			public void onPolylineClick(Polyline polyline) {
//				float pid=polyline.getZIndex();
////				int dex=Integer.parseInt(pid);
//				if(gpsDataList !=  null && gpsDataList.size() >pid) {
//					GPSDataBean bean = gpsDataList.get(pid);   //显示当时车辆位置
//					OnLineTruckBean obean = new OnLineTruckBean();
//					obean.setDeviceId(bean.getDeviceId());
//					obean.setWeight(bean.getWeight());
//					obean.setGpsUploadDate(bean.getDate());
//					obean.setX(bean.getX());
//					obean.setY(bean.getY());
//					if (markerOption2 == null) {
//						markerOption2 = new MarkerOptions();
//					}
//					addAddr(markerOption2, obean);
//				}
//			}
//		});
		mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				LatLng latLngMarker = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
				newbounds.include(latLngMarker);
				CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(newbounds.build(), 15);
				//加上这句好像点击mark后不能显示在屏幕内
				// mAMap.animateCamera(mCameraUpdate);
				return false;
			}
		});

		mAMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				marker.hideInfoWindow();
			}
		});
		if (!NotNull.isNotNull(gpsDataList) || gpsDataList.size() <= 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					showTips("无GPS数据");
					closeProgressDialog();
				}
			});

			if (bean != null) {
				if (NotNull.isNotNull(bean.getY()) && NotNull.isNotNull(bean.getX())) {
					LatLng mCenterLatLng = new LatLng(Double.parseDouble(bean.getY()), Double.parseDouble(bean.getX()));
					CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(mCenterLatLng, 10, 0, 0));
					mAMap.animateCamera(mCameraUpdate);
				}
			}
			return;
		}
		double x=0;
		double y=0;
		for (int i = 0; i < gpsDataList.size(); i++) {
			// sourceLatLng待转换坐标点 LatLng类型
			if (NotNull.isNotNull(gpsDataList.get(i).getY())&&NotNull.isNotNull(gpsDataList.get(i).getX())){
				LatLng sourceLatLng = new LatLng(Double.parseDouble(gpsDataList.get(i).getY()), Double.parseDouble(gpsDataList.get(i).getX()));
				converter.coord(sourceLatLng);
				// 执行转换操作
				LatLng desLatLng = converter.convert();
//				mPolylineOptions.add(desLatLng);
				latList.add(desLatLng);
				colorList.add(getColorList(gpsDataList.get(i)));
				 x += sourceLatLng.latitude;
				 y += sourceLatLng.longitude;


				if (i == 0) {
					converter.coord(new LatLng(Double.parseDouble(gpsDataList.get(0).getY()), Double.parseDouble(gpsDataList.get(0).getX())));
					// 执行转换操作
					LatLng startLatLng = converter.convert();
					markerOption.position(startLatLng);
					if (NotNull.isNotNull(gpsDataList.get(0).getWeight())) {
						double gpsDataWeight = Double.parseDouble(gpsDataList.get(0).getWeight());
						gpsWeightStart = myformat.format(gpsDataWeight);
					}

					markerOption.title("设备ID:" + truckModel.getDeviceId() + "\n车牌号:" + truckModel.getCarNumber() + "\n车重:" + gpsWeightStart + "(吨)").snippet("日期:" + gpsDataList.get(0).getDate());
					markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.start)));
				} else if (i == gpsDataList.size() - 1) {
					converter.coord(new LatLng(Double.parseDouble(gpsDataList.get(gpsDataList.size() - 1).getY()), Double.parseDouble(gpsDataList.get(gpsDataList.size() - 1).getX())));
					// 执行转换操作
					LatLng endtLatLng = converter.convert();
					markerOption.position(endtLatLng);
					if (NotNull.isNotNull(gpsDataList.get(gpsDataList.size() - 1).getWeight())) {
						double gpsDataWeight = Double.parseDouble(gpsDataList.get(gpsDataList.size() - 1).getWeight());
						gpsWeightEnd = myformat.format(gpsDataWeight);
					}
					markerOption.title("设备ID:" + truckModel.getDeviceId() + "\n车牌号:" + truckModel.getCarNumber() + "\n车重:" + gpsWeightEnd + "(吨)").snippet("日期:" + gpsDataList.get(i).getDate());
					markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.end)));
				}
				markerOption.setFlat(true);
				mAMap.addMarker(markerOption);
				newbounds.include(desLatLng);//轨迹全部显示在屏幕内
			}
		}
		CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(new LatLng((double) (x/gpsDataList.size()),(double)(y/gpsDataList.size())));
		mAMap.animateCamera(mCameraUpdate);//第二个参数为四周留空宽度.
		mAMap.setMinZoomLevel(6f);
		mAMap.setMaxZoomLevel(13f);
		mAMap.moveCamera(CameraUpdateFactory.zoomTo(8));
		mPolylineOptions.addAll(latList);
		mPolylineOptions.colorValues(colorList);
		mAMap.addPolyline(mPolylineOptions);
		//        closeProgressDialog();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showResult(gpsDataList);  //重量曲线
//				rl_rooter.setVisibility(View.VISIBLE);
//				isRootShow=true;
				closeProgressDialog();
			}
		},1500);

	}
	//轨迹曲线颜色 根据 速度变化而变化(30以下 蓝色  30到100 绿色  100以上红色)
	public int getColorList(	GPSDataBean b0){
		int colorInt;
		String speed=b0.getSpeed();
		if(!Tools.isEmpty(speed)){
			float speedFlo=Float.parseFloat(speed);
			if(speedFlo <=30f){
				colorInt=Color.parseColor("#2066bd");
			}else if(speedFlo >30f  && speedFlo <=100f){
				colorInt=Color.parseColor("#33CC33");
			}else{
				colorInt=Color.parseColor("#ea5550");
			}
		}else{
			colorInt=Color.parseColor("#2066bd");
		}
		return colorInt;
	}
	//添加车辆实时位置
	private void addTruckOnTimeMarker(RealTimeTruckBean bean) {
		if (NotNull.isNotNull(bean.getY()) && NotNull.isNotNull(bean.getX())) {
			if (addMarker != null) {
				addMarker.remove();
			}

			Bitmap bitmap = doAddBitmap(bean);
			LatLng gpsLatLng = new LatLng(Double.parseDouble(bean.getY()), Double.parseDouble(bean.getX()));
			converter.coord(gpsLatLng);
			// 执行转换操作
			LatLng gdlatLng = converter.convert();
			markerOptions.position(gdlatLng);
			if (NotNull.isNotNull(bean.getSpeed())) {
				double truckSpeed = Double.parseDouble(bean.getSpeed());
				speed = myformat.format(truckSpeed);
			}
			if (NotNull.isNotNull(bean.getWeight())) {
				double wetght = Double.parseDouble(bean.getWeight());
				statusWeight = myformat.format(wetght);
			}
			//            markerOptions.title("设备ID:" + truckStatus.getDeviceId() + "\n车牌号:" + truckStatus.getCarNumber() + "\n速度:" + speed + "(公里/小时)" + "\n车重:" + statusWeight + "(吨)").snippet("日期:" + truckStatus.getGpsUploadDate());
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
			addMarker = mAMap.addMarker(markerOptions);
			newbounds.include(gdlatLng);//轨迹全部显示在屏幕内
		}
	}

	public Bitmap  doAddBit(OnLineTruckBean bean,String speed){
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.map_usering_rtsl, null);
		TextView  tSpeed=(TextView)view.findViewById(R.id.tv_current_speed);
		if(Tools.isEmpty(speed)){
			speed="0.0";
		}
		tSpeed.setText(speed+" km/h");
		Bitmap bitmap = convertViewToBitmap(view);
		return bitmap;
	}

	public Bitmap  doAddBitCompany(RealTimeTruckBean bean){
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.map_usering_data, null);
		//		TextView  tv_trend_device_id=(TextView)view.findViewById(R.id.tv_trend_device_id);
		//		tv_trend_device_id.setText(bean.getDeviceId());
		//		TextView  tv_trend_weight=(TextView)view.findViewById(R.id.tv_trend_weight);
		//		String wei=bean.getWeight();
		//		DecimalFormat df = new DecimalFormat("0.00");
		//		String wei2= df.format(Float.parseFloat(wei));
		//		tv_trend_weight.setText(wei2);
		//		TextView  tv_weight_time=(TextView)view.findViewById(R.id.tv_weight_time);
		//		tv_weight_time.setText(bean.getGpsUploadDate());
		Bitmap bitmap = convertViewToBitmap(view);
		return bitmap;
	}

	public Bitmap  doAddBitmap(RealTimeTruckBean bean){
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.map_usering_data, null);
		TextView  tv_trend_weight=(TextView)view.findViewById(R.id.tv_trend_weight);
		String wei=bean.getWeight();
		DecimalFormat df = new DecimalFormat("0.00");
		String wei2= df.format(Float.parseFloat(wei));
		tv_trend_weight.setText(wei2);
		TextView  tSpeed=(TextView)view.findViewById(R.id.tv_current_speed);
		String speed=bean.getSpeed();
		if(Tools.isEmpty(speed)){
			speed="0.0";
		}
		tSpeed.setText(speed+" km/h");
		Bitmap bitmap = convertViewToBitmap(view);
		return bitmap;
	}

	//添加车辆实时位置
	private void addNewAddress(RealTimeTruckBean bean) {
		if (NotNull.isNotNull(bean.getY()) && NotNull.isNotNull(bean.getX())) {
			if (addMarker != null) {
				addMarker.remove();
			}
			Bitmap bitmap = doAddBitmap(bean);
			LatLng gpsLatLng = new LatLng(Double.parseDouble(bean.getY()), Double.parseDouble(bean.getX()));
			converter.coord(gpsLatLng);
			// 执行转换操作
			LatLng gdlatLng = converter.convert();
			markerOptions.position(gdlatLng);

			//            markerOptions.title("设备ID:" + bean.getDeviceId() + "\n车牌号:" + bean.getCarNumber() + "\n速度:" + speed + "(公里/小时)" + "\n车重:" + statusWeight + "(吨)").snippet("日期:" + bean.getGpsUploadDate());
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
			addMarker = mAMap.addMarker(markerOptions);
			newbounds.include(gdlatLng);//轨迹全部显示在屏幕内

			CameraUpdate mCameraUpdate = CameraUpdateFactory.changeLatLng(gdlatLng);//改变位置
			mAMap.moveCamera(mCameraUpdate);//第二个参数为四周留空宽度.
			mAMap.moveCamera(CameraUpdateFactory.zoomTo(6f));
			//            mAMap.addPolyline(mPolylineOptions);
		}
	}

	//添加车辆当时位置
	private void addAddr(MarkerOptions opt,OnLineTruckBean bean,String speed) {
		if (NotNull.isNotNull(bean.getY()) && NotNull.isNotNull(bean.getX())) {
			if (addMarker != null) {
				addMarker.remove();
			}
			float zoom= mAMap.getCameraPosition().zoom;
			if(zoom <4){
				zoom=10f;
			}
			Bitmap bitmap = doAddBit(bean,speed);
			LatLng gpsLatLng = new LatLng(Double.parseDouble(bean.getY()), Double.parseDouble(bean.getX()));
			converter.coord(gpsLatLng);
			// 执行转换操作
			LatLng gdlatLng = converter.convert();
			opt.position(gdlatLng);

			//            markerOptions.title("设备ID:" + bean.getDeviceId() + "\n车牌号:" + bean.getCarNumber() + "\n速度:" + speed + "(公里/小时)" + "\n车重:" + statusWeight + "(吨)").snippet("日期:" + bean.getGpsUploadDate());
			opt.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
			addMarker = mAMap.addMarker(opt);
			newbounds.include(gdlatLng);//轨迹全部显示在屏幕内

			CameraUpdate mCameraUpdate = CameraUpdateFactory.changeLatLng(gdlatLng);//改变位置
			mAMap.moveCamera(mCameraUpdate);//第二个参数为四周留空宽度.
			mAMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
			//            mAMap.addPolyline(mPolylineOptions);
		}
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(String title) {
		dialog = new CustomDialog(mContext,R.style.CustomDialog);
		dialog.show();
//		new Thread("cancle_progressDialog") {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(7000);
//					// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
//					// 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
//					if(dialog !=null ){
//						dialog.cancel();
//					}
//					// dialog.dismiss();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}.start();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapTracking.getMap().clear();
		mMapTracking.onDestroy();
		mMapTracking = null;
	}


	public void  showTips(String tip){
		com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(mContext,tip);
	}

	/**
	 * view转bitmap
	 */
	private static Bitmap convertViewToBitmap(View view){
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}


	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//处理数据   不是当天数据 不去实时定位显示
	private boolean isDidffentDay(RealTimeTruckBean bean){
		boolean isSame=true;
		try{
			long currentTime = System.currentTimeMillis(); //获取当前时间
			String date=bean.getDate();
			Date parse = simpleDateFormat.parse(date);
			long time=parse.getTime();
			isSame= DateUtil.compareDay(time,currentTime);
			return isSame;
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
	}

}
