package com.hand.handtruck.domain;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.bean.CityBean;
import com.hand.handtruck.bean.CityResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/5/4
 * Describe: 获取公司列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class CityListTask {
    private static Handler mHandler;
    private static Context mContext;
    private static CityListTask instance = null;
    public CityListTask() {
    }


    public static CityListTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new CityListTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*获取城市*/
    public void getCityList(Map<String, String> map) {
        String url = Constants.HttpUrl.CITY_LIST;
        LogUtil.e("获取城市URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取城市返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    CityResultBean companyResult = CommonKitUtil.parseJsonWithGson(response.toString(), CityResultBean.class);
                    List<CityBean> message = companyResult.getResult();
                    if (message!=null && message.size() >0) {
                        mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS2, companyResult));
                    } else {
                        ToastUtil.showMsgShort(mContext, "获取城市失败");
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                }

            }
        });

    }
}
