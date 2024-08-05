package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class VersionBean implements Parcelable {

    /**
     * appType : Android
     * createDate : 2022-12-09 10:11:01
     * description : 1
     * mustUpdate : true
     * sequence : 1
     * updateUrl : http://111.230.203.250/yfa.apk
     * version : 1.0.1
     */
    private String appType;
    private String createDate;
    private String description;
    private boolean mustUpdate;
    private int sequence;
    private String updateUrl;
    private String version;

    protected VersionBean(Parcel in) {
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

    public static final Creator<VersionBean> CREATOR = new Creator<VersionBean>() {
        @Override
        public VersionBean createFromParcel(Parcel in) {
            return new VersionBean(in);
        }

        @Override
        public VersionBean[] newArray(int size) {
            return new VersionBean[size];
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

    @Override
    public String toString() {
        return "VersionBean{" +
                "appType='" + appType + '\'' +
                ", createDate='" + createDate + '\'' +
                ", description='" + description + '\'' +
                ", mustUpdate=" + mustUpdate +
                ", sequence=" + sequence +
                ", updateUrl='" + updateUrl + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
