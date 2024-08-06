package com.yft.home.router;

import com.yft.zbase.router.IRouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HomeRouter implements IRouter {
    // 首页
    public final static String FRAGMENT_HOME_VLAYOUT = "com/yft/home/HomeVLayoutFragment";
    // 搜索
    public final static String ACTIVITY_SEARCH = "com/yft/home/SearchActivity";

    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> routerMap = new ConcurrentHashMap<>();
        routerMap.put("HomeVLayoutFragment", FRAGMENT_HOME_VLAYOUT);
        routerMap.put("SearchActivity", ACTIVITY_SEARCH);
        return routerMap;
    }
}
