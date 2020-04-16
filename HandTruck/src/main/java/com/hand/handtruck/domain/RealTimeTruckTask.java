package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.model.RealTimeTruckBean;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/3/9
 * Describe: 登录任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class RealTimeTruckTask {
    private static Handler mHandler;
    private static Context mContext;
    private static RealTimeTruckTask instance = null;

    public RealTimeTruckTask() {
    }

    public static RealTimeTruckTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new RealTimeTruckTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    public void getRealTimeTruckInfo(Map<String, String> map) {
       /* final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext);*/

        String url = Constants.HttpUrl.CAR_REAL_TIME;//
        LogUtil.e("获取真实车辆信息請求URL=="+url+map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
               // dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取真实车辆信息请求返回的response==" + response.toString());

                if (!TextUtils.isEmpty(response)) {
                    //dialog.dismiss();
                    RealTimeTruckBean realTimeTruckResult = null;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if ("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            realTimeTruckResult = CommonKitUtil.parseJsonWithGson(data.toString(), RealTimeTruckBean.class);
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS2, realTimeTruckResult));
                        }else {
                            ToastUtil.showMsgShort(mContext,msg);
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                            if(msg.contains("重新登录")){
                                Intent i=new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(i);
                            }
                        }

                    } catch (Exception e) {
                        LogUtil.e("获取真实车辆信息主动获取的异常=="+e.getMessage());
                    }


                } else {
                    //dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                }

            }
        });

    }
}
