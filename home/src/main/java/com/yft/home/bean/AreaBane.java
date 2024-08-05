package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AreaBane implements Parcelable {
    // 是否选中
    private boolean isSel;
    // 名字
    private String name;

    public AreaBane() {

    }

    protected AreaBane(Parcel in) {
        isSel = in.readByte() != 0;
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSel ? 1 : 0));
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AreaBane> CREATOR = new Creator<AreaBane>() {
        @Override
        public AreaBane createFromParcel(Parcel in) {
            return new AreaBane(in);
        }

        @Override
        public AreaBane[] newArray(int size) {
            return new AreaBane[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }
}
