package com.hkbyte.ad.adstart;

/**
 * 广告类型枚举
 */
public enum AdType {
    CSJ_SPLASH("103179783", "CSJ_SPLASH"), // 穿山甲开屏广告
    CSJ_BANNER("", "CSJ_BANNER"), // 穿山甲BANNER
    CSJ_REWARD("", "CSJ_REWARD"), // 穿山甲激励视频
    CSJ_INSERT("", "CSJ_INSERT"); // 穿山甲插屏

    // 可以添加其它类型的广告比如快手的SDK等等，这么做的主要目的就是为接入多广告sdk做准备

    private String adId; // 配置一个广告位默认id.
    private String alias;

    AdType(String adId, String alias) {
        this.adId = adId;
        this.alias = alias;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
