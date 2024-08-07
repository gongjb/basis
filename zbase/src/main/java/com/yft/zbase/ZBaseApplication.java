package com.yft.zbase;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.hk.xnet.XNetImpl;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.server.DynamicMarketManage;

public class ZBaseApplication extends Application {

    private static Application application;

    public static Application get() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MMKV.initialize(this);
        //OkGo.getInstance().init(this);
        // 初始化应用管理类
        DynamicMarketManage.getInstance().init(this);
       // CrashReport.initCrashReport(getApplicationContext(), "13f61ad858", false);
        XNetImpl.getInstance().initHttp(this, BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
