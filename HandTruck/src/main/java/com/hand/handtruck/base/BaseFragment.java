package com.hand.handtruck.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hand.handtruck.application.MyApplication;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseFragment extends Fragment {
    public Handler mBaseHandler = new Handler();
    protected boolean mIsLoadingDialogShowing = false;
    protected ProgressDialog mLoadingProgressDialog;
    public Context mContext;

    /*fragment是否可见，可见时，调用加载网络数据的方法*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);  //注册
    }


    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(MyApplication.context).inflate(getLayoutId(), null);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        findViews(mContentView);
        initView();
        init();
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void initView() {
        mContext = getActivity();
    }

    /**
     * 获取布局资源ID
     *
     * @return ID
     */
    protected abstract int getLayoutId();

    /**
     * 绑定控件
     *
     * @param
     */
    protected abstract void findViews(View view);

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 设置监听
     */
    protected abstract void setListeners();

    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    protected abstract void onUserVisible();

    protected void showLoadProgressDilog(final int messageResId, final boolean indeterminate, final boolean cancelable) {
        mBaseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsLoadingDialogShowing && isAdded()) {
                    mLoadingProgressDialog = ProgressDialog.show(BaseFragment.this.getActivity(), "", getString(messageResId), indeterminate, cancelable);
                    mIsLoadingDialogShowing = true;
                }
            }
        });
    }

    protected void showLoadProgressDilog(final String messageResId, final boolean indeterminate, final boolean cancelable) {
        mBaseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsLoadingDialogShowing && isAdded()) {
                    mLoadingProgressDialog = ProgressDialog.show(BaseFragment.this.getActivity(), "", messageResId, indeterminate, cancelable);
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

    protected boolean mIsConnProgressDlgShow = false;
    protected ProgressDialog mConnProgressDialog;

    protected void dismissConnProgressDilog() {
        if (mConnProgressDialog != null || mIsConnProgressDlgShow) {
            mConnProgressDialog.dismiss();
            mIsConnProgressDlgShow = false;
        }
    }

    @Override
    public void onDestroy() {
        try {
            mLoadingProgressDialog.dismiss();
        } catch (Exception e) {
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}