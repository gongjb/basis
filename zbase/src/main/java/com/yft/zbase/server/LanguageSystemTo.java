package com.yft.zbase.server;

public enum LanguageSystemTo {
    E1("The server system is busy!", "服务器系统繁忙！"),
    E10001("Token invalidation", "令牌失效"),
    E10002("The signature is incorrect", "签名有误"),
    E30001("The parameter is incorrect", "参数有误"),
    E30002("The phone number is incorrect", "手机号有误"),
    E30003("TThe email address is incorrect", "邮箱地址有误"),
    E30004("Wrong password", "密码错误"),
    E30005("The password is in the wrong format", "密码格式有误"),
    E30006("The verification code is incorrect", "验证码错误"),
    E30007("The mailbox is occupied", "邮箱已占用"),
    E30008("The account does not exist", "账号不存在"),
    E30009("You entered an incorrect password!", "您输入的密码不正确！"),
    E30010("You are currently logged in.", "您当前已是登录状态了。"),
    E30013("The QR code has expired", "二维码已过期"),
    E30014("The login has not been confirmed", "尚未确认登录"),
    E30015("You have already filled in the invitation code, please do not submit it repeatedly", "您已经填写过邀请码请勿重复提交"),
    E30016("Wrong invitation code!", "邀请码错误！"),
    E30017("You can't fill in your own invitation code!", "不能填写自己的邀请码！"),
    E50001("The event has expired", "活动已过期"),
    E50002("Have already participated in the event", "已参加过该活动"),
    E50003("I've checked in today", "今日已签到过"),
    //连接异常，请检查网络连接情况！
    NOT_NETWORK_CONNECTION("The connection is abnormal, please check the network connection!", "连接异常，请检查网络连接情况！"),
    //您访问的接口已经不存在，请联系开发者！
    NOT_CONTACT_DEVELOPER("The interface you are accessing no longer exists, please contact the developer!", "您访问的接口已经不存在，请联系开发者！"),
    //"请检查网络是否已经断开！"
    NOT_NETWORK_CHECK("Please check if the network has been disconnected!", "请检查网络是否已经断开！"),
    //数据异常，请稍后重试！
    NOT_DATA_CHECK("The data is abnormal, please try again later!", "数据异常，请稍后重试！"),
    //网络异常
    NOT_NETWORK_ABNORMAL("The network is abnormal", "网络异常"),
    //设备被挤出
    SQUEEZED_OUT("The device is squeezed out", "设备被挤出"),
    //token 失效
    TOKEN_INVALIDATION("Token invalidation", "token 失效"),
    //跳转到其他页面
    E50004("Check-in failed, check-in function is closed!", "签到失败，签到功能已关闭！");

    /**
     *
     *         errorCode.put("50004","签到失败，签到功能已关闭！");
     */

    private String en;
    private String cn;

    LanguageSystemTo(String en, String cn) {
        this.en = en;
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
