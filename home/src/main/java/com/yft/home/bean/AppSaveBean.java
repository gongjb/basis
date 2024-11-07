package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AppSaveBean implements Parcelable {
    private String packageName;

    public AppSaveBean(String packageName) {
        this.packageName = packageName;
    }

    protected AppSaveBean(Parcel in) {
        packageName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppSaveBean> CREATOR = new Creator<AppSaveBean>() {
        @Override
        public AppSaveBean createFromParcel(Parcel in) {
            return new AppSaveBean(in);
        }

        @Override
        public AppSaveBean[] newArray(int size) {
            return new AppSaveBean[size];
        }
    };

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
