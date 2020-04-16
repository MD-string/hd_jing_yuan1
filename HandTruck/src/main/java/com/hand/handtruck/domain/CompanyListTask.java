package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.bean.CompanyResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/5/4
 * Describe: 获取公司列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class CompanyListTask {
    private static Handler mHandler;
    private static Context mContext;


    public CompanyListTask(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    /*获取公司信息*/
    public void getCompanyList(Map<String, String> map) {
        String url = Constants.HttpUrl.COMPANY_LIST;
        LogUtil.e("获取公司請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取公司请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    CompanyResultBean companyResult = CommonKitUtil.parseJsonWithGson(response.toString(), CompanyResultBean.class);
                    String message = companyResult.getMessage();
                    if ("success".equals(message)) {
                        mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS1, companyResult));
                    } else {
                        ToastUtil.showMsgShort(mContext, message);
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                        if(message.contains("重新登录")){
                            Intent i=new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(i);
                        }
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                }

            }
        });

    }
}
