package com.yft.zbase.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SpecssBean implements Serializable {

    /**
     * addImage : false
     * goodsId : 1724620916939821056
     * id : 1724620917157924864
     * items : [{"goodsId":"1724620916939821056","id":"1724620917392805888","image":"","name":"1","sort":0,"specsId":"1724620917157924864"},{"goodsId":"1724620916939821056","id":"1724620917485080576","image":"","name":"2","sort":1,"specsId":"1724620917157924864"}]
     * name : 颜色
     * position : positionOne
     * sort : 0
     */
    @SerializedName("addImage")
    private boolean addImage;
    @SerializedName("goodsId")
    private String goodsId;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("position")
    private String position;
    @SerializedName("sort")
    private int sort;
    @SerializedName("items")
    private List<ItemsBean> items;

    public boolean isAddImage() {
        return addImage;
    }

    public void setAddImage(boolean addImage) {
        this.addImage = addImage;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean extends com.gongjiebin.latticeview.KVBean implements Serializable {
        /**
         * goodsId : 1724620916939821056
         * id : 1724620917392805888
         * image :
         * name : 1
         * sort : 0
         * specsId : 1724620917157924864
         */

        private String goodsId;
        private String id;
        private String image;
        private String name;
        private int sort;
        private String specsId;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getSpecsId() {
            return specsId;
        }

        public void setSpecsId(String specsId) {
            this.specsId = specsId;
        }
    }
}
