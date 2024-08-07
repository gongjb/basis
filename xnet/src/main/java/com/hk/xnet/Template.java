package com.hk.xnet;

/**
 * 请求默认模版，可自定义模版
 */
public class Template {
    protected String getCode() {
        return "code";
    }

    protected String getBody(){
        return "data";
    }

    protected String getMessage() {
        return "msg";
    }

    protected String successCode() {
        return "0";
    }
}
