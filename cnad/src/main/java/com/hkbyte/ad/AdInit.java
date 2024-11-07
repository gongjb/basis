package com.hkbyte.ad;

import static com.yft.zbase.utils.Logger.LOGE;

import android.app.Application;
import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdSdk;

/**
 * 广告初始化
 */
public class AdInit {
    private static AdInit instance;

    private AdInit() {};

    public static synchronized AdInit getInstance() {
        if (instance == null) {
            synchronized (AdInit.class) {
                if (instance == null) {
                    instance = new AdInit();
                }
            }
        }
        return instance;
    }

    public void initAd(Application context,String appId) {
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        // TTAdsdk.init仅进行初始化,不会获取个人信息,如果要展示广告,需要再调用TTAdSdk.start方法
        TTAdSdk.init(context, buildConfig(context, appId));

        //setp1.2：启动SDK
        TTAdSdk.start(new TTAdSdk.Callback() {
            @Override
            public void success() {
                // Log.i(TAG, "success: " + TTAdSdk.isSdkReady());
                LOGE("TTAdSdk", "success=" + TTAdSdk.isSdkReady());
            }

            @Override
            public void fail(int code, String msg) {
                LOGE("TTAdSdk", "fail : code=" + code + ":msg=" + msg);
                //   Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
            }
        });
    }

    private static TTAdConfig buildConfig(Context context, String appId) {
        return new TTAdConfig.Builder()
                .appId(appId)//应用ID
                .useMediation(true)//开启聚合功能，默认false
                .supportMultiProcess(true)//开启多进程
                .build();
    }

    public void cleanAd() {
        instance = null;
    }
}
