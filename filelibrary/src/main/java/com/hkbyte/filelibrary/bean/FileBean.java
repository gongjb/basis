package com.hkbyte.filelibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * 文件管理bean
 */
public class FileBean implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("imageUrl")
    private String imageUrl; // 本地路径图片
    @SerializedName("fileUrl")
    private String fileUrl; //文件路径 (原来的路径)
    @SerializedName("imageIntId")
    private int imageIntId; // 资源id
    /**
     * 大类型 {@link com.hkbyte.filelibrary.viewmodel.FileViewModel#FILE_VIDEO}
     */
    @SerializedName("type")
    private int type; // 大的类型
    /**
     * 文件路径 (目标的路径)
     */
    @SerializedName("originallyPath")
    private String originallyPath;

    /**
     * 当前文件类型 取值{@link com.luck.picture.lib.config.PictureMimeType#MIME_TYPE_MP4}
     */
    @SerializedName("mimeType")
    private String mimeType;

    @SerializedName("progress")
    private int progress = 100; // 默认是加载完成的状态

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("isCheck")
    private boolean isCheck; // 临时变量

    @SerializedName("isRecover")
    private boolean isRecover;

    public FileBean() {}

    protected FileBean(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        fileUrl = in.readString();
        imageIntId = in.readInt();
        type = in.readInt();
        originallyPath = in.readString();
        mimeType = in.readString();
        progress = in.readInt();
        currentPage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(fileUrl);
        dest.writeInt(imageIntId);
        dest.writeInt(type);
        dest.writeString(originallyPath);
        dest.writeString(mimeType);
        dest.writeInt(progress);
        dest.writeInt(currentPage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

    public String getOriginallyPath() {
        return originallyPath;
    }

    public void setOriginallyPath(String originallyPath) {
        this.originallyPath = originallyPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImageIntId() {
        return imageIntId;
    }

    public void setImageIntId(int imageIntId) {
        this.imageIntId = imageIntId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isRecover() {
        return isRecover;
    }

    public void setRecover(boolean recover) {
        isRecover = recover;
    }

    public FileBean clone() {
        FileBean cloned = new FileBean();
        cloned.name = this.name;
        cloned.type = this.type;
        cloned.fileUrl = this.fileUrl;
        cloned.currentPage = this.currentPage;
        cloned.imageIntId = this.imageIntId;
        cloned.originallyPath = this.getOriginallyPath();
        cloned.progress = this.progress;
        cloned.mimeType = this.mimeType;
        cloned.imageUrl = this.imageUrl;
        cloned.isCheck = this.isCheck;
        cloned.isRecover = this.isRecover;
        return cloned;
    }
}
