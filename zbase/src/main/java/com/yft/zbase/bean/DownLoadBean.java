package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 下载APK
 */
public class DownLoadBean implements Parcelable {

    /**
     * {"appType":"Android",
     * "createDate":"2023-12-17 15:10:43",
     * "description":"首发版本",
     * "mustUpdate":false,
     * "sequence":1,
     * "version":"1.0"}
     *
     *
     * appType : Android
     * createDate : 2022-05-09 12:08:50
     * description : 优化了链接速度
     * mustUpdate : true
     * sequence : 1
     * updateUrl : http://www.baidu.com
     * version : v1.0.3
     */

    @SerializedName("appType")
    private String appType;

    @SerializedName("createDate")
    private String createDate;

    @SerializedName("description")
    private String description;

    @SerializedName("mustUpdate")
    private boolean mustUpdate;

    @SerializedName("sequence")
    private int sequence;

    @SerializedName("updateUrl")
    private String updateUrl;

    @SerializedName("version")
    private String version;

    protected DownLoadBean(Parcel in) {
        appType = in.readString();
        createDate = in.readString();
        description = in.readString();
        mustUpdate = in.readByte() != 0;
        sequence = in.readInt();
        updateUrl = in.readString();
        version = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appType);
        dest.writeString(createDate);
        dest.writeString(description);
        dest.writeByte((byte) (mustUpdate ? 1 : 0));
        dest.writeInt(sequence);
        dest.writeString(updateUrl);
        dest.writeString(version);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DownLoadBean> CREATOR = new Creator<DownLoadBean>() {
        @Override
        public DownLoadBean createFromParcel(Parcel in) {
            return new DownLoadBean(in);
        }

        @Override
        public DownLoadBean[] newArray(int size) {
            return new DownLoadBean[size];
        }
    };

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
