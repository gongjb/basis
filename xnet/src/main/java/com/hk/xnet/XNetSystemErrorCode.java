package com.hk.xnet;


/*
  系统级别Error
 */
public enum XNetSystemErrorCode {

    EXIT_APP_ERROR("-999", "退出app"),

    START_OTHER_APP("-888","跳转到其他页面"),

    TOKEN_ERROR("10001", "token 失效"),

    DEVICE_ERROR("30011", "设备被挤出"),
    DEVICE_ERROR_TEST("0", "测试被挤出！！！"),
    //  public final static String ERROR_NET_WORK = "-444";
    NET_ERROR("-444","网络异常");

    private String mCode;
    private String mMsg;
    XNetSystemErrorCode(String code, String message) {
        this.mCode = code;
        this.mMsg = message;
    }

    public String getCode() {
        return mCode;
    }

    public boolean isSystemErrorCode(String code) {
        return mCode.equals(code);
    }
}
