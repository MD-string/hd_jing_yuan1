package com.hand.handtruck.domain;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.bean.WeightTrendResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/3/9
 * Describe: 登录任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class WeightDataTask {
    private static Handler mHandler;
    private static Context mContext;
    private static WeightDataTask instance = null;

    public WeightDataTask() {
    }

    public static WeightDataTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new WeightDataTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    public void getWeightDataTask(Map<String, String> map) {
//        final ProgressDialog dialog = new ProgressDialog(mContext);
//        DialogUtil.setProgressDialog(dialog, mContext);

        String url = Constants.HttpUrl.CAR_WEIGHT_DATA;
        LogUtil.e("获取重量趋势請求URL=="+url+map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取重量趋势请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
//                    dialog.dismiss();
                    WeightTrendResultBean weightTrendResult = null;
                    try {
                        weightTrendResult = CommonKitUtil.parseJsonWithGson(response.toString(), WeightTrendResultBean.class);
                        String message = weightTrendResult.getMessage();
                        if ("success".equals(message)) {
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, weightTrendResult));
                        }else {
                            ToastUtil.showMsgShort(mContext,message);
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                        }

                    } catch (Exception e) {
                        LogUtil.e("获取重量趋势的异常=="+e.getMessage());
                    }


                } else {
//                    dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                }

            }
        });

    }
}
