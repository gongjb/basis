package com.fuan.chameleon;

import com.fuan.chameleon.router.AppRouter;
import com.yft.home.router.HomeRouter;
import com.yft.user.router.UserRouter;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.router.IRouter;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.router.ZbaseRouter;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于初始化模块之间路由配置等...
 */
public class ActionApplication extends ZBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化各个模块的页面路径 （新增模块需在此配置）
        List<IRouter> routers = RouterFactory.factoryRouterPages(new UserRouter(), new AppRouter(),
                new HomeRouter(), new ZbaseRouter());

        ConcurrentMap<String, String> pagesMap = new ConcurrentHashMap<>();
        for (IRouter router : routers) {
            // 加载页面路径
            pagesMap.putAll(router.initPages());
        }

        // 初始化所有页面
        RouterFactory.initPages(pagesMap);

        // 注入渠道号, 为了解决渠道分发问题...
        IUser iUser = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
        iUser.saveFlavor(BuildConfig.CNAME);
    }
}
