package com.hand.handtruck.domain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.DialogUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/5/4
 * Describe: 获取公司列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class TruckAddTask {
    private static Handler mHandler;
    private static Context mContext;
    private static TruckAddTask instance = null;

    public TruckAddTask() {
    }

    public static TruckAddTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new TruckAddTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }


    /*更新车辆信息*/
    public void updateTruckInfo(Map<String, String> map) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext);

        String url = Constants.HttpUrl.CAR_ADD_EDITOR;
        LogUtil.e("更新车辆請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("更新车辆请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    dialog.dismiss();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String result = jsonObject.getString("result");
                        String message = jsonObject.getString("message");
                        if (message.equals("车辆更新成功!")){
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_SUCCESS);
                        }else {
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                        }
                        ToastUtil.showMsgShort(mContext, message);
                        if(message.contains("重新登录")){
                            Intent i=new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                }

            }
        });

    }

}
