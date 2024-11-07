package com.hkbyte.ad.adstart;

import android.app.Activity;

/**
 * 顶层接口.给广告中的
 */
public interface IAdAdapter {
    /**
     * 清除掉数据
     */
    void onClean();

    /**
     * 测试方法
     * @param activity
     */
    default void test(Activity activity) {};
}
