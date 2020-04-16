package com.hand.handtruck.ui.Examine.presenter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.ui.form.bean.FormBean;
import com.hand.handtruck.ui.form.bean.PagerOrderBean;
import com.hand.handtruck.utils.CommonUtils;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Describe: 运输任务列表接口
 */


public class BossEXTask {
    private static Handler mHandler;
    private static Context mContext;
    private static BossEXTask instance = null;
    public BossEXTask() {
    }


    public static BossEXTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new BossEXTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*运输任务列表接口*/
    public void getTransportList(Map<String, String> map) {
        String url = Constants.HttpUrl.ORDER_LIST;
        LogUtil.e("获取运输任务列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    JSONObject jsonObject1 = null;
                    String mMessage="";
                    try {
                        jsonObject = new JSONObject(response.toString());

                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        mMessage=msg;
                        if("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            jsonObject1 = new JSONObject(data.toString());
                            String result = jsonObject1.getString("result");
                            PagerOrderBean companyResult = CommonKitUtil.parseJsonWithGson(result, PagerOrderBean.class);
                            List<FormBean> message = companyResult.getContent();
                            if (message!=null && message.size() >0) {
                                mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, companyResult));
                            } else {
                                //                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                            }
                        }else{
                            if("500".equals(code)){
                                CommonUtils.reStartLoginAgain(mContext);//重新登录
                            }else {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                }

            }
        });

    }


    /*运输任务列表接口*/
    public void pullListData(Map<String, String> map) {
        String url = Constants.HttpUrl.ORDER_LIST;
        LogUtil.e("获取运输任务列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    JSONObject jsonObject1 = null;
                    String mMessage="";
                    try {
                        jsonObject = new JSONObject(response.toString());

                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        mMessage=msg;
                        if("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            jsonObject1 = new JSONObject(data.toString());
                            String result = jsonObject1.getString("result");
                            PagerOrderBean companyResult = CommonKitUtil.parseJsonWithGson(result, PagerOrderBean.class);
                            List<FormBean> message = companyResult.getContent();
                            if (message!=null && message.size() >0) {
                                mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS1, companyResult));
                            } else {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                            }
                        }else{
                            if("500".equals(code)){
                                CommonUtils.reStartLoginAgain(mContext);//重新登录
                            }else {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                }

            }
        });

    }

    /*运输任务列表接口*/
    public void loadMoreData(Map<String, String> map) {
        String url = Constants.HttpUrl.ORDER_LIST;
        LogUtil.e("获取运输任务列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取运输任务列表返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    JSONObject jsonObject1 = null;
                    String mMessage="";
                    try {
                        jsonObject = new JSONObject(response.toString());

                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        mMessage=msg;
                        if("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            jsonObject1 = new JSONObject(data.toString());
                            String result = jsonObject1.getString("result");
                            PagerOrderBean companyResult = CommonKitUtil.parseJsonWithGson(result, PagerOrderBean.class);
                            List<FormBean> message = companyResult.getContent();
                            if (message!=null && message.size() >0) {
                                mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS2, companyResult));
                            } else {
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_EMPTY);
                            }
                        }else{
                            if("500".equals(code)){
                                CommonUtils.reStartLoginAgain(mContext);//重新登录
                            }else{

                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL2);
                }

            }
        });

    }

}
