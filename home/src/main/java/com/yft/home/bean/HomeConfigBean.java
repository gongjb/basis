package com.yft.home.bean;

import com.google.gson.annotations.SerializedName;
import com.hkbyte.cnbase.router.ChameleonJumpRouter;
import com.yft.zbase.bean.TargetBean;

import java.io.Serializable;
import java.util.ArrayList;
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

    public static class KingkongListBean extends TargetBean implements Serializable {
        /**
         * 测试使用的本地
         */
        private int thatImageId;

        public void setThatImageId(int thatImageId) {
            this.thatImageId = thatImageId;
        }

        public int getThatImageId() {
            return thatImageId;
        }

        public static List<KingkongListBean> getKingkongListBeans() {
            List<KingkongListBean> list = new ArrayList<>();
            KingkongListBean k1 = new KingkongListBean();
            k1.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k1.setName("更换图标");
            k1.setActionType(ChameleonJumpRouter.TO_CALCULATOR_CAMOUFLAGE);
            list.add(k1);

            KingkongListBean k2 = new KingkongListBean();
            k2.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k2.setName("新手教程");
            list.add(k2);

            KingkongListBean k3 = new KingkongListBean();
            k3.setThatImageId(com.yft.zbase.R.mipmap.icon_wechat_pay);
            k3.setName("常见问题");
            list.add(k3);

            KingkongListBean k4 = new KingkongListBean();
            k4.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k4.setName("无痕游览");
            list.add(k4);

            KingkongListBean k5 = new KingkongListBean();
            k5.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k5.setActionType(ChameleonJumpRouter.TO_FILE_IMAGE);
            k5.setName("私密相册");
            list.add(k5);

            KingkongListBean k6 = new KingkongListBean();
            k6.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k6.setName("私密视频");
            k6.setActionType(ChameleonJumpRouter.TO_FILE_VIDEO);
            list.add(k6);

            KingkongListBean k7 = new KingkongListBean();
            k7.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k7.setActionType(ChameleonJumpRouter.TO_FILE_AUDIO);
            k7.setName("私密音频");
            list.add(k7);

            KingkongListBean k8 = new KingkongListBean();
            k8.setThatImageId(com.yft.zbase.R.mipmap.ic_launcher);
            k8.setActionType(ChameleonJumpRouter.TO_FILE_DOCUMENTATION);
            k8.setName("私密文档");
            list.add(k8);

            return list;
        }
    }
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
