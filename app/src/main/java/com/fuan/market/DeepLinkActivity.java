package com.fuan.market;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;
import com.yft.zbase.utils.JsonUtil;
import com.yft.zbase.utils.Utils;

import java.util.List;

import com.fuan.market.model.WelcomeViewModel;

import com.fuan.market.R;
import com.fuan.market.databinding.ActivityWelcomeLayoutBinding;

/**
 * 最主要做一个分发功能
 */
public class DeepLinkActivity extends BaseActivity<ActivityWelcomeLayoutBinding, WelcomeViewModel> {
    // web页面的地址
    private static final String ADDRESS_TAG = "address";
    // 商品详情的页面
    private static final String DETAILS = "productDetails";
    // 产品id
    private static final String productId = "productId";
    // 推荐吗
    private static final String pmc = "pmc";
    // 携带的参数 json格式的
    private static final String BODY_CONTENT = "json";
    // web页面的title
    private static final String BODY_TITLE = "title";
    // web页面跳转的url
    private static final String BODY_URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        try {
            String scheme = data.getScheme();
            if (!TextUtils.isEmpty(scheme)) {
                List<String> params = data.getPathSegments();
                if (Utils.isCollectionEmpty(params)) {
                    RouterFactory.startRouterActivity(this, "com.fuan.market.WelcomeActivity");
                    this.finish();
                    return;
                }
                // 从网页传过来的数据
                String testId = params.get(0);
                if (TextUtils.isEmpty(testId)) {
                    // 无法匹配路径
                    RouterFactory.startRouterActivity(this, "com.fuan.market.WelcomeActivity");
                    this.finish();
                    return;
                }
                // 跳商品详情
                String json = data.getQueryParameter(BODY_CONTENT);
                JsonObject jsonObject = JsonUtil.parseJsonToJsonObj(json);
                if (jsonObject == null) {
                    // 无法获取关键参数
                    RouterFactory.startRouterActivity(this, "com.fuan.market.WelcomeActivity");
                    this.finish();
                    return;
                }
                if (ADDRESS_TAG.equals(testId)) {
                    if (jsonObject.has(BODY_TITLE) && jsonObject.has(BODY_URL)) {
                        // title 和 url 必不可少的
//                        Bundle bundle = new Bundle();
//                        bundle.putString(BODY_TITLE,jsonObject.get(BODY_TITLE).getAsString());
//                        bundle.putString(BODY_URL,jsonObject.get(BODY_URL).getAsString());
//                        // 跳转至webview
//                        RouterFactory.startRouterBundleActivity(this , RouterFactory.ACTIVITY_WEB, bundle);
                        TargetBean targetBean = new TargetBean();
                        targetBean.setTarget(jsonObject.get(BODY_URL).getAsString());
                        targetBean.setActionType(RouterFactory.JUMP_LINK_MODULE);
                        RouterFactory.jumpToActivity(this, targetBean);
                        this.finish();
                    } else {
                        RouterFactory.startRouterActivity(this, "com.fuan.market.WelcomeActivity");
                        this.finish();
                    }
                } else if (DETAILS.equals(testId)) {
                    String id = jsonObject.get(productId).getAsString();
                    TargetBean targetBean = new TargetBean();
                    if(jsonObject.has("pmc")) {
                        targetBean.setPmc(jsonObject.get(pmc).getAsString());
                    }
                    targetBean.setTarget(id);
                    targetBean.setActionType(RouterFactory.JUMP_GOODS_DETAIL_MODULE);
                    RouterFactory.jumpToActivity(this, targetBean);
                    finish();
                }
            } else {
                RouterFactory.startRouterActivity(this, "com.fuan.market.WelcomeActivity");
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.finish();
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_welcome_layout;
    }
}
