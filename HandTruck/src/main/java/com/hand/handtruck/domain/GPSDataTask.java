package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.model.GPSDataBean;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/3/9
 * Describe: 登录任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class GPSDataTask {
    private static Handler mHandler;
    private static Context mContext;
    private static GPSDataTask instance = null;

    public GPSDataTask() {
    }

    public static GPSDataTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new GPSDataTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    public void getGPSDataTask(Map<String, String> map) {
        //        final ProgressDialog dialog = new ProgressDialog(mContext);
        //        DialogUtil.setProgressDialog(dialog, mContext);

        String url = Constants.HttpUrl.HISTORY_LIST;
        LogUtil.e("获取GPS請求URL=="+url+map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //                dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取GPS请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    //                    dialog.dismiss();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if("200".equals(code)){
                            String data = jsonObject.getString("data");
                            List<GPSDataBean> list= jsonStringConvertToList(data.toString(), GPSDataBean[].class);
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS1,list));
                        }else {
                            ToastUtil.showMsgShort(mContext,msg);
                            if(msg.contains("重新登录")){
                                Intent i=new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(i);
                            }else{
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                            }
                        }

                    } catch (Exception e) {
                        LogUtil.e("获取GPS的异常=="+e.getMessage());
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                    }


                } else {
                    //                    dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                }

            }
        });

    }

    public static <T> List<T> jsonStringConvertToList(String string, Class<T[]> cls) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(string, cls);
        return Arrays.asList(array);
    }
}
