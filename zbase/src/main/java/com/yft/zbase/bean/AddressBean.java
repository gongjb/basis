package com.yft.zbase.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;


public class AddressBean implements Parcelable {

    // 是否是管理状态
    private boolean isManage;
    /**
     * addressId : 71c9429293ed4abb8f0d8a7c16027fcc
     * city : 120100
     * cityName : 天津城区
     * default : false
     * detail : 6464
     * district : 120102
     * districtName : 河东区
     * name : 工
     * phone : 18681079006
     * phoneArea : 86
     * province : 120000
     * provinceName : 天津市
     */

    private String addressId;
    private String city;
    private String cityName;
    @SerializedName("default")
    private boolean defaultX;
    private String detail;
    private String district;
    private String districtName;
    private String name;
    private String phone;
    private String phoneArea;
    private String province;
    private String provinceName;
    private String surname;

    public AddressBean() {

    }

    protected AddressBean(Parcel in) {
        isManage = in.readByte() != 0;
        addressId = in.readString();
        city = in.readString();
        cityName = in.readString();
        defaultX = in.readByte() != 0;
        detail = in.readString();
        district = in.readString();
        districtName = in.readString();
        name = in.readString();
        phone = in.readString();
        phoneArea = in.readString();
        province = in.readString();
        provinceName = in.readString();
        surname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isManage ? 1 : 0));
        dest.writeString(addressId);
        dest.writeString(city);
        dest.writeString(cityName);
        dest.writeByte((byte) (defaultX ? 1 : 0));
        dest.writeString(detail);
        dest.writeString(district);
        dest.writeString(districtName);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(phoneArea);
        dest.writeString(province);
        dest.writeString(provinceName);
        dest.writeString(surname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel in) {
            return new AddressBean(in);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isDefaultX() {
        return defaultX;
    }

    public void setDefaultX(boolean defaultX) {
        this.defaultX = defaultX;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneArea() {
        return phoneArea;
    }

    public void setPhoneArea(String phoneArea) {
        this.phoneArea = phoneArea;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String address() {
        return provinceName + " " + cityName + " " + districtName + " " + detail;
    }

    public String addressToDistrictName() {
        return provinceName + " " + cityName + " " + districtName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public boolean isManage() {
        return isManage;
    }

    public void setManage(boolean manage) {
        isManage = manage;
    }

    public String getSurname() {
        if (!TextUtils.isEmpty(name)) {
            return name.substring(0,1);
        }
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
