package com.hkbyte.filelibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 复制文件的进度
 */
public class CopyProgressBean implements Parcelable {
    @SerializedName("count")
    private int count; // 总文件数
    @SerializedName("rightNow")
    private int rightNow; // 当前已经完成的数量

    public CopyProgressBean() {

    }

    protected CopyProgressBean(Parcel in) {
        count = in.readInt();
        rightNow = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(rightNow);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CopyProgressBean> CREATOR = new Creator<CopyProgressBean>() {
        @Override
        public CopyProgressBean createFromParcel(Parcel in) {
            return new CopyProgressBean(in);
        }

        @Override
        public CopyProgressBean[] newArray(int size) {
            return new CopyProgressBean[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRightNow() {
        return rightNow;
    }

    public void setRightNow(int rightNow) {
        this.rightNow = rightNow;
    }
}
