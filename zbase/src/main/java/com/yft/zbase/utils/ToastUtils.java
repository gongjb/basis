package com.yft.zbase.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.hk.xnet.WebServiceThrowable;
import com.lzy.okgo.exception.HttpException;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.server.LanguageManage;

import org.json.JSONException;

import java.net.ConnectException;

public class ToastUtils {

    public static void toast(String message) {
        Toast.makeText(ZBaseApplication.get(), message, Toast.LENGTH_LONG).show();
    }

    public static void currencyToast(Throwable throwable) {
        if (throwable instanceof ConnectException) {
            toast(LanguageManage.getSysytemString("连接异常，请检查网络连接情况！"));
            return;
        }

        if (throwable instanceof HttpException) {
            toast(LanguageManage.getSysytemString("您访问的接口已经不存在，请联系开发者！"));
            return;
        }
        if (throwable instanceof NetworkErrorException) {
            toast(LanguageManage.getSysytemString("请检查网络是否已经断开！"));
            return;
        }

        if (throwable instanceof WebServiceThrowable) {
            WebServiceThrowable serviceThrowable = (WebServiceThrowable) throwable;
            if (serviceThrowable != null && !TextUtils.isEmpty(serviceThrowable.getErrorCode())) {
                ErrorCode.showToast(serviceThrowable.getErrorCode(), serviceThrowable.getErrorMsg());
            }
            return;
        }

        if (throwable instanceof JSONException) {
            toast(LanguageManage.getSysytemString("数据异常，请稍后重试！"));
            return;
        }
    }
}
