package com.yft.zbase.server;

import android.app.Activity;
import android.content.Context;

import com.tencent.mmkv.MMKV;
import com.yft.zbase.bean.WeChatPayParams;
import com.yft.zbase.utils.Constant;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class PayImplServer implements IPay {
    private ConcurrentHashMap<String, OnCallbackPayListener> mOnCallbackPayListenerMap;

    private MMKV payMMKV;

    @Override
    public void WXApiPay(Activity activity , String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign) {

    }

    @Override
    public synchronized void WXApiPay(Activity activity, final WeChatPayParams params) {

    }

    @Override
    public synchronized String getWXAppId() {
        if (!payMMKV.containsKey(WX_ID)) {
            return "";
        }
        return payMMKV.decodeString(WX_ID, "");
    }


    @Override
    public synchronized void aliPay(final String orderInfo,final Activity activity,final OnCallbackPayListener onCallbackPayListener) {

    }


    @Override
    public synchronized void aliSynPay(String orderInfo, Activity activity, OnCallbackPaySynListener onCallbackPaySynListener) {

    }


    @Override
    public synchronized void setOnCallbackPay(OnCallbackPayListener onCallbackPayListener) {
    }

    @Override
    public synchronized void addOnCallbackPayListener(String key, OnCallbackPayListener onCallbackPayListener) {
        if (!mOnCallbackPayListenerMap.contains(key)) {
            mOnCallbackPayListenerMap.put(key, onCallbackPayListener);
        }
    }

    @Override
    public synchronized boolean removeOnCallbackPayListener(String key) {
        if (mOnCallbackPayListenerMap.contains(key)) {
            mOnCallbackPayListenerMap.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendWXPayCallback(int type, String code, String msg, String result) {
        Set<?> set = mOnCallbackPayListenerMap.entrySet();/// 返回此映射所包含的映射关系的 Set 视图。
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            OnCallbackPayListener onCallbackPayListener = (OnCallbackPayListener) mapentry.getValue();
            if (onCallbackPayListener != null) {
                onCallbackPayListener.onPayCallback(type, code, msg); // 本地处理
                onCallbackPayListener.onPayCallback(result); // h5 处理
            }
        }
    }


    @Override
    public synchronized void initServer(Context context) {
        payMMKV = MMKV.mmkvWithID(Constant.WX_PAY);
        mOnCallbackPayListenerMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return PAY_SERVER;
    }

    @Override
    public void cleanInfo() {
    }
}
