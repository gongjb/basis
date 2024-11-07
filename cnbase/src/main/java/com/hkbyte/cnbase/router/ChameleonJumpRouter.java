package com.hkbyte.cnbase.router;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.router.IToHomePageListener;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * 变色龙应用统一跳转入口
 */
public class ChameleonJumpRouter implements RouterFactory.IRouterJumpPages {

    private static ChameleonJumpRouter instance;

    private ChameleonJumpRouter() {}

    public static synchronized ChameleonJumpRouter getInstance() {
        if (instance == null) {
            synchronized (ChameleonJumpRouter.class) {
                if (instance == null) {
                    instance = new ChameleonJumpRouter();
                }
            }
        }
        return instance;
    }

    /*
    */
    public final static String JUMP_INNER_MODULE = "InnerModule";
    public final static String JUMP_LINK_MODULE = "link";
    public final static String JUMP_OUT_LINK_MODULE = "outLink";
    public final static String JUMP_GOODS_LIST_MODULE = "goodsList";
    public final static String JUMP_GOODS_DETAIL_MODULE = "videoDetail";
    public final static String JUMP_INVITE_FRIEND = "inviteFriend"; // 邀请好友

    /**
     */
    public final static String TO_HOME_PAGE = "toHomePage";
    public final static String TO_HOME_TOOLS = "toTools";
    public final static String TO_HOME_VIP = "toVip";
    public final static String TO_HOME_MINE = "toHomeMine";

    //私密视频
    public final static String TO_FILE_VIDEO = "toFileVideo";
    //私密图片
    public final static String TO_FILE_IMAGE = "toFileImage";
    //私密音频
    public final static String TO_FILE_AUDIO = "toFileAudio";
    //私密文档
    public final static String TO_FILE_DOCUMENTATION = "toFileDocumentation";
    //计算器伪装
    public final static String TO_CALCULATOR_CAMOUFLAGE = "toCalculatorCamouflage";

    private List<IToHomePageListener> mToHomePageListener;

    public synchronized void addToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (mToHomePageListener == null) {
            mToHomePageListener = new ArrayList<>();
        }
        mToHomePageListener.add(iToHomePageListener);
    }

    public synchronized void removeToHomePageListener(IToHomePageListener iToHomePageListener) {
        if (mToHomePageListener != null) {
            mToHomePageListener.remove(iToHomePageListener);
        }
    }

    @Override
    public boolean jumpToActivity(Context context, TargetBean homeListBean) {
                if (homeListBean == null) {return false;}

        if (ChameleonJumpRouter.JUMP_INNER_MODULE.equals(homeListBean.getActionType())) {
            IUser user = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
            if (!user.isLogin()) {
//                RouterFactory.startRouterActivity(context, RouterFactory.ACTIVITY_USER_LOGIN);
                RouterFactory.getInstance().startRouterActivity(context, RouterFactory.getInstance().getPage("LoginActivity"));
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
                    //RouterFactory.startRouterBundleActivity(context, RouterFactory.ACTIVITY_WEB, bundle);
                    RouterFactory.getInstance().startRouterBundleActivity(context, RouterFactory.getInstance().getPage("WebYftActivity"), bundle);
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
                    RouterFactory.getInstance().startRouterBundleActivity(context, RouterFactory.getInstance().getPage("LoginActivity"), bundle);
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

    private synchronized boolean startInnerModule(Context context, TargetBean homeListBean) {
        IUser iUser = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
        try {
            switch (homeListBean.getTarget()) {
                case TO_HOME_PAGE:
                case TO_HOME_TOOLS:
                case TO_HOME_VIP:
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
                        RouterFactory.getInstance().startRouterBundleActivity(context, RouterFactory.getInstance().getPage("MainActivity"), bundle);
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
}
