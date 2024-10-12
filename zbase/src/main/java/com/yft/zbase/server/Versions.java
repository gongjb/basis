package com.yft.zbase.server;

import com.yft.zbase.BuildConfig;

public enum Versions {
    OFFICIAL_VERSION_NAME("reelShort", BuildConfig.versionCode), // 正式版
    SHARE_VERSION_NAME("shareShort", BuildConfig.versionCode); // 分享版
    private String name;
    private String code;
    Versions(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
