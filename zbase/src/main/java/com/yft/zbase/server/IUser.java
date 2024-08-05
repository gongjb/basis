package com.yft.zbase.server;

import com.yft.zbase.bean.AuthenticationBean;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.bean.UserInfo;

public interface IUser extends IServerAgent {

    /**
     * 获取用户信息
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 是否已经登录
     * @return
     */
    boolean isLogin();


    /**
     * 保存用户信息
     * @param userInfo
     * @return
     */
    boolean sveaUserInfo(UserInfo userInfo);

    /**
     * 获取app访问的server url
     * @return
     */
    ServiceBean getServiceUrl();

    /**
     * 获取推荐请求路径
     */
    String getBastServiceURL();

    /**
     * 清除用户信息
     */
    void cleanUserInfo();

    /**
     * 保存服务器信息
     * @param serviceBean
     * @return
     */
    boolean sveaServiceUrl(ServiceBean serviceBean);

    /**
     * 得到小程序的服务器路径
     * @return
     */
    String getAppletURL();

    /**
     * 得到分享出去的路径
     * @return
     */
    String getShareURL();

    /**
     * 获取格式化之后的电话号码
     * @return
     */
    String getPhoneFormat();

    /**
     * 保存开屏广告
     * @param targetBean
     * @return
     */
    boolean savePreloadOpen(TargetBean targetBean);

    /**
     * 得到广告实体
     * @return
     */
    TargetBean getPreloadOpen();

    /**
     * 保存实名认证信息
     * @param authenticationBean
     */
    boolean saveAuthentication(AuthenticationBean authenticationBean);

    /**
     *
     * @return 获取实名认证信息
     */
    AuthenticationBean getAuthentication();

    /**
     * 获取搜索历史
     * @return
     */
    String[] getSearchHistory();

    /**
     * 保存一个搜索记录， 注意如果保存的值为“” 或者 null, 将直接清除掉历史记录
     * @param key
     * @return
     */
    boolean saveSearchKey(String key);

    boolean removeSearchHistoryKey(String key);

    void setPrivacy(boolean isPrivacy);

    /**
     * 当前用户是否同意隐私权限
     * @return true 同意了， 没有同意
     */
    boolean isPrivacy();

    /**
     * 设置用户存储、拍照权限
     * @return
     */
    void setCameraStoragePermissions(boolean isPermissions);


    boolean isCameraStoragePermissions();

    /**
     * 授权相机权限（单独）
     */
    void setCameraPermissions(boolean isCameraPermissions);

    /**
     * 是否授权相机权限
     * @return
     */
    boolean isCameraPermissions();


    /**
     * 授权相机权限（单独）
     */
    void setStoragePermissions(boolean isCameraPermissions);

    /**
     * 是否授权相机权限
     * @return
     */
    boolean isStoragePermissions();

    /**
     * 获取当前渠道名称
     * @return
     */
    String getFlavor();

    /**
     * 保存渠道名称
     * @param flavor
     * @return
     */
    boolean saveFlavor(String flavor);

    /**
     * 获取用户等级
     * @return
     */
    UserLevelType getUserLevel();
}
