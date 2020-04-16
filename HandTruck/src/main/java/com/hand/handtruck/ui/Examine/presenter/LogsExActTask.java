package com.hand.handtruck.ui.Examine.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.CommonUtils;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Describe: 运输任务列表接口
 */


public class LogsExActTask {
    private static Handler mHandler;
    private static Context mContext;
    private static LogsExActTask instance = null;
    public LogsExActTask() {
    }


    public static LogsExActTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new LogsExActTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*运输任务列表接口*/
    public void getTransportList(Map<String, String> map) {
        String url = Constants.HttpUrl.CHECK_MORE1;
        LogUtil.e("获取运输任务列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取运输任务列表返回的response==" + response.toString());
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
//                            String data = jsonObject.getString("data");
//                            jsonObject1 = new JSONObject(data.toString());
//                            String result = jsonObject1.getString("result");
                                mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, msg));
                        }else{
                            if("500".equals(code)){
                                CommonUtils.reStartLoginAgain(mContext);//重新登录
                            }else {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                            }
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
