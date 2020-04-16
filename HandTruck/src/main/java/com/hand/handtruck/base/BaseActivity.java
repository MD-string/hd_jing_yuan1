package com.hand.handtruck.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.hand.handlibray.util.CommonKitUtil;
import com.hand.handlibray.view.PromptDialog;
import com.hand.handtruck.R;
import com.hand.handtruck.application.MyApplication;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseActivity extends FragmentActivity {
    public static final String SYSTEM_EXIT = "com.hand.handtruck.system_exit";
    public Context mContext;
    private ExitReceiver mExitReceiver;
    protected boolean mIsLoadingDialogShowing = false;
    protected ProgressDialog mLoadingProgressDialog;
    protected Handler mBaseHandler = new Handler();
    private PromptDialog mdialog;
    @Subscribe(threadMode = ThreadMode.MainThread)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
         /*将创建的activity添加进去*/
        MyApplication.getInstance().addActivity(this);
        //透明状态栏
          CommonKitUtil.windowTranslucentStatus(BaseActivity.this);
        //注册EventBus
       EventBus.getDefault().register(this);
        initView();
        findViews();
        inIt();
        setListeners();
    }

    /**
     * 获取页面布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 绑定控件
     */
    protected abstract void findViews();

    /**
     * 设置监听
     */
    protected abstract void setListeners();

    /**
     * 初始化操作
     */
    protected abstract void inIt();

    private void initView() {
        mContext = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYSTEM_EXIT);
        mExitReceiver = new ExitReceiver();
        this.registerReceiver(mExitReceiver, filter);
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    protected void showLoadProgressDilog(final int messageResId, final boolean indeterminate, final boolean cancelable) {
        mBaseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsLoadingDialogShowing) {
                    mLoadingProgressDialog = ProgressDialog.show(BaseActivity.this, "", getString(messageResId), indeterminate, cancelable);
                    mIsLoadingDialogShowing = true;
                }
            }
        });
    }

    protected void dismissLoadProgressDilog() {
        mBaseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingProgressDialog != null || mIsLoadingDialogShowing) {
                    mLoadingProgressDialog.dismiss();
                    mIsLoadingDialogShowing = false;
                }
            }
        });
    }

    protected void dismissLoadProgressDilogDelay(long time) {
        mBaseHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mIsLoadingDialogShowing) {
                    dismissLoadProgressDilog();
                }
            }
        }, time);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void EventBusGetMessage(final String info) {

    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        try {
            mLoadingProgressDialog.dismiss();
        } catch (Exception e) {
        }
        super.onDestroy();
        unregisterReceiver(mExitReceiver);
       EventBus.getDefault().unregister(this);
        if (mdialog != null) {
            mdialog.cancel();
        }

    }


}
