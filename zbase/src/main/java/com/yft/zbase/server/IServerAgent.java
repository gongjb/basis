package com.yft.zbase.server;

import android.content.Context;

public interface IServerAgent {
    /**
     * 用户服务
     */
    String USER_SERVER = "user_server";
    /**
     * 设备服务
     */
    String DEVICE_SERVER = "device_server";

    /**
     * 地址服务
     */
    String ADDRESS_SERVER = "address_server";

    /**
     * 分享服务
     */
    String SHARE_SERVER = "share_server";

    /**
     * 支付服务
     */
    String PAY_SERVER ="pay_server";

    /**
     * 支付服务
     */
    String LANGUAGE_SERVER ="language_server";

    /**
     * 初始化程序
     */
    void initServer(Context context);

    /**
     * 获取当前的服务
     * @return 服务
     */
    <T extends IServerAgent> T getServer();

    /**
     * 得到服务的名称
     * @return 返回服务名称
     */
    String serverName();

    void cleanInfo();
}
