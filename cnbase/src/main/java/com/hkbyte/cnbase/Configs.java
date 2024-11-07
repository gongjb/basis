package com.hkbyte.cnbase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 系统配置
 */
public class Configs implements Parcelable {
    @SerializedName("style")
    private int style; // 样式

    @SerializedName("cover")
    private boolean isCover; // 是否启动遮罩层

    public Configs(int style) {
        this.style = style;
    }


    protected Configs(Parcel in) {
        style = in.readInt();
        isCover = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(style);
        dest.writeByte((byte) (isCover ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Configs> CREATOR = new Creator<Configs>() {
        @Override
        public Configs createFromParcel(Parcel in) {
            return new Configs(in);
        }

        @Override
        public Configs[] newArray(int size) {
            return new Configs[size];
        }
    };

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }
}
