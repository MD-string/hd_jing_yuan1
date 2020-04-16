package com.hand.handlibray.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by xiajun on 2017/4/4 0004 下午 12:56
 * Describe: TODO 服务工具类
 * Company: Shen Zhen Jiu Li Supply chain(深圳九立供应链)
 */

public class ServiceUtil {

    /**
     * 用来判断服务是否运行.
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
