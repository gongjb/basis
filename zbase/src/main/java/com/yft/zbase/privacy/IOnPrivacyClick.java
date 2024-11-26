package com.yft.zbase.privacy;

public interface IOnPrivacyClick {
    int not = 0; // 不同意
    int consent = 1; // 同意

    void onPrivacyClick(int type);
}
