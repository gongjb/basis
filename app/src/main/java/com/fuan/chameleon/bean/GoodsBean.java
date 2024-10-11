package com.fuan.chameleon.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsBean implements Serializable {
    private int id;
    private int code;
    private String name;
    private String nameShort;
    private String videoUrl;
    private String videoCover;
    private int categoryId;
    private int categoryIds;
    private int supplierId;
    private int supplierCode;
    private String supplierName;
    private int brandId;
    private String brandName;
    private int priceOriginal;
    private int pricesupplier;
    private int priceSales;
    private String tags;
    private int stock;
    private int stockWarning;
    private int salesCount;
    private int salesShow;
    private int salesVirtual;
    private int score;
    private int step;
    private int buyLimit;
    private String ipBuyLimit;
    private int accountBuyLimit;
    private String searchKeys;
    private int sortWeight;
    private List<String> imageUrls;
    private List<KsGoodsSpecsBean> ksGoodsSpecs;
    private List<KsGoodsSpecsItemBean> ksGoodsSpecsItem;
    private List<KsGoodsSpecSkuBean> ksGoodsSpecSku;

    public static class KsGoodsSpecsBean implements Serializable {
        /**
         * id : 0
         * sort : 1
         * position : positionOne
         * name : 口味
         * addImage : false
         */

        private int id;
        private int sort;
        private String position;
        private String name;
        private boolean addImage;
    }

    public static class KsGoodsSpecsItemBean implements Serializable {
        /**
         * id : 0
         * specs_id : 0
         * name : 不辣
         * image :
         */

        private int id;
        private int specs_id;
        private String name;
        private String image;
    }

    public static class KsGoodsSpecSkuBean implements Serializable {
        /**
         * id : 0
         * specsItem1Id : 0
         * specsItem1Name : 口味
         * specsItem2Id : 1
         * specsItem2Name : 规格
         * stock : 100
         */

        private int id;
        private int specsItem1Id;
        private String specsItem1Name;
        private int specsItem2Id;
        private String specsItem2Name;
        private int stock;
    }
}
