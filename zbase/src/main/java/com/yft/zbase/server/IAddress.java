package com.yft.zbase.server;

import com.yft.zbase.bean.AddressBean;

import java.util.List;

public interface IAddress extends IServerAgent {

    /**
     * 保存单个地址信息
     * @param addressBean
     * @return  是否保存成功
     */
    boolean saveAddress(AddressBean addressBean);

    /**
     * 获取默认地址，不会出现多个默认地址的情况
     * @return 没有默认地址会返回null
     */
    AddressBean getDefAddress();

    /**
     * 修改默认地址
     * @return
     */
    boolean setDefAddress(String addressId);

    /**
     * 批量保存地址信息
     * @param list
     * @return
     */
    boolean saveAddressList(List<AddressBean> list);

    /**
     * 返回地址列表
     * @return
     */
    List<AddressBean> getAddressList();

    /**
     * 移除单个address
     * @param addressBean
     */
    boolean removeAddress(AddressBean addressBean);

    /**
     * 获取单个地址信息
     * @param addressId
     * @return
     */
    AddressBean getAddress(String addressId);
}
