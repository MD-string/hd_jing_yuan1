package com.hand.handtruck.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.Widget.OneMarkerView;
import com.hand.handtruck.bean.GPSDataResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.GPSDataTask;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.model.GPSDataBean;
import com.hand.handtruck.utils.ACache;
import com.hand.handtruck.utils.LogUtil;

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


/**
 *  重量曲线
 * @author hxz
 */

public class WeightCurveFragment extends BaseFragment implements OnClickListener {

    private BroadcastReceiver receiver;
    private TextView tv_quxian_weight,tv_quxian_date,tv_no_weightData;
    private LineChart chart;
    Context context;
    private SharedPreferences sp;
    private String token,sign;
    private ACache acache;
    private List<GPSDataBean> gpsDataList;
    private DecimalFormat myformat;
    private CompanyTruckBean truckModel;
    private CustomDialog dialog;
    @SuppressLint("HandlerLeak")
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (getActivity() == null || getActivity().isFinishing()) {
                return;
            }
            switch (msg.what) {
                //获取GPS成功
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    GPSDataResultBean gpsDataResult = (GPSDataResultBean) msg.obj;
                    gpsDataList = new ArrayList<>();
                    gpsDataList = gpsDataResult.getResult();

                    showResult(gpsDataList);

                    break;
                //获取GPS失败
                case ConstantsCode.MSG_REQUEST_FAIL1:
                    gpsDataList = new ArrayList<>();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.frgment_weight_curve, container, false);
        context=getActivity();
        acache= ACache.get(context);
        initView(view);
        inIt();
        registerBrodcat();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        tv_quxian_weight=(TextView)view.findViewById(R.id.tv_quxian_weight);
        tv_quxian_date=(TextView)view.findViewById(R.id.tv_quxian_date);
        tv_no_weightData=(TextView)view.findViewById(R.id.tv_no_weightData);
        chart=(LineChart)view.findViewById(R.id.chart);

    }


    private void inIt() {
        sp = context.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        myformat = new DecimalFormat("0.0");
        sign = acache.getAsString("thruck_sign");
        truckModel = (CompanyTruckBean) acache.getAsObject("thruck_truckModel");


//        showProgressDialog("正在加载");

        GPSDataTask gpsDataTask = GPSDataTask.getInstance(context, mhandler);
        Map<String, String> mapParam = initMapParams();
        gpsDataTask.getGPSDataTask(mapParam);
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

    }
    private void showResult(final List<GPSDataBean> glist) {
        try {
            chart.setVisibility(View.GONE);
            tv_no_weightData.setVisibility(View.VISIBLE);

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
                    String weight = glist.get(i).getWeight();
                    float weight1 = Float.valueOf(weight);
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
                chart.setNoDataText("暂无重量数据");//没有数据时显示的文字
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
                leftAxis.setGranularity(5f);//设置Y轴坐标之间的最小间隔
                                                                leftAxis.setLabelCount(10,true);
                //
                //                                              leftAxis.setXOffset(1);//设置Y轴偏移量
                leftAxis.setValueFormatter(new ValueFormatter(){
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                       String str=myformat.format((double)value);
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

                chart.setVisibility(View.VISIBLE);
                tv_no_weightData.setVisibility(View.GONE);

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
                        tv_quxian_weight.setText("车重:" + String.valueOf(e.getY()) + "吨");
                        tv_quxian_date.setText("日期:" + dateValues.get((int) e.getX()));
                        int dex=(int)e.getX();
                        //提示当前重量
                        OneMarkerView markview=new OneMarkerView(context,R.layout.pub_current_wei_1,String.valueOf(e.getY()));
                        chart.setMarker(markview);

//                        if(glist !=  null && glist.size() >dex){
//                            GPSDataBean bean= glist.get(dex);   //显示当时车辆位置
//                            OnLineTruckBean obean=new OnLineTruckBean();
//                            obean.setDeviceId(bean.getDeviceId());
//                            obean.setWeight(bean.getWeight());
//                            obean.setGpsUploadDate(bean.getDate());
//                            obean.setX(bean.getX());
//                            obean.setY(bean.getY());
//                            if (markerOption1 == null) {
//                                markerOption1 = new MarkerOptions();
//                            }
//                        }

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
    @NonNull
    private Map<String, String> initMapParams() {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("deviceId", truckModel.getDeviceId());
        mapParams.put("startTime",  getYesterday()+ ":00");
        mapParams.put("endTime",  getNowDate() + ":00");
        mapParams.put("spaceTime", "120");
        //        mapParams.put("total", mTvTrendDataCount.getText().toString());
        return mapParams;
    }


    private String  getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(date);
        //        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date date = calendar.getTime();

        String now = sdf.format(date);
        return now;
    }

    private String  getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        Date date1 = new Date();
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(date1);
//        calendar1.add(Calendar.DATE, -1);
//        date1 = calendar1.getTime();
        Date as = new Date(new Date().getTime()-2*24*60*60*1000);
        String yesterday = sdf.format(as);
        return yesterday;
    }

}
