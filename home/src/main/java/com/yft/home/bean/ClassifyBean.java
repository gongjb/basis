package com.yft.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class ClassifyBean implements Parcelable {
    @SerializedName("treeName")
    private String name;
    @SerializedName("treeLogo")
    private String url;
    private int rid;
    private boolean isSel;
    /**
     * parentCode : 0
     * parentCodes : 0,
     * profitRate : false
     * sort : 0
     * treeCode : 1744535489217040384
     * treeLeaf : true
     * treeLevel : 0
     * treeLogo : https://kshop003.oss-cn-guangzhou.aliyuncs.com/28bb1c3c508c49848775dd51ba92c265.png
     * treeName : 酒水饮料
     * treeNames : 酒水饮料
     * treeSort : 129
     * treeSorts : 0000000129,
     * zone : activity
     */

    @SerializedName("parentCode")
    private String parentCode;
    @SerializedName("parentCodes")
    private String parentCodes;
    @SerializedName("profitRate")
    private boolean profitRate;
    @SerializedName("sort")
    private int sort;
    @SerializedName("treeCode")
    private String treeCode;
    @SerializedName("treeLeaf")
    private boolean treeLeaf;
    @SerializedName("treeLevel")
    private int treeLevel;
    @SerializedName("treeNames")
    private String treeNames;
    @SerializedName("treeSort")
    private int treeSort;
    @SerializedName("treeSorts")
    private String treeSorts;
    @SerializedName("zone")
    private String zone;


    protected ClassifyBean(Parcel in) {
        name = in.readString();
        url = in.readString();
        rid = in.readInt();
        isSel = in.readByte() != 0;
        parentCode = in.readString();
        parentCodes = in.readString();
        profitRate = in.readByte() != 0;
        sort = in.readInt();
        treeCode = in.readString();
        treeLeaf = in.readByte() != 0;
        treeLevel = in.readInt();
        treeNames = in.readString();
        treeSort = in.readInt();
        treeSorts = in.readString();
        zone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(rid);
        dest.writeByte((byte) (isSel ? 1 : 0));
        dest.writeString(parentCode);
        dest.writeString(parentCodes);
        dest.writeByte((byte) (profitRate ? 1 : 0));
        dest.writeInt(sort);
        dest.writeString(treeCode);
        dest.writeByte((byte) (treeLeaf ? 1 : 0));
        dest.writeInt(treeLevel);
        dest.writeString(treeNames);
        dest.writeInt(treeSort);
        dest.writeString(treeSorts);
        dest.writeString(zone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClassifyBean> CREATOR = new Creator<ClassifyBean>() {
        @Override
        public ClassifyBean createFromParcel(Parcel in) {
            return new ClassifyBean(in);
        }

        @Override
        public ClassifyBean[] newArray(int size) {
            return new ClassifyBean[size];
        }
    };

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentCodes() {
        return parentCodes;
    }

    public void setParentCodes(String parentCodes) {
        this.parentCodes = parentCodes;
    }

    public boolean isProfitRate() {
        return profitRate;
    }

    public void setProfitRate(boolean profitRate) {
        this.profitRate = profitRate;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public boolean isTreeLeaf() {
        return treeLeaf;
    }

    public void setTreeLeaf(boolean treeLeaf) {
        this.treeLeaf = treeLeaf;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getTreeNames() {
        return treeNames;
    }

    public void setTreeNames(String treeNames) {
        this.treeNames = treeNames;
    }

    public int getTreeSort() {
        return treeSort;
    }

    public void setTreeSort(int treeSort) {
        this.treeSort = treeSort;
    }

    public String getTreeSorts() {
        return treeSorts;
    }

    public void setTreeSorts(String treeSorts) {
        this.treeSorts = treeSorts;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public ClassifyBean() {
    }
}
