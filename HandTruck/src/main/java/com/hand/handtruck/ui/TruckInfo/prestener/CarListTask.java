package com.hand.handtruck.ui.TruckInfo.prestener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.ui.TruckInfo.ICarListView;
import com.hand.handtruck.ui.TruckInfo.bean.CarInfo;
import com.hand.handtruck.utils.CommonUtils;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Describe: 运输任务列表接口
 */


public class CarListTask {
    private static Context mContext;

    private ICarListView mView;

    public CarListTask(Context context, ICarListView view) {
        mContext = context;
        mView = view;
    }

    /*车辆实时数据列表接口 */
    public void getCarList(Map<String, String> map) {
        String url = Constants.HttpUrl.COMPANY_LIST_CAR;
        LogUtil.e("车辆实时数据列表接口==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mView.doError("获取车辆实时数据列表失败");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("车辆实时数据列表接口response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if("200".equals(code)){
                            String data = jsonObject.getString("data");
                            List<CarInfo> calist = jsonStringConvertToList(data, CarInfo[].class);
                            mView.doSuccess(calist);
                        }else if("500".equals(code)){
                            CommonUtils.reStartLoginAgain(mContext);//重新登录
                        }else {
                            mView.doError(msg);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    mView.doError("获取车辆实时数据列表失败");
                }

            }
        });

    }
    public static <T> List<T> jsonStringConvertToList(String string, Class<T[]> cls) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(string, cls);
        return Arrays.asList(array);
    }
    public void doLoginAgain(String message){
        if(message.contains("重新登录")){
            Intent i=new Intent(mContext, LoginActivity.class);
            mContext.startActivity(i);
        }
    }
}
