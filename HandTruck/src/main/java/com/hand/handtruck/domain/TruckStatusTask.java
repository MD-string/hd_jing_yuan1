package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.bean.OnLineTruckResultBean;
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
 * Created by wcf on 2018/3/12
 * Describe: 获取车辆列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class TruckStatusTask {
    private static Handler mHandler;
    private static Context mContext;
    private static TruckStatusTask instance = null;

    public TruckStatusTask() {
    }

    public static TruckStatusTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new TruckStatusTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }


    /*获取在线车辆*/
    public void getTruckStatusInfo(Map<String, String> map) {
     /*   final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext);*/

        String url = Constants.HttpUrl.CAR_STATUS;
        LogUtil.e("获取车辆状态請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
               // dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取车辆状态请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                   // dialog.dismiss();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String result = jsonObject.getString("result");
                        if (!result.equals("[]")){
                            OnLineTruckResultBean onLineTurckBean = CommonKitUtil.parseJsonWithGson(response.toString(), OnLineTruckResultBean.class);
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS3, onLineTurckBean));
                        }else {
                            String message = jsonObject.getString("message");;
                            ToastUtil.showMsgShort(mContext, message);
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                            if(message.contains("重新登录")){
                                Intent i=new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(i);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                    }



                } else {
                    //dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                }

            }
        });

    }



}
