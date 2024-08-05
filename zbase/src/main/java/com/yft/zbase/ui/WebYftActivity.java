package com.yft.zbase.ui;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.RouteStatus;
import com.chenenyu.router.annotation.Route;
import com.jaeger.library.StatusBarUtil;
import com.yft.zbase.R;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseModel;
import com.yft.zbase.base.BasePhotoActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.bean.KVBean;
import com.yft.zbase.bean.PayBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.bean.WeChatPayParams;
import com.yft.zbase.databinding.ActivityWebviewLayoutBinding;
import com.yft.zbase.error.XNetSystemErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IDevice;
import com.yft.zbase.server.IPay;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IShare;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.server.ManageThreadPoolService;
import com.yft.zbase.utils.JsonUtil;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Route(RouterFactory.ACTIVITY_WEB)
public class WebYftActivity extends BasePhotoActivity<ActivityWebviewLayoutBinding, WebViewModel> implements WebJsInterface.IWebActivity {
    private SubFragmentDialog subFragmentView;
    // 通知视图控制
    private NoticeControl mNoticeControl;
    //
    private DialogPhotoFragment mDialogPhotoFragment;
    // 方法名称
    private volatile String function;
    private volatile String module;
    // share
    private IShare mShare;
    private boolean isWebTitle;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.rlMain.setBackground(grad);
        mShare = DynamicMarketManage.getInstance().getServer(IServerAgent.SHARE_SERVER);
        mDialogPhotoFragment = DialogPhotoFragment.newInstance();
        subFragmentView = SubFragmentDialog.newInstance("");
        mNoticeControl = new NoticeControl();
        setting(mDataBing.wbView);
        isWebTitle = getIntent().getBooleanExtra("isWebTitle", true);
        String title = getIntent().getStringExtra("title");
        final String url = getIntent().getStringExtra("url");

        mDataBing.tlt.setTitle(title);
        //
        mDataBing.tlt.setTopViewHide(View.GONE);

        mDataBing.tlt.setRightText(mViewModel.getString("关闭"), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebYftActivity.this.finish();
            }
        });
        mDataBing.tlt.setLeftBackImage(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataBing.wbView.canGoBack()) {
                    mDataBing.wbView.goBack();
                } else {
                    WebYftActivity.this.finish();
                }
            }
        });
        mDataBing.wbView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // 在此处进行跳转URL的处理, 一般情况下_black需要重新打开一个页面, 这里我直接让当前的webview重新load了url
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }



            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;
                // 创建一个文件选择的Intent
                String[] types = fileChooserParams.getAcceptTypes();

                if (types == null) {
                    return false;
                }

                for (String type : types) {
                    if (type.contains("image")) {
                        toPhotoSelection("", "");
                        return true;
                    }
                }

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, 2);
                } catch (Exception e) {
                    mFilePathCallback = null;
                    return false;
                }
                return true;
            }

//            @Override
//            public void onPermissionRequest(PermissionRequest request) {
//                super.onPermissionRequest(request);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    request.grant(request.getResources());
//                    request.getOrigin();
//                }
//            }
//
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                result.confirm();
//                return true;
//            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!isWebTitle) {
                    // 不需要跟随系统web的title
                    return;
                }

                if (title != null && !TextUtils.isEmpty(view.getUrl())
                        && !view.getUrl().contains(title)) {
                    // 设置页面title
                    mDataBing.tlt.setTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                synchronized (this) {
                    // 网页加载到百分之70的时候，就算加载完成
                    if (newProgress > 70) {
                        if (subFragmentView.isShow()) {
                            ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
                        }
                    }
                }
            }
        });

        if (!TextUtils.isEmpty(url)) {
            mDataBing.wbView.loadUrl(url);
        }

        if (!Utils.isConnected(this)) {
            // 网络错误
            mNoticeControl.showNoticeState(mDataBing.notice, mDataBing.wbView, NoticeControl.NoticeState.ERROR_NETWORK_RELOAD, new NoticeControl.OnNoticeClickListener() {
                @Override
                public void onClickNoticeView(NoticeView view, NoticeControl.NoticeState noticeState) {
                    if (Utils.isConnected(WebYftActivity.this)) {
                        view.setVisibility(View.GONE);
                        mDataBing.wbView.setVisibility(View.VISIBLE);
                        mDataBing.wbView.reload();
                    }
                }
            });
        } else {
            if (TextUtils.isEmpty(url)) {
                // 提示用户当前页面网址是错误的
                mNoticeControl.showNoticeState(mDataBing.notice, mDataBing.wbView, NoticeControl.NoticeState.ERROR_URL);
            }
        }
    }


    @Override
    public void initListener() {
        mViewModel.getKvBeanMutableLiveData().observe(this, this::onRequestAny);
        mViewModel.getErrorMutableLiveData().observe(this, this::onError);
        mViewModel.getSuccessMutableLiveData().observe(this, this::onSuccess);
        mDialogPhotoFragment.setOnPhotoSelectListener(new DialogPhotoFragment.OnPhotoSelectListener() {
            @Override
            public void onPhotoSelect(int type) {
                if (type == DialogPhotoFragment.TYPE_CAMERA) {
                    thisRequest = CAMERA_REQUEST_CODE;
                    requestPermissionV2(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            openCamera();
                        }
                    });
                } else {
                    thisRequest = ALBUM_REQUEST_CODE;
                    requestPermissionV2(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            openAlbum();
                        }
                    });
                }
            }

            @Override
            public void onDismiss(boolean isDes) {
                if (isDes && mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                }
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_webview_layout;
    }

    @Override
    public void handlerPhoto(int type, String url) {
        if (mFilePathCallback != null) {
            if (subFragmentView != null && subFragmentView.isShow()) {
                ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
            }

            Uri[] results = new Uri[]{Uri.parse("file:///" + url)};
            // 传递选择的文件给WebView
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            if (subFragmentView != null && !subFragmentView.isShow()) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.show(fragmentManager, "handlerPhoto"));
            }
            final String m = module;
            mViewModel.updateImageFile(url, m);
        }
    }


    public void onError(String tag) {
        if (subFragmentView != null && subFragmentView.isShow()) {
            ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
        }
        String f = function;
        String error = "{\"code\":\"-446\",\"msg\":\"上传失败\"}";
        getWebView().loadUrl("javascript:" + f + "('" + error + "')");
        function = "";
        module = "";
    }

    public void onSuccess(String data) {
        // 拿到图片
        if (subFragmentView != null && subFragmentView.isShow()) {
            ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
        }
        if (TextUtils.isEmpty(function)) {
            LOGE("=======>>>" + function);
            return;
        }
        final String f = function;
        getWebView().loadUrl("javascript:" + f + "('" + data + "')");
        function = "";
        module = "";
    }

    @Override
    public void selectPhotoError() {
        // 拿到图片
        if (TextUtils.isEmpty(function)) {
            LOGE("=======>>>" + function);
            return;
        }
        final String f = function;
        Map<String, String> map = new HashMap<>();
        map.put("code", "404");
        map.put("msg", "图片获取失败");
        String json = JsonUtil.getMapToString(map);
        getWebView().loadUrl("javascript:" + f + "('" + json + "')");
        function = "";
        module = "";
    }

    @Override
    public String permissionDescription() {
        return LanguageManage.getString("使用该功能拍摄或存储、读取相册的照片，以便于您完成上传图片、发表购物评价的功能，请您确认授权，否则无法使用该功能。");
    }

    @Override
    public String permissionTitle() {
        return LanguageManage.getString("拍摄、存储权限说明");
    }


    private void setting(WebView dWebView) {
        /*
            在Android 4.4 以下的系统中存在一共三个有远程代码执行漏洞的隐藏接口。
            分别是位于android/webkit/webview中的“searchBoxJavaBridge”
            接口和android/webkit/AccessibilityInjector.java中的“accessibility”
            接口和“accessibilityTraversal”接口
            。调用此三个接口的APP在开启辅助功能选项中第三方服务的Android系统上将面临远程代码执行漏洞。
         */
        dWebView.removeJavascriptInterface("searchBoxJavaBridge");
        dWebView.removeJavascriptInterface("accessibility");
        dWebView.removeJavascriptInterface("accessibilityTraversal");


        dWebView.setWebViewClient(new IWebViewClient());
        WebSettings webSetting = dWebView.getSettings();

        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccessFromFileURLs(true);

        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//这句很重要
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//
        dWebView.addJavascriptInterface(new WebJsInterface(this), "JSQTWeb");
    }

    @Override
    public UserInfo getUserInfo() {
        if (mViewModel != null) {
            if (mViewModel.isLogin()) {
                return mViewModel.getUserInfo();
            }
            return null;
        }
        return null;
    }

    @Override
    public String getToken() {
        if (mViewModel != null) {
            if (mViewModel.isLogin()) {
                return mViewModel.getUserInfo().getToken();
            }
        }
        return "";
    }

    @Override
    public WebView getWebView() {
        return mDataBing.wbView;
    }

    @Override
    public String getDeviceId() {
        return mViewModel.getDeviceId();
    }

    @Override
    public void request(String url, int isShowDialog, String callbackFunction, String json) {
        if (isShowDialog == 1) {
            // 需要显示dialog
            if (!subFragmentView.isShow()) {
                ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.show(getSupportFragmentManager(), "request"));
            }
        }

        mViewModel.requestAny(url, callbackFunction, json);
    }


    public void onRequestAny(KVBean kvBean) {
        if (subFragmentView.isShow()) {
            ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
        }

        String json = (String) kvBean.value;
        getWebView().loadUrl("javascript:" + kvBean.value2 + "('" + json + "')");
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void toLoginActivity(final String callbackFunction) {
        Bundle bundle = new Bundle();
        bundle.putString("function", callbackFunction);
        RouterFactory.startRouterRequestActivity(WebYftActivity.this, RouterFactory.ACTIVITY_USER_LOGIN, 10000, bundle, new RouteCallback() {
            @Override
            public void callback(RouteStatus status, Uri uri, String message) {
            }
        });
    }

    @Override
    public void toPhotoSelection(String callbackFunction, String module) {
        function = callbackFunction;
        this.module = module;
        if (!mDialogPhotoFragment.isShow()) {
            mDialogPhotoFragment.show(getSupportFragmentManager(), getClass().getCanonicalName());
        }
    }

    @Override
    public void toAddressSelection(String callbackFunction) {
        Bundle bundle = new Bundle();
        bundle.putString("function", callbackFunction);
        RouterFactory.startRouterRequestActivity(WebYftActivity.this, RouterFactory.ACTIVITY_USER_SITE, 10002, bundle, new RouteCallback() {
            @Override
            public void callback(RouteStatus status, Uri uri, String message) {
            }
        });
    }

    @Override
    public void toShare(String shareType, String content) {
        if ("0".equals(shareType)) {
            mShare.openShareText(this, content);
        } else {
            mShare.openShareImage(this, mDataBing.flWeb);
        }
    }

    @Override
    public void appOpenLocaltionPay(final String callbackFunction, final PayBean payBean) {
        if ("APP".equalsIgnoreCase(payBean.getTradeType())) {
            // 原生支付
            if ("ALP".equalsIgnoreCase(payBean.getPayChannel())) {
                // 吊起支付宝支付
                mViewModel.getPay().aliPay(payBean.getTarget(), this, new IPay.OnCallbackPayListener() {
                    @Override
                    public void onPayCallback(int type, String code, String msg) {
                        // do noting
                    }

                    @Override
                    public void onPayCallback(String result) {
                        ManageThreadPoolService.getInstance().executeOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (WebYftActivity.this.isFinishing()) {
                                    return;
                                }
                                getWebView().loadUrl("javascript:" + callbackFunction + "('" + result + "')");
                            }
                        });
                    }
                });
            } else if ("WXP".equalsIgnoreCase(payBean.getPayChannel())) {
                WeChatPayParams weChatPayParams = Utils.parseParams(payBean.getTarget());
                mViewModel.getPay().addOnCallbackPayListener("WebYftActivity", new IPay.OnCallbackPayListener() {
                    @Override
                    public void onPayCallback(int type, String code, String msg) {
                        // do noting
                    }

                    @Override
                    public void onPayCallback(String result) {
                        ManageThreadPoolService.getInstance().executeOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (WebYftActivity.this.isFinishing()) {
                                    return;
                                }
                                getWebView().loadUrl("javascript:" + callbackFunction + "('" + result + "')");
                            }
                        });
                    }
                });
                mViewModel.getPay().WXApiPay(this, weChatPayParams);
            } else {
                ToastUtils.toast("未接入该支付渠道，具体可联系客服！");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            if (resultCode == 100) {
                // 登录成功
                String function = data.getStringExtra("function");
                UserInfo userInfo = data.getParcelableExtra("userData");
                if (userInfo != null) {
                    String json = JsonUtil.parseBeanToJson(userInfo);
                    getWebView().loadUrl("javascript:" + function + "('" + json + "')");
                }
            } else if (resultCode == 101) {
                String function = data.getStringExtra("function");
                String error = "{\"code\":\"-446\",\"msg\":\"网络异常！\"}";
                getWebView().loadUrl("javascript:" + function + "('" + error + "')");
            }
        } else if (requestCode == 10002 && resultCode == 10001) {
            String function = data.getStringExtra("function");
            AddressBean addressBean = data.getParcelableExtra("address");
            if (addressBean != null) {
                String json = JsonUtil.parseBeanToJson(addressBean);
                getWebView().loadUrl("javascript:" + function + "('" + json + "')");
            }
        } else if (requestCode == 2) {
            if (mFilePathCallback == null) {
                return;
            }
            Uri[] results = null;
            // 检查选择的文件是否为空
            if (resultCode == RESULT_OK && data != null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
            // 传递选择的文件给WebView
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }


    public class IWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!subFragmentView.isShow()) {
                ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.show(getSupportFragmentManager(), subFragmentView.getClass().getSimpleName()));
            }
        }

        @Override
        public void onReceivedError(final WebView webview, int errorCode, String description, String failingUrl) {
            super.onReceivedError(webview, errorCode, description, failingUrl);
            if (!isFinishing()) {
                mNoticeControl.showNoticeState(mDataBing.notice, mDataBing.wbView, NoticeControl.NoticeState.ERROR_PAGE_RELOAD, new NoticeControl.OnNoticeClickListener() {
                    @Override
                    public void onClickNoticeView(NoticeView view, NoticeControl.NoticeState noticeState) {
                        view.setVisibility(View.GONE);
                        webview.setVisibility(View.VISIBLE);
                        mDataBing.wbView.reload();
                    }
                });
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            runOnUiThread(() -> {
                LOGE("=====>> 执行3");
                if (!isFinishing() && subFragmentView.isShow()) {
                    ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
                }
            });
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LOGE("-------" + view.getTitle());
            if (url.startsWith("http") || url.startsWith("https")) {
                return super.shouldOverrideUrlLoading(view, url);
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    if (subFragmentView.isShow()) {
                        ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
                    }
                    return true;
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    if (subFragmentView.isShow()) {
                        ManageThreadPoolService.getInstance().executeOnUiThread(() -> subFragmentView.dismiss());
                    }
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDataBing.wbView.canGoBack()) {
                mDataBing.wbView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mDataBing.wbView.setWebViewClient(null);
        mDataBing.wbView.setWebChromeClient(null);
        mDataBing.wbView.removeJavascriptInterface("JSQTWeb");
        mDataBing.wbView.removeAllViews();
        mViewModel.getPay().removeOnCallbackPayListener("WebYftActivity");
        super.onDestroy();
    }
}
