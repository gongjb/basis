package com.yft.zbase.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 商品
 */
public class CommodityBean implements Serializable {
    /**
     * brandId : 1723621831903522816
     * brandName : 新百伦（NB)
     * buyLimit : 0
     * collectCount : 0
     * detailUrls : https://kshop001.oss-cn-guangzhou.aliyuncs.com/1699789400787bg_自行车个人中心@3x.png,https://kshop001.oss-cn-guangzhou.aliyuncs.com/1699789400820bg_自行车个人中心@3x.png
     * freeShipping : true
     * id : 1724620916939821056
     * imageUrls : https://kshop001.oss-cn-guangzhou.aliyuncs.com/1699789374415bg_自行车个人中心@2x.png
     * name : 商品标题
     * nameShort : 商品副标题
     * noReason : true
     * priceOriginal : 20
     * priceSales : 0
     * salesShow : 0
     * skus : [{"goodsCode":"","goodsId":"1724620916939821056","goodsName":"商品标题","id":"1724620917803847680","previewImage":"5","priceOriginal":21,"priceSales":0,"priceSupplier":5,"specsCode":"1700016643353","specsItem1Id":"1724620917392805888","specsItem1Name":"1","specsItem2Id":"1724620917568966656","specsItem2Name":"a","stock":2},{"goodsCode":"","goodsId":"1724620916939821056","goodsName":"商品标题","id":"1724620917896122368","previewImage":"5","priceOriginal":20,"priceSales":10,"priceSupplier":5,"specsCode":"1700016643353","specsItem1Id":"1724620917392805888","specsItem1Name":"1","specsItem2Id":"1724620917652852736","specsItem2Name":"b","stock":2},{"goodsCode":"","goodsId":"1724620916939821056","goodsName":"商品标题","id":"1724620917988397056","previewImage":"5","priceOriginal":50,"priceSales":20,"priceSupplier":10,"specsCode":"1700016643353","specsItem1Id":"1724620917485080576","specsItem1Name":"2","specsItem2Id":"1724620917568966656","specsItem2Name":"a","stock":2},{"goodsCode":"","goodsId":"1724620916939821056","goodsName":"商品标题","id":"1724620918084866048","previewImage":"5","priceOriginal":220,"priceSales":120,"priceSupplier":60,"specsCode":"1700016643353","specsItem1Id":"1724620917485080576","specsItem1Name":"2","specsItem2Id":"1724620917652852736","specsItem2Name":"b","stock":2}]
     * specss : [{"addImage":false,"goodsId":"1724620916939821056","id":"1724620917157924864","items":[{"goodsId":"1724620916939821056","id":"1724620917392805888","image":"","name":"1","sort":0,"specsId":"1724620917157924864"},{"goodsId":"1724620916939821056","id":"1724620917485080576","image":"","name":"2","sort":1,"specsId":"1724620917157924864"}],"name":"颜色","position":"positionOne","sort":0},{"addImage":false,"goodsId":"1724620916939821056","id":"1724620917250199552","items":[{"goodsId":"1724620916939821056","id":"1724620917568966656","image":"","name":"a","sort":2,"specsId":"1724620917250199552"},{"goodsId":"1724620916939821056","id":"1724620917652852736","image":"","name":"b","sort":3,"specsId":"1724620917250199552"}],"name":"款式","position":"positionTwo","sort":1}]
     * stock : 8
     * tags : 商品标题
     * videoCover : https://kshop001.oss-cn-guangzhou.aliyuncs.com/1699789396201BG-活动专区@3x.png
     * videoUrl : https://kshop001.oss-cn-guangzhou.aliyuncs.com/16997893895401.mp4
     * zone : normal
     *
     * {"goodsId":"1724704623118659584","goodsName":"HUAWEI/华为Mate60Pro旗舰手机新品上市=>46=>17","id":"1724731908478570496","payCount":0,"priceOriginal":6599,"priceSales":6599,"showImage":"https://kshop001.oss-cn-guangzhou.aliyuncs.com/1700036233332005.webp","showImageRatio":1.0,"sort":0}
     */
    @SerializedName("brandId")
    private String brandId;
    @SerializedName("brandName")
    private String brandName;
    @SerializedName("buyLimit")
    private int buyLimit;
    @SerializedName("collectCount")
    private int collectCount;
    @SerializedName("detailUrls")
    private String detailUrls;
    @SerializedName("freeShipping")
    private boolean freeShipping;
    @SerializedName("id")
    private String id;
    @SerializedName("goodsId")
    private String goodsId;
    @SerializedName("imageUrls")
    private String imageUrls;
    @SerializedName(value = "name", alternate = {"goodsName"})
    private String name;
    @SerializedName("nameShort")
    private String nameShort;
    @SerializedName("noReason")
    private boolean noReason;
    @SerializedName("priceOriginal")
    private String priceOriginal;
    @SerializedName("priceSales")
    private String priceSales;
    @SerializedName("salesShow")
    private int salesShow;
    @SerializedName("stock")
    private int stock;
    @SerializedName("tags")
    private String tags;
    @SerializedName("videoCover")
    private String videoCover;
    @SerializedName("videoUrl")
    private String videoUrl;
    @SerializedName("zone")
    private String zone;
    @SerializedName("showImageRatio")
    private double showImageRatio;
    @SerializedName("showImage")
    private String showImage;
    @SerializedName("skus")
    private List<SkusBean> skus;
    @SerializedName("specss")
    private List<SpecssBean> specss;
    @SerializedName("integral")
    private String integral; // 元宝比例
    // 每日返现
    @SerializedName("cashbackDays")
    private int cashbackDays;
    @SerializedName("salesVirtualStr")
    private String salesVirtualStr;

    @SerializedName("salesVirtual")
    private long salesVirtual;

    private boolean sel;
    private int carNumber;
    private int height;

    public static class SkusBean implements Serializable {
        /**
         * goodsCode :
         * goodsId : 1724620916939821056
         * goodsName : 商品标题
         * id : 1724620917803847680
         * previewImage : 5
         * priceOriginal : 21
         * priceSales : 0
         * priceSupplier : 5
         * specsCode : 1700016643353
         * specsItem1Id : 1724620917392805888
         * specsItem1Name : 1
         * specsItem2Id : 1724620917568966656
         * specsItem2Name : a
         * stock : 2
         * integral
         *
         * 123、132、213、231、312、321
         */

        @SerializedName("goodsCode")
        private String goodsCode;
        @SerializedName("goodsId")
        private String goodsId;
        @SerializedName("goodsName")
        private String goodsName;
        @SerializedName("id")
        private String id;
        @SerializedName("previewImage")
        private String previewImage;
        @SerializedName("priceOriginal")
        private long priceOriginal;
        @SerializedName("priceSales")
        private long priceSales;
        @SerializedName("priceSupplier")
        private long priceSupplier;
        @SerializedName("specsCode")
        private String specsCode;
        @SerializedName("specsItem1Id")
        private String specsItem1Id;
        @SerializedName("specsItem1Name")
        private String specsItem1Name;
        @SerializedName("specsItem2Id")
        private String specsItem2Id;
        @SerializedName("specsItem2Name")
        private String specsItem2Name;
        @SerializedName("stock")
        private int stock;
        @SerializedName("integral")
        private String integral;

        private String groupId;

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPreviewImage() {
            return previewImage;
        }

        public void setPreviewImage(String previewImage) {
            this.previewImage = previewImage;
        }

        public long getPriceOriginal() {
            return priceOriginal;
        }

        public void setPriceOriginal(long priceOriginal) {
            this.priceOriginal = priceOriginal;
        }

        public long getPriceSales() {
            return priceSales;
        }

        public void setPriceSales(long priceSales) {
            this.priceSales = priceSales;
        }

        public long getPriceSupplier() {
            return priceSupplier;
        }

        public void setPriceSupplier(long priceSupplier) {
            this.priceSupplier = priceSupplier;
        }

        public String getSpecsCode() {
            return specsCode;
        }

        public void setSpecsCode(String specsCode) {
            this.specsCode = specsCode;
        }

        public String getSpecsItem1Id() {
            return specsItem1Id;
        }

        public void setSpecsItem1Id(String specsItem1Id) {
            this.specsItem1Id = specsItem1Id;
        }

        public String getSpecsItem1Name() {
            return specsItem1Name;
        }

        public void setSpecsItem1Name(String specsItem1Name) {
            this.specsItem1Name = specsItem1Name;
        }

        public String getSpecsItem2Id() {
            return specsItem2Id;
        }

        public void setSpecsItem2Id(String specsItem2Id) {
            this.specsItem2Id = specsItem2Id;
        }

        public String getSpecsItem2Name() {
            return specsItem2Name;
        }

        public void setSpecsItem2Name(String specsItem2Name) {
            this.specsItem2Name = specsItem2Name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getGroupId() {
            if (TextUtils.isEmpty(specsItem1Id)) {
                specsItem1Id = "";
            }

            if (TextUtils.isEmpty(specsItem2Id)) {
                specsItem2Id="";
            }
            return specsItem1Id + specsItem2Id;
        }

        public String getGroupId2() {
            if (TextUtils.isEmpty(specsItem1Id)) {
                specsItem1Id = "";
            }

            if (TextUtils.isEmpty(specsItem2Id)) {
                specsItem2Id="";
            }
            return specsItem2Id + specsItem1Id;
        }
    }


    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getDetailUrls() {
        return detailUrls;
    }

    public void setDetailUrls(String detailUrls) {
        this.detailUrls = detailUrls;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public boolean isNoReason() {
        return noReason;
    }

    public void setNoReason(boolean noReason) {
        this.noReason = noReason;
    }

    public String getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(String priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public String getPriceSales() {
        return priceSales;
    }

    public void setPriceSales(String priceSales) {
        this.priceSales = priceSales;
    }

    public int getSalesShow() {
        return salesShow;
    }

    public void setSalesShow(int salesShow) {
        this.salesShow = salesShow;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<SkusBean> getSkus() {
        return skus;
    }

    public void setSkus(List<SkusBean> skus) {
        this.skus = skus;
    }

    public List<SpecssBean> getSpecss() {
        return specss;
    }

    public void setSpecss(List<SpecssBean> specss) {
        this.specss = specss;
    }

    public double getShowImageRatio() {
        return showImageRatio;
    }

    public void setShowImageRatio(double showImageRatio) {
        this.showImageRatio = showImageRatio;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public boolean isSel() {
        return sel;
    }

    public void setSel(boolean sel) {
        this.sel = sel;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCashbackDays() {
        return cashbackDays;
    }

    public void setCashbackDays(int cashbackDays) {
        this.cashbackDays = cashbackDays;
    }

    public String getSalesVirtualStr() {
        return salesVirtualStr;
    }

    public void setSalesVirtualStr(String salesVirtualStr) {
        this.salesVirtualStr = salesVirtualStr;
    }

    public long getSalesVirtual() {
        return salesVirtual;
    }

    public void setSalesVirtual(long salesVirtual) {
        this.salesVirtual = salesVirtual;
    }
}
