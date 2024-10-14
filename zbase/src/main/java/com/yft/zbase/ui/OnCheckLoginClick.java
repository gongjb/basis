package com.yft.zbase.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IUser;

/**
 * 所有需要检测登录的按钮，都需要绑定此类
 */
public abstract class OnCheckLoginClick implements View.OnClickListener {

    private IUser mUser;
    private String mPmc;

    public OnCheckLoginClick() {
        mUser = DynamicMarketManage.getInstance().getServer(IServerAgent.USER_SERVER);
    }

    public OnCheckLoginClick(String pmc) {
        this();
        this.mPmc = pmc;
    }

    public abstract void onCheckClick(View view);

    public void onClick(View view) {
        if (ButtonUtils.isFastDoubleClick(view.getId())) {
            return;
        }
        if (isCheckLogin()) {
            // 登录成功
            onCheckClick(view);
            return;
        }

        if (TextUtils.isEmpty(mPmc)) {
            RouterFactory.getInstance().startRouterActivity(view.getContext(), RouterFactory.getInstance().getPage("LoginActivity"));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("pmc", mPmc);
            RouterFactory.getInstance().startRouterBundleActivity(view.getContext(), RouterFactory.getInstance().getPage("LoginActivity"), bundle);
        }
    }

    public boolean isCheckLogin() {
        return mUser.isLogin();
    }
}
