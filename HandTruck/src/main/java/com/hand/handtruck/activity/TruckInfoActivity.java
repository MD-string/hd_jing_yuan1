package com.hand.handtruck.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.listview.XListView;
import com.hand.handtruck.Widget.sortlistviewdemo.PinyinComparator;
import com.hand.handtruck.Widget.sortlistviewdemo.PinyinUtils;
import com.hand.handtruck.Widget.sortlistviewdemo.SortAdapter;
import com.hand.handtruck.Widget.sortlistviewdemo.WaveSideBar;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CompanyTruckGroupBean;
import com.hand.handtruck.bean.CompanyTruckResultBean;
import com.hand.handtruck.bean.OnLineTruckListBean;
import com.hand.handtruck.bean.TruckChildBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.db.ACache;
import com.hand.handtruck.domain.GetCompanyTruckTask;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.model.CompanyTruckBean;
import com.hand.handtruck.model.OnLineTruckBean;
import com.hand.handtruck.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hxz on 2018/10/12
 * Describe: 公司及车辆信息列表页
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class TruckInfoActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener {
    private TextView mTvTitle;
    private RelativeLayout mRlTitle;
    private List<CompanyTruckGroupBean> mGroupList;
    private List<CompanyTruckBean> truckBeanList;  //公司以及车辆信息
    private List<OnLineTruckBean> onLineTruckBeanList;//在线车辆
    private ArrayList deviceIdList;
    private Map<String, String> mapParams;
    private GetCompanyTruckTask companyTruckTask;
    private String token;
    private XListView mlistview;
    private SortAdapter mAdapter;
    private static final String TAG = "TruckInfoActivity";
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;
    private WaveSideBar mSideBar;
    private RelativeLayout rl_et;
    private ImageView tv_add;
    private ACache acache;
    private boolean isRefresh=false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /*车辆列表*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    CompanyTruckResultBean carResultBean = (CompanyTruckResultBean) msg.obj;
                    truckBeanList = carResultBean.getResult();
                    companyTruckTask.getOnlineTruckListInfo(mapParams);
                    DLog.d(TAG,"车辆列表刷新成功");
                    /*在线车辆*/
                    break;
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    OnLineTruckListBean onLineTurckResultBean = (OnLineTruckListBean) msg.obj;
                    onLineTruckBeanList = onLineTurckResultBean.getResult();
                    /*车辆列表请求成功后再去请求在线车辆*/
                    initGroupListData();
                    if(isRefresh && mlistview !=null){
                        onLoad();
                        DLog.d(TAG,"在线车辆刷新成功");
                    }
                    break;
                case ConstantsCode.MSG_REQUEST_FAIL:

                    break;

                case ConstantsCode.MSG_REQUEST_FAIL1:
                    break;
                default:

                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_truck_info;
    }

    @Override
    protected void findViews() {
        mContext=this;
        acache= ACache.get(mContext,TAG);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tv_add=(ImageView)findViewById(R.id.tv_add);
        tv_add.setVisibility(View.GONE); //加号    添加车辆
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPub();
            }
        });


        mSideBar = (WaveSideBar) findViewById(R.id.sideBar);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {

            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if(position != -1){
                    mlistview.setSelection(position);
                }
            }

        });
        mComparator = new PinyinComparator();
        mlistview = (XListView) findViewById(R.id.list_1);
        View v= LayoutInflater.from(this).inflate(R.layout.car_list_header, null);
        rl_et=(RelativeLayout)v.findViewById(R.id.rl_et);
        rl_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bundle bund=new Bundle();
                bund.putSerializable("search_list",(Serializable)mGroupList);
                CommonKitUtil.startActivity(TruckInfoActivity.this, SearchTruckActivity.class, bund, false);
                return false;
            }
        });
        mlistview.addHeaderView(v);
        mlistview.setPullRefreshEnable(true);
        mlistview.setPullLoadEnable(false);
        mlistview.setXListViewListener(this);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position ==0 || position ==1){

                }else{
                    Bundle bundle=new Bundle();
                    bundle.putInt("click_position",position);
                    CommonKitUtil.startActivity(TruckInfoActivity.this, CompanyTruckActivity.class, bundle, false);

                }

            }
        });


    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void inIt() {
        truckBeanList = new ArrayList<>();
        mTvTitle.setText("公司列表");
        onLineTruckBeanList = new ArrayList<>();
        mapParams = new HashMap<>();
        SharedPreferences sp = getSharedPreferences(ConstantsCode.FILE_NAME, 0);
        token = (String) sp.getString("token", null);
        LogUtil.e("token==" + token);
        mapParams.put("token", token);

        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调网络接口 请求数据
                companyTruckTask = GetCompanyTruckTask.getInstance(TruckInfoActivity.this, mHandler);
                companyTruckTask.getCarList(mapParams);
            }
        }, 300);
    }


    private void initGroupListData() {
        mGroupList = new ArrayList<>();
        deviceIdList = new ArrayList<>();

        //获取在线车辆ID
        for (int k = 0; k < onLineTruckBeanList.size(); k++) {
            String deviceId = onLineTruckBeanList.get(k).getDeviceId();
            deviceIdList.add(deviceId);
        }

        ArrayList companyList = new ArrayList();
        //处理公司名下有多少车辆问题
        for (int i = 0; i < truckBeanList.size(); i++) {
            String companyName = truckBeanList.get(i).getCompanyName();
            if (!companyList.contains(companyName)) {
                companyList.add(companyName);
                List<TruckChildBean> listChild = new ArrayList<TruckChildBean>();
                int j = 0;
                for (int k = 0; k < truckBeanList.size(); k++) {
                    CompanyTruckBean companyTruckBean = truckBeanList.get(k);
                    CompanyTruckBean model = new CompanyTruckBean();
                    model.setId(companyTruckBean.getId());
                    model.setDeviceId(companyTruckBean.getDeviceId());
                    model.setCarNumber(companyTruckBean.getCarNumber());
                    model.setGpsId(companyTruckBean.getGpsId());
                    model.setCompanyName(companyTruckBean.getCompanyName());
                    model.setCity(companyTruckBean.getCity());
                    model.setProvince(companyTruckBean.getProvince());
                    model.setLoadCapacity(companyTruckBean.getLoadCapacity());
                    model.setMfgDate(companyTruckBean.getMfgDate());
                    model.setUpdateDate(companyTruckBean.getUpdateDate());

                    if (model.getCompanyName().equals(companyName)) {
                        if (deviceIdList.contains(model.getDeviceId())) {
                            TruckChildBean truckChildBean = new TruckChildBean();
                            truckChildBean.setName(model.getCarNumber());
                            truckChildBean.setSign("在线");
                            truckChildBean.setTruckModel(model);
                            listChild.add(truckChildBean);
                        } else {
                            TruckChildBean truckChildBean = new TruckChildBean();
                            truckChildBean.setName(model.getCarNumber());
                            truckChildBean.setSign("离线");
                            truckChildBean.setTruckModel(model);
                            listChild.add(truckChildBean);
                            j++;

                        }
                    }
                }
                CompanyTruckGroupBean companyGroupBean = new CompanyTruckGroupBean();
                companyGroupBean.setName(companyName);
                companyGroupBean.setTruckNumber(listChild.size());
                companyGroupBean.setOnlineNumber(listChild.size() - j);
                companyGroupBean.setChildren(listChild);
                mGroupList.add(companyGroupBean);//公司名下有多少辆车
            }
        }

        mGroupList = filledData(mGroupList);//获取首字母
        // 根据a-z进行排序源数据
        Collections.sort(mGroupList, mComparator);
        List<String> letter = new ArrayList<>();
        for (int i = 0; i < mGroupList.size(); i++) {
            letter.add(mGroupList.get(i).getLetters());
        }
        letter=pastLeep1(letter);//去重
        mSideBar.updateListView(letter);
        mAdapter = new SortAdapter(this, mGroupList);
        mlistview.setAdapter(mAdapter);

        acache.put("truck_list",(Serializable)mGroupList);//缓存
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
        }
    }

    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<CompanyTruckGroupBean> filledData(List<CompanyTruckGroupBean> date) {
        List<CompanyTruckGroupBean> mSortList = new ArrayList<>();
        if (date == null || date.size() <= 0) {
            return mSortList;
        } else {
            for (int i = 0; i < date.size(); i++) {
                CompanyTruckGroupBean sortModel = date.get(i);
                //汉字转换成拼音
                String pinyin = PinyinUtils.getPingYin(sortModel.getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setLetters(sortString.toUpperCase());
                } else {
                    sortModel.setLetters("#");
                }

                mSortList.add(sortModel);
            }
            return mSortList;
        }

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CompanyTruckGroupBean> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            //            filterDateList = filledData(getResources().getStringArray(R.array.date));
        } else {
            filterDateList.clear();
            for (CompanyTruckGroupBean sortModel : mGroupList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mComparator);
        mGroupList.clear();
        mGroupList.addAll(filterDateList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //set集合去重，不改变原有的顺序
    public static List<String> pastLeep1(List<String> list){
        System.out.println("list = [" + list.toString() + "]");
        List<String> listNew=new ArrayList<>();
        Set set=new HashSet();
        for (String str:list) {
            if(set.add(str)){
                listNew.add(str);
            }
        }
        System.out.println("listNew = [" + listNew.toString() + "]");
        return listNew;
    }


    //右上角 +
    public void showPub(){
        View view= LayoutInflater.from(mContext).inflate(R.layout.pub_add_right, null,false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        final PopupWindow  popupwindow = new PopupWindow();
        //        popupwindow.setAnimationStyle(R.style.AnimationFade);
        RelativeLayout rl_1=(RelativeLayout)view.findViewById(R.id.rl_1);
        RelativeLayout rl_2=(RelativeLayout)view.findViewById(R.id.rl_2);
        popupwindow.setOutsideTouchable(true);
        popupwindow.setFocusable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }

                return false;
            }
        });
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonKitUtil.startActivity(TruckInfoActivity.this, AddTruckActivity.class, new Bundle(), false);
                popupwindow.dismiss();
            }
        });
        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonKitUtil.startActivity(TruckInfoActivity.this, AddCompanyActivity.class, new Bundle(), false);
                popupwindow.dismiss();
            }
        });
        popupwindow. setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);//必须存在 不然不显示

        popupwindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow.setContentView(view);
        popupwindow.showAsDropDown(tv_add,-10,3);
    }

    //刷新
    @Override
    public void onRefresh() {  //方法无回调？
        //不延迟请求总报Token无效
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调网络接口 请求数据
                companyTruckTask = GetCompanyTruckTask.getInstance(TruckInfoActivity.this, mHandler);
                companyTruckTask.getCarList(mapParams);
                onLoad();
                isRefresh=true;
            }
        }, 300);
    }

    //加载更多
    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        mlistview.stopRefresh();
        mlistview.stopLoadMore();
    }
}
