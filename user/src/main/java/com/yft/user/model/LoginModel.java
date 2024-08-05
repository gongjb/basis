package com.yft.user.model;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yft.zbase.base.BaseModel;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.bean.UserToken;
import com.yft.zbase.xnet.InterfacePath;
import com.yft.zbase.xnet.ResponseDataListener;

import java.util.List;
import java.util.TreeMap;

/**
 *  登录注册相关model
 */
public class LoginModel extends BaseModel {

    /**
     * 注册接口
     * @param phone 电话号码
     * @param code 验证码
     * @param invitationCode 邀请码
     * @param responseDataListener 回调
     */
    public void postUserReg(String phone, String code, String invitationCode, ResponseDataListener<UserToken> responseDataListener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("phone", phone);
        treeMap.put("code", code);
        treeMap.put("invitationCode", invitationCode);
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_REG,
                getRequestParameter(treeMap), responseDataListener, UserToken.class);
    }

    /**
     * 登录
     * @param phone
     * @param code
     * @param responseDataListener
     */
    public void postUserLogin(String phone, String code, ResponseDataListener<UserToken> responseDataListener) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        treeMap.put("phone", phone);
        treeMap.put("code", code);
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_LOGIN,
                getRequestParameter(treeMap), responseDataListener, UserToken.class);
    }

    /**
     * 请求用户信息
     * @param responseDataListener
     */
    public void postMyUserInfo(ResponseDataListener<UserInfo> responseDataListener) {
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_MY_INFO,
                getRequestParameter(), responseDataListener, UserInfo.class);
    }

    /**
     * 修改用户信息
     * @param logo
     * @param nikeName
     * @param responseDataListener
     */
    public void postUpdateUserInfo (String logo, String nikeName, ResponseDataListener<String> responseDataListener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        if (!TextUtils.isEmpty(logo)) {
            treeMap.put("logo", logo);
        }

        if (!TextUtils.isEmpty(nikeName)) {
            treeMap.put("nickname", nikeName);
        }

        //USER_UPDATE_INFO
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_UPDATE_INFO,
                getRequestParameter(treeMap), responseDataListener, String.class);
    }

    /**
     * 获取地址列表
     * @param responseDataListener
     */
    public void requestAddressCoddList(final ResponseDataListener<List<AddressBean>> responseDataListener) {
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_ADDRESS_LIST,
                        getRequestParameter(), responseDataListener, new TypeToken<List<AddressBean>>() {}.getType());
    }

    /**
     * 退出登录
     * @param responseDataListener
     */
    public void exitLogin(final ResponseDataListener<String> responseDataListener) {
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_EXIT_LOGIN,
                getRequestParameter(), responseDataListener, String.class);
    }


    /**
     * 注销
     * @param responseDataListener
     */
    public void userLogOff(String describe, ResponseDataListener<String> responseDataListener) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        treeMap.put("reason", describe);
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.USER_EXIT_LOG_Off,
                getRequestParameter(treeMap), responseDataListener, String.class);
    }

    /**
     * 修改密码
     * @param olbPwd
     * @param newPwd
     * @param responseDataListener
     * /user/change/password
     */
    public void updatePwd(String olbPwd, String newPwd, ResponseDataListener<String> responseDataListener) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        if(!TextUtils.isEmpty(olbPwd)){
            treeMap.put("oldPassword",olbPwd);
        }
        treeMap.put("newPassword",newPwd);
        getNetWork().easyPost(mUser.getBastServiceURL() + InterfacePath.CHANGE_PASSWORD,
                getRequestParameter(treeMap), responseDataListener, String.class);
    }
}
