package com.hand.handtruck.ui.TransportThing.presenter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.Tools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Describe: 运输任务列表接口
 */


public class WeiLanTask {
    private static Handler mHandler;
    private static Context mContext;
    private static WeiLanTask instance = null;
    public WeiLanTask() {
    }


    public static WeiLanTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new WeiLanTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*运输任务列表接口*/
    public void getWeiLan(Map<String, String> map) {
        String url = Constants.HttpUrl.WEI_LAN;
        LogUtil.e("获取运输任务列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
              mHandler.sendEmptyMessage(ConstantsCode.MSG_WEI_LAN_FAIL);
//                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    JSONObject jsonObject1 = null;
                    JSONObject jsonObject2 = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("districts");

                        jsonObject1 = new JSONObject(code);

                        String code1 = jsonObject1.getString("0");

                        jsonObject2 = new JSONObject(code);

                        String polyline = jsonObject2.getString("polyline");

                    if (!Tools.isEmpty(polyline)) {
                        mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_WEI_LAN_SUCCESS, polyline));
                    } else {
//                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_WEI_LAN_FAIL);
                }

            }
        });

    }

}
