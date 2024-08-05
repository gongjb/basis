package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TargetBean implements Parcelable, Serializable {


    /**
     * actionType : videoDetail
     * createDate : 1713271963000
     * id : 1780217758476816384
     * image : https://krs.oss-cn-hongkong.aliyuncs.com/5d4225195a5f441fa22acd7e465ad148.jpeg
     * name : test01
     * position : banner
     * publishStatus : online
     * sort : 1
     * target : 123123
     */

    @SerializedName("actionType")
    private String actionType;
    @SerializedName("createDate")
    private long createDate;
    @SerializedName("id")
    private String id;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("position")
    private String position;
    @SerializedName("publishStatus")
    private String publishStatus;
    @SerializedName("sort")
    private int sort;
    @SerializedName("target")
    private String target;
    @SerializedName("pmc")
    private String pmc;
    @SerializedName("episodes")
    private int episodes;
    @SerializedName("videoUrl")
    private String videoUrl;		// 视频地址
    @SerializedName("tvIntroduce")
    private String tvIntroduce;		// 剧介绍
    @SerializedName("tvIntroduceEn")
    private String tvIntroduceEn;		// 短剧英文介绍

    public TargetBean() {}

    protected TargetBean(Parcel in) {
        actionType = in.readString();
        createDate = in.readLong();
        id = in.readString();
        image = in.readString();
        name = in.readString();
        position = in.readString();
        publishStatus = in.readString();
        sort = in.readInt();
        target = in.readString();
        pmc = in.readString();
        episodes = in.readInt();
        videoUrl = in.readString();
        tvIntroduce = in.readString();
        tvIntroduceEn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionType);
        dest.writeLong(createDate);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(position);
        dest.writeString(publishStatus);
        dest.writeInt(sort);
        dest.writeString(target);
        dest.writeString(pmc);

        dest.writeInt(episodes);
        dest.writeString(videoUrl);
        dest.writeString(tvIntroduce);
        dest.writeString(tvIntroduceEn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TargetBean> CREATOR = new Creator<TargetBean>() {
        @Override
        public TargetBean createFromParcel(Parcel in) {
            return new TargetBean(in);
        }

        @Override
        public TargetBean[] newArray(int size) {
            return new TargetBean[size];
        }
    };

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPmc() {
        return pmc;
    }

    public void setPmc(String pmc) {
        this.pmc = pmc;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTvIntroduce() {
        return tvIntroduce;
    }

    public void setTvIntroduce(String tvIntroduce) {
        this.tvIntroduce = tvIntroduce;
    }

    public String getTvIntroduceEn() {
        return tvIntroduceEn;
    }

    public void setTvIntroduceEn(String tvIntroduceEn) {
        this.tvIntroduceEn = tvIntroduceEn;
    }
}
