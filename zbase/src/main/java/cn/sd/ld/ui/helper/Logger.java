package cn.sd.ld.ui.helper;

import android.util.Log;

import com.yft.zbase.BuildConfig;

public class Logger {
    public static final String TAG = "Logger";

    public static final boolean isDeBug = BuildConfig.DEBUG;

    public static void LOGE(String val) {
        if (!isDeBug) return;
        Log.e(TAG, "LOGE: " + val);
    }


    public static void LOGE(String tag, String val) {
        if (!isDeBug) return;
        Log.e(tag, "LOGE: " + val);
    }

    public static void LOGD(String val) {
        if (!isDeBug) return;
        Log.d(TAG, "LOGE: " + val);
    }
}
