package com.hand.handtruck.activity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.TextUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.BottomSelectPopupWindow;
import com.hand.handtruck.Widget.OnMyItemClickListener;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.CityBean;
import com.hand.handtruck.bean.CityResultBean;
import com.hand.handtruck.bean.ProvinceBean;
import com.hand.handtruck.bean.ProvinceResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.CityListTask;
import com.hand.handtruck.domain.CompanyChangeListTask;
import com.hand.handtruck.domain.ProListTask;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * describe:添加公司页
 */
public class AddCompanyActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlTitle;
    private LinearLayout ll_select_company;
    private EditText mTvSelectCompany;
    private EditText mEtPhone,mEtEmail,mEtAddress;
    private Button mBtnCompany;
    private Activity mContext;
    private String token;
    private CompanyChangeListTask truckAddTask;
    private LinearLayout ll_pro,ll_city;
    private TextView tv_pro,tv_city;
    private     List<ProvinceBean> plist;//省份
    private  List<CityBean> clist;//城市

    private LinearLayout ll_all;
    private String province;//当前选择的省份
    private String city;//当前选择的城市
    private String cityId;
    private String pId;
    private TextView tv_operator;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                //获取省份成功
                case ConstantsCode.MSG_REQUEST_SUCCESS1:
                    LogUtil.e("获取省份成功成功");
                    ProvinceResultBean companyResult = (ProvinceResultBean) msg.obj;
                    plist = companyResult.getResult();

                    break;
                //获取省份失败
                case ConstantsCode.MSG_REQUEST_FAIL1:
                    LogUtil.e("获取省份失败");
                    break;
                //获取city成功
                case ConstantsCode.MSG_REQUEST_SUCCESS2:
                    LogUtil.e("获取city成功");
                    CityResultBean cityreuslt = (CityResultBean) msg.obj;
                    clist = cityreuslt.getResult();

                    break;
                //获取city失败
                case ConstantsCode.MSG_REQUEST_FAIL2:
                    LogUtil.e("获取city失败");
                    break;
                //添加公司成功
                case ConstantsCode.MSG_REQUEST_SUCCESS3:
                    LogUtil.e("添加公司成功");
                    showTips("添加公司成功");
                    break;
                //添加公司失败
                case ConstantsCode.MSG_REQUEST_FAIL3:
                    LogUtil.e("添加公司失败");
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_add;
    }

    @Override
    protected void findViews() {
        ll_all=(LinearLayout)findViewById(R.id.ll_all);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("添加公司");
        mRlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tv_operator=(TextView)findViewById(R.id.tv_operator);
        tv_operator.setVisibility(View.GONE);

        ll_select_company=(LinearLayout) findViewById(R.id.ll_select_company);
        mTvSelectCompany = (EditText) findViewById(R.id.et_select_company);

        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtEmail = (EditText) findViewById(R.id.tv_company_email);
        mEtAddress = (EditText) findViewById(R.id.et_more_address);

        ll_pro=(LinearLayout)findViewById(R.id.ll_pro);
        tv_pro=(TextView)findViewById(R.id.tv_pro);

        ll_city=(LinearLayout)findViewById(R.id.ll_city);
        tv_city=(TextView)findViewById(R.id.tv_city);

        mBtnCompany = (Button) findViewById(R.id.btn_add_company);

    }


    @Override
    protected void setListeners() {
        mTvBack.setOnClickListener(this);
        mBtnCompany.setOnClickListener(this);
        ll_pro.setOnClickListener(this);
        ll_city.setOnClickListener(this);
    }

    @Override
    protected void inIt() {
        mContext=AddCompanyActivity.this;
        SharedPreferences sp = getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        getProvinceList();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
            /*选择省份*/
            case R.id.ll_pro:
                if(plist !=null && plist.size() >0){

                    final String[] items=new String[plist.size()];
                    for(int i=0 ;i<plist.size();i++){
                        items[i]=plist.get(i).getProvinceName();
                    }
                    BottomSelectPopupWindow pBottomPopuwindow=new BottomSelectPopupWindow(mContext,items);
                    pBottomPopuwindow.setOnItemClickListener(new OnMyItemClickListener() {
                        @Override
                        public void onItemClick(View parent, View view, int position) {
                            String item=items[position];
                            province=item;
                            pId=plist.get(position).getId();
                            tv_pro.setText(item);

                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getCityList(pId);
                                }
                            },200);
                        }
                    });
                    pBottomPopuwindow.showAtLocation(ll_all, Gravity.BOTTOM,0,0);

                }else{
                    showTips("未获取省份信息");
                }
                break;
            /*选择城市*/
            case R.id.ll_city:
                if(clist !=null && clist.size() >0){

                   final String[] citme=new String[clist.size()];
                    for(int i=0 ;i<clist.size();i++){
                        citme[i]=clist.get(i).getCityName();
                    }
                    BottomSelectPopupWindow cBottomPopuwindow=new BottomSelectPopupWindow(mContext,citme);
                    cBottomPopuwindow.setOnItemClickListener(new OnMyItemClickListener() {
                        @Override
                        public void onItemClick(View parent, View view, int position) {
                            String item=citme[position];
                            city=item;
                            cityId=clist.get(position).getId();
                            tv_city.setText(item);
                        }
                    });
                    cBottomPopuwindow.showAtLocation(ll_all, Gravity.BOTTOM,0,0);
                }else{
                    showTips("未获取城市信息");
                }
                break;
            /*选择公司*/
            case R.id.btn_add_company:
                submit();
                break;
        }
    }
    private void hideKeyBorad() {
        InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    private void submit() {
        // validate
        if (!CommonKitUtil.isNetworkAvailable(mContext)) {
            ToastUtil.showMsgShort(mContext, ConstantsCode.NETWORK_ERROR);
            return;
        }
        String company = mTvSelectCompany.getText().toString().trim();
        if (TextUtils.isEmpty(company)) {
            Toast.makeText(mContext, "公司名称不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.focusView(mTvSelectCompany);
            return;
        }

        String mail = mEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(mContext, "公司邮箱不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.focusView(mEtEmail);
            return;
        }

        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtPhone);
            return;
        }else if (!TextUtil.isMobileNO(phone)){
            Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtPhone);
            return;
        }

        String address = mEtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(mContext, "公司详细地址不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.focusView(mEtAddress);
            return;
        }

        String city = tv_city.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(mContext, "公司所在城市不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Tools.isEmpty(cityId)){
            Toast.makeText(mContext, "公司所在城市编号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("type", "1");
        mapParams.put("companyName", company);
        mapParams.put("tel", phone);
        mapParams.put("email", mail);
        mapParams.put("address", address);
        mapParams.put("cityId",cityId);
        truckAddTask= CompanyChangeListTask.getInstance(mContext,mHandler);
        truckAddTask.changeCompany(mapParams);
    }
    public void  showTips(String tip){
        com.hand.handtruck.utils.ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }

    //获取省份
    public void getProvinceList(){
        if (!CommonKitUtil.isNetworkAvailable(mContext)) {
            ToastUtil.showMsgShort(mContext, ConstantsCode.NETWORK_ERROR);
            return;
        }
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        ProListTask proTask= ProListTask.getInstance(mContext,mHandler);
        proTask.getProList(mapParams);
    }
    //获取城市
    public void getCityList(String id){
        String pstr=tv_pro.getText().toString().trim();
        if(Tools.isEmpty(pstr)){
            return;
        }
        if(Tools.isEmpty(id)){
            return;
        }

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("proviceId", id);
        CityListTask proTask= CityListTask.getInstance(mContext,mHandler);
        proTask.getCityList(mapParams);
    }


}
