package com.yft.zbase.router;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.Router;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;

import java.util.ArrayList;
import java.util.List;

public class RouterFactory {

    //用户
    public final static String FRAGMENT_USER = "com/yft/user/UserFragment";
    // 首页
    public final static String FRAGMENT_HOME_VLAYOUT = "com/yft/home/HomeVLayoutFragment";
    // 搜索
    public final static String ACTIVITY_SEARCH = "com/yft/home/SearchActivity";

    // 欢迎页面
    public final static String ACTIVITY_WELCOME = "cn/fuan/market/WelcomeActivity";
    // 引导页面
    public final static String ACTIVITY_GUIDE = "cn/fuan/market/GuideActivity";
    // 主页面
    public final static String ACTIVITY_MAIN = "cn/fuan/market/MainActivity";

    // 登陆页面
    public final static String ACTIVITY_USER_LOGIN = "com/yft/user/LoginActivity";
    // webview 页面
    public final static String ACTIVITY_WEB = "com/yft/zbase/ui/WebYftActivity";

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


    public final static String ACTIVITY_SET = "com/yft/user/SetActivity";

    // 扫描文本展示页面
    public final static String  ACTIVITY_TEXT = "com/yft/zbase/ui/TextActivity";


    /*----------------------------------地址选择模块-------------------------*/
    // 地址列表
    public final static String ACTIVITY_USER_SITE = "com/yft/user/UserSiteActivity";
    //关于我们
    public final static String ACTIVITY_ABOUT = "com/yft/user/AboutActivity";
    public static final String ROUTER_TEST = "";

    /*
       ---------------------------------首页跳转类型----------------------------
       link              跳转内部链接
       outLink       跳转外部链接
       goodsList    跳转商品列表
       goodsDetail 跳转商品详情
       innerModule  跳转商品详情
     */

    public final static String JUMP_INNER_MODULE = "InnerModule";
    public final static String JUMP_LINK_MODULE = "link";
    public final static String JUMP_OUT_LINK_MODULE = "outLink";
    public final static String JUMP_GOODS_LIST_MODULE = "goodsList";
    public final static String JUMP_GOODS_DETAIL_MODULE = "videoDetail";
    public final static String JUMP_INVITE_FRIEND = "inviteFriend"; // 邀请好友

    /**
     * toHomePage 跳转App首页Item
     * toHomeAppraise 跳转App品鉴Item
     * toHomeNews 跳转App消息Item
     * toHomeShopCar 跳转App购物车Item
     * toHomeMine 跳转App个人中心Item
     *
     * toAddressList 跳转收货地址列表
     * toUserIdentity 跳转实名认证页面
     * toOrderList 跳转订单列表
     * toCollectList 跳转收藏列表
     * toProfitPlanList 跳转返现计划列表
     * toInviteFriendList 跳转邀请好友列表
     *
     * toGoldcoinsList 跳转元宝列表
     * toBalanceList 跳转余额列表
     * toGiftVoucherList 跳转兑换券列表
     */
    public final static String TO_HOME_PAGE = "toHomePage";
    public final static String TO_HOME_APPRAISE = "toHomeAppraise";
    public final static String TO_HOME_NEWS = "toHomeNews";
    public final static String TO_HOME_SHOPCAR ="toHomeShopCar";
    public final static String TO_HOME_MINE = "toHomeMine";



    private static List<IToHomePageListener> mToHomePageListener;

    public synchronized static void addToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (mToHomePageListener == null) {
            mToHomePageListener = new ArrayList<>();
        }
        RouterFactory.mToHomePageListener.add(iToHomePageListener);
    }

    public synchronized static void removeToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (RouterFactory.mToHomePageListener != null) {
            RouterFactory.mToHomePageListener.remove(iToHomePageListener);
        }
    }

    public static void startRouterRequestActivity(Context context, String path, int requestCode, RouteCallback routerCallback) {
        Router.build(path)
                .callback(routerCallback)
                .requestCode(requestCode)
                .go(context);
    }

    public static void startRouterRequestActivity(Context context, String path, int requestCode,
                                                  Bundle bundle, RouteCallback routerCallback) {
        Router.build(path)
                .with(bundle)
                .requestCode(requestCode)
                .go(context, routerCallback);
    }

    public static void startRouterBundleActivity(Context context, String path, Bundle bundle) {
        Router.build(path)
                .with(bundle)
                .go(context);
    }

    public static void startRouterRequestActivity(Fragment fragment, String path, int requestCode,
                                                  RouteCallback routerCallback) {
        Router.build(path)
                .callback(routerCallback)
                .requestCode(requestCode)
                .go(fragment);
    }

    public static void startRouterRequestActivity(Fragment fragment, String path, int requestCode, Bundle bundle) {
        Router.build(path)
                .requestCode(requestCode)
                .with(bundle)
                .go(fragment);
    }

    public static void startRouterActivity(Context context, String path) {
        Router.build(path).go(context);
    }

    /**
     * 获取Fragment并携带参数
     *
     * @param context
     * @param path
     * @return
     */
    public static Fragment getFragment(Context context, String path, Bundle bundle) {
        return Router.build(path).with(bundle).getFragment(context);
    }

    /**
     * 获取Fragment
     *
     * @param context
     * @param path
     * @return
     */
    public static Fragment getFragment(Context context, String path ) {
        return Router.build(path).getFragment(context);
    }

    public static boolean jumpToActivity(Context context, TargetBean homeListBean) {
        if (homeListBean == null) {return false;}

        if (RouterFactory.JUMP_INNER_MODULE.equals(homeListBean.getActionType())) {
            IUser user = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
            if (!user.isLogin()) {
                RouterFactory.startRouterActivity(context, RouterFactory.ACTIVITY_USER_LOGIN);
                return false;
            }
        }

        try {
            switch (homeListBean.getActionType()) {
                case JUMP_LINK_MODULE:
                    // 内部跳转
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "");
                    bundle.putString("url", homeListBean.getTarget());
                    RouterFactory.startRouterBundleActivity(context, RouterFactory.ACTIVITY_WEB, bundle);
                    break;
                case JUMP_OUT_LINK_MODULE:
                    // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
                    Uri webdress = Uri.parse(homeListBean.getTarget());  // 解析网址
                    Intent intent = new Intent(Intent.ACTION_VIEW, webdress); // 创建绑定
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent); // 开始活动
                    break;
                case JUMP_INNER_MODULE:
                    return startInnerModule(context, homeListBean);
                case JUMP_INVITE_FRIEND:
                    bundle = new Bundle();
                    bundle.putString("pmc", homeListBean.getPmc());
                    RouterFactory.startRouterBundleActivity(context, RouterFactory.ACTIVITY_USER_LOGIN, bundle);
                    break;
                case JUMP_GOODS_DETAIL_MODULE:

                    break;
                default:{
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * toHomePage 跳转App首页Item
     * toHomeAppraise 跳转App品鉴Item
     * toHomeNews 跳转App消息Item
     * toHomeShopCar 跳转App购物车Item
     * toHomeMine 跳转App个人中心Item
     *
     * toAddressList 跳转收货地址列表
     * toUserIdentity 跳转实名认证页面
     * toOrderList 跳转订单列表
     * toCollectList 跳转收藏列表
     * toProfitPlanList 跳转返现计划列表
     * toInviteFriendList 跳转邀请好友列表
     *
     * toGoldcoinsList 跳转元宝列表
     * toBalanceList 跳转余额列表
     * toGiftVoucherList 跳转兑换券列表
     */
    private static boolean startInnerModule(Context context, TargetBean homeListBean) {
        IUser iUser = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
        try {
            switch (homeListBean.getTarget()) {
                case TO_HOME_PAGE:
                case TO_HOME_APPRAISE:
                case TO_HOME_NEWS:
                case TO_HOME_SHOPCAR:
                case TO_HOME_MINE:
                    if (mToHomePageListener != null) {
                        for (IToHomePageListener iToHomePageListener : mToHomePageListener) {
                            if (iToHomePageListener != null) {
                                iToHomePageListener.onToHomePage(homeListBean);
                            }
                        }
                    } else {
                        // 从广告页点击进入，
                        String pageStr = homeListBean.getTarget();
                        Bundle bundle = new Bundle();
                        bundle.putString("initPage", pageStr);
                        RouterFactory.startRouterBundleActivity(context, RouterFactory.ACTIVITY_MAIN, bundle);
                    }
                    break;

                default:{
                    return false;
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static void exApp() {
        //Router.c();
        //Router.
    }
}
