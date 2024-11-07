package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgressBean implements Parcelable {
    private String packageName; // 包名
    private int progress; // 进度
    private String filePath;
    private String apkName;

    public ProgressBean(String packageName, int progress, String filePath, String apkName) {
        this.packageName = packageName;
        this.progress = progress;
        this.filePath = filePath;
        this.apkName = apkName;
    }


    protected ProgressBean(Parcel in) {
        packageName = in.readString();
        progress = in.readInt();
        filePath = in.readString();
        apkName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeInt(progress);
        dest.writeString(filePath);
        dest.writeString(apkName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProgressBean> CREATOR = new Creator<ProgressBean>() {
        @Override
        public ProgressBean createFromParcel(Parcel in) {
            return new ProgressBean(in);
        }

        @Override
        public ProgressBean[] newArray(int size) {
            return new ProgressBean[size];
        }
    };

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }
}
