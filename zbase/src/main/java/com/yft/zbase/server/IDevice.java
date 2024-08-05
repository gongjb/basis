package com.yft.zbase.server;

public interface IDevice extends IServerAgent {
    /**
     * 获取设备id(唯一id)
     * @return
     */
    String getDeviceId();

    /**
     * 获取状态栏高度
     * @return
     */
    int getStatusBarHi();

    String getAndroid();

    String getAppAlias();
}
