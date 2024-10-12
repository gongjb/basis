package com.yft.zbase.utils;

import android.util.Log;

import com.yft.zbase.BuildConfig;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IDevice;
import com.yft.zbase.server.IServerAgent;

public class Logger {
    public static final String TAG = "Logger";
    public volatile static IDevice iDevice;

    public static void LOGE(String val) {
        if (iDevice == null) {
            iDevice = DynamicMarketManage.getInstance().getServer(IServerAgent.DEVICE_SERVER);
        }

        if (!iDevice.isDebug()) return;
        Log.e(TAG, "LOGE: " + val);
    }


    public static void LOGE(String tag, String val) {
        if (iDevice == null) {
            iDevice = DynamicMarketManage.getInstance().getServer(IServerAgent.DEVICE_SERVER);
        }
        if (!iDevice.isDebug()) return;
        Log.e(tag, "LOGE: " + val);
    }

    public static void LOGD(String val) {
        if (iDevice == null) {
            iDevice = DynamicMarketManage.getInstance().getServer(IServerAgent.DEVICE_SERVER);
        }
        if (!iDevice.isDebug()) return;
        Log.d(TAG, "LOGE: " + val);
    }
}
