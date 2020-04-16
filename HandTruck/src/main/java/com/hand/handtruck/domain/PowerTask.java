package com.hand.handtruck.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hand.handtruck.R;
import com.hand.handtruck.Widget.CustomDialog;
import com.hand.handtruck.bean.PowerBean;
import com.hand.handtruck.bean.PowerResultBean;
import com.hand.handtruck.constant.Constants;
import com.hand.handtruck.constant.ConstantsCode;
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
 * Describe: 登录任务  权限
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */


public class PowerTask {
    private static Context mContext;
    private static PowerTask instance = null;
    private static Handler mHanlder;
    private CustomDialog dialog;
    private static SharedPreferences mShared;

    public PowerTask() {
    }

    public static PowerTask getInstance(Context context, SharedPreferences sare, Handler handler) {
        if (instance == null) {
            instance = new PowerTask();
        }
        mContext = context;
        mShared=sare;
        mHanlder=handler;
        return instance;
    }

    public void postPower(Map<String, String> map) {

        String url = Constants.HttpUrl.URL_POWER;
        LogUtil.e("登陸請求URL==" + url + map.toString());
        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //                dialog.dismiss();
                closeProgressDialog();
                LogUtil.e("权限主动获取的异常==" );
                mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_FAIL);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("权限请求返回的response==" + response.toString());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;

                    PowerResultBean user = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        String code = jsonObject.getString("code");
                        if ("200".equals(code)) {
                            String data = jsonObject.getString("data");
                            List<PowerBean> bean = jsonStringConvertToList(data.toString(),  PowerBean[].class);
                            StringBuffer st=new StringBuffer();
                            if(bean !=null && bean.size() > 0){
                                for(int i=0;i<bean.size();i++){
                                    st.append(bean.get(i).getSourceKey()+";");
                                }
                                String str=st.toString();
//                                str= str.substring(0,str.length()-1);
//                                String ohter=str.replace("transportTask:listTask","");
                                SharedPreferences.Editor editor = mShared.edit();
                                editor.putString("sourceKey", str);
                                editor.commit();
                                mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_SUCCESS);

                                LogUtil.e("权限主动获success" );
                            }else{
                                LogUtil.e("权限主动获取数据为空" );
                                mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_FAIL);
                            }

                        } else {
//                            ToastUtil.showMsgShort(mContext, message);
                            LogUtil.e("权限主动获取的异常==" );
                            mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_FAIL);
                        }

                    } catch (Exception e) {
                        LogUtil.e("权限主动获取的异常==" + e.getMessage());
                        mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_FAIL);
                    }


                } else {
                    LogUtil.e("权限主动获取的异常==" );
                    mHanlder.sendEmptyMessage(ConstantsCode.MSG_QUAN_XIAN_FAIL);
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
    public static <T> List<T> jsonStringConvertToList(String string, Class<T[]> cls) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(string, cls);
        return Arrays.asList(array);
    }
}
