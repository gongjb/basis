package com.yft.zbase.bean;

import java.io.Serializable;

public class WeChatPayParams implements Serializable {
    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String nonceStr;
    private String timeStamp;
    private String sign;

    // Getters and Setters
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    // Constructor
    public WeChatPayParams(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign) {
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.packageValue = packageValue;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.sign = sign;
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "WeChatPayParams{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
