package com.yft.zbase.server;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.utils.Constant;
import com.yft.zbase.utils.JsonUtil;
import com.yft.zbase.utils.Utils;

import java.util.List;

/**
 * 地址操作实现类
 */
public class AddressImplServer implements IAddress {

    private MMKV mAddressMmkv;

    @Override
    public void initServer(Context context) {
        mAddressMmkv = MMKV.mmkvWithID(Constant.ADDRESS, MMKV.MULTI_PROCESS_MODE);
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return ADDRESS_SERVER;
    }

    @Override
    public void cleanInfo() {
        if (mAddressMmkv != null) {
            mAddressMmkv.clear();
        }
    }

    @Override
    public boolean saveAddress(AddressBean addressBean) {
        if (!mAddressMmkv.containsKey(Constant.ADDRESS_LIST)) {
            return false;
        }

        String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
        List<AddressBean> addressBeans =  JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        if (addressBeans != null) {
            addressBeans.add(addressBean);
            // 存入缓存中
            return saveAddressList(addressBeans);
        }
        return false;
    }

    @Override
    public AddressBean getDefAddress() {
        if (!mAddressMmkv.containsKey(Constant.ADDRESS_LIST)) {
            return null;
        }
        String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
        List<AddressBean> addressBeans =  JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        if (Utils.isCollectionEmpty(addressBeans)) {
            return null;
        }

        for (AddressBean a : addressBeans) {
            if (a.isDefaultX()) {
                // 找到默认地址
                return a;
            }
        }

        if (addressBeans.get(0) != null) {
            // 找不到默认地址，就直接获取第一个地址为默认地址
            return addressBeans.get(0);
        }

        return null;
    }

    @Override
    public boolean setDefAddress(String addressId) {
        if (TextUtils.isEmpty(addressId)) {
            return false;
        }

        if (!mAddressMmkv.containsKey(Constant.ADDRESS_LIST)) {
            return false;
        }

        String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
        List<AddressBean> addressBeans =  JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        if (Utils.isCollectionEmpty(addressBeans)) {
            return false;
        }

        for (AddressBean a : addressBeans) {
            if (a.getAddressId().equals(addressId)) {
                // 当前选中的地址更改为默认地址
                a.setDefaultX(true);
            } else {
                // 其它就是非默认
                a.setDefaultX(false);
            }
        }
        return saveAddressList(addressBeans);
    }

    @Override
    public boolean saveAddressList(List<AddressBean> list) {
        mAddressMmkv.encode(Constant.ADDRESS_LIST, JsonUtil.listToJson(list));
        return true;
    }

    @Override
    public List<AddressBean> getAddressList() {
        if (mAddressMmkv.containsKey(Constant.ADDRESS_LIST)) {
            String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
            return JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        }
        return null;
    }

    @Override
    public boolean removeAddress(AddressBean addressBean) {
        if (!mAddressMmkv.containsKey(Constant.ADDRESS_LIST)) {
            return false;
        }

        String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
        List<AddressBean> addressBeans =  JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        if (Utils.isCollectionEmpty(addressBeans)) {
            return false;
        }

        for (AddressBean a : addressBeans) {
            if (a.getAddressId().equals(addressBean.getAddressId())) {
                addressBeans.remove(a);
                break;
            }
        }

        // 存入缓存中
        return saveAddressList(addressBeans);
    }

    @Override
    public AddressBean getAddress(String addressId) {
        if (!mAddressMmkv.containsKey(Constant.ADDRESS_LIST) || TextUtils.isEmpty(addressId)) {
            return null;
        }
        String json = mAddressMmkv.decodeString(Constant.ADDRESS_LIST);
        List<AddressBean> addressBeans =  JsonUtil.parseJsonToList(json, new TypeToken<List<AddressBean>>() {}.getType());
        if (Utils.isCollectionEmpty(addressBeans)) {
            return null;
        }

        for (AddressBean a : addressBeans) {
            if (a.getAddressId().equals(addressId)) {
                return a;
            }
        }

        return null;
    }
}
