package com.hk.xnet;

/**
 *  服务器异常
 */
public class WebServiceThrowable extends Throwable {

    public static final String EMPTY_ERROR_CODE = "-10020";
    public static final String EMPTY_ERROR_MSG = "数据为空，请检查配置项！";

    private String errorCode;

    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public WebServiceThrowable setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public WebServiceThrowable setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
