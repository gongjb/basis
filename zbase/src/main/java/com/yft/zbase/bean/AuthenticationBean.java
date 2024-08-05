package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 实名认证实体
 */
public class AuthenticationBean implements Parcelable {
    @SerializedName("name")
    private String name; // 真实姓名
    @SerializedName("phoneArea")
    private String phoneArea = "86";
    @SerializedName("phone")
    private String phone; // 手机号码
    @SerializedName("code")
    private String code; // 验证码
    @SerializedName("idCardNo")
    private String idCardNo; // 身份证号码
    @SerializedName("idCardFrontImage")
    private String idCardFrontImage; //身份证正面图片
    @SerializedName("idCardBackImage")
    private String idCardBackImage; // 身份证反面图片
    @SerializedName("idCardHandImage")
    private String idCardHandImage; // 手持
    @SerializedName("bankName")
    private String bankName; //银行名称
    @SerializedName("subBankName")
    private String subBankName; // 支行名称
    @SerializedName("bankCardNo")
    private String bankCardNo; // 银行卡号
    @SerializedName("bankCardImage")
    private String bankCardImage; // 银行卡照片
    @SerializedName("isSubmit")
    private boolean isSubmit;
    @SerializedName("remarks")
    private String remarks;

    public AuthenticationBean() {
    }


    protected AuthenticationBean(Parcel in) {
        name = in.readString();
        phoneArea = in.readString();
        phone = in.readString();
        code = in.readString();
        idCardNo = in.readString();
        idCardFrontImage = in.readString();
        idCardBackImage = in.readString();
        idCardHandImage = in.readString();
        bankName = in.readString();
        subBankName = in.readString();
        bankCardNo = in.readString();
        bankCardImage = in.readString();
        isSubmit = in.readByte() != 0;
        remarks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneArea);
        dest.writeString(phone);
        dest.writeString(code);
        dest.writeString(idCardNo);
        dest.writeString(idCardFrontImage);
        dest.writeString(idCardBackImage);
        dest.writeString(idCardHandImage);
        dest.writeString(bankName);
        dest.writeString(subBankName);
        dest.writeString(bankCardNo);
        dest.writeString(bankCardImage);
        dest.writeByte((byte) (isSubmit ? 1 : 0));
        dest.writeString(remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticationBean> CREATOR = new Creator<AuthenticationBean>() {
        @Override
        public AuthenticationBean createFromParcel(Parcel in) {
            return new AuthenticationBean(in);
        }

        @Override
        public AuthenticationBean[] newArray(int size) {
            return new AuthenticationBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneArea() {
        return phoneArea;
    }

    public void setPhoneArea(String phoneArea) {
        this.phoneArea = phoneArea;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardFrontImage() {
        return idCardFrontImage;
    }

    public void setIdCardFrontImage(String idCardFrontImage) {
        this.idCardFrontImage = idCardFrontImage;
    }

    public String getIdCardBackImage() {
        return idCardBackImage;
    }

    public void setIdCardBackImage(String idCardBackImage) {
        this.idCardBackImage = idCardBackImage;
    }

    public String getIdCardHandImage() {
        return idCardHandImage;
    }

    public void setIdCardHandImage(String idCardHandImage) {
        this.idCardHandImage = idCardHandImage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCardImage() {
        return bankCardImage;
    }

    public void setBankCardImage(String bankCardImage) {
        this.bankCardImage = bankCardImage;
    }


    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "AuthenticationBean{" +
                "name='" + name + '\'' +
                ", phoneArea='" + phoneArea + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", idCardFrontImage='" + idCardFrontImage + '\'' +
                ", idCardBackImage='" + idCardBackImage + '\'' +
                ", idCardHandImage='" + idCardHandImage + '\'' +
                ", bankName='" + bankName + '\'' +
                ", subBankName='" + subBankName + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                ", bankCardImage='" + bankCardImage + '\'' +
                '}';
    }
}
