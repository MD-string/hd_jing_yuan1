package com.hand.handtruck.ui.Examine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hand.handtruck.R;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.Examine.presenter.LogsExActTask;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.home.BaseFragment;
import com.hand.handtruck.utils.ToastUtil;
import com.hand.handtruck.utils.Tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 分管领导审核
 */

public class PicLeaderExamineFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private DecimalFormat mineformat= new DecimalFormat("0.00");
    private Map<String, String> mapParams;
    private SharedPreferences sp = null;
    private FormBean bean;
    private String token;
    private EditText et_other_advice;
    private LogsExActTask tranTask;
    private RelativeLayout rl_un_pass,rl_pass;
    private RelativeLayout bt_close;
    private TextView tv_examine_market,tv_market_msg,tv_ex_leader,tv_last_advice,tv_hint_tip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        View view = inflater.inflate(R.layout.activity_examine_leader, container, false);

        sp = mContext.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");
        bean= (FormBean)getArguments().getSerializable("formBean");
        findViews(view);
        setListeners();
        inIt();
        return view;
    }


    protected void findViews(View view) {

        tv_examine_market=(TextView)view.findViewById(R.id.tv_examine_market);
        tv_market_msg=(TextView)view.findViewById(R.id.tv_market_msg);
        tv_ex_leader=(TextView)view.findViewById(R.id.tv_ex_leader);
        tv_last_advice = (TextView) view.findViewById(R.id.tv_last_advice);  //上次建议


        et_other_advice = (EditText)view.findViewById(R.id.et_other_advice);//审核意见

        tv_hint_tip=(TextView)view.findViewById(R.id.tv_hint_tip);
        tv_hint_tip.setVisibility(View.VISIBLE);

        rl_pass=(RelativeLayout)view.findViewById(R.id.rl_pass);//通过
        rl_un_pass=(RelativeLayout)view.findViewById(R.id.rl_un_pass);//不通过

        bt_close=(RelativeLayout)view.findViewById(R.id.bt_close);

        et_other_advice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_hint_tip.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void setListeners() {
        rl_pass.setOnClickListener(this);
        rl_un_pass.setOnClickListener(this);
        bt_close.setOnClickListener(this);
    }

    protected void inIt() {

        String status=bean.getLevel1CheckStatus();  //物流部审核状态0-未审核1-审核通过2审核不通过
        if("2".equals(status)){
            tv_examine_market.setText("不通过");
            tv_examine_market.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_red));
        }else if("1".equals(status)){
            tv_examine_market.setText("通过");
            tv_examine_market.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }else{
            tv_examine_market.setText("未审核");
            tv_examine_market.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_blue));
        }
        String advice=bean.getLevel1CheckMsg();
        if(Tools.isEmpty(advice)){
            tv_market_msg.setText(advice+"");
        }else{
            tv_market_msg.setText("");
        }

        String status1=bean.getLevel2CheckStatus();  //分管领导审核状态0-未审核1-审核通过2审核不通过
        if("2".equals(status1)){
            tv_ex_leader.setText("不通过");
            tv_ex_leader.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_red));
        }else if("1".equals(status1)){
            tv_ex_leader.setText("通过");
            tv_ex_leader.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }else{
            tv_ex_leader.setText("未审核");
            tv_ex_leader.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_blue));
        }
        String advice1=bean.getLevel2CheckMsg();
        if(!Tools.isEmpty(advice1)){
            tv_last_advice.setText(advice1+"");
        }else{
            tv_last_advice.setText("");
        }

        tranTask = LogsExActTask.getInstance(mContext, mHandler);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.rl_pass:
                String  status=bean.getDetailStatus(); ////人工处理状态，0未处理、1已处理
                if(!"1".equals(status)){
                    showTips("没有取证处理");
                    return;
                }
                String et_ad=et_other_advice.getText().toString().trim();
                if(Tools.isEmpty(et_ad)){
                    showTips("审核意见不能为空");
                    return;
                }
                HashMap  mapParams = new HashMap<>();
                String orderCode=bean.getOrderCode();
                mapParams.put("token", token);
                mapParams.put("checkType","2"); //审核类型(默认1)1：物流部审核、2：分管领导审核、3：总经理审核
                mapParams.put("orderCode",orderCode);
                mapParams.put("checkStatus","1");//审核状态(默认0):0:未审核,1:已审核,2:审核不通过
                mapParams.put("checkMsg",et_ad);
                tranTask.getTransportList(mapParams);
                break;
            case R.id.rl_un_pass:
                String  status1=bean.getDetailStatus(); ////人工处理状态，0未处理、1已处理
                if(!"1".equals(status1)){
                    showTips("没有取证处理");
                    return;
                }
                String et_ad1=et_other_advice.getText().toString().trim();
                if(Tools.isEmpty(et_ad1)){
                    showTips("审核意见不能为空");
                    return;
                }
                HashMap  mapParams1 = new HashMap<>();
                String orderCode1=bean.getOrderCode();
                mapParams1.put("token", token);
                mapParams1.put("checkType","2"); //审核类型(默认1)1：物流部审核、2：分管领导审核、3：总经理审核checkMsg
                mapParams1.put("orderCode",orderCode1);
                mapParams1.put("checkStatus","2");//审核状态(默认0):0:未审核,1:已审核,2:审核不通过
                mapParams1.put("checkMsg",et_ad1);
                tranTask.getTransportList(mapParams1);
                break;
            case R.id.bt_close:
                String  status3=bean.getDetailStatus(); ////人工处理状态，0未处理、1已处理
                if(!"1".equals(status3)){
                    showTips("没有取证处理");
                    return;
                }
                String et_ad3=et_other_advice.getText().toString().trim();
                if(Tools.isEmpty(et_ad3)){
                    showTips("审核意见不能为空");
                    return;
                }
                HashMap  mapParams3 = new HashMap<>();
                String orderCode3=bean.getOrderCode();
                mapParams3.put("token", token);
                mapParams3.put("checkType","2"); //审核类型(默认1)1：物流部审核、2：分管领导审核、3：总经理审核checkMsg
                mapParams3.put("orderCode",orderCode3);
                mapParams3.put("checkStatus","3");//审核状态(默认0):0:未审核,1:已审核,2:审核不通过
                mapParams3.put("checkMsg",et_ad3);
                tranTask.getTransportList(mapParams3);
                break;

        }

    }
    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                        ToastUtil.getInstance().showCenterMessage(mContext, "审核成功");
                    getActivity().finish();

                    Intent allIntent=new Intent(ConstantsCode.EXAMINE_RESHE_PAGE_LEADER);//刷新页面
                    mContext.sendBroadcast(allIntent);
                    break;
                case ConstantsCode.MSG_REQUEST_FAIL:
                        ToastUtil.getInstance().showCenterMessage(mContext, "审核失败");
                    break;
                default:
                    break;
            }
        }
    };

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }

}
