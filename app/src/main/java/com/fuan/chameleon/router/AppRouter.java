package com.fuan.chameleon.router;

import com.yft.zbase.router.IRouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AppRouter implements IRouter {

    // 欢迎页面
    public final static String ACTIVITY_WELCOME = "cn/fuan/market/WelcomeActivity";
    // 引导页面
    public final static String ACTIVITY_GUIDE = "cn/fuan/market/GuideActivity";
    // 主页面
    public final static String ACTIVITY_MAIN = "cn/fuan/market/MainActivity";
    // deeplink 应用内不使用deeplink 拉起

    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> routerMap = new ConcurrentHashMap<>();
        routerMap.put("WelcomeActivity", ACTIVITY_WELCOME);
        routerMap.put("GuideActivity", ACTIVITY_GUIDE);
        routerMap.put("MainActivity", ACTIVITY_MAIN);
        return routerMap;
    }
}
