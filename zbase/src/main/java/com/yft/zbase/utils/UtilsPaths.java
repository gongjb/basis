package com.yft.zbase.utils;

import com.yft.zbase.BuildConfig;

public class UtilsPaths {
    private final static String[] paths = new String[]{
            "https://api.jwruihe.com/proxy",
            "https://api2.jwruihe.com/proxy",
            "https://api3.jwruihe.com/proxy"
    };

    private final static String[] deBugPaths = new String[]{
            "https://api.jwruihe.com/proxy",
            "https://api2.jwruihe.com/proxy",
            "https://api3.jwruihe.com/proxy"
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
