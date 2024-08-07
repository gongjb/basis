package com.yft.zbase;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.xnet.XNetImpl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

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
        XNetImpl.getInstance().initHttp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
