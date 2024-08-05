package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yft.zbase.utils.Utils;

/**
 * 用户信息
 */
public class UserInfo implements Parcelable {

    /**
     * {"amount":0.0,"amountComing":0.0,"exchangeAmount":0.0,"exchangeAmountComing":0.0,"integral":0.0,"integralComing":0.0,"logo":"http://119.91.137.201/jwr_logo.png","nickname":"用户9005","phone":"18681079005","real":false,"recommendCode":"42712921"}
     */

    /**
     * amount : 0.0
     * amountComing : 0.0
     * exchangeAmount : 0.0
     * exchangeAmountComing : 0.0
     * integral : 0.0
     * integralComing : 0.0
     * logo : http://119.91.137.201/jwr_logo.png
     * nickname : 用户9005
     * phone : 18681079005
     * real : false
     * recommendCode : 42712921
     */

    @SerializedName("amount")
    private double amount;

    @SerializedName("amountComing")
    private double amountComing;

    @SerializedName("exchangeAmount")
    private double exchangeAmount;

    @SerializedName("exchangeAmountComing")
    private double exchangeAmountComing;

    @SerializedName("integral")
    private String integral;

    @SerializedName("integralComing")
    private double integralComing;

    @SerializedName("logo")
    private String logo;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("phone")
    private String phone;

    @SerializedName("real")
    private boolean real;

    @SerializedName("recommendCode")
    private String recommendCode;

    @SerializedName("serviceLinkUrl")
    private String serviceLinkUrl; // 客服

    @SerializedName("showAmountModel")
    private boolean showAmountModel;

    @SerializedName("consumeIntegral")
    private String consumeIntegral;
    @SerializedName("token")
    private String token;
    @SerializedName("rechargeCount")
    private int rechargeCount;
    @SerializedName("level")
    private int level;
    @SerializedName("hasPassword")
    private boolean hasPassword;

    @SerializedName("email")
    private String email;

    protected UserInfo(Parcel in) {
        amount = in.readDouble();
        amountComing = in.readDouble();
        exchangeAmount = in.readDouble();
        exchangeAmountComing = in.readDouble();
        integral = in.readString();
        integralComing = in.readDouble();
        logo = in.readString();
        nickname = in.readString();
        phone = in.readString();
        real = in.readByte() != 0;
        recommendCode = in.readString();
        serviceLinkUrl = in.readString();
        showAmountModel = in.readByte() != 0;
        consumeIntegral = in.readString();
        token = in.readString();
        rechargeCount = in.readInt();
        level = in.readInt();
        hasPassword = in.readByte() != 0;
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeDouble(amountComing);
        dest.writeDouble(exchangeAmount);
        dest.writeDouble(exchangeAmountComing);
        dest.writeString(integral);
        dest.writeDouble(integralComing);
        dest.writeString(logo);
        dest.writeString(nickname);
        dest.writeString(phone);
        dest.writeByte((byte) (real ? 1 : 0));
        dest.writeString(recommendCode);
        dest.writeString(serviceLinkUrl);
        dest.writeByte((byte) (showAmountModel ? 1 : 0));
        dest.writeString(consumeIntegral);
        dest.writeString(token);
        dest.writeInt(rechargeCount);
        dest.writeInt(level);
        dest.writeByte((byte) (hasPassword ? 1 : 0));
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getAmount() {
        return Utils.format(amount);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountComing() {
        return Utils.format(amountComing);
    }

    public void setAmountComing(double amountComing) {
        this.amountComing = amountComing;
    }

    public double getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(double exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    public double getExchangeAmountComing() {
        return exchangeAmountComing;
    }

    public void setExchangeAmountComing(double exchangeAmountComing) {
        this.exchangeAmountComing = exchangeAmountComing;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public double getIntegralComing() {
        return integralComing;
    }

    public void setIntegralComing(double integralComing) {
        this.integralComing = integralComing;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isReal() {
        return real;
    }

    public void setReal(boolean real) {
        this.real = real;
    }

    public String getRecommendCode() {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceLinkUrl() {
        return serviceLinkUrl;
    }

    public void setServiceLinkUrl(String serviceLinkUrl) {
        this.serviceLinkUrl = serviceLinkUrl;
    }

    public boolean isShowAmountModel() {
        return showAmountModel;
    }

    public void setShowAmountModel(boolean showAmountModel) {
        this.showAmountModel = showAmountModel;
    }

    public String getConsumeIntegral() {
        return consumeIntegral;
    }

    public void setConsumeIntegral(String consumeIntegral) {
        this.consumeIntegral = consumeIntegral;
    }

    public int getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfo() {}


}
