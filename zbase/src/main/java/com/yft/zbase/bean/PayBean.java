package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class PayBean implements Parcelable {

    /**
     * payChannel : ALP
     * payOrderId : 202312081307004388562
     * tradeType : H5
     */

    @SerializedName("payChannel")
    private String payChannel;

    @SerializedName("payOrderId")
    private String payOrderId;
    @SerializedName("tradeType")
    private String tradeType;
    @SerializedName("target")
    private String target;
    @SerializedName("outOpen")
    private boolean outOpen;

    public PayBean() {}

    protected PayBean(Parcel in) {
        payChannel = in.readString();
        payOrderId = in.readString();
        tradeType = in.readString();
        target = in.readString();
        outOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payChannel);
        dest.writeString(payOrderId);
        dest.writeString(tradeType);
        dest.writeString(target);
        dest.writeByte((byte) (outOpen ? 1 : 0));
    }

    public static final Creator<PayBean> CREATOR = new Creator<PayBean>() {
        @Override
        public PayBean createFromParcel(Parcel in) {
            return new PayBean(in);
        }

        @Override
        public PayBean[] newArray(int size) {
            return new PayBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isOutOpen() {
        return outOpen;
    }

    public void setOutOpen(boolean outOpen) {
        this.outOpen = outOpen;
    }
}
