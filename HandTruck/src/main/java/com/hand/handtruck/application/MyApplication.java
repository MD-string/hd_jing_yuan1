package com.hand.handtruck.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;

import com.hand.handtruck.utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.Bugly;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyApplication extends Application {
    private List<FragmentActivity> activityList = new ArrayList<FragmentActivity>();
    private static MyApplication instance;
    public static Context context;
    public static int environment =1;//项目当前所用的服务器(0代表测试环境，1代表正式环境)
    public static boolean isUser =true;//是用户直接跳转到车辆列表，添加车辆、修改车辆不在显示给客户
    public static final String APP_ID = "3112e7189a"; // TODO 替换成bugly上注册的appid
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApplication.context = this.getApplicationContext();
           /*关闭打开日志入口，正式环境时置为false，不打印*/
        LogUtil.isDebug=true;

        //配置OKHtt参数
        File cacheFile = new File(this.getApplicationContext().getExternalCacheDir().toString(), "handtruck_cache");
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
             // .cache(new Cache(cacheFile, 1024 * 1024 * 20))//设置缓存大小20M
              //.addNetworkInterceptor(new CacheInterceptor()) //调用在线缓存，当服务器不支持的情况下
              // .addInterceptor(intercepter) //离线缓存
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//配置https方式请求
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//设置读取数据超时时间
                .build();
        OkHttpUtils.initClient(okHttpClient);


        File loaderCacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), ".android/hand/imageLoader/Cache");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                //				.showImageForEmptyUri(R.drawable.defalt_picture)
                //				.showImageOnFail(R.drawable.defalt_picture)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
        @SuppressWarnings("deprecation")
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .writeDebugLogs()
                .discCache(new UnlimitedDiscCache(loaderCacheDir, null, new FileNameGenerator() {

                    @Override
                    public String generate(String imageUri) {
                        return String.valueOf(imageUri.hashCode() + ".jpg");
                    }
                }))
                .build();//
        ImageLoader.getInstance().init(config);

        //极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        doset();//bugly自动更新功能
        // 初始化MultiDex
        MultiDex.install(this);
    }

    private void doset() {
        /**
         * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
         * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
         * 参数1： applicationContext
         * 参数2：appId
         * 参数3：是否开启debug
         */

        Bugly.init(instance, APP_ID, false);
    }
    // 如果服未与服务端沟通好，务器不支持缓存，需要使用Interceptor来重写Respose的头部信息，从而让okhttp支持缓存。否则无法实现缓存
    //但是，缺点是一刀切,所有get请求方式第二次请求都取缓存数据，及时更新的无法得到更新
    /*如果服务器支持缓存，就不需要设置拦截，请求返回的Response会带有这样的Header:Cache-Control, max-age=xxx*/
    public class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Response response1 = response.newBuilder()
                    .removeHeader("Pragma")//移除它的原因是因为pragma也是控制缓存的一个消息头属性
                    .removeHeader("Cache-Control")
                    //0就是不缓存，由每个接口单独控制
                    .header("Cache-Control", "max-age=" + 3600)
                    .build();
            return response1;
        }
    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    public void addActivity(FragmentActivity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (FragmentActivity activity : activityList) {
            try {
                activity.finish();
            } catch (Exception e) {
                continue;
            }
        }
        System.exit(0);
    }
}
