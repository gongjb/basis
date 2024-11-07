package com.hkbyte.ad.adstart;

/**
 * 闪屏广告工厂...
 */
public class AdFactory {
    private static AdFactory mInstance;

    private AdFactory() {};

    public static synchronized AdFactory getInstance() {
        if (mInstance == null) {
            synchronized (AdFactory.class) {
                if (mInstance == null) {
                    mInstance = new AdFactory();
                }
            }
        }
        return mInstance;
    }


    public <T extends ISplashAdAdapter> T createSplashAd(AdType adType) {
        switch (adType) {
            case CSJ_SPLASH:
                // 穿山甲
                return AdSplashStart.getInstance();
            default:
                return null;
        }
    }


    public <T extends IRewardAdAdapter> T createRewardAd(AdType adType) {
        switch (adType) {
            case CSJ_REWARD:
                // 穿山甲
                return AdRewardStart.getInstance();
            default:
                return null;
        }
    }

}
