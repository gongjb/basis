package com.yft.user.router;

import com.yft.zbase.router.IRouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用户模块页面集合
 */
public class UserRouter implements IRouter {
    // 登录页面
    public final static String ACTIVITY_USER_LOGIN = "com/yft/user/LoginActivity";
    // 用户信息
    public final static String ACTIVITY_USER_INFORMATION = "com/yft/user/ActivityUserInformation";
    // 邀请好友
    public final static String ACTIVITY_INVITE_FRIEND = "com/yft/user/InviteFriendsActivity";
    // 邀请好友v2
    public final static String ACTIVITY_INVITE_FRIEND_2 = "com/yft/user/InviteFriends2Activity";
    // 绑定电话号码
    public final static String ACTIVITY_BIND_PHONE = "com/yft/user/BindPhoneActivity";
    // 修改昵称
    public final static String ACTIVITY_NICKNAME = "com/yft/user/NicknameActivity";
    // 设置页面
    public final static String ACTIVITY_SET = "com/yft/user/SetActivity";
    // 地址列表
    public final static String ACTIVITY_USER_SITE = "com/yft/user/UserSiteActivity";
    // 关于我们
    public final static String ACTIVITY_ABOUT = "com/yft/user/AboutActivity";
    // 我的
    public final static String FRAGMENT_USER = "com/yft/user/UserFragment";


    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> routerMap = new ConcurrentHashMap<>();
        routerMap.put("LoginActivity", ACTIVITY_USER_LOGIN);
        routerMap.put("ActivityUserInformation", ACTIVITY_USER_INFORMATION);
        routerMap.put("InviteFriendsActivity", ACTIVITY_INVITE_FRIEND);
        routerMap.put("InviteFriends2Activity", ACTIVITY_INVITE_FRIEND_2);
        routerMap.put("BindPhoneActivity", ACTIVITY_BIND_PHONE);
        routerMap.put("NicknameActivity", ACTIVITY_NICKNAME);
        routerMap.put("SetActivity", ACTIVITY_SET);
        routerMap.put("UserSiteActivity", ACTIVITY_USER_SITE);
        routerMap.put("AboutActivity", ACTIVITY_ABOUT);
        routerMap.put("UserFragment", FRAGMENT_USER);
        return routerMap;
    }
}
