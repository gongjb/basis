package com.hkbyte.ad.adstart;

import android.content.Context;
import android.view.View;

/**
 * 广告桥接器， 可以集成多个
 */
public interface ISplashAdAdapter extends IAdAdapter {

    boolean startSplashAd(final Context context, final String adId, final IOpenViewListener openViewListener);

    interface IOpenViewListener {
        /*
            渲染成功
        */
        void openAdView(View view);

        /**
         * 广告显示
         * @param view
         */
        default void onSplashAdShow(View view) {}

        /**
         * 广告被点击
         * @param view
         */
        default void onSplashAdClick(View view) {}

        /**
         * 广告被关闭
         * @param code
         */
        default void onSplashAdClose(int code) {}

        /**
         * 广告加载失败
         */
        default void onSplashLoadFail(String msg, int code) {}
        /**
         * 广告加载成功
         */
        default void  onSplashLoadSuccess(View view) {}

        /**
         * 渲染失败
         * @param view
         * @param msg
         * @param code
         */
        default void onSplashRenderFail(View view, String msg, int code) {}
    }
}
