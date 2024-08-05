package com.yft.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MeItemBean implements Parcelable {

    public MeItemBean(int id, String title, String stateStr, boolean isImage, String imageUrl, boolean isLine, int stateImage) {
        this.id = id;
        this.title = title;
        this.stateStr = stateStr;
        this.isImage = isImage;
        this.imageUrl = imageUrl;
        this.isLine = isLine;
        this.stateImage = stateImage;
    }

    public MeItemBean(boolean isLine) {
        this.isLine = isLine;
    }

    private int id = -1;

    private String title;

    private String stateStr;
    // 是图片吗(右边显示图片)
    private boolean isImage;
    // 图片地址
    private String imageUrl;

    private boolean isLine;
    private int stateImage;

    protected MeItemBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        stateStr = in.readString();
        isImage = in.readByte() != 0;
        imageUrl = in.readString();
        isLine = in.readByte() != 0;
        stateImage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(stateStr);
        dest.writeByte((byte) (isImage ? 1 : 0));
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isLine ? 1 : 0));
        dest.writeInt(stateImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeItemBean> CREATOR = new Creator<MeItemBean>() {
        @Override
        public MeItemBean createFromParcel(Parcel in) {
            return new MeItemBean(in);
        }

        @Override
        public MeItemBean[] newArray(int size) {
            return new MeItemBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isLine() {
        return isLine;
    }

    public void setLine(boolean line) {
        isLine = line;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStateImage() {
        return stateImage;
    }

    public void setStateImage(int stateImage) {
        this.stateImage = stateImage;
    }
}
