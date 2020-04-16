package com.hand.handtruck.ui.Examine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.adapter.WFragmentAdapter;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 审核
 * @author hxz
 */

public class ExamineOrderFragment  extends BaseFragment {

    private static final String TAG = "TruckinfoFragment";
    Context mContext;
    private ACache acache;
    private TextView tv_title;
    private LinearLayout ll_tab_title;
    private TextView mTabLineIv;
    private ViewPager mPageVp;
    private LinearLayout ll_tab_1,ll_tab_2,ll_tab_3,ll_tab_4,ll_tab_5;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int screenWidth;
    private WFragmentAdapter mFragmentAdapter;
    //tab数量
    private int tabCount = 3;
    private int currentIndex;
    private TextView tv_tab_1,tv_tab_2,tv_tab_3,tv_tab_4,tv_tab_5;
    private ImageView tv_add;
    private SharedPreferences sp;
    private String sourceKey;
    private StringBuffer sbuffer;
    private String[] shave;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        View view = inflater.inflate(R.layout.fragment_examine_order, container, false);
        acache= ACache.get(mContext,TAG);

        sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        sourceKey = (String) sp.getString("sourceKey", null);//资源权限标示

        initView(view);
        init();
        initTabLineWidth();

        return view;
    }

    private void initView(View view) {
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("审核");
        ll_tab_title=(LinearLayout)view.findViewById(R.id.ll_tab_title);
        ll_tab_title.setVisibility(View.VISIBLE);
        mTabLineIv=(TextView)view.findViewById(R.id.id_tab_line_iv);
        mPageVp=(ViewPager)view.findViewById(R.id.id_page_vp);
        ll_tab_1=(LinearLayout)view.findViewById(R.id.ll_tab_1);
        ll_tab_1.setVisibility(View.GONE);
        ll_tab_2=(LinearLayout)view.findViewById(R.id.ll_tab_2);
        ll_tab_3=(LinearLayout)view.findViewById(R.id.ll_tab_3);
        ll_tab_4=(LinearLayout)view.findViewById(R.id.ll_tab_4);
        ll_tab_4.setVisibility(View.GONE);
        ll_tab_5=(LinearLayout)view.findViewById(R.id.ll_tab_5);
        tv_tab_1=(TextView)view.findViewById(R.id.tv_tab_1);
        tv_tab_2=(TextView)view.findViewById(R.id.tv_tab_2);
        tv_tab_3=(TextView)view.findViewById(R.id.tv_tab_3);
        tv_tab_4=(TextView)view.findViewById(R.id.tv_tab_4);
        tv_tab_5=(TextView)view.findViewById(R.id.tv_tab_5);

    }
    private void init() {
        sbuffer=new StringBuffer();
//        if(!sourceKey.contains(ConString.DataStrng.DATA_CHECK_HAND) && sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_MARKET) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_SALE) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_FINANCE)
//                && !sourceKey.contains(ConString.DataStrng.DATA_TOTAL_EXAMINE)){
//            LogisiicsExFragment logisFragment=new LogisiicsExFragment();
//            mFragmentList.add(logisFragment);
//            ll_tab_title.setVisibility(View.GONE);
//            tv_title.setText("销售部");
//            tabCount=1;
//        }else if(!sourceKey.contains(ConString.DataStrng.DATA_CHECK_HAND) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_MARKET) && sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_SALE) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_FINANCE)
//                && !sourceKey.contains(ConString.DataStrng.DATA_TOTAL_EXAMINE)){
//            PicLeaderEXFragment leaderFragment=new PicLeaderEXFragment();
//            mFragmentList.add(leaderFragment);
//            ll_tab_title.setVisibility(View.GONE);
//            tv_title.setText("分管领导");
//            tabCount=1;
//        }
//
//        else if(!sourceKey.contains(ConString.DataStrng.DATA_CHECK_HAND) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_MARKET) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_SALE) && !sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_FINANCE)
//                && sourceKey.contains(ConString.DataStrng.DATA_TOTAL_EXAMINE)){
//            TotalExamineFragment totalFragment=new TotalExamineFragment();
//            mFragmentList.add(totalFragment);
//            ll_tab_title.setVisibility(View.GONE);
//            tv_title.setText("审核汇总");
//            tabCount=1;
//        }else {
            tabCount=0;
            ll_tab_2.setVisibility(View.GONE);
            ll_tab_3.setVisibility(View.GONE);
            ll_tab_5.setVisibility(View.GONE);
//
//            if(sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_MARKET)){

                LogisiicsExFragment logisFragment=new LogisiicsExFragment();
                mFragmentList.add(logisFragment);
                tabCount++;
                ll_tab_2.setVisibility(View.VISIBLE);
                sbuffer.append("b;");
//            }
//            if(sourceKey.contains(ConString.DataStrng.DATA_CHECKLIST_SALE)){
                BossExFragment leaderFragment=new BossExFragment();
                mFragmentList.add(leaderFragment);
                tabCount++;
                ll_tab_3.setVisibility(View.VISIBLE);
                sbuffer.append("c;");
//            }
//
//            if(sourceKey.contains(ConString.DataStrng.DATA_TOTAL_EXAMINE)){
                TotalExamineFragment totalFragment=new TotalExamineFragment();
                mFragmentList.add(totalFragment);
                ll_tab_5.setVisibility(View.VISIBLE);
                tabCount++;
                sbuffer.append("e;");
//            }
//
//        }
        if(!"".equals(sbuffer) && sbuffer.length() >0){
            shave=sbuffer.toString().split(";");
            int length=shave.length;
              int pos1=0;
             int pos2=0;
             int pos3=0;
//
            for(int i=0;i<length;i++){
                String sh=shave[i];
//
                    if(sh.contains("b")){
                    pos1=i;
                }else if(sh.contains("c")){
                    pos2=i;
                }else if(sh.contains("e")){
                    pos3=i;
                }
            }
            final int b1=pos1;
            final int c1=pos2;
            final int e1=pos3;
            ll_tab_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageVp.setCurrentItem(b1,true);
                }
            });
            ll_tab_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageVp.setCurrentItem(c1,true);
                }
            });
            ll_tab_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageVp.setCurrentItem(e1,true);
                }
            });
        }

        mFragmentAdapter = new WFragmentAdapter(
                this.getChildFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setOffscreenPageLimit(3);
        mPageVp.setCurrentItem(0);
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

                int tabWidth = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(getActivity(), 40))/ tabCount);
                //

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
                            * (screenWidth / tabCount));


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
//
//                else if (currentIndex ==2&& position == 2) // 3-2
//                {
//
//                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
//                            * (screenWidth / tabCount));
//
//                }
//                else if (currentIndex == 3 && position == 2) // 2-3
//                {
//
//                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex*(screenWidth/tabCount));
//
//                }
//
//                else if (currentIndex ==3&& position == 3) // 4-3
//                {
//
//                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabCount) + currentIndex
//                            * (screenWidth / tabCount));
//
//                }
//                else if (currentIndex == 4 && position == 3) // 3-4
//                {
//
//                    lp.leftMargin=(int)(-(1-offset) *(screenWidth*1.0/tabCount)+currentIndex*(screenWidth/tabCount));
//
//                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switchBorderTab(position);
                resetTextView() ;
                switch (position) {
                    case 0:
                        String sh1=shave[0];
                        if(sh1.contains("b")){
                            tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else if(sh1.contains("c")){
                            tv_tab_3.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else  if(sh1.contains("e")){
                            tv_tab_5.setTextColor(getResources().getColor(R.color.hand_blue));
                        }
                        break;
                    case 1:
                        String sh2=shave[1];
                        if(sh2.contains("b")){
                            tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else if(sh2.contains("c")){
                            tv_tab_3.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else  if(sh2.contains("e")){
                            tv_tab_5.setTextColor(getResources().getColor(R.color.hand_blue));
                        }

                        break;
                    case 2:
                        String sh3=shave[2];
                        if(sh3.contains("b")){
                            tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else if(sh3.contains("c")){
                            tv_tab_3.setTextColor(getResources().getColor(R.color.hand_blue));
                        }else  if(sh3.contains("e")){
                            tv_tab_5.setTextColor(getResources().getColor(R.color.hand_blue));
                        }

                        break;

                }
                currentIndex = position;
            }
        });

        mPageVp.setCurrentItem(0);
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
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case ConstantsCode.MSG_REQUEST_FAIL1:
                    break;
                default:

                    break;

            }
        }
    };

}
