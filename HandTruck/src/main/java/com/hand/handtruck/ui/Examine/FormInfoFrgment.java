package com.hand.handtruck.ui.Examine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hand.handlibray.util.NotNull;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.Widget.MyMarkerView;
import com.hand.handtruck.bean.RealTimeTruckResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GPSDataTask;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.model.GPSDataBean;
import com.hand.handtruck.model.OnLineTruckBean;
import com.hand.handtruck.model.RealTimeTruckBean;
import com.hand.handtruck.ui.form.FormBasicDetailsActivity;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.WeiBean;
import com.hand.handtruck.ui.form.bean.WeiLan;
import com.hand.handtruck.ui.form.presenter.OrderInfoTask;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.DateUtil;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;
import com.hand.handtruck.view.MapContainer;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describe:重量曲线和地图
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class FormInfoFrgment extends BaseFragment implements View.OnClickListener {

    private TextView tv_back,tv_title,tv_operator;
    private CustomDialog dialog;
    private RelativeLayout mLlTrendWeight;
    private MapView mMapTracking;
    private RelativeLayout ll_root,rl_rooter;
    private LineChart chart;
    private boolean isRootShow;
    private TextView tv_no_weightData,mTvTruckWeight,mTvWeightDate;
    private AMap mAMap;
    private MarkerOptions markerOption;
    private CoordinateConverter converter;
    public Context mContext;
    private LatLngBounds.Builder newbounds;
    private Marker addMarker;
    private DecimalFormat mineformat= new DecimalFormat("#.0");
    final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
    private MarkerOptions markerOptions;
    private DecimalFormat myformat;
    private PolylineOptions mPolylineOptions;
    private FormBean bean;
    private List<GPSDataBean> gpsDataList=new ArrayList<>();
    private SharedPreferences sp;
    private  String token;
    private MarkerOptions markerOption1;
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String gpsWeightStart,gpsWeightEnd;
    private RealTimeTruckBean mRealWeight;
    private boolean isHaveWei; //是否存在围栏
    private TextView tv_quxian_speed;
    private LinearLayout ll_line_weight,ll_line_speed,ll_area;
    private ImageView img_line_weight,img_lien_speed,img_area;
    private TextView tv_lien_weight,tv_line_speed,tv_area;
    private boolean isWeght,isSpeed,isArea;
    private ArrayList<Entry> areaValues=new ArrayList<>();
    private  Polygon polygon;
    private  ArrayList<ILineDataSet> dataSets =new ArrayList<>();
    private ArrayList<Entry> speedValues=new ArrayList<>();
    private final ArrayList<Entry> wegValues=new ArrayList<>();
    private final  List<Float> weilist=new ArrayList<>();
    private  final    List<Float> speedList=new ArrayList<>();
    private  final ArrayList<String> dateValues=new ArrayList<>();
    private RelativeLayout rl_x_date;
    private TextView tv_start_time,tv_end_time;
    private OrderInfoTask ordTask;
    private HashMap<String, String> mapParams;
    private  final ArrayList<Circle> cicleList=new ArrayList<>();
    private   List<WeiBean> wBean;
    private ArrayList<LatLng> mlis;
    private String isCicre="0";
    private String idHaveCity="0";
    private    List<LatLng> llis=new ArrayList<>();

    @SuppressLint("HandlerLeak")
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
                    final  List<GPSDataBean> gDataList= (List<GPSDataBean>) msg.obj;
                    //					List<GPSDataBean>	gDataList = new ArrayList<>();

                    //去重
                    gpsDataList=doDelDouble(gDataList);

                    //获取 在围栏内的点
                    haveInputPoint(gpsDataList);


                    break;
                //获取GPS失败
                case ConstantsCode.MSG_REQUEST_FAIL1:
                    gpsDataList = new ArrayList<>();
                    showTips("未获取历史数据");
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
                    if (markerOptions != null) {
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
                /*获取车辆实时及最后状态成功*/
                case ConstantsCode.MSG_WEI_LAN_SUCCESS:
                    wBean= (List<WeiBean>) msg.obj;
                    doHaveLat(wBean);
                    break;
                //获取车辆实时及最后状态失败
                case ConstantsCode.MSG_WEI_LAN_FAIL:

                    break;
                //获取车辆实时及最后状态为空
                case ConstantsCode.MSG_REQUEST_EMPTY:
                    dogetData();
                    break;
            }
        }
    };

    private void haveInputPoint(final List<GPSDataBean> glist) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(areaValues !=null && areaValues.size() >0){
                        areaValues.clear();
                    }
                    int size =glist.size();

                    for (int i = 0; i < size ; i++) {
                        if("1".equals(isCicre)){
                            //判断点是否在围栏内
                            Double lat1=Double.parseDouble(glist.get(i).getY());
                            Double lat2=Double.parseDouble(glist.get(i).getX());
                            LatLng lat=new LatLng(lat1,lat2);
                            boolean isHaveIn=false;
                            //                            for(int k=0;k<wBean.size();k++){
                            //                                WeiBean wbena=wBean.get(k);
                            //                                String clat=wbena.getLat();
                            //                                String clon=wbena.getLng();
                            //                                String  cr=wbena.getR();
                            //                                Double clat1 = Double.parseDouble(clat);
                            //                                Double clon2 = Double.parseDouble(clon);
                            //                                LatLng latLng = new LatLng(clat1,clon2);
                            //                                boolean isIN= LatlngUtil.isInCircle(lat,latLng,Double.parseDouble(cr));
                            //                                if(isIN){
                            //                                    isHaveIn=true;
                            //                                }
                            //
                            //                            }
                            int csize=cicleList.size();
                            for(int j = 0; j < csize ; j++){
                                Circle cbean=cicleList.get(j);
                                if(cbean.contains(lat)){
                                    isHaveIn=true;
                                    break;
                                }

                            }
                            if(isHaveIn){
                                //获取重量
                                areaValues.add(new Entry(i, 5000f));
                            }else{
                                if("2".equals(idHaveCity)){
                                    //判断点是否在围栏内
                                    Double lat10=Double.parseDouble(glist.get(i).getY());
                                    Double lat20=Double.parseDouble(glist.get(i).getX());
                                    LatLng lat0=new LatLng(lat10,lat20);
                                    boolean isIN=PtInPolygon(lat0,llis);
                                    if(isIN){
                                        //获取重量
                                        areaValues.add(new Entry(i, 5000f));
                                    }else{
                                        //获取重量
                                        areaValues.add(new Entry(i, 0f));
                                    }

                                }
                            }
                        }else{
                            if("2".equals(idHaveCity)){
                                //判断点是否在围栏内
                                Double lat10=Double.parseDouble(glist.get(i).getY());
                                Double lat20=Double.parseDouble(glist.get(i).getX());
                                LatLng lat0=new LatLng(lat10,lat20);
                                boolean isIN=PtInPolygon(lat0,llis);
                                if(isIN){
                                    //获取重量
                                    areaValues.add(new Entry(i, 5000f));
                                }else{
                                    //获取重量
                                    areaValues.add(new Entry(i, 0f));
                                }

                            }else{
                                //获取重量
                                areaValues.add(new Entry(i, 0f));
                            }

                        }
                        //                        else{
                        //                            areaValues.clear();
                        //                        }

                        String wet = glist.get(i).getWeight();
                        float wet1 = Float.valueOf(wet);
                        weilist.add(wet1);
                        wegValues.add(new Entry(i, wet1));

                        String date = glist.get(i).getDate();
                        dateValues.add(date);

                        String speed = glist.get(i).getSpeed();
                        if(!Tools.isEmpty(speed)){
                            float speed1 = Float.valueOf(speed);
                            speedList.add(speed1);
                            speedValues.add(new Entry(i, speed1));
                        }

                    }

                    DecimalFormat df = new DecimalFormat("0.00");//
                    float  maxWei1= Collections.max(weilist);//取最大值
                    maxWei1= Float.parseFloat(df.format(Math.ceil(maxWei1)));//向上取整
                    int  weiYU1=(int)(maxWei1/5);
                    maxWei1=5*(weiYU1+1);

                    if(areaValues !=null && areaValues.size() >0){
                        for(int k=0 ;k<areaValues.size();k++){
                            float y=  areaValues.get(k).getY();
                            if(y==5000f){
                                areaValues.get(k).setY(maxWei1);
                            }
                        }

                    }
                    setMapTracking(glist,mRealWeight); //gps曲线
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }
    private TextView mTvTrendWeight,mTvUnit,mTvTrendDeviceId,tv_weight_time;
    private MapContainer mMapContainer;
    private TextView mTvLandscape,mTvPortrait,mTvLocationUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        View view = inflater.inflate(R.layout.activity_examine_info, container, false);
        sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        bean= (FormBean)getArguments().getSerializable("formBean");

        findViews(view);
        inIt(savedInstanceState);
        setListeners();
        return view;
    }

    protected void findViews(View view) {
        tv_back=(TextView)view.findViewById(R.id.tv_back);
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("订单详情");
        tv_operator=(TextView)view.findViewById(R.id.tv_operator);
        tv_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBasicDetailsActivity.start(mContext,bean);
            }
        });

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
        mMapContainer = (MapContainer)view. findViewById(R.id.map_container);
        mMapContainer.setVisibility(View.VISIBLE);
        mTvLandscape = (TextView)view. findViewById(R.id.tv_landscape);
        mTvPortrait = (TextView)view. findViewById(R.id.tv_portrait);
        mTvLocationUpdate = (TextView)view. findViewById(R.id.tv_truck_location_update);


    }

    private void initRoot(View view) {
        ll_root=(RelativeLayout)view.findViewById(R.id.ll_root);
        rl_rooter=(RelativeLayout)view.findViewById(R.id.rl_rooter);
        ll_root.setVisibility(View.VISIBLE);
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
        tv_quxian_speed=(TextView)view.findViewById(R.id.tv_quxian_speed);

        ll_line_weight=(LinearLayout)view.findViewById(R.id.ll_line_weight);
        img_line_weight=(ImageView)view.findViewById(R.id.img_line_weight);
        tv_lien_weight=(TextView)view.findViewById(R.id.tv_lien_weight);
        ll_line_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWeght){
                    img_line_weight.setImageDrawable(getResources().getDrawable(R.mipmap.line_weight));
                    tv_lien_weight.setTextColor(getResources().getColor(R.color.color4));
                    isWeght=false;
                }else{
                    img_line_weight.setImageDrawable(getResources().getDrawable(R.mipmap.line_weight_unselect));
                    tv_lien_weight.setTextColor(getResources().getColor(R.color.colorGray));
                    isWeght=true;
                }

                showResult(gpsDataList,isWeght,isSpeed,isArea);

                //                doshowLine(dataSets,isWeght,isSpeed,isArea);
            }
        });

        ll_line_speed=(LinearLayout)view.findViewById(R.id.ll_line_speed);
        img_lien_speed=(ImageView)view.findViewById(R.id.img_lien_speed);
        tv_line_speed=(TextView)view.findViewById(R.id.tv_line_speed);
        ll_line_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSpeed){
                    img_lien_speed.setImageDrawable(getResources().getDrawable(R.mipmap.line_speed));
                    tv_line_speed.setTextColor(getResources().getColor(R.color.color4));
                    isSpeed=false;
                }else{
                    img_lien_speed.setImageDrawable(getResources().getDrawable(R.mipmap.line_weight_unselect));
                    tv_line_speed.setTextColor(getResources().getColor(R.color.colorGray));
                    isSpeed=true;
                }
                showResult(gpsDataList,isWeght,isSpeed,isArea);

                //                doshowLine(dataSets,isWeght,isSpeed,isArea);
            }
        });

        ll_area=(LinearLayout)view.findViewById(R.id.ll_area);
        ll_area.setVisibility(View.GONE);
        img_area=(ImageView)view.findViewById(R.id.img_area);
        tv_area=(TextView)view.findViewById(R.id.tv_area);
        ll_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isArea){
                    img_area.setImageDrawable(getResources().getDrawable(R.mipmap.line_special_area));
                    tv_area.setTextColor(getResources().getColor(R.color.color4));
                    isArea=false;
                }else{
                    img_area.setImageDrawable(getResources().getDrawable(R.mipmap.line_special_area_un));
                    tv_area.setTextColor(getResources().getColor(R.color.colorGray));
                    isArea=true;
                }
                showResult(gpsDataList,isWeght,isSpeed,isArea);

                //                doshowLine(dataSets,isWeght,isSpeed,isArea);
            }
        });
        rl_x_date=(RelativeLayout)view.findViewById(R.id.rl_x_date);
        rl_x_date.setVisibility(View.GONE);
        tv_start_time=(TextView)view.findViewById(R.id.tv_start_time);
        tv_end_time=(TextView)view.findViewById(R.id.tv_end_time);

    }
    protected void setListeners() {
        tv_back.setOnClickListener(this);
    }
    //    public void  doshowLine(ArrayList<ILineDataSet> dataSets,boolean isW,boolean isP,boolean isA){
    //        if(dataSets !=null && dataSets.size() >0){
    //                if(isP){
    //                    dataSets.remove(0);
    //                }else{
    //                    addLineZreo(dataSets,speedValues);
    //                }
    //                if(isW){
    //                    dataSets.remove(1);
    //                }else{
    //                    addLineOne(dataSets,wegValues,false,getResources().getColor(R.color.hd_red));
    //                }
    //                if(isA){
    //                    dataSets.remove(2);
    //                }else{
    //                    addLineOne(dataSets,areaValues,true,getResources().getColor(R.color.hd_red));
    //                }
    //
    //            final LineData lineData = new LineData(dataSets);
    //            chart.setData(lineData);
    //            chart.invalidate();
    //        }
    //    }

    protected void inIt(Bundle savedInstanceState) {

        mTvTrendDeviceId.setText(bean.getDeviceId());
        tv_weight_time.setText(bean.getDeviceOrder().getLastDeviceTime());

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
        //					img_2.setImageResource(R.mipmap.icon_bo_down);
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

        ordTask=OrderInfoTask.getInstance(mContext,mHandler);
        getWeilan();
    }

    //获取城市坐标点
    public void getWeilan(){
        final String custID=bean.getId();
        showProgressDialog("正在加载");
        if(!Tools.isEmpty(custID)){
            mapParams = new HashMap<>();
            mapParams.put("token", token);
            mapParams.put("orderId",custID);
            ordTask.getWeiLan(mapParams);

            isHaveWei=true;
        }else{
            dogetData();
            isHaveWei=false;
        }
    }

    //画城市 覆盖区
    public void  doHaveLat( final List<WeiBean> list){
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(cicleList !=null && cicleList.size() >0){
                        cicleList.clear();
                    }

                    if(llis !=null && llis.size() >0){
                        llis.clear();
                    }
                    if(list !=null && list.size() >0) {
                        for (int j = 0; j < list.size(); j++) {
                            WeiBean bean = list.get(j);
                            String type=bean.getType();//1.行政区域，2 多边形
                            String tag=bean.getTag();//1，区域围栏 2 经销商围栏
                            if("1".equals(type)){
                                DLog.e("FormInfoFrgment","doHaveLat/1="+type);
                                String lat=bean.getLat();
                                String lon=bean.getLng();
                                String  r=bean.getR();
                                if(!Tools.isEmpty(lat) && !Tools.isEmpty(lon)){
                                    Double lat1 = Double.parseDouble(lat);
                                    Double lon2 = Double.parseDouble(lon);
                                    Double r3 = Double.parseDouble(r);
                                    LatLng latLng = new LatLng(lat1,lon2);
                                    Circle myCircle=mAMap.addCircle(new CircleOptions().
                                            center(latLng).
                                            radius(r3).
                                            fillColor(Color.argb(170, 238, 89, 83)).
                                            strokeColor(Color.argb(170, 13, 130, 235)).
                                            strokeWidth(3f));
                                    if("1".equals(tag)){
                                        myCircle.setFillColor(Color.argb(102, 129, 247, 81));
                                    }
                                    cicleList.add(myCircle);
                                    isCicre="1";
                                }
                            }else if("3".equals(type)){

                                String data=bean.getRailData();
                                List<WeiLan> wllis  =jsonToBeanList(data,WeiLan.class);
                                List<LatLng>  list3=new ArrayList<>();
                                for (int i = 0; i < wllis.size(); i++) {
                                    WeiLan point = wllis.get(i);
                                    Double lat1 = Double.parseDouble(point.getLat());
                                    Double lat2 = Double.parseDouble(point.getLng());
                                    LatLng lat = new LatLng(lat1, lat2);
                                    list3.add(lat);
                                    llis.add(lat);
                                }
                                // 声明 多边形参数对象
                                PolygonOptions polygonOptions = new PolygonOptions();
                                // 添加 多边形的每个顶点（顺序添加）
                                for (int t = 0; t < list3.size(); t++) {
                                    LatLng latp = list3.get(t);
                                    polygonOptions.add(latp);
                                }
                                polygonOptions.strokeWidth(4) // 多边形的边框 深绿色:#98ED84
                                        .strokeColor(Color.argb(100, 13, 130, 235)) // 边框颜色
                                        .fillColor(Color.argb(100, 238, 89, 83));   // 多边形的填充色
                                if("1".equals(tag)){
                                    polygonOptions.fillColor(Color.argb(102, 129, 247, 81));
                                }

                                mAMap.addPolygon(polygonOptions);
                                idHaveCity="2";
                            }else{
                                try{

                                    DLog.e("FormInfoFrgment","doHaveLat/2="+type);
                                    String data=bean.getRailData();//可能出现多个多边形
                                    try{

                                        JSONArray jsonArray = new JSONArray(data);

                                        //遍历这个json格式的数组
                                        for (int k=0;k<jsonArray.length();k++){

                                            String string = jsonArray.getString(k);

                                            List<WeiLan> wllis  =jsonToBeanList(string,WeiLan.class);
                                            DLog.e("FormInfoFrgment","doHaveLat/wllis="+wllis.size());
                                            List<LatLng>  list31=new ArrayList<>();
                                            for (int i = 0; i < wllis.size(); i++) {
                                                WeiLan point = wllis.get(i);
                                                Double lat1 = Double.parseDouble(point.getLat());
                                                Double lat2 = Double.parseDouble(point.getLng());
                                                LatLng lat = new LatLng(lat1, lat2);
                                                llis.add(lat);
                                                list31.add(lat);
                                            }
                                            // 声明 多边形参数对象
                                            PolygonOptions polygonOptions = new PolygonOptions();
                                            // 添加 多边形的每个顶点（顺序添加）
                                            for (int t = 0; t < list31.size(); t++) {
                                                LatLng latp = list31.get(t);
                                                polygonOptions.add(latp);
                                            }
                                            polygonOptions.strokeWidth(4) // 多边形的边框 深绿色:#98ED84
                                                    .strokeColor(Color.argb(100, 13, 130, 235)) // 边框颜色
                                                    .fillColor(Color.argb(100, 238, 89, 83));   // 多边形的填充色
                                            if("1".equals(tag)){
                                                polygonOptions.fillColor(Color.argb(102, 129, 247, 81));
                                            }

                                            mAMap.addPolygon(polygonOptions);
                                        }
                                    }catch (IllegalStateException e){
                                        List<WeiLan> wllis  =jsonToBeanList(data,WeiLan.class);
                                        List<LatLng>  list32=new ArrayList<>();
                                        for (int i = 0; i < wllis.size(); i++) {
                                            WeiLan point = wllis.get(i);
                                            Double lat1 = Double.parseDouble(point.getLat());
                                            Double lat2 = Double.parseDouble(point.getLng());
                                            LatLng lat = new LatLng(lat1, lat2);
                                            llis.add(lat);
                                            list32.add(lat);
                                        }
                                        // 声明 多边形参数对象
                                        PolygonOptions polygonOptions = new PolygonOptions();
                                        // 添加 多边形的每个顶点（顺序添加）
                                        for (int t = 0; t < list32.size(); t++) {
                                            LatLng latp = list32.get(t);
                                            polygonOptions.add(latp);
                                        }
                                        polygonOptions.strokeWidth(4) // 多边形的边框 深绿色:#98ED84
                                                .strokeColor(Color.argb(100, 13, 130, 235)) // 边框颜色
                                                .fillColor(Color.argb(100, 238, 89, 83));   // 多边形的填充色
                                        if("1".equals(tag)){
                                            polygonOptions.fillColor(Color.argb(102, 129, 247, 81));
                                        }

                                        mAMap.addPolygon(polygonOptions);

                                    }
                                    idHaveCity="2";

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                        }
                    }else {
                        idHaveCity="0";
                        isCicre="0";
                    }
                }
            }).start();


            dogetData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void  dogetData(){
        if(mAMap != null){
            mAMap.clear();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                GPSDataTask gpsDataTask = GPSDataTask.getInstance(mContext, mHandler);
                Map<String, String> mapParam = initMapParams();
                gpsDataTask.getGPSDataTask(mapParam);  //试试异步
            }
        }).start();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

        }

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
                }else{
                    //                    for  ( int  j  =  gList.size()  -  1 ; j  >  i; j -- )  {
                    //                        GPSDataBean b2=gList.get(j);
                    //                        String x2=b2.getX();
                    //                        String y2=b2.getY();
                    //                        if  (x1.equals(x2) && y1.equals(y2))  {//经纬度 一样去掉
                    //                            gList.remove(j);
                    //                        }
                    //                    }
                }
            }
        }
        return gList;
    }

    @NonNull
    private Map<String, String> initMapParams() {
        Map<String, String> mapParams = new HashMap<>();
        String startTime=bean.getEmptyLoadTime();
        String endTime=bean.getUnloadEndTime();
        //        String endTime=bean.getLeaveTime();
        //        String startTime=DateUtil.getLastTwoDay(endTime);
        DLog.e("FormInfoActivity","startTime=="+startTime+"//endTime=="+endTime);

        final String status=bean.getRailCheckStatus();//订单状态  0-未审核  1-正常卸货  2异常卸货
        if ("0".equals(status)){ //
            endTime=bean.getDeviceOrder().getLastDeviceTime();
            //             startTime=DateUtil.getLastTwoDay(endTime);
        }
        mapParams.put("token", token);
        mapParams.put("deviceId", bean.getDeviceId());
        mapParams.put("carNumber", bean.getCarNumber());
        mapParams.put("startTime", startTime);
        mapParams.put("endTime", endTime);
        mapParams.put("spaceTime", "30");
        //        mapParams.put("total", mTvTrendDataCount.getText().toString());
        return mapParams;
    }
    private void showResult(final List<GPSDataBean> glist, final boolean isW, final boolean isSp, final boolean isAR) {
        mTvTruckWeight.setText(getString(R.string.truck_weight));
        mTvWeightDate.setText(getString(R.string.weight_inquire_date));
        tv_quxian_speed.setText("速度:");
        chart.setVisibility(View.GONE);
        tv_no_weightData.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    if(chart !=null){
                        chart.clear();
                    }
                    if(dataSets!=null && dataSets.size() >0){
                        dataSets.clear();
                    }

                    int weightSize = glist.size();
                    if (weightSize != 0) {
                        if (weightSize < 3) {
                            return;
                        }
                        //                        final    List<Float> speedList = new ArrayList<Float>();
                        //
                        //                        speedValues = new ArrayList<Entry>();
                        //
                        //                        for (int i = 0; i < weightSize ; i++) {
                        //                            String date = glist.get(i).getDate();
                        //                            String deviceId = glist.get(i).getDeviceId();
                        //
                        //                            String weight = glist.get(i).getWeight();
                        //                            float weight1 = Float.valueOf(weight);
                        //                            //获取重量
                        //                            wegValues.add(new Entry(i, weight1));
                        //                            dateValues.add(date);
                        //
                        //
                        //                            String speed = glist.get(i).getSpeed();
                        //
                        //                            if(!Tools.isEmpty(speed)){
                        //                                float speed1 = Float.valueOf(speed);
                        //                                speedList.add(speed1);
                        //                                speedValues.add(new Entry(i, speed1));
                        //                            }
                        //
                        //                        }
                        DecimalFormat df = new DecimalFormat("0.00");//
                        float  maxWei= Collections.max(weilist);//取最大值
                        maxWei= Float.parseFloat(df.format(Math.ceil(maxWei)));//向上取整
                        int  weiYU=(int)(maxWei/5);
                        maxWei=5*(weiYU+1);
                        //                        if(maxWei <50){
                        //                            maxWei=50f;
                        //                        }
                        DLog.e("FormInfoFrgment","showResult"+maxWei);
                        float midWei=maxWei/10f; //平均值
                        midWei=Float.parseFloat(df.format(midWei));
                        int size =weightSize;
                        if(size <200){
                            size=200;
                        }
                        float asize=Float.valueOf(size+"");

                        float  speedMax= Collections.max(speedList);//取最大值
                        int  speedYU=(int)(speedMax/5);
                        speedMax=5*(speedYU+1);
                        DLog.e("FormInfoFrgment","showResult"+speedMax);
                        //                        if(speedMax <120){
                        //                            speedMax=120;
                        //                        }

                        Description description = new Description();
                        description.setText("");
                        description.setTextColor(Color.RED);
                        description.setTextSize(15);
                        chart.setDescription(description);//设置图表描述信息
                        chart.setNoDataText("暂无重量数据");//没有数据时显示的文字
                        chart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
                        chart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
                        chart.setDrawBorders(false);//禁止绘制图表边框的线
                        //chart.setBorderColor(); //设置 chart 边框线的颜色。
                        //chart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
                        //chart.setLogEnabled(true);//打印日志
                        //                        chart.notifyDataSetChanged();//刷新数据
                        //                        chart.invalidate();//重绘、

                        //Y轴默认显示左右两个轴线
                        //获取右边的轴线
                        YAxis rightAxis = chart.getAxisRight();
                        //设置网格线为虚线效果
                        rightAxis.enableGridDashedLine(10f, 10f, 0f);
                        rightAxis.setGridColor(R.color.hd_blue);
                        rightAxis.setDrawGridLines(true);//是否显示网格线
                        rightAxis.setAxisMinimum(0f);
                        rightAxis.setAxisMaximum(speedMax);

                        //是否绘制0所在的网格线
                        rightAxis.setDrawZeroLine(false);
                        rightAxis.setTextColor(R.color.back2);
                        rightAxis.setGranularity(20f);//设置Y轴坐标之间的最小间隔
                        rightAxis.setLabelCount(6,true);
                        //                                                leftAxis.setXOffset(1);//设置Y轴偏移量
                        rightAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                String str=mFormat.format(value);
                                return  str ;
                            }
                        });

                        if(!isSp){
                            addLineZreo(dataSets,speedValues);

                            //设置图表右边的y轴禁用
                            rightAxis.setEnabled(true);

                        }else{
                            //设置图表右边的y轴禁用
                            rightAxis.setEnabled(false);
                        }

                        //获取左边的轴线
                        final YAxis leftAxis = chart.getAxisLeft();
                        //设置网格线为虚线效果
                        leftAxis.enableGridDashedLine(10f, 10f, 0f);
                        leftAxis.setGridColor(R.color.color3);
                        leftAxis.setDrawGridLines(true);//是否显示网格线
                        leftAxis.setAxisMinimum(0f);
                        leftAxis.setAxisMaximum(maxWei);

                        //是否绘制0所在的网格线
                        leftAxis.setDrawZeroLine(false);
                        leftAxis.setTextColor(R.color.back2);
                        leftAxis.setGranularity(5f);//设置Y轴坐标之间的最小间隔
                        leftAxis.setLabelCount(6,true);
                        //                                                leftAxis.setXOffset(1);//设置Y轴偏移量
                        leftAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                String str=mFormat.format(value);
                                return  str ;
                            }
                        });

                        if(!isW){
                            addLineTWO(dataSets,areaValues,true,getResources().getColor(R.color.hd_jian_red1));
                            addLineOne(dataSets,wegValues,false,getResources().getColor(R.color.hd_red));

                            leftAxis.setEnabled(true);
                            leftAxis.setDrawGridLines(true);//是否显示网格线

                        }else{
                            leftAxis.setEnabled(false);
                        }

                        //                        if(!isAR){
                        //
                        //                            //                            //设置数据1  参数1：数据源 参数2：图例名称
                        //                            //                            final LineDataSet set2 = new LineDataSet(areaValues, "");
                        //                            //                            set2.setColor(getResources().getColor(R.color.hd_red));
                        //                            //                            set2.setCircleColor(Color.RED);
                        //                            //                            set2.setCircleColorHole(Color.RED);
                        //                            //                            set2.setLineWidth(1.4f);//设置线宽
                        //                            //                            set2.setCircleRadius(3f);//设置焦点圆心的大小
                        //                            //                            set2.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                        //                            //                            //                            set2.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
                        //                            //                            set2.setHighlightEnabled(true);//是否禁用点击高亮线
                        //                            //                            set2.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
                        //                            //                            set2.setValueTextSize(9f);//设置显示值的文字大小
                        //                            //                            set2.setDrawValues(false);//设置是否显示数据文字
                        //                            //                            set2.setDrawFilled(true);//设置禁用范围背景填充
                        //                            //                            set2.setFillColor(getResources().getColor(R.color.hd_red));
                        //                            //                            set2.setDrawCircles(false);//设置是否显示点
                        //                            //                            set2.setCubicIntensity(2f);//折线弯曲程度
                        //                            //                            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                        //
                        //                            //                            dataSets.add(set2);
                        //
                        ////                            addLineOne(dataSets,areaValues,true,getResources().getColor(R.color.hd_red));
                        //
                        //                            leftAxis.setEnabled(true);
                        //                        }else{
                        //                            if(isW){
                        //                                leftAxis.setEnabled(false);
                        //                            }else{
                        //                                leftAxis.setEnabled(true);
                        //                            }
                        //                        }

                        //获取此图表的x轴
                        final XAxis xAxis = chart.getXAxis();
                        xAxis.setEnabled(false);//设置轴启用或禁用 如果禁用以下的设置全部不生效
                        xAxis.setDrawAxisLine(true);//是否绘制轴线
                        xAxis.setDrawGridLines(false);//是否显示网格线(x轴上每个点对应的线)
                        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置

                        xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
                        xAxis.setLabelRotationAngle(3f);//设置x轴标签的旋转角度

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


                        final LineData lineData = new LineData(dataSets);
                        chart.setData(lineData);
                        //                        chart.setVisibleXRangeMaximum(asize);//设置在曲线图中显示的最大数量
                        chart.invalidate();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                chart.setVisibility(View.VISIBLE);
                                tv_no_weightData.setVisibility(View.GONE);
                                rl_x_date.setVisibility(View.VISIBLE);
                                String startTime=bean.getEmptyLoadTime();
                                String endTime=bean.getUnloadEndTime();

                                final String status=bean.getRailCheckStatus();//订单状态  0-未审核  1-正常卸货  2异常卸货
                                if ("0".equals(status)){ //
                                    endTime=bean.getDeviceOrder().getLastDeviceTime();
                                }
                                tv_start_time.setText(startTime);
                                tv_end_time.setText(endTime);

                            }
                        });
                        //                        //点击图表时，执行语句
                        //                        chart.setOnClickListener(new View.OnClickListener() {
                        //                            @Override
                        //                            public void onClick(View v) {
                        //                                //LogUtil.e("点击onClick: " + v.getScaleX());
                        //                                if (v.getScaleX() > 35) {
                        //                                    set1.setDrawCircles(true);//设置是否显示点
                        //                                } else {
                        //                                    set1.setDrawCircles(false);//设置是否显示点
                        //                                }
                        //                            }
                        //                        });

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
                                mTvTruckWeight.setText("车重:" + weight + "吨");
                                mTvWeightDate.setText("日期:" + dateValues.get((int) e.getX()));
                                float speed=speedList.get((int)e.getX());
                                tv_quxian_speed.setText("速度:"+speed);
                                int dex=(int)e.getX();
                                //提示当前重量
                                MyMarkerView markview=new MyMarkerView(mContext,R.layout.pub_current_wei,weight,String.valueOf(speed));
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
                                    addAddr(markerOption1,obean,speed);
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
                        rl_x_date.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void addLineZreo(ArrayList<ILineDataSet>sets,ArrayList<Entry> svalue){
        //设置数据1  参数1：数据源 参数2：图例名称
        LineDataSet set0= new LineDataSet(svalue, "");
        set0.setColor(getResources().getColor(R.color.hd_blue_1));
        set0.setCircleColor(Color.RED);
        set0.setLineWidth(1.4f);//设置线宽
        set0.setCircleRadius(3f);//设置焦点圆心的大小
        set0.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        //                            set0.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set0.setHighlightEnabled(true);//是否禁用点击高亮线
        set0.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        set0.setValueTextSize(9f);//设置显示值的文字大小
        set0.setDrawValues(false);//设置是否显示数据文字
        set0.setDrawFilled(true);//设置禁用范围背景填充
        set0.setDrawCircles(false);//设置是否显示点
        set0.setCubicIntensity(2f);//折线弯曲程度
        set0.setFillColor(getResources().getColor(R.color.hd_blue_1));
        set0.setAxisDependency(YAxis.AxisDependency.RIGHT);
        sets.add(set0);
    }

    private void addLineOne(ArrayList<ILineDataSet>sets,ArrayList<Entry> values,boolean ishave,int color){
        //设置数据1  参数1：数据源 参数2：图例名称
        final LineDataSet set1 = new LineDataSet(values, "");
        set1.setColor(color);
        set1.setCircleColor(color);
        set1.setLineWidth(1.4f);//设置线宽
        set1.setCircleRadius(3f);//设置焦点圆心的大小
        set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        //                            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set1.setHighlightEnabled(true);//是否禁用点击高亮线
        set1.setHighLightColor(color);//设置点击交点后显示交高亮线的颜色
        set1.setValueTextSize(9f);//设置显示值的文字大小
        set1.setDrawValues(false);//设置是否显示数据文字
        set1.setDrawFilled(false);//设置禁用范围背景填充
        set1.setDrawCircles(false);//设置是否显示点
        set1.setCubicIntensity(2f);//折线弯曲程度
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        sets.add(set1);

    }
    private void addLineTWO(ArrayList<ILineDataSet>sets,ArrayList<Entry> values,boolean ishave,int color){
        //设置数据1  参数1：数据源 参数2：图例名称
        LineDataSet set2= new LineDataSet(values, "");
        set2.setCircleColor(Color.RED);
        set2.setColor(getResources().getColor(R.color.hd_red_1));
        set2.setLineWidth(1.4f);//设置线宽
        set2.setCircleRadius(3f);//设置焦点圆心的大小
        set2.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        //                            set0.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set2.setHighlightEnabled(true);//是否禁用点击高亮线
        set2.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        set2.setValueTextSize(9f);//设置显示值的文字大小
        set2.setDrawValues(false);//设置是否显示数据文字
        set2.setDrawFilled(true);//设置禁用范围背景填充
        set2.setFillColor(getResources().getColor(R.color.hd_red_1));
        set2.setDrawCircles(false);//设置是否显示点
        set2.setCubicIntensity(2f);//折线弯曲程度
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        sets.add(set2);

    }
    /*车辆行驶轨迹*/
    private void setMapTracking(final List<GPSDataBean> gpsDataList, RealTimeTruckBean bean) {
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

                    //                    markerOption.title("设备ID:" + bean.getDeviceId() + "\n车牌号:" + bean.getCarNumber() + "\n车重:" + gpsWeightStart + "(吨)").snippet("日期:" + gpsDataList.get(0).getDate());
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
                    //                    markerOption.title("设备ID:" + bean.getDeviceId() + "\n车牌号:" + bean.getCarNumber() + "\n车重:" + gpsWeightEnd + "(吨)").snippet("日期:" + gpsDataList.get(i).getDate());
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.end)));
                }
                markerOption.setFlat(true);
                mAMap.addMarker(markerOption);
                newbounds.include(desLatLng);//轨迹全部显示在屏幕内
            }
        }
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(new LatLng((double) (x/gpsDataList.size()),(double)(y/gpsDataList.size())));
        mAMap.animateCamera(mCameraUpdate);//第二个参数为四周留空宽度.
        mAMap.setMinZoomLevel(3f);
        mAMap.setMaxZoomLevel(15f);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        mPolylineOptions.addAll(latList);
        mPolylineOptions.colorValues(colorList);
        mAMap.addPolyline(mPolylineOptions);
        //        closeProgressDialog();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showResult(gpsDataList,false,false,false);  //重量曲线
                rl_rooter.setVisibility(View.VISIBLE);
                isRootShow=true;
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
                colorInt= Color.parseColor("#2066bd");
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

    public Bitmap doAddBit(OnLineTruckBean bean,float speed){
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.map_usering_data, null);

        TextView  tv_trend_weight=(TextView)view.findViewById(R.id.tv_trend_weight);
        String wei=bean.getWeight();
        DecimalFormat df = new DecimalFormat("0.00");
        String wei2= df.format(Float.parseFloat(wei));
        tv_trend_weight.setText(wei2);
        TextView  tv_car_speed=(TextView)view.findViewById(R.id.tv_current_speed);
        tv_car_speed.setText(speed+" km/h");
        Bitmap bitmap = convertViewToBitmap(view);
        return bitmap;
    }
    public Bitmap  doAddBitmap(RealTimeTruckBean bean){
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.map_usering, null);
        TextView  tv_trend_device_id=(TextView)view.findViewById(R.id.tv_trend_device_id);
        String devid=bean.getDeviceId();
        if(Tools.isEmpty(devid)){
            devid="";
        }
        tv_trend_device_id.setText(devid);
        TextView  tv_trend_weight=(TextView)view.findViewById(R.id.tv_trend_weight);
        String wei=bean.getWeight();
        DecimalFormat df = new DecimalFormat("0.00");
        String wei2= df.format(Float.parseFloat(wei));
        tv_trend_weight.setText(wei2);
        TextView  tv_weight_time=(TextView)view.findViewById(R.id.tv_weight_time);
        tv_weight_time.setText(bean.getDate());
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
    private void addAddr(MarkerOptions opt,OnLineTruckBean bean,float speed) {
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

    /**
     * 某个点是否在区域内
     * @param latLng 需要判断的点
     * @return
     */
    public static boolean polygonCon(    Polygon pol  ,LatLng latLng ){
        if(pol ==null){
            return false;
        }
        //        for (LatLng i : latLngList){
        //            options.add(i);
        //        }
        //        options.strokeWidth(4) // 多边形的边框
        //                .strokeColor(Color.argb(200, 179, 118, 125)) // 边框颜色
        //                .fillColor(Color.argb(50, 235, 204, 202));   // 多边形的填充色
        //        options.visible(true); //设置区域是否显示
        Polygon polygon = pol;
        boolean contains = polygon.contains(latLng);
        polygon.remove();
        return contains;
    }



    // 功能：判断点是否在多边形内
    // 方法：求解通过该点的水平线与多边形各边的交点
    // 结论：单边交点为奇数，成立!
    //参数：
    // POINT p   指定的某个点
    // LPPOINT ptPolygon 多边形的各个顶点坐标（首末点可以不一致）
    public static boolean PtInPolygon(LatLng point, List<LatLng> APoints) {
        int nCross = 0;
        for (int i = 0; i < APoints.size(); i++)   {
            LatLng p1 = APoints.get(i);
            LatLng p2 = APoints.get((i + 1) % APoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if ( p1.longitude == p2.longitude)      // p1p2 与 y=p0.y平行
                continue;
            if ( point.longitude <  Math.min(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            if ( point.longitude >= Math.max(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            // 求交点的 X 坐标 --------------------------------------------------------------
            double x = (double)(point.longitude - p1.longitude) * (double)(p2.latitude - p1.latitude) / (double)(p2.longitude - p1.longitude) + p1.latitude;
            if ( x > point.latitude )
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    public static <T> List<T> jsonToBeanList(String json, Class<T> t) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        for (JsonElement element : jsonarray
        ) {
            list.add(gson.fromJson(element, t));
        }
        return list;
    }
}
