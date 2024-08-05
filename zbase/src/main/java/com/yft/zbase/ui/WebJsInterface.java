package com.yft.zbase.ui;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.RouteStatus;
import com.google.gson.JsonObject;
import com.yft.zbase.BuildConfig;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.bean.PayBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.bean.UserToken;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IAddress;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;
import com.yft.zbase.server.ManageThreadPoolService;
import com.yft.zbase.utils.JsonUtil;
import com.yft.zbase.utils.Utils;

import org.json.JSONObject;

import java.util.List;

public class WebJsInterface {
    private Handler handler = new Handler(Looper.getMainLooper());
    private IWebActivity iWebActivity;
    private IAddress mAddress;

    // 广告控制器
    public WebJsInterface(IWebActivity webView) {
        this.iWebActivity = webView;
        mAddress = DynamicMarketManage.getInstance().getServer(IServerAgent.ADDRESS_SERVER);
    }

    /**
     * @param json 请求携带的参数
     */
    @JavascriptInterface
    public void appRequest(final String json) {
        LOGE(json);
        if (!TextUtils.isEmpty(json)) {
            JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
            int isShowDialog = 0; // 默认不显示dialog
            String url = "";
            String jsonStr = "";
            String callbackFunction = "";
            if (jsonObject.has("url")) {
                url = jsonObject.get("url").getAsString();
            }
            if (jsonObject.has("json")) {
                jsonStr = jsonObject.get("json").toString();
            }
            if (jsonObject.has("callbackFunction")) {
                callbackFunction = jsonObject.get("callbackFunction").getAsString();
            }

            if (jsonObject.has("isShowDialog")) {
                isShowDialog = jsonObject.get("isShowDialog").getAsInt();
            }

            // 发起请求
            appRequest(url, isShowDialog, callbackFunction, jsonStr);
        }
    }


    /**
     * js交互->不显示dialog也不需要回传给js
     *
     * @param url  请求路径
     * @param json 请求携带的参数
     */
    @JavascriptInterface
    public void request(final String url, final String json) {
        appRequest(url, null, json);
    }


    /**
     * js交互->不显示dialog但需要回调给js
     *
     * @param url              请求路径
     * @param callbackFunction 请求完成之后调用的方法名称
     * @param json             请求携带的参数
     */
    @JavascriptInterface
    public void appRequest(final String url, final String callbackFunction, final String json) {
        appRequest(url, 0, callbackFunction, json);
    }

    /**
     * js交互->自定义是否需要显示dialog 并需要回调给js
     *
     * @param url              请求路径
     * @param callbackFunction 请求完成之后调用的方法名称
     * @param json             请求携带的参数
     * @isShowDialog 0 不显示dialog, 1 显示dialog
     */
    @JavascriptInterface
    public void appRequest(final String url, int isShowDialog, final String callbackFunction, final String json) {
        LOGE("-------> 参数" + json + "回调方法：" + "javascript:" + callbackFunction + "('" + 1 + "','" + json + "')" + "Thread Name=" + JsonUtil.parseJsonToMap(json));
        // 发起请求
        //iWebActivity.getWebView().loadUrl("javascript:"+callbackFunction+"('"+ json +"')");
        iWebActivity.request(url, isShowDialog, callbackFunction, json);
    }


    /**
     * 获取用户信息
     *
     * @return 返回用户信息
     */
    @JavascriptInterface
    public void getAppUserInfo(String callbackFunction) {
        if (iWebActivity != null) {
            UserInfo userInfo = iWebActivity.getUserInfo();
            if (userInfo == null) {
                // 去登录
                iWebActivity.toLoginActivity(callbackFunction);
            } else {
                String json = JsonUtil.parseBeanToJson(iWebActivity.getUserInfo());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + json + "')");
                    }
                });
            }
        }
    }

    /**
     * 获取token
     *
     * @return 返回token
     */
    @JavascriptInterface
    public void getAppToken(final String callbackFunction) {
        String token = "";
        if (iWebActivity.getUserInfo() != null) {
            token = iWebActivity.getUserInfo().getToken();
        }
        final String tk = token;
        handler.post(() -> iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + tk + "')"));
    }


    /**
     * 获取设备唯一码
     *
     * @return 返回获取设备唯一码，就是接口提交的序列号字段（serialNumber）
     */
    @JavascriptInterface
    public String getAppDeviceId() {
        if (iWebActivity != null) {
            return iWebActivity.getDeviceId();
        }
        return "";
    }


    /**
     * 获取当前应用的版本号
     *
     * @return 返回当前应用的版本号（类似v2.0.4）
     */
    @JavascriptInterface
    public String getAppVersion() {
        return BuildConfig.versionName;
    }

    /**
     * 获取当前手机类型
     *
     * @return 返回当前手机类型
     */
    @JavascriptInterface
    public String getAppModel() {
        return Build.MODEL;
    }

    /**
     * 打开外部浏览器
     */
    @JavascriptInterface
    public int appOpenExternalWeb(String json) {
        LOGE("---->" + json);
        try {
            JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
            Uri webAddress = Uri.parse(jsonObject.get("url").getAsString());  // 解析网址
            Intent intent = new Intent(Intent.ACTION_VIEW, webAddress); // 创建绑定
            iWebActivity.getActivity().startActivity(intent); // 开始活动
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * @param url              判断本地是否安装了某个软件
     * @param callbackFunction 回调函数
     */
    @JavascriptInterface
    public int appInstallExternalApp(final String url, final String callbackFunction) {
        boolean has = Utils.hasApplication(iWebActivity.getActivity(), url);
        return has ? 1 : 0;
    }

    /**
     * @param url              打开
     * @param callbackFunction 回调函数 返回1有安装0没有安装
     */
    @JavascriptInterface
    public int appOpenExternalApp(final String url, final String callbackFunction) {
        if (TextUtils.isEmpty(url)) {
            return 0;
        }
        boolean has = Utils.hasApplication(iWebActivity.getActivity(), url);
        if (has) {
            try {
                PackageManager packageManager = iWebActivity.getActivity().getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 启动目标应用程序
                iWebActivity.getActivity().startActivity(intent);
            } catch (Exception e) {
                return 0;
            }
        }

        return has ? 1 : 0;
    }

    /**
     * 新打开一个WebView视图控制器链接地址
     *
     * @param url              链接地址
     * @param title            新视图展示名称
     * @param callbackFunction 回调函数，返回1成功0失败
     */
    @JavascriptInterface
    public int appOpenWebVC(final String url, final String title, final String callbackFunction) {
        handler.post(() -> {
            LOGE("=====>" + url);
            try {
                Intent intent = new Intent(iWebActivity.getActivity(), WebYftActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                iWebActivity.getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return 1;
    }

    /**
     * 关闭一个WebView视图控制器链接地址
     *
     * @param url              链接地址，浏览器会判断当前url和WebView持有的url是否一致，一致情况下才会关闭当前视图
     * @param callbackFunction 回调函数，返回1成功0失败
     */
    @JavascriptInterface
    public int appCloseWebVC(final String url, final String callbackFunction) {
        handler.post(() -> {
            if (!iWebActivity.getActivity().isFinishing()) {
                iWebActivity.getActivity().finish();
            }
        });
        return 1;
    }

    @JavascriptInterface
    public void appPhotoSelection(final String json) {
        // 图片选择器
        handler.post(() -> {
            try {
                JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
                if (jsonObject == null || !jsonObject.has("callbackFunction")) {
                    LOGE("WebJsInterface", "没有方法callbackFunction， 参数错误");
                    return;
                }
                String module = "other";
                if (jsonObject.has("module")) {
                    module = jsonObject.get("module").getAsString();
                }

                if (TextUtils.isEmpty(module)) {
                    module = "other";
                }

                iWebActivity.toPhotoSelection(jsonObject.get("callbackFunction").getAsString(), module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @JavascriptInterface
    public void appGetDefaultAddress(final String json) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(json)) {
                    LOGE("WebJsInterface", "接收===>" + json);
                    return;
                }

                JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
                if (jsonObject == null && !jsonObject.has("callbackFunction")) {
                    LOGE("WebJsInterface", "appGetDefaultAddress 没有方法callbackFunction， 参数错误");
                    return;
                }
                String callbackFunction = jsonObject.get("callbackFunction").getAsString();
                AddressBean addressBean = mAddress.getDefAddress();
                if (addressBean == null) {
                    List<AddressBean> addressBeanList = mAddress.getAddressList();
                    if (Utils.isCollectionEmpty(addressBeanList)) {
                        iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + new JsonObject().toString() + "')");
                        return;
                    } else {
                        addressBean = addressBeanList.get(0);
                    }
                }

                String callbackJson = JsonUtil.parseBeanToJson(addressBean);

                LOGE("WebJsInterface", "发送===>" + callbackJson);
                if (!TextUtils.isEmpty(callbackJson)) {
                    iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + callbackJson + "')");
                } else {
                    iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + new JsonObject().toString() + "')");
                }
            }
        });
    }

    @JavascriptInterface
    public void appHaveDefaultAddress(final String json) {
        // 图片选择器
        handler.post(() -> {
            try {
                JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
                if (jsonObject == null || !jsonObject.has("callbackFunction")) {
                    LOGE("WebJsInterface", " appHaveDefaultAddress 没有方法callbackFunction， 参数错误");
                    return;
                }
                String callbackFunction = jsonObject.get("callbackFunction").getAsString();
                String isDef = mAddress.getDefAddress() == null ? "0" : "1";
                iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + isDef + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @JavascriptInterface
    public void appOpenAddressListVC(final String json) {
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenAddressListVC 没有方法callbackFunction， 参数错误");
            return;
        }
        iWebActivity.toAddressSelection(jsonObject.get("callbackFunction").getAsString());
    }

    @JavascriptInterface
    public void appOpenGoodsInfo(final String json) {
        // 跳转商品详情
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }
        String callbackFunction = jsonObject.get("callbackFunction").getAsString();

        if (!jsonObject.has("goodsId")) {
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
            return;
        }
        String goodsId = jsonObject.get("goodsId").getAsString();
        TargetBean targetBean = new TargetBean();
        targetBean.setActionType(RouterFactory.JUMP_GOODS_DETAIL_MODULE);
        targetBean.setTarget(goodsId);

        boolean isSuccess = RouterFactory.jumpToActivity(iWebActivity.getActivity(), targetBean);
        int succ = isSuccess ? 1 : 0;
        iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + succ + "')");
    }

    /**
     * 调用本地分享
     *
     * @param json shareType： 0=文本，1=图片
     */
    @JavascriptInterface
    public void appOpenLocaltionShare(final String json) {
        //var json = '{"shareType":"'+shareType+'", "shareContent":'+ shareContent +', "callbackFunction":"' + name + '"}'
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }
        String type = jsonObject.get("shareType").getAsString();
        String content = "";
        if (jsonObject.has("shareContent")) {
            if ("0".equals(type)) {
                JsonObject obj = jsonObject.get("shareContent").getAsJsonObject();
                String link = obj.get("url").getAsString();
                String content1 = obj.get("content").getAsString();
                content1 = String.format("【ReelShort】\n%s", content1);
                content = "<a href=\"" + link + "\">" + content1 + "" + "</a>";
            }
        }
        iWebActivity.toShare(type, content);
    }

    @JavascriptInterface
    public void appJumpTargetVC(final String json) {
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }

        String callbackFunction = jsonObject.get("callbackFunction").getAsString();

        if (jsonObject.has("actionType")) {
            String actionType = jsonObject.get("actionType").getAsString();
            String target = "";
            String pmc = "";
            if (jsonObject.has("target")) {
                target = jsonObject.get("target").getAsString();
            }

            if (jsonObject.has("pmc")) {
                pmc = jsonObject.get("pmc").getAsString();
            }

            TargetBean targetBean = new TargetBean();
            targetBean.setActionType(actionType);
            targetBean.setTarget(target);
            targetBean.setPmc(pmc);

            boolean isSuccess = RouterFactory.jumpToActivity(iWebActivity.getActivity(), targetBean);
            int succ = isSuccess ? 1 : 0;
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + succ + "')");
        } else {
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
        }
    }

    @JavascriptInterface
    public void onAppHaveUserCertifyIdentity(final String json) {
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }
        String callbackFunction = jsonObject.get("callbackFunction").getAsString();
        try {
            if (iWebActivity.getUserInfo() == null) {
                iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
                return;
            }
            boolean isReal = iWebActivity.getUserInfo().isReal();
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + (isReal ? 1 : 0) + "')");
        } catch (Exception e) {
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
        }
    }

    @JavascriptInterface
    public void appOpenUserCertifyIdentity(final String json) {
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }
        String callbackFunction = jsonObject.get("callbackFunction").getAsString();
        try {
            RouterFactory.startRouterActivity(iWebActivity.getActivity(),  "com.yft.user.ActivityUserInformation");
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 1 + "')");
        } catch (Exception e) {
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
        }
    }

    @JavascriptInterface
    public void appOpenLocaltionPay(final String json) {
        JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
        if (jsonObject == null || !jsonObject.has("callbackFunction")) {
            LOGE("WebJsInterface", " appOpenLocaltionShare 没有方法callbackFunction， 参数错误");
            return;
        }
        String callbackFunction = jsonObject.get("callbackFunction").getAsString();
        try {
            JsonObject payContent = jsonObject.get("payContent").getAsJsonObject();
            PayBean payBean = JsonUtil.parseJsonToBean(payContent.toString(), PayBean.class);
            String base64Target = Utils.base64ToPlain(payBean.getTarget());
            payBean.setTarget(base64Target);
            iWebActivity.appOpenLocaltionPay(callbackFunction, payBean);
        }catch (Exception e){
            iWebActivity.getWebView().loadUrl("javascript:" + callbackFunction + "('" + 0 + "')");
        }
    }


    public interface IWebActivity {

        UserInfo getUserInfo();

        String getToken();

        WebView getWebView();

        String getDeviceId();

        // 发起请求
        void request(final String url, int isShowDialog, final String callbackFunction, final String json);

        Activity getActivity();

        // 去登录
        void toLoginActivity(final String callbackFunction);

        // 去图片选择
        void toPhotoSelection(final String callbackFunction, String model);

        // 去选择地址页面
        void toAddressSelection(final String callbackFunction);

        // 去分享
        void toShare(String shareType, String content);

        void appOpenLocaltionPay(final String callbackFunction, final PayBean payBean);

    }
}
