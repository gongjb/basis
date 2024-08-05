package com.yft.home.bean;

import com.google.gson.annotations.SerializedName;
import com.yft.zbase.bean.TargetBean;

import java.io.Serializable;
import java.util.List;

/**
 *  首页配置项
 */
public class HomeConfigBean implements Serializable {
    @SerializedName("bannerList")
    private List<BannerListBean> bannerList;

    @SerializedName("kingkongList")
    private List<KingkongListBean> kingkongList;

    @SerializedName("ShortClsList")
    private List<List<ShortClsListBean>> shortClsListBeans;

    @SerializedName("ShortNewClsListBean")
    private List<ShortNewClsListBean> shortNewClsListBeans;

    public static class BannerListBean extends TargetBean implements Serializable {}
    public static class KingkongListBean extends TargetBean implements Serializable {}
    public static class ShortClsListBean extends TargetBean implements Serializable {
        private String title; // 标题
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ShortNewClsListBean extends TargetBean implements Serializable {
    }

    public List<BannerListBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerListBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<KingkongListBean> getKingkongList() {
        return kingkongList;
    }

    public void setKingkongList(List<KingkongListBean> kingkongList) {
        this.kingkongList = kingkongList;
    }

    public List<List<ShortClsListBean>> getShortClsListBeans() {
        return shortClsListBeans;
    }

    public void setShortClsListBeans(List<List<ShortClsListBean>> shortClsListBeans) {
        this.shortClsListBeans = shortClsListBeans;
    }

    public List<ShortNewClsListBean> getShortNewClsListBeans() {
        return shortNewClsListBeans;
    }

    public void setShortNewClsListBeans(List<ShortNewClsListBean> shortNewClsListBeans) {
        this.shortNewClsListBeans = shortNewClsListBeans;
    }
}
