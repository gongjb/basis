package com.yft.home;

import android.content.Intent;

import com.chenenyu.router.annotation.Route;
import com.yft.home.databinding.ActivityRouterTestBinding;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;

@Route(RouterFactory.ROUTER_TEST)
public class RouterTestActivity extends BaseActivity<ActivityRouterTestBinding, BaseViewModel> {
    @Override
    public void initView() {
        String key = getIntent().getStringExtra("key");
        mDataBing.tvRouter.setText(key);
        mDataBing.tvRouter.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("key", "你好");
            this.setResult(100, intent);
            this.finish();
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_router_test;
    }
}