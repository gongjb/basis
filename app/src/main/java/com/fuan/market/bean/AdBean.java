package com.fuan.market.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yft.zbase.bean.KVBean;

public class AdBean extends KVBean implements Parcelable {

    public AdBean() {}

    public AdBean(String imageUrl, String toUrl) {
        this.imageUrl = imageUrl;
        this.toUrl = toUrl;
    }

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("toUrl")
    private String toUrl;

    protected AdBean(Parcel in) {
        imageUrl = in.readString();
        toUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(toUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel in) {
            return new AdBean(in);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }

}
