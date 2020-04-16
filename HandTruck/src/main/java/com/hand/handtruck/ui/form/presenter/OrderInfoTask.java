package com.hand.handtruck.ui.form.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.form.bean.WeiBean;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Describe: 订单围栏
 */


public class OrderInfoTask {
    private static Handler mHandler;
    private static Context mContext;
    private static OrderInfoTask instance = null;
    public OrderInfoTask() {
    }


    public static OrderInfoTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new OrderInfoTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*订单围栏*/
    public void getWeiLan(Map<String, String> map) {
        String url = Constants.HttpUrl.ORDER_RAIL;
        LogUtil.e("获取订单围栏URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
              mHandler.sendEmptyMessage(ConstantsCode.MSG_WEI_LAN_FAIL);
//                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取订单围栏返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    JSONObject jsonObject1 = null;
                    String mMessage="";
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        mMessage=msg;
                        if("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            if (Tools.isEmpty(data) ) {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                            } else {

                                jsonObject1 = new JSONObject(data.toString());
                                String areaRaillist = jsonObject1.getString("areaRaillist"); //区域围栏
                                String agencyRaillist = jsonObject1.getString("agencyRaillist"); //经销商围栏agencyRaillist
                                List<WeiBean> wList1=new ArrayList<>();
                                if(!Tools.isEmpty(areaRaillist) && !areaRaillist.contains("[]")){
                                     wList1 = CommonKitUtil.jsonStringConvertToList(areaRaillist, WeiBean[].class);
                                }
                                List<WeiBean> wList2=new ArrayList<>();
                                if(!Tools.isEmpty(agencyRaillist) && !agencyRaillist.contains("[]")){
                                    wList2 = CommonKitUtil.jsonStringConvertToList(agencyRaillist, WeiBean[].class);
                                }
                                List<WeiBean> wList=new ArrayList<>();
                                if(wList1 !=null && wList1.size() >0){
                                    for(int i=0;i<wList1.size();i++){
                                        WeiBean wbean=wList1.get(i);
                                        wbean.setTag("1");
                                        wList.add(wbean);
                                    }
                                }

                                if(wList2 !=null && wList2.size() >0){
                                    for(int j=0;j<wList2.size();j++){
                                        WeiBean wbean1=wList2.get(j);
                                        wbean1.setTag("2");
                                        wList.add(wbean1);
                                    }
                                }

                                if (wList != null && wList.size() > 0) {
                                    mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_WEI_LAN_SUCCESS, wList));
                                } else {
                                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                                }
                            }
                        }else{
                            doLoginAgain(mMessage);//重新登录
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                        }
                    } catch (Exception e) {
                        doLoginAgain(mMessage);//重新登录
                        e.printStackTrace();
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                }

            }
        });

    }
    public void doLoginAgain(String message){
        if(message.contains("重新登录")){
            Intent i=new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i);
        }
    }
}
