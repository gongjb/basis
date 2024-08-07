package com.yft.zbase.error;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.hk.xnet.XNetSystemErrorCode;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.adapter.AdapterFactoryManage;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.ui.ButtonUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ErrorCode {

    public static final Map<String,String> errorCode = new HashMap<>();
    // 退出APP
    public final static String EXIT_APP_ERROR = "-999";
    // 跳转到其他页面
    public final static String START_OTHER_APP = "-888";
    // token 失效
    public final static String TOKEN_ERROR = "10001";
    // 设备被挤出
    public final static String DEVICE_ERROR = "30011";
    // 已参加活动
    public final static String ACTION_JOIN= "50002";

    static {
        /**
         * 10001	令牌失效
         * 10002	签名有误
         * 30001	参数有误
         * 30002	手机号有误
         * 30003	邮箱地址有误
         * 30004	密码错误
         * 30005	密码格式有误
         * 30006	验证码错误
         * 30007	邮箱已占用
         * 30008	账号不存在
         * 30010	不能切换自己
         *
         * 50001	活动已过期
         * 50002	已参加过该活动
         * 50003	今日已签到过
         *
         * 50100	节点已失效，重新选择
         * 50101	VIP 已过期，引导用户购买
         * 50102	SVIP 专属节点，请购买 SVIP
         */
        errorCode.put("-1", "服务器系统繁忙！");
        errorCode.put("10001", "令牌失效");
        errorCode.put("10002", "签名有误");
        errorCode.put("30001", "参数有误");
        errorCode.put("30002", "手机号有误");
        errorCode.put("30003", "邮箱地址有误");
        errorCode.put("30004", "密码错误");
        errorCode.put("30005", "密码格式有误");
        errorCode.put("30006", "验证码错误");
        errorCode.put("30007", "邮箱已占用");
        errorCode.put("30008", "账号不存在");
        errorCode.put("30009", "您输入的密码不正确！");
        errorCode.put("30010", "您当前已是登录状态了。");

        errorCode.put("30013", "二维码已过期");
        errorCode.put("30014", "尚未确认登录");
        errorCode.put("30015", "您已经填写过邀请码请勿重复提交");
        errorCode.put("30016", "邀请码错误！");
        errorCode.put("30017", "不能填写自己的邀请码！");

        errorCode.put("50001", "活动已过期");
        errorCode.put(ACTION_JOIN, "已参加过该活动");
        errorCode.put("50003", "今日已签到过");
        errorCode.put("50004", "签到失败，签到功能已关闭！");
    }

    public static void showToast(String code, String message){
        if(errorCode.containsKey(code)){
            Toast.makeText(ZBaseApplication.get(), LanguageManage.getSysytemString(errorCode.get(code)),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ZBaseApplication.get(), message ,Toast.LENGTH_LONG).show();
        }
        //xiufu
    }

    /**
     *
     * @param code
     */
    public static void errorStartActivity(XNetSystemErrorCode code) {
        if (ErrorCode.EXIT_APP_ERROR.equals(code.getCode())){
            exitApp();
            return;
        }

        if (ErrorCode.START_OTHER_APP.equals(code.getCode())) {
            // 打开游览器
            Uri uri = Uri.parse("https://fanyi.baidu.com/?aldtype=16047#auto/zh");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            ZBaseApplication.get().startActivity(intent);
            exitApp();
            return;
        }

        if(ErrorCode.TOKEN_ERROR.equals(code.getCode())) {
            toLoginActivity();
            return;
        }

        if(ErrorCode.DEVICE_ERROR.equals(code.getCode())) {
            // 设备达到上限， 被挤出
            toWelcomeActivity();
        }
    }

    public static void toLoginActivity() {
        if (ButtonUtils.isFastDoubleClick()) return; // 重复调用无效
        Context mContext = ZBaseApplication.get();
        // 跳转到登录 ACTIVITY_USER_LOGIN
        RouterFactory.startRouterActivity(mContext, RouterFactory.getPage("LoginActivity"));
    }

    public static void toWelcomeActivity() {
        if (ButtonUtils.isFastDoubleClick()) return; // 重复调用无效
        AdapterFactoryManage.getInstance().cleanAllAdapter();
        // 清除用户信息
        DynamicMarketManage.getInstance().cleanInfoAll();
        Context mContext = ZBaseApplication.get();
        // 跳转到欢迎页面
        RouterFactory.startRouterActivity(mContext, RouterFactory.getPage("WelcomeActivity"));
        finishNotActivity("com.fuan.market.WelcomeActivity");
    }


    /**
     *  重启app
     */
    public static void welcomeToJWY() {
        if (ButtonUtils.isFastDoubleClick()) return; // 重复调用无效
        AdapterFactoryManage.getInstance().cleanAllAdapter();
        Context mContext = ZBaseApplication.get();
        // 跳转到欢迎页面
        RouterFactory.startRouterActivity(mContext, RouterFactory.getPage("WelcomeActivity"));
        finishNotActivity("com.fuan.market.WelcomeActivity");
    }

    public static void restartApp() {
        Context mContext = ZBaseApplication.get();
        Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage(mContext.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        exitApp();
    }

    public static void exitApp() {
        Set set = BaseActivity.mapActivity.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            try {
                Map.Entry mapentry = (Map.Entry) iterator.next();
                Activity activity = (Activity) mapentry.getValue();
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }catch (Exception e) {

            }
        }
        BaseActivity.mapActivity.clear();
        AdapterFactoryManage.getInstance().cleanAllAdapter();
    }

    /**
     * 关闭非指定的Activity
     */
    public static void finishNotActivity(String canonicalName) {
        Set set = BaseActivity.mapActivity.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            FragmentActivity activity = (FragmentActivity) mapentry.getValue();
            if (!activity.getClass().getCanonicalName().equals(canonicalName)) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

    public static void finishActivity(Class<?> cls) {
        Set set = BaseActivity.mapActivity.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            FragmentActivity activity = (FragmentActivity) mapentry.getValue();
            if (activity.getClass().getCanonicalName().equals(cls.getCanonicalName())) {
                activity.finish();
                return;
            }
        }
    }
}
