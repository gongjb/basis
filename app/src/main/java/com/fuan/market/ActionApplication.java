package com.fuan.market;

import com.fuan.market.router.AppRouter;
import com.hk.xnet.XNetImpl;
import com.yft.home.router.HomeRouter;
import com.yft.user.router.UserRouter;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.router.IRouter;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.router.ZbaseRouter;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IDevice;
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
        List<IRouter> routers = RouterFactory.getInstance().factoryRouterPages(new UserRouter(), new AppRouter(),
                new HomeRouter(), new ZbaseRouter());

        ConcurrentMap<String, String> pagesMap = new ConcurrentHashMap<>();
        for (IRouter router : routers) {
            // 加载页面路径
            pagesMap.putAll(router.initPages());
        }

        // 初始化所有页面
        RouterFactory.getInstance().initPages(pagesMap);

        // 注入当前调试模式、项目别名、版本号、渠道号
        IDevice iDevice = DynamicMarketManage.getInstance().getServer(IServerAgent.DEVICE_SERVER);
        iDevice.saveDebug(BuildConfig.DEBUG);
        iDevice.saveAppAlias("hk"); // 项目别名
        iDevice.saveVersion(BuildConfig.VERSION_NAME);
        iDevice.saveFlavor(BuildConfig.CNAME);

        // 初始化网络请求
        XNetImpl.getInstance().initHttp(this, iDevice.isDebug());
    }
}
