package com.yft.zbase.router;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZbaseRouter implements IRouter {
    // webview 页面
    public final static String ACTIVITY_WEB = "com/yft/zbase/ui/WebYftActivity";
    // 扫描文本展示页面
    public final static String  ACTIVITY_TEXT = "com/yft/zbase/ui/TextActivity";

    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> routerMap = new ConcurrentHashMap<>();
        routerMap.put("WebYftActivity", ACTIVITY_WEB);
        routerMap.put("TextActivity", ACTIVITY_TEXT);
        return routerMap;
    }
}
