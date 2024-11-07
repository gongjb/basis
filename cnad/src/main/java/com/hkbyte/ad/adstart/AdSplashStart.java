package com.hkbyte.ad.adstart;

import static com.yft.zbase.utils.Logger.LOGE;

import android.content.Context;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;

import com.qq.e.comm.pi.POFactory;
import com.yft.zbase.utils.Utils;


/**
 * 获取开屏广告
 */
public class AdSplashStart implements ISplashAdAdapter {
    private static ISplashAdAdapter mInstance;

    private static String TAG = "OPEN AD";

    private volatile boolean mAdOpen;

    private CSJSplashAd mCsjSplashAd;

    private AdSplashStart() {};

    public static synchronized <T extends IAdAdapter> T getInstance() {
        if (mInstance == null) {
            synchronized (AdSplashStart.class) {
                if (mInstance == null) {
                    mInstance = new AdSplashStart();
                }
            }
        }
        return (T) mInstance;
    }

    public boolean startSplashAd(final Context context, final String adId, final IOpenViewListener openViewListener) {
        if (mAdOpen) {
            LOGE(TAG, "Ads are getting, please later!");
            return false;
        }

        if (openViewListener == null) {
            LOGE(TAG, "openViewListener is null");
            return false;
        }

        if (TextUtils.isEmpty(adId)) {
            LOGE(TAG, "adId is null");
            return false;
        }

        if (context == null)  {
            LOGE(TAG, "adId is null");
            return false;
        }

        if (TTAdSdk.isSdkReady()) {
            LOGE(TAG, "Please initialize the ad!");
            return false;
        }


        mAdOpen = true;
        TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(context);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(adId)
                .setImageAcceptedSize((int) Utils.getScreenWidth(context), (int) Utils.getScreenHeight(context))//单位px
                .build();

        adNativeLoader.loadSplashAd(adSlot, new TTAdNative.CSJSplashAdListener() {
            @Override
            public void onSplashRenderSuccess(CSJSplashAd csjSplashAd) {
                /** 渲染成功后，展示广告 */
                mAdOpen = false;
                mCsjSplashAd = csjSplashAd;
                LOGE(TAG , "open ad is success");
                csjSplashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
                    @Override
                    public void onSplashAdShow(CSJSplashAd csjSplashAd) {
                        openViewListener.onSplashAdShow(csjSplashAd.getSplashView());
                    }

                    @Override
                    public void onSplashAdClick(CSJSplashAd csjSplashAd) {
                        openViewListener.onSplashAdClick(csjSplashAd.getSplashClickEyeView());
                    }

                    @Override
                    public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                        openViewListener.onSplashAdClose(i);
                    }
                });
                openViewListener.openAdView(csjSplashAd.getSplashView());
            }

            @Override
            public void onSplashLoadSuccess(CSJSplashAd csjSplashAd) {
                mAdOpen = false;
                mCsjSplashAd = csjSplashAd;
                openViewListener.onSplashLoadSuccess(csjSplashAd.getSplashView());
            }

            @Override
            public void onSplashLoadFail(CSJAdError csjAdError) {
                /**
                 * 仅CSJ
                 csjAdError.getCode() == 1，物料加载失败
                 csjAdError.getCode() == 2，素材加载失败
                 csjAdError.getCode() == 3，渲染失败、渲染超时
                 csjAdError.getCode() == 23，加载超时
                 */
                LOGE(TAG , "onSplashLoadFail:" + csjAdError.getCode() + "==" + csjAdError.getMsg());
                mAdOpen = false;
                openViewListener.onSplashLoadFail(csjAdError.getMsg(), csjAdError.getCode());
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError csjAdError) {
                LOGE(TAG , "onSplashRenderFail");
                mAdOpen = false;
                openViewListener.onSplashRenderFail(csjSplashAd.getSplashView() , csjAdError.getMsg(), csjAdError.getCode());
            }
        }, 3500);
        return true;
    }

    public void onClean() {
        if (mCsjSplashAd != null && mCsjSplashAd.getMediationManager() != null) {
            LOGE(TAG , "ad splash onDestroy");
            mCsjSplashAd.getMediationManager().destroy();
            mCsjSplashAd = null; // 销毁
        }
    }
}
