package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.bean.CompanyChangeResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/5/4
 * Describe: 获取公司列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class CompanyChangeListTask {
    private static Handler mHandler;
    private static Context mContext;
    private static CompanyChangeListTask instance = null;

    public static CompanyChangeListTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new CompanyChangeListTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*获取公司信息*/
    public void changeCompany(Map<String, String> map) {
        String url = Constants.HttpUrl.COMPANY_CHANGE_LIST;
        LogUtil.e("获取公司請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            private JSONObject jsonObject;
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取公司请求返回的response==" + response.toString());
                try{
                    if (!TextUtils.isEmpty(response)) {
                        jsonObject = new JSONObject(response);
                        String  message=jsonObject.optString("message");
                        if ("success".equals(message)|| message.contains("成功")) {
                            CompanyChangeResultBean companyResult =new CompanyChangeResultBean();
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS3, companyResult));
                        } else {
                            ToastUtil.showMsgShort(mContext, message);
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                            if(message.contains("重新登录")){
                                Intent i=new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(i);
                            }
                        }

                    } else {
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL3);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
}
