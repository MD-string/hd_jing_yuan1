package com.hand.handtruck.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hand.handlibray.util.Aes;
import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.util.SPUtil;
import com.hand.handlibray.util.ToastUtil;
import com.hand.handtruck.R;
import com.hand.handtruck.base.BaseActivity;
import com.hand.handtruck.bean.UserResultBean;
import com.hand.handtruck.constant.ConstantsCode;
import com.hand.handtruck.domain.LoginTask;
import com.hand.handtruck.domain.PowerTask;
import com.hand.handtruck.log.DLog;
import com.hand.handtruck.ui.home.MainFragmentActivity;
import com.hand.handtruck.utils.LogUtil;
import com.hand.handtruck.utils.ToUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by hxz on 2019/5/15
 * Describe: 登录页
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtLoginCount, mEtLoginPassword;
    private CheckBox mCbLogin;
    private Button mBtnLogin, mBtnLoginRegister;
    private TextView mTvVersion;
    private String count, password;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    SharedPreferences sp = null;
    private LinearLayout ll_remember;
    private Context context;
    private boolean isRemeber;
    private ImageView img_eye;
    private boolean isEyeOpen;
    private TextView tv_remb_text;


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /*登录成功*/
                case ConstantsCode.MSG_REQUEST_SUCCESS:
                    UserResultBean user = (UserResultBean) msg.obj;
                    boolean CheckBoxLogin = mCbLogin.isChecked();
                    if (CheckBoxLogin) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("uname", count);
                        editor.putString("upswd", password);
                        editor.putBoolean("checkboxBoolean", true);
                        editor.putString("token", user.getToken());
                        editor.putString("roleName", user.getResult().getRoleName());
                        editor.commit();

                    } else {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("uname", count);
                        editor.putString("upswd", null);
                        editor.putBoolean("checkboxBoolean", false);
                        editor.putString("token", user.getToken());
                        editor.putString("roleName", user.getResult().getRoleName());
                        editor.commit();
                    }

                    //极光推送 设置别名
                    String useId = user.getResult().getUserId();
                    JPushInterface.setAlias(context, useId, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {//i  0 表示调用成功。 s原设置的别名 set 原设置的标签
                            if (0 == i) {
                                DLog.e("LoginActivity", "JPushInterface" + "调用成功");
                            }
                        }
                    });

                    ToUtil.getInstance().showCenterMessage(mContext, "登录成功");
                    Map<String, String> mapPower = new HashMap<>();
                    mapPower.put("token", user.getToken() + "");
                    PowerTask.getInstance(context, sp, mHandler).postPower(mapPower);
                    //                    CommonKitUtil.startActivity(LoginActivity.this, MainFragmentActivity.class, new Bundle(), false);

                    break;
                /*登录错误*/
                case ConstantsCode.MSG_REQUEST_ERROR:
                    break;
                /*登录失败*/
                case ConstantsCode.MSG_REQUEST_FAIL:
                    try {
                        UserResultBean user1 = (UserResultBean) msg.obj;
                        String message = user1.getMessage();
                        ToUtil.getInstance().showCenterMessage(mContext, message);
                    } catch (Exception e) {
                        ToUtil.getInstance().showCenterMessage(mContext, "登录失败");
                    }
                    break;
                case ConstantsCode.MSG_QUAN_XIAN_SUCCESS://权限成功
                    CommonKitUtil.startActivity(LoginActivity.this, MainFragmentActivity.class, new Bundle(), false);
                    break;
                case ConstantsCode.MSG_QUAN_XIAN_FAIL://权限失败
                    ToUtil.getInstance().showCenterMessage(mContext, "获取权限失败");

                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        context = this;
        mEtLoginCount = (EditText) findViewById(R.id.et_login_count);
        mEtLoginPassword = (EditText) findViewById(R.id.et_login_password);
        mCbLogin = (CheckBox) findViewById(R.id.cb_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLoginRegister = (Button) findViewById(R.id.btn_login_register);
        mTvVersion = (TextView) findViewById(R.id.tv_version);

        img_eye = (ImageView) findViewById(R.id.img_eye);
        img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_close_eye));
        isEyeOpen = false;
        img_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEyeOpen) {
                    isEyeOpen = true;
                    img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_open_eye));
                    mEtLoginPassword.setInputType(128);//设置为显示密码
                } else {
                    isEyeOpen = false;
                    img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_close_eye));
                    mEtLoginPassword.setInputType(129);//设置为隐藏密码
                }
            }
        });
        ll_remember = (LinearLayout) findViewById(R.id.ll_remember);
        tv_remb_text = (TextView) findViewById(R.id.tv_remb_text);

        ll_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRemeber) {
                    isRemeber = false;
                    mCbLogin.setChecked(false);
                    tv_remb_text.setText("记住密码");
                    tv_remb_text.setTextColor(context.getResources().getColor(R.color.log_text1));
                } else {
                    isRemeber = true;
                    mCbLogin.setChecked(true);
                    tv_remb_text.setText("忘记密码");
                    tv_remb_text.setTextColor(context.getResources().getColor(R.color.log_text4));
                }
            }
        });

    }

    @Override
    protected void setListeners() {
        mBtnLogin.setOnClickListener(this);
        mBtnLoginRegister.setOnClickListener(this);
        //避免部分手机退到后台 重新登录问题
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    protected void inIt() {
        sp = this.getSharedPreferences(ConstantsCode.FILE_NAME, Context.MODE_PRIVATE);
        mTvVersion.setText("版本号:" + CommonKitUtil.getVersionName(this).toString().trim());
        boolean checkboxBoolean = (boolean) SPUtil.get(this, "checkboxBoolean", false);
        if (checkboxBoolean) {
            String uname = (String) sp.getString("uname", null);
            String upswd = (String) sp.getString("upswd", null);
            mEtLoginCount.setText(uname);
            mEtLoginPassword.setText(upswd);
            mCbLogin.setChecked(true);
            tv_remb_text.setText("忘记密码");
            tv_remb_text.setTextColor(context.getResources().getColor(R.color.log_text4));
        } else {
            String uname = (String) sp.getString("uname", null);
            mEtLoginCount.setText(uname);
            mEtLoginPassword.setText("");
            mCbLogin.setChecked(false);
            tv_remb_text.setText("记住密码");
            tv_remb_text.setTextColor(context.getResources().getColor(R.color.log_text1));
        }
        mEtLoginCount.setSelection(mEtLoginCount.getText().length());
        mEtLoginPassword.setSelection(mEtLoginPassword.getText().length());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*登录*/
            case R.id.btn_login:
                submit();
                break;
            /*暂时无注册功能*/
            case R.id.btn_login_register:

                break;
        }
    }

    private void submit() {
        count = mEtLoginCount.getText().toString().trim();
        password = mEtLoginPassword.getText().toString().trim();
        /*检查网络*/
        if (!CommonKitUtil.isNetworkAvailable(this)) {
            ToastUtil.showMsgShort(this, ConstantsCode.NETWORK_ERROR);
            return;
        }
        if (TextUtils.isEmpty(count)) {
            ToastUtil.showMsgShort(this, "账号不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.showMsgShort(this, "密码不能为空");
            return;
        }
        //        else if (!TextUtil.isPSD(password)) {
        //            ToastUtil.showMsgShort(LoginActivity.this, "密码至少是6位，且只能是字母，数字，下划线");
        //            return;
        //        }
        final String inputString = count + "#" + password;

        try {
            String encryStr = Aes.encrypt(inputString, PASSWORD_STRING);
            LogUtil.e("登录页Token==" + encryStr);
            Map<String, String> mapLogin = new HashMap<>();
            mapLogin.put("token", encryStr);
            LoginTask.getInstance(this, mHandler).postLogin(mapLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;//消费掉后退键
        }
        return super.onKeyDown(keyCode, event);
    }


}
