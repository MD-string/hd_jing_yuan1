package com.hand.handtruck.domain;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.bean.UserResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/3/9
 * Describe: 登录任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class LoginTask {
    private static Handler mHandler;
    private static Context mContext;
    private static LoginTask instance = null;
    private CustomDialog dialog;

    public LoginTask() {
    }

    public static LoginTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new LoginTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    public void postLogin(Map<String, String> map) {
        //        final ProgressDialog dialog = new ProgressDialog(mContext);
        //        DialogUtil.setProgressDialog(dialog, mContext);
        showProgressDialog();

        String url = Constants.HttpUrl.LOGIN;
        LogUtil.e("登陸請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //                dialog.dismiss();
                closeProgressDialog();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("登录请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    closeProgressDialog();
                    JSONObject jsonObject = null;
                    UserResultBean user = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if ("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            user = CommonKitUtil.parseJsonWithGson(data.toString(), UserResultBean.class);
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, user));
                        } else {
                            UserResultBean user2=new UserResultBean();
                            user2.setMessage(msg+"");
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_FAIL, user2));
                        }

                    } catch (Exception e) {
                        LogUtil.e("登录主动获取的异常==" + e.getMessage());
                    }


                } else {
                    //                    dialog.dismiss();
                    closeProgressDialog();
                    UserResultBean user3=new UserResultBean();
                    user3.setMessage("登录失败");
                    mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_FAIL, user3));
                }

            }
        });

    }
        public void getUserPower(String token) {
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            String url = Constants.HttpUrl.USER_POWER;
            OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    //                dialog.dismiss();
//                    closeProgressDialog();
//                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
//                    ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtil.e("获取权限response==" + response.toString());
                    if (!TextUtils.isEmpty(response)) {

                        UserResultBean user = null;
                        try {
                            user = CommonKitUtil.parseJsonWithGson(response.toString(), UserResultBean.class);
                            String message = user.getMessage();
                            if ("success".equals(message)) {
                                mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, user));
                            }else {
                                ToastUtil.showMsgShort(mContext,message);
                                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                            }

                        } catch (Exception e) {
                            LogUtil.e("获取权限异常=="+e.getMessage());
                        }


                    } else {
                        //                    dialog.dismiss();
                        closeProgressDialog();
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                    }

                }
            });

    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        dialog = new CustomDialog(mContext, R.style.LoadDialog);
        dialog.show();
        new Thread("cancle_progressDialog") {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
                    if(dialog !=null ){
                        dialog.cancel();
                    }
                    // dialog.dismiss();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
