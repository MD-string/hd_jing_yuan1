package com.hand.handtruck.ui.TruckInfo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hand.handlibray.util.NotNull;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.RealTimeTruckTask;
import com.hand.handtruck.fragment.BaseFragment;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.model.RealTimeTruckBean;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.utils.ACache;
import com.hand.handtruck.utils.CommonUtils;
import com.hand.handtruck.utils.DateUtil;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;
import com.hand.handtruck.view.MapContainer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *  实时定位  （车辆行驶轨迹）  高德地图
 * @author hxz
 */

public class LocusFragment extends BaseFragment implements OnClickListener {

	private BroadcastReceiver receiver;
	private Context context;
	private ACache acache;
	private MapView mMapTracking;
	private MapContainer map_container;
	private AMap mAMap;
	private CarInfo truckModel;
	private String sign;
	private String token;
	private CoordinateConverter converter;
	private LatLngBounds.Builder newbounds;
	private SharedPreferences sp;
	private PolylineOptions mPolylineOptions;
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private MarkerOptions markerOption;
	private DecimalFormat myformat;
	private String speed = "0.00";
	private String statusWeight = "0.00";
	private String gpsWeightStart = "0.00";
	private String gpsWeightEnd = "0.00";
	private String gpsWeight = "0.00";
	private CustomDialog dialog;
	private Marker addMarker;
	private MarkerOptions markerOptions;
	private RealTimeTruckBean mRealWeight;
	private ImageView tv_current_location;
	private GeocodeSearch geocodeSearch;
	@SuppressLint("HandlerLeak")
	Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (getActivity() == null || getActivity().isFinishing()) {
				return;
			}
			switch (msg.what) {
				//获取实时重量成功
				case ConstantsCode.MSG_REQUEST_SUCCESS2:
					LogUtil.e("在重量趋势界面获取实时重量成功");
					RealTimeTruckBean timeTruck = (RealTimeTruckBean) msg.obj;

					mRealWeight=timeTruck;

					if (markerOptions == null) {
						markerOptions = new MarkerOptions();
					}
					if (markerOptions != null

						//							&& !isDidffentDay(timeTruck)
					) {

						doshow(timeTruck,minfo);

						addNewAddress(timeTruck);
						//                        //地理搜索类
						LatLng latLng =new LatLng(Double.parseDouble(timeTruck.getY()), Double.parseDouble(timeTruck.getX()));
						getAddressByLatlng(latLng);
//						mAMap.inv();
					}
					break;
				//获取实时重量失败
				case ConstantsCode.MSG_REQUEST_FAIL2:
					LogUtil.e("在重量趋势界面获取实时重量失败");
					break;

				default:
					break;
			}
		}
	};


	private String simpleAddress;
	private TextView tv_current_address;
	private TextView tv_company_name;
	private TextView tv_order_carnuber,tv_ji_tuan,tv_location_time,tv_pake_weight,tv_car_speed,tv_out_address;
	private CarInfo minfo;
	private boolean isClick;
	private RelativeLayout rl_content_more,rl_fu_ceng;
	private ImageView img_go_more;
	private TextView tv_car_status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_locus, container, false);
		context=getActivity();
		acache= ACache.get(context);
		minfo=(CarInfo)acache.getAsObject("thruck_truckModel");
		initView(view);
		inIt(savedInstanceState);
		registerBrodcat();


		return view;
	}


	private void initView(View view) {

		rl_content_more=(RelativeLayout)view.findViewById(R.id.rl_content_more);
		rl_content_more.setVisibility(View.GONE);
		rl_fu_ceng=(RelativeLayout)view.findViewById(R.id.rl_fu_ceng);
		img_go_more=(ImageView)view.findViewById(R.id.img_go_more);
		rl_fu_ceng.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isClick){
					rl_content_more.setVisibility(View.VISIBLE);
					isClick=true;
					img_go_more.setImageDrawable(getResources().getDrawable(R.mipmap.icon_content_next));
				}else{
					rl_content_more.setVisibility(View.GONE);
					isClick=false;
					img_go_more.setImageDrawable(getResources().getDrawable(R.mipmap.icon_content_back));
				}
			}
		});
		img_go_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isClick){
					rl_content_more.setVisibility(View.VISIBLE);
					isClick=true;
					img_go_more.setImageDrawable(getResources().getDrawable(R.mipmap.icon_content_next));
				}else{
					rl_content_more.setVisibility(View.GONE);
					isClick=false;
					img_go_more.setImageDrawable(getResources().getDrawable(R.mipmap.icon_content_back));
				}
			}
		});
		tv_company_name=(TextView)view.findViewById(R.id.tv_company_name); //公司

		tv_order_carnuber=(TextView)view.findViewById(R.id.tv_order_carnuber); //车牌
		tv_ji_tuan=(TextView)view.findViewById(R.id.tv_ji_tuan);  //设备编号
		tv_location_time=(TextView)view.findViewById(R.id.tv_location_time); //定为时间
		tv_pake_weight=(TextView)view.findViewById(R.id.tv_pake_weight); //载重
		tv_car_speed=(TextView)view.findViewById(R.id.tv_car_speed);  //车速

		tv_out_address=(TextView)view.findViewById(R.id.tv_out_address); //卸货地址

		tv_car_status=(TextView)view.findViewById(R.id.tv_car_status);//车辆状态


		map_container=(MapContainer)view.findViewById(R.id.map_container);
		mMapTracking=(MapView)view.findViewById(R.id.map_tracking);


		tv_current_location=(ImageView)view.findViewById(R.id.tv_current_location);
		tv_current_location.setOnClickListener(this);
	}

	private void doshow(RealTimeTruckBean bean,CarInfo minfo) {
		String name=minfo.getParentName();
		if(Tools.isEmpty(name)){
			name="";
		}
		tv_company_name.setText(name);

		tv_order_carnuber.setText(bean.getCarNumber());
		tv_car_speed.setText(bean.getSpeed()+"km/h");
		tv_ji_tuan.setText(bean.getDeviceId()+"");
		tv_location_time.setText(bean.getDate()+"");
		tv_pake_weight.setText(bean.getWeight()+"吨");
//		tv_out_address.setText(bean.getAddress()+"");

		String status=bean.getStatus();
		if("0".equals(status)){ //不在线
			tv_car_status.setText("离线");
			tv_car_status.setTextColor(context.getResources().getColor(R.color.hd_red));
			tv_order_carnuber.setBackground(context.getResources().getDrawable(R.drawable.shape_form_blue));
		}else if("1".equals(status)){
			tv_car_status.setText("在线");
			tv_car_status.setTextColor(context.getResources().getColor(R.color.green));
			tv_order_carnuber.setBackground(context.getResources().getDrawable(R.drawable.shape_form_blue));
		}

	}

	private void inIt(Bundle savedInstanceState) {
		sp = context.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
		token = sp.getString("token", "");
		myformat = new DecimalFormat("0.00");
		sign=acache.getAsString("thruck_sign");
		truckModel=(CarInfo)acache.getAsObject("thruck_truckModel");

		mMapTracking.onCreate(savedInstanceState);// 此方法须覆写。
		//地图
		if (mAMap == null) {
			mAMap = mMapTracking.getMap();
		}
		UiSettings mUiSettings = mAMap.getUiSettings();
		mUiSettings.setZoomGesturesEnabled(true);//设置地图是否可以手势缩放大小
		mUiSettings.setScrollGesturesEnabled(true);//设置地图是否可以手势滑动
		mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mUiSettings.setZoomControlsEnabled(false);//设置地图默认的缩放按钮是否显示
		mUiSettings.setLogoBottomMargin(-100);//隐藏logo
		mAMap.moveCamera(CameraUpdateFactory.zoomTo(8));
		mPolylineOptions = new PolylineOptions();
		mPolylineOptions.width(10);
		mPolylineOptions.color(getResources().getColor(R.color.blue));


		/*将GPS定位坐标类型转换为高德*/
		converter = new CoordinateConverter(context);
		// CoordType.GPS 待转换坐标类型
		converter.from(CoordinateConverter.CoordType.GPS);
		newbounds = new LatLngBounds.Builder();

		doLocus();//开始定位


		geocodeSearch = new GeocodeSearch(context);
		geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
			@Override
			public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {//得到逆地理编码异步查询结果
				RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
				String formatAddress = regeocodeAddress.getFormatAddress();
				simpleAddress = formatAddress.substring(9);
				if(!Tools.isEmpty(simpleAddress)){
					tv_out_address.setText(simpleAddress+"");
				}else{
					tv_out_address.setText("");
				}
				DLog.e("LocusFragment","查询经纬度对应详细地址：\n" + simpleAddress);
			}

			@Override
			public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

			}
		});

	}


	private void doLocus() {
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("token", token);
		mapParam.put("carNumber", truckModel.getCarNumber());
		mapParam.put("deviceid", truckModel.getDeviceId());
		RealTimeTruckTask.getInstance(context, mhandler).getRealTimeTruckInfo(mapParam);

	}
	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();

			}

		};
		IntentFilter filter = new IntentFilter();
		//		filter.addAction(BleConstant.ACTION_BLE_HANDLER_DATA);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapTracking.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		}

	}

	private String dataConvert(long time) {
		long lcc_time = Long.valueOf(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date(lcc_time * 1000L));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			/*车辆实时定位*/
			case R.id.tv_current_location:
				if(CommonUtils.isFastDoubleClick()){//连续点击算一次
					return;
				}
				mAMap.clear();
				doLocus();
				break;
			default:
				break;
		}

	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(String title) {
		dialog = new CustomDialog(context,R.style.CustomDialog);
		dialog.show();
		new Thread("cancle_progressDialog") {
			@Override
			public void run() {
				try {
					Thread.sleep(7000);
					// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
					// 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
					if(dialog !=null ){
						dialog.cancel();
					}
					// dialog.dismiss();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
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

	public Bitmap  doAddBitmap(RealTimeTruckBean bean){
		View view = LayoutInflater.from(context).inflate(
				R.layout.map_usering, null);
		LinearLayout ll_wei=(LinearLayout)view.findViewById(R.id.ll_wei);
		ll_wei.setVisibility(View.VISIBLE);
		TextView tv_dev_number =(TextView)view.findViewById(R.id.tv_dev_number);
		tv_dev_number.setText(bean.getDeviceId()+"");
		TextView tv_car_number =(TextView)view.findViewById(R.id.tv_car_number);
		tv_car_number.setText(bean.getCarNumber()+"");
		TextView tv_loc_time =(TextView)view.findViewById(R.id.tv_loc_time);
		tv_loc_time.setText(bean.getDate()+"");
		tv_current_address =(TextView)view.findViewById(R.id.tv_current_address);
		tv_current_address.setVisibility(View.GONE);
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



	private void getAddressByLatlng(LatLng latLng) {
		//逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
		LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
		//异步查询
		geocodeSearch.getFromLocationAsyn(query);
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

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

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
			newbounds.include(gdlatLng);//轨迹全部显示在屏幕内                                                                                                                                                                                      h

			CameraUpdate mCameraUpdate = CameraUpdateFactory.changeLatLng(gdlatLng);//改变位置
			mAMap.moveCamera(mCameraUpdate);//第二个参数为四周留空宽度.
			mAMap.moveCamera(CameraUpdateFactory.zoomTo(13));
			//            mAMap.addPolyline(mPolylineOptions);
		}
	}
}
