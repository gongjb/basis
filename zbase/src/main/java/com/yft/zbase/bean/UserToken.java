package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserToken implements Parcelable {

    /**
     * {"code":0,"data":{"token":"t_8f3c152cd71445c4a20ff7d67cf56e36","user":{"amount":0.0,"amountComing":0.0,"exchangeAmount":0.0,"exchangeAmountComing":0.0,"integral":0.0,"integralComing":0.0,"logo":"http://119.91.137.201/jwr_logo.png","nickname":"用户9005","phone":"18681079005","real":false,"recommendCode":"42712921"}},"msg":"success"}
     */
    @SerializedName("user")
    private UserInfo user;

    @SerializedName("token")
    private String token;

    protected UserToken(Parcel in) {
        user = in.readParcelable(UserInfo.class.getClassLoader());
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserToken> CREATOR = new Creator<UserToken>() {
        @Override
        public UserToken createFromParcel(Parcel in) {
            return new UserToken(in);
        }

        @Override
        public UserToken[] newArray(int size) {
            return new UserToken[size];
        }
    };

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserToken() {}

}
