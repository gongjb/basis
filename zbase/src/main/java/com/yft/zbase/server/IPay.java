package com.yft.zbase.server;

import android.app.Activity;

import com.yft.zbase.bean.WeChatPayParams;

/**
 *  支付
 */
public interface IPay extends IServerAgent {
    int WX_PAY_TYPE = 0;
    int ALI_PAY_TYPE = 1;
    String WX_ID = "WX_ID";
    /**
     *   request.appId = "wxd930ea5d5a258f4f";
     *   request.partnerId = "1900000109";
     *   request.prepayId= "1101000000140415649af9fc314aa427",;
     *   request.packageValue = "Sign=WXPay";
     *   request.nonceStr= "1101000000140429eb40476f8896f4c9";
     *   request.timeStamp= "1398746574";
     *   request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
     *   微信支付
     */
    void WXApiPay(Activity activity,String appId, String partnerId,
                  String prepayId, String packageValue, String nonceStr, String timeStamp, String sign);

    void WXApiPay(Activity activity, WeChatPayParams params);

    String getWXAppId();

    void aliPay(String orderInfo, Activity activity, OnCallbackPayListener onCallbackPayListener);


    @Deprecated
    void aliSynPay(String orderInfo, Activity activity, OnCallbackPaySynListener onCallbackPaySynListener);

    /**
     * 设置支付回掉
     */
    void setOnCallbackPay(OnCallbackPayListener onCallbackPayListener);

    /**
     * 当支付结果不能在当前类获取时， 需要注册监听器
     * @param key
     * @param onCallbackPayListener
     */
    void addOnCallbackPayListener(String key, OnCallbackPayListener onCallbackPayListener);

    boolean removeOnCallbackPayListener(String key);

    /**
     * 通知支付结果
     * @param type
     * @param code
     * @param meg
     */
    void sendWXPayCallback(int type, String code, String meg, String request);

    interface OnCallbackPayListener {
        /**
         * 支付结果回调
         *
         * @param type {@link com.yft.zbase.server.IPay}
         * @param code 错误码
         * @param msg 内容
         */
        void onPayCallback(int type, String code, String msg);

        /**
         * 支付结果回调（用于第三方请求支付结果，例如h5）
         *
         */
        default void onPayCallback(String result) {
            // do noting
        }
    }

    interface OnCallbackPaySynListener {
        /**
         * 支付结果回掉
         *
         */
        void onPayCallback(String result);
    }
}
