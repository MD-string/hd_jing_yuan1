package com.hand.handtruck.ui.Examine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 审核汇总
 */

public class TotalContentExFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private DecimalFormat mineformat= new DecimalFormat("0.00");
    private Map<String, String> mapParams;
    private SharedPreferences sp = null;
    private FormBean bean;
    private String token;
    private LogsExActTask tranTask;
    private TextView tv_examine_market,tv_market_msg;
    private TextView tv_leader_check,tv_leader_msg;
    private TextView tv_boss_check,tv_boss_advise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        View view = inflater.inflate(R.layout.activity_examine_total, container, false);

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


        tv_leader_check=(TextView)view.findViewById(R.id.tv_leader_check);
        tv_leader_msg=(TextView)view.findViewById(R.id.tv_leader_msg);


        tv_boss_check=(TextView)view.findViewById(R.id.tv_boss_check);
        tv_boss_advise=(TextView)view.findViewById(R.id.tv_boss_advise);


    }

    protected void setListeners() {
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

//        String status1=bean.getLevel2CheckStatus();  //分管领导审核状态0-未审核1-审核通过2审核不通过
//        if("2".equals(status1)){
//            tv_leader_check.setText("不通过");
//            tv_leader_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_red));
//        }else if("1".equals(status1)){
//            tv_leader_check.setText("通过");
//            tv_leader_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
//        }else{
//            tv_leader_check.setText("未审核");
//            tv_leader_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_blue));
//        }
//        String advice1=bean.getLevel2CheckMsg();
//        if(!Tools.isEmpty(advice1)){
//            tv_leader_msg.setText(advice1+"");
//        }else{
//            tv_leader_msg.setText("");
//        }

        String status2=bean.getLevel3CheckStatus();  //总经理审核状态0-未审核1-审核通过2审核不通过
        if("2".equals(status2)){
            tv_boss_check.setText("不通过");
            tv_boss_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_red));
        }else if("1".equals(status2)){
            tv_boss_check.setText("通过");
            tv_boss_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_green));
        }else{
            tv_boss_check.setText("未审核");
            tv_boss_check.setBackground(mContext.getResources().getDrawable(R.drawable.shape_form_blue));
        }
        String advice2=bean.getLevel3CheckMsg();
        if(!Tools.isEmpty(advice2)){
            tv_boss_advise.setText(advice2+"");
        }else{
            tv_boss_advise.setText("");
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.bt_pass:

                String  status=bean.getDetailStatus(); ////人工处理状态，0未处理、1已处理
                if(!"1".equals(status)){
                    showTips("没有取证处理");
                    return;
                }
                String status_market=bean.getMarketCheckStatus();  //物流部审核状态0-未审核1-审核通过2审核不通过
                if(!"1".equals(status_market)){
                    showTips("销售部审核未审核或不通过");
                    return;
                }
//                String status_leader=bean.getSalesCheckStatus();  //分管领导审核状态0-未审核1-审核通过2审核不通过
//                if(!"1".equals(status_leader)){
//                    showTips("分管领导审核未审核或不通过");
//                    return;
//                }
                String et_ad="";
                if(Tools.isEmpty(et_ad)){
                    showTips("审核意见不能为空");
                    return;
                }
                HashMap  mapParams = new HashMap<>();
                String orderCode=bean.getOrderCode();
                mapParams.put("token", token);
                mapParams.put("checkType","3"); //审核类型(默认1)1：物流部审核、2：分管领导审核、3：总经理审核
                mapParams.put("orderCode",orderCode);
                mapParams.put("checkStatus","1");//审核状态(默认0):0:未审核,1:已审核,2:审核不通过
                mapParams.put("checkMsg",et_ad);
                tranTask.getTransportList(mapParams);
                break;
            case R.id.bt_unpass:
                String  status1=bean.getDetailStatus(); ////人工处理状态，0未处理、1已处理
                if(!"1".equals(status1)){
                    showTips("没有取证处理");
                    return;
                }
                String status_market1=bean.getMarketCheckStatus();  //物流部审核状态0-未审核1-审核通过2审核不通过
                if(!"1".equals(status_market1)){
                    showTips("销售部审核未审核或不通过");
                    return;
                }
//                String status_leader1=bean.getSalesCheckStatus();  //分管领导审核状态0-未审核1-审核通过2审核不通过
//                if(!"1".equals(status_leader1)){
//                    showTips("分管领导审核未审核或不通过");
//                    return;
//                }
                String et_ad1="";
                if(Tools.isEmpty(et_ad1)){
                    showTips("审核意见不能为空");
                    return;
                }
                HashMap  mapParams1 = new HashMap<>();
                String orderCode1=bean.getOrderCode();
                mapParams1.put("token", token);
                mapParams1.put("checkType","3"); //审核类型(默认1)1：物流部审核、2：分管领导审核、3：总经理审核checkMsg
                mapParams1.put("orderCode",orderCode1);
                mapParams1.put("checkStatus","2");//审核状态(默认0):0:未审核,1:已审核,2:审核不通过
                mapParams1.put("checkMsg",et_ad1);
                tranTask.getTransportList(mapParams1);
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
