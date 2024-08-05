package com.yft.zbase.utils;

import com.yft.zbase.BuildConfig;

public class UtilsPaths {
    /**
     * https://api.jwruihe.com
     * https://api2.jwruihe.com
     * https://api3.jwruihe.com
     */
    private final static String[] paths = new String[]{
            /**
             https://api.rs99.top
             https://api2.rs99.top
             https://api3.rs99.top
            */
            "https://api.rs99.top/proxy",
            "https://api2.rs99.top/proxy",
            "https://api.rs356.top/proxy"
    };

    private final static String[] deBugPaths = new String[]{
//            "http://43.128.31.206/proxy",
//            "http://43.128.31.206/proxy",
//            "http://43.128.31.206/proxy"

            "https://api.rs99.top/proxy",
            "https://api2.rs99.top/proxy",
            "https://api.rs356.top/proxy"
    };

    // 预埋地址
    public static String getBaseUrl(int position) {
        if (position < 0) {
            return "";
        }

        if (position > 2) {
            return "";
        }

        if (BuildConfig.DEBUG) {
           return deBugPaths[position];
        }

        return paths[position];
    }
}
