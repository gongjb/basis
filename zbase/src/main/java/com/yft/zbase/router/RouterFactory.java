package com.yft.zbase.router;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.Router;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RouterFactory {
    public static final String ROUTER_TEST = "";

    private static RouterFactory instance;

    private final ConcurrentMap<String, String> routerPages = new ConcurrentHashMap<>();

    public synchronized  void initPages(ConcurrentMap<String, String> concurrentMap) {
        routerPages.putAll(concurrentMap);
    }

    private RouterFactory() {}

    public static synchronized RouterFactory getInstance() {
        if (instance == null) {
            synchronized (RouterFactory.class) {
                if (instance == null) {
                    instance = new RouterFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 获取页面路径
     * @param pageKey
     * @return
     */
    public synchronized String getPage(String pageKey) {
        return routerPages.get(pageKey);
    }

    /**
     * 创建页面路由集合
     * @param routers
     * @return
     */
    public synchronized List<IRouter> factoryRouterPages(IRouter...routers) {
        if (routers == null) {
            throw new RuntimeException("routers is null");
        }

       return Arrays.asList(routers);
    }


    // app中全局只能设置一个地方集中跳转
    private static IRouterJumpPages iRouterJumpPages;

    public synchronized void setRouterJumpPages(IRouterJumpPages iRouterJumpPages) {
        if (RouterFactory.iRouterJumpPages == null) {
            RouterFactory.iRouterJumpPages = iRouterJumpPages;
        }
    }

    public synchronized void addToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (RouterFactory.iRouterJumpPages != null) {
            RouterFactory.iRouterJumpPages.addToHomePageListener(iToHomePageListener);
        } else {
            throw new RuntimeException("iRouterJumpPages is null addToHomePageListener");
        }
    }

    public synchronized void removeToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (RouterFactory.iRouterJumpPages != null) {
            RouterFactory.iRouterJumpPages.removeToHomePageListener(iToHomePageListener);
        } else {
            throw new RuntimeException("iRouterJumpPages is null removeToHomePageListener");
        }
    }

    public synchronized void startRouterRequestActivity(Context context, String path, int requestCode, RouteCallback routerCallback) {
        Router.build(path)
                .callback(routerCallback)
                .requestCode(requestCode)
                .go(context);
    }

    public synchronized void startRouterRequestActivity(Context context, String path, int requestCode,
                                                  Bundle bundle, RouteCallback routerCallback) {
        Router.build(path)
                .with(bundle)
                .requestCode(requestCode)
                .go(context, routerCallback);
    }

    public synchronized void startRouterBundleActivity(Context context, String path, Bundle bundle) {
        Router.build(path)
                .with(bundle)
                .go(context);
    }

    public synchronized void startRouterRequestActivity(Fragment fragment, String path, int requestCode,
                                                  RouteCallback routerCallback) {
        Router.build(path)
                .callback(routerCallback)
                .requestCode(requestCode)
                .go(fragment);
    }

    public synchronized void startRouterRequestActivity(Fragment fragment, String path, int requestCode, Bundle bundle) {
        Router.build(path)
                .requestCode(requestCode)
                .with(bundle)
                .go(fragment);
    }

    public synchronized void startRouterActivity(Context context, String path) {
        Router.build(path).go(context);
    }

    /**
     * 获取Fragment并携带参数
     *
     * @param context
     * @param path
     * @return
     */
    public synchronized Fragment getFragment(Context context, String path, Bundle bundle) {
        return Router.build(path).with(bundle).getFragment(context);
    }

    /**
     * 获取Fragment
     *
     * @param context
     * @param path
     * @return
     */
    public synchronized Fragment getFragment(Context context, String path ) {
        return Router.build(path).getFragment(context);
    }

    public synchronized boolean jumpToActivity(Context context, TargetBean homeListBean) {
        if (RouterFactory.iRouterJumpPages != null) {
            // 将逻辑全部抛出去
            return RouterFactory.iRouterJumpPages.jumpToActivity(context, homeListBean);
        } else {
            throw new RuntimeException("iRouterJumpPages is null");
        }
    }

    public interface IRouterJumpPages {
        /*
         提供跳转整个app内部的页面跳转实现
         */
        boolean jumpToActivity(Context context, TargetBean homeListBean);

        /*
          首页或者其它页面有tab页面的， 添加IToHomePageListener接口实现跳转..
         */
        void addToHomePageListener(IToHomePageListener iToHomePageListener);

        /*
         在页面关闭时记得移除IToHomePageListener接口
         */
        void removeToHomePageListener(IToHomePageListener iToHomePageListener);
    }

    public static void exApp() {
//        Router.c();
        //Router.
    }
}
