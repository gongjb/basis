package com.yft.zbase;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.hk.xnet.XNetImpl;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.server.DynamicMarketManage;

public class ZBaseApplication extends Application {

    private static Application application;

    private static boolean isInit;

    public static Application get() {
        return application;
    }

    public static boolean isInit() {
        return isInit;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (!isInit) {
            init();
        }
    }

    /**
     * 交给子类去初始化
     */
    protected void init() {
        MMKV.initialize(this);
        // 初始化应用管理类
        DynamicMarketManage.getInstance().init(this);
        isInit = true;
    }

    protected void init(Context base) {
        MMKV.initialize(base);
        // 初始化应用管理类
        DynamicMarketManage.getInstance().init(base);
        isInit = true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
