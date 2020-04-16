package com.hand.handtruck.ui.TransportThing;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDatePicker;
import com.hand.handtruck.adapter.WFragmentAdapter;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.form.bean.SearchBean;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.CommonUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 报警
 * @author hxz
 */

public class TransportFragment extends BaseFragment {

	private static final String TAG = "TruckinfoFragment";
	Context mContext;
	private ACache acache;
	private TextView tv_title;
	private LinearLayout ll_tab_title;
	private TextView mTabLineIv;
	private ViewPager mPageVp;
	private LinearLayout ll_tab_1,ll_tab_2,ll_tab_3,ll_tab_4;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int screenWidth;
    private WFragmentAdapter mFragmentAdapter;
    private int leftStartMargin;
    //tab数量
    private int tabCount = 5;
    private int currentIndex;
    private TextView tv_tab_1,tv_tab_2,tv_tab_3,tv_tab_4,tv_tab_5;
    private ImageView tv_add;
    private LinearLayout ll_tab_5;
    private BroadcastReceiver receiver;
    private TextView tv_number_1,tv_number_2,tv_number_3,tv_number_4,tv_number_5;
    private RelativeLayout rl_message;
    private TextView tv_message_1;
    boolean isFirst=false;
    private int errorNumb;
    private ImageView tv_search;
    private TextView mTvTrendStartTime,mTvTrendEndTime;
    private AlertDialog adddialog;
    private CustomDatePicker customDatePickerS,customDatePickerE;
    private AlertDialog devtempDialog;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mContext=getActivity();
        View view = inflater.inflate(R.layout.fragment_transport_thing, container, false);
		acache= ACache.get(mContext,TAG);

		initView(view);
        initTabLineWidth();
        init();
        registerBrodcat();

		return view;
	}

	private void initView(View view) {
		tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("报警");
        tv_add=(ImageView)view.findViewById(R.id.tv_add);
        tv_add.setVisibility(View.GONE);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTransportActivity.start(mContext);
            }
        });
        ll_tab_title=(LinearLayout)view.findViewById(R.id.ll_tab_title);
        ll_tab_title.setVisibility(View.VISIBLE);
        mTabLineIv=(TextView)view.findViewById(R.id.id_tab_line_iv);
		mPageVp=(ViewPager)view.findViewById(R.id.id_page_vp);
		ll_tab_1=(LinearLayout)view.findViewById(R.id.ll_tab_1);
        ll_tab_2=(LinearLayout)view.findViewById(R.id.ll_tab_2);
        ll_tab_3=(LinearLayout)view.findViewById(R.id.ll_tab_3);
        ll_tab_4=(LinearLayout)view.findViewById(R.id.ll_tab_4);
        ll_tab_5=(LinearLayout)view.findViewById(R.id.ll_tab_5);
        tv_tab_1=(TextView)view.findViewById(R.id.tv_tab_1);
        tv_tab_2=(TextView)view.findViewById(R.id.tv_tab_2);
        tv_tab_3=(TextView)view.findViewById(R.id.tv_tab_3);
        tv_tab_4=(TextView)view.findViewById(R.id.tv_tab_4);
        tv_tab_5=(TextView)view.findViewById(R.id.tv_tab_5);

        tv_number_1=(TextView)view.findViewById(R.id.tv_number_1);
        tv_number_2=(TextView)view.findViewById(R.id.tv_number_2);
        tv_number_3=(TextView)view.findViewById(R.id.tv_number_3);
        tv_number_4=(TextView)view.findViewById(R.id.tv_number_4);
        tv_number_5=(TextView)view.findViewById(R.id.tv_number_5);

        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message); //添加点击事件 清除所有未读数据标签
        rl_message.setVisibility(View.GONE);
        rl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(2);
                String totalNumb=tv_number_3.getText().toString();
                String totalNumb1=totalNumb.substring(1,totalNumb.length()-1);
                int cha=Integer.valueOf(totalNumb1)-errorNumb;

                showMsgCount(mContext,errorNumb+"",cha+"");
            }
        });
        tv_message_1=(TextView)view.findViewById(R.id.tv_message_1);

		ll_tab_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(0,true);
			}
		});
		ll_tab_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(1,true);
			}
		});
		ll_tab_3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
 				mPageVp.setCurrentItem(2,true);
			}
		});
        ll_tab_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(3,true);
            }
        });
        ll_tab_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(4,true);
            }
        });

        tv_search=(ImageView)view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchOrder();
            }
        });
	}
    private void init() {
        OutLineFragment outlineFragment=new OutLineFragment();
        PowerOffFragment closeAlarmFragment=new PowerOffFragment();
        ErrorTransportInfoFragment erFragment=new ErrorTransportInfoFragment();
        ErrorPowerOffFragment erArmFragment=new ErrorPowerOffFragment();

        ParkThruckFragment parkFragment=new ParkThruckFragment();
        mFragmentList.add(outlineFragment);
        mFragmentList.add(closeAlarmFragment);
        mFragmentList.add(erFragment);
        mFragmentList.add(erArmFragment);
        mFragmentList.add(parkFragment);

        mFragmentAdapter = new WFragmentAdapter(
                this.getChildFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setOffscreenPageLimit(5);
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                DLog.d("offset1:", offset+ "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记x个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                int tabWidth = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(getActivity(), 20))/ tabCount);
                //

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth /tabCount));


                } else if (currentIndex == 1 && position == 0) // 1->0
                {

                    lp.leftMargin = (int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex *(screenWidth/tabCount));

                }else if (currentIndex == 1&& position == 1) // 1-2
                {

                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth / tabCount));

                }
                else if (currentIndex == 2 && position == 1) // 1-2
                {

                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex*(screenWidth/tabCount));

                }

                else if (currentIndex ==2&& position == 2) // 3-2
                {

                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth / tabCount));

                }
                else if (currentIndex == 3 && position == 2) // 2-3
                {

                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex*(screenWidth/tabCount));

                }
                else if (currentIndex ==3&& position == 3) // 4-3
                {

                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth / tabCount));

                }
                else if (currentIndex == 4 && position == 3) // 3-4
                {

                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex*(screenWidth/tabCount));

                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switchBorderTab(position);
                resetTextView() ;
                switch (position) {
                    case 0:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_4.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_5.setTextColor(getResources().getColor(R.color.txt_content_black));



                        tv_number_1.setTextColor(getResources().getColor(R.color.gh_blue));
                        tv_number_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_3.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_4.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_5.setTextColor(getResources().getColor(R.color.gh_shen_gray));

                        break;
                    case 1:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_4.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_5.setTextColor(getResources().getColor(R.color.txt_content_black));

                        tv_number_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_2.setTextColor(getResources().getColor(R.color.gh_blue));
                        tv_number_3.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_4.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_5.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        break;
                    case 2:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_4.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_5.setTextColor(getResources().getColor(R.color.txt_content_black));

                        tv_number_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_3.setTextColor(getResources().getColor(R.color.gh_blue));
                        tv_number_4.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_5.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        break;
                    case 3:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_4.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_5.setTextColor(getResources().getColor(R.color.txt_content_black));


                        tv_number_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_3.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_4.setTextColor(getResources().getColor(R.color.gh_blue));
                        tv_number_5.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        break;
                    case 4:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_4.setTextColor(getResources().getColor(R.color.txt_content_black));
                        tv_tab_5.setTextColor(getResources().getColor(R.color.hand_blue));

                        tv_number_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_3.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_4.setTextColor(getResources().getColor(R.color.gh_shen_gray));
                        tv_number_5.setTextColor(getResources().getColor(R.color.gh_blue));
                        break;
                }
                currentIndex = position;
            }
        });

        mPageVp.setCurrentItem(2);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/x(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth/tabCount;
        mTabLineIv.setLayoutParams(lp);
    }


    /**
     * 重置颜色
     */
    private void resetTextView() {
        tv_tab_1.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_2.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_3.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_4.setTextColor(getResources().getColor(R.color.txt_content_black));
        tv_tab_5.setTextColor(getResources().getColor(R.color.txt_content_black));

        tv_number_1.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        tv_number_2.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        tv_number_3.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        tv_number_4.setTextColor(getResources().getColor(R.color.gh_shen_gray));
        tv_number_5.setTextColor(getResources().getColor(R.color.gh_shen_gray));
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

				case 11:
                    rl_message.setVisibility(View.VISIBLE);
                    if(!isFirst){
                        isFirst=true;
                        if(errorNumb > 99){
                            tv_message_1.setText("99+");
                            tv_message_1.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_num_point));
                        }else if(errorNumb <=0){
                            rl_message.setVisibility(View.GONE);
                        }else{
                            tv_message_1.setText(errorNumb+"");
                            tv_message_1.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_num_point));
                        }
                    }
					break;
				default:

					break;

			}
		}
	};


    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                 if(action.equals(ConstantsCode.TRANSPORT_OUTLINE)){
                    String listTotal=intent.getStringExtra("tranps_total");
                    tv_number_1.setText("("+listTotal+")");

                }
                 else if(action.equals(ConstantsCode.TRANSPORT_DUANDIAN)){
                     String totalPage= intent.getStringExtra("tranps_poweroff");
                     tv_number_2.setText("("+totalPage+")");
                 }
                 else if(action.equals(ConstantsCode.TRANSPORT_ERROR_TRANS)){
                    String normalTotal= intent.getStringExtra("error_trans");
                    tv_number_3.setText("("+normalTotal+")");
                    mHandler.sendEmptyMessage(11);
                     errorNumb=Integer.parseInt(normalTotal);


                }
                else if(action.equals(ConstantsCode.TRANSPORT_ERROR_DUANDIAN)){
                    String noneWei= intent.getStringExtra("error_poweroff");
                    tv_number_4.setText("("+noneWei+")");
                }
                else if(action.equals(ConstantsCode.TRANSPORT_PARK_CAR)){
                    String devError= intent.getStringExtra("error_park_car");
                    tv_number_5.setText("("+devError+")");
                }
                 else if(action.equals(ConstantsCode.TRANS_ERROR_READED)){
                     rl_message.setVisibility(View.VISIBLE);
                     errorNumb--;
                     if(errorNumb > 99){
                         tv_message_1.setText("99+");
                         tv_message_1.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_num_point));
                     }else if(errorNumb <=0){
                         rl_message.setVisibility(View.GONE);
                     }else{
                         tv_message_1.setText(errorNumb+"");
                         tv_message_1.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_num_point));
                     }
                 }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsCode.TRANSPORT_OUTLINE);
        filter.addAction(ConstantsCode.TRANSPORT_DUANDIAN);
        filter.addAction(ConstantsCode.TRANSPORT_ERROR_TRANS);
        filter.addAction(ConstantsCode.TRANSPORT_ERROR_DUANDIAN);
        filter.addAction(ConstantsCode.TRANSPORT_PARK_CAR);
        filter.addAction(ConstantsCode.TRANS_ERROR_READED);
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

    private void  doSearchOrder(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_for_login, null);
        builder.setView(view);
        builder.setCancelable(true);
        final EditText et_car_number=(EditText)view.findViewById(R.id.et_car_number);
        final EditText et_cust_name=(EditText)view.findViewById(R.id.et_cust_name);
        final EditText et_offload_address=(EditText)view.findViewById(R.id.et_offload_address);
        mTvTrendStartTime=(TextView)view.findViewById(R.id.tv_start_time);
        mTvTrendEndTime=(TextView)view.findViewById(R.id.tv_end_time);

        doshow();
        mTvTrendStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                Date date1 = new Date();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date1);
                calendar1.add(Calendar.DAY_OF_MONTH, -1);
                date1 = calendar1.getTime();
                String yesterday = sdf.format(date1);
                customDatePickerS.show(yesterday);
            }
        });
        mTvTrendEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();

                String now = sdf.format(date);
                customDatePickerE.show(now);
            }
        });
        TextView tv_cancle=(TextView)view.findViewById(R.id.tv_cancle);//取消
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
            }
        });
        TextView tv_login=(TextView)view.findViewById(R.id.tv_login);//搜索
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carNumber=et_car_number.getText().toString();
                String custName=et_cust_name.getText().toString();
                String offLoadAddr=et_offload_address.getText().toString();
                String startTime=mTvTrendStartTime.getText().toString();
                String endTime=mTvTrendEndTime.getText().toString();
                SearchBean sbean=new SearchBean();
                sbean.setAddress(offLoadAddr+"");
                sbean.setCarNumber(carNumber+"");
                sbean.setCustName(custName+"");
                sbean.setEndTime(endTime+"");
                sbean.setStartTime(startTime+"");

                Intent i=new Intent(ConstantsCode.BRO_INFORMATION_SEARCH);
                i.putExtra("search_bean",(Serializable)sbean );
                mContext.sendBroadcast(i);

                closedialog();

            }
        });
        //取消或确定按钮监听事件处理
        adddialog = builder.create();
        adddialog.show();
    }


    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
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
        calendar1.add(Calendar.DAY_OF_MONTH, -10);
        date1 = calendar1.getTime();
        String yesterday10 = sdf.format(date1);
        mTvTrendStartTime.setText(yesterday10);
        return sdf;

    }

    public void  doshow(){
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
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
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
    }

    //展示未读消息数量 和已读数据数量
    public void showMsgCount(final Context context,String unread,String read) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_restart_bleor_dev, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("未读消息："+unread);
        TextView tv_title1=(TextView)view.findViewById(R.id.tv_title1);
        tv_title1.setText("已读消息："+read);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() { //全部已读
            @Override
            public void onClick(View v) {
                Intent readIntent=new Intent(ConstantsCode.TRANS_ERROR_READED);//标识 异常卸货 已读
                errorNumb=0;
                mContext.sendBroadcast(readIntent);

                Intent allIntent=new Intent(ConstantsCode.TRANS_ERROR_ALL_READED);//标识 异常卸货 已读
                mContext.sendBroadcast(allIntent);
                devtempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devtempDialog.dismiss();
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        devtempDialog = dialog.create();
        devtempDialog.setView(view, 0, 0, 0, 0);
        devtempDialog.show();
    }
}
