package com.yft.user;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.chenenyu.router.annotation.Route;
import com.yft.user.databinding.ActivityAboutLayoutBinding;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.BuildConfig;
import com.yft.zbase.utils.ClipboardHelper;
import com.yft.zbase.utils.ToastUtils;

@Route(UserRouter.ACTIVITY_ABOUT)
public class AboutActivity extends BaseActivity<ActivityAboutLayoutBinding, BaseViewModel> {
    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.loginBackground.setBackground(grad);
        mDataBing.setVersion("v" + BuildConfig.versionName);
        mDataBing.setCoop(mViewModel.getUserServer().getServiceUrl());
        mDataBing.tlt.setTitle("关于我们");
        mDataBing.tlt.setLeftBackImage();
    }

    @Override
    public void initListener() {
        mDataBing.llA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardHelper.getInstance(AboutActivity.this).copyText(mViewModel.getUserServer().getServiceUrl().getCustomerServiceEmail());
                ToastUtils.toast("邮箱地址已复制到剪切板");
            }
        });

        mDataBing.llB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardHelper.getInstance(AboutActivity.this).copyText(mViewModel.getUserServer().getServiceUrl().getCustomerServiceNumber());
                ToastUtils.toast("电话号码已复制到剪切板");
            }
        });

        mDataBing.tvXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle build = new Bundle();
                build.putString("title", "用户协议");
                build.putString("url", mViewModel.getUserServer().getServiceUrl().getUserAgreementUrl());
                //RouterFactory.ACTIVITY_WEB
                RouterFactory.startRouterBundleActivity(AboutActivity.this,
                        RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }
        });

        mDataBing.tvZhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle build = new Bundle();
                build.putString("title", "隐私政策");
                build.putString("url", mViewModel.getUserServer().getServiceUrl().getPrivacyAgreementUrl());
                RouterFactory.startRouterBundleActivity(AboutActivity.this,
                        RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_about_layout;
    }
}
