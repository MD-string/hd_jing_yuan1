package com.hand.handlibray.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * Created by WCF on 2016/8/2 0002.
 */
public class ToastUtil {

    public static Boolean isShow = true;//是否需要Toast，可以在Application中的onCreate()方法中初始化
    /**
     * 短吐司
     *
     * @param context
     * @param msg
     */
    public static void showMsgShort(Context context, String msg) {
        if(isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长吐司
     *
     * @param context
     * @param msg
     */
    public static void showMsgLong(Context context, String msg) {
        if(isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
