package com.hand.handtruck.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.activity.LoginActivity;
import com.hand.handtruck.bean.CompanyTruckResultBean;
import com.hand.handtruck.bean.OnLineTruckListBean;
import com.hand.handtruck.bean.OnLineTruckResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wcf on 2018/3/12
 * Describe: 获取车辆列表任务
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class GetCompanyTruckTask {
    private static Handler mHandler;
    private static Context mContext;
    private static GetCompanyTruckTask instance = null;
    private CustomDialog dialog;

    public GetCompanyTruckTask() {
    }

    public static GetCompanyTruckTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new GetCompanyTruckTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    /*获取所有车辆信息*/
    public void getCarList(final Map<String, String> map) {
      /*  final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext);*/

        String url = Constants.HttpUrl.CAR_LIST;
        LogUtil.e("获取公司及车辆請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取公司及车辆请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    //   dialog.dismiss();
                    CompanyTruckResultBean carResultBean = CommonKitUtil.parseJsonWithGson(response.toString(), CompanyTruckResultBean.class);
                    String message = carResultBean.getMessage();
                    if ("success".equals(message)) {
                        mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, carResultBean));

                    } else {
                        ToastUtil.showMsgShort(mContext, message);
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                        if(message.contains("重新登录")){
                            Intent i=new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(i);
                        }
                    }

                } else {
                    //  dialog.dismiss();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                }

            }
        });

    }

    /*获取在线车辆列表*/
    public void getOnlineTruckListInfo(Map<String, String> map) {

        showProgressDialog();

        String url = Constants.HttpUrl.CAR_REAL_TIME_LIST;
        LogUtil.e("获取在线车辆請求列表URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressDialog();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取在线车辆列表请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    closeProgressDialog();
                    OnLineTruckListBean onLineTurckResultBean = CommonKitUtil.parseJsonWithGson(response.toString(), OnLineTruckListBean.class);
                    String message = onLineTurckResultBean.getMessage();
                    if ("success".equals(message)) {
                        mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS1, onLineTurckResultBean));
                    } else {
                        ToastUtil.showMsgShort(mContext, message);
                        mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                    }

                } else {
                    closeProgressDialog();
                    mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
                }

            }
        });

    }
    /*获取在线车辆*/
    public void getOnlineTruckInfo(Map<String, String> map) {
        /*final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext);
*/
        String url = Constants.HttpUrl.CAR_STATUS;
        LogUtil.e("获取在线车辆請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
              //  dialog.dismiss();
                mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("获取在线车辆请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    //dialog.dismiss();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String result = jsonObject.getString("result");
                        if (!result.equals("[]")){
                            OnLineTruckResultBean onLineTurckBean = CommonKitUtil.parseJsonWithGson(response.toString(), OnLineTruckResultBean.class);
                            mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS, onLineTurckBean));
                        }else {
                            String message = jsonObject.getString("message");;
                            ToastUtil.showMsgShort(mContext, message);
                            mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } else {
                   // dialog.dismiss();
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
