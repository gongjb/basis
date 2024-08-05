package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class HomeBean implements Parcelable {

    private int type;

    protected HomeBean(Parcel in) {
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeBean> CREATOR = new Creator<HomeBean>() {
        @Override
        public HomeBean createFromParcel(Parcel in) {
            return new HomeBean(in);
        }

        @Override
        public HomeBean[] newArray(int size) {
            return new HomeBean[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HomeBean() {
    }
}
