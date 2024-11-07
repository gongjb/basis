package com.fuan.chameleon;


import static com.hkbyte.cnbase.util.Constant.TO_CALCULATOR_STYLE_1;
import static com.hkbyte.cnbase.util.Constant.TO_CALCULATOR_STYLE_2;
import static com.hkbyte.cnbase.util.Constant.TO_MAIL_STYLE;
import static com.yft.zbase.utils.Logger.LOGE;

import android.view.View;

import com.chenenyu.router.annotation.Route;
import com.fuan.chameleon.router.AppRouter;
import com.hkbyte.ad.adstart.AdFactory;
import com.hkbyte.ad.adstart.AdType;
import com.hkbyte.ad.adstart.ISplashAdAdapter;
import com.yft.home.adapterutil.AdapterCreateFactory;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.ButtonUtils;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.utils.UIUtils;

import com.fuan.chameleon.model.WelcomeViewModel;

import com.fuan.chameleon.databinding.ActivityWelcomeLayoutBinding;


@Route(AppRouter.ACTIVITY_WELCOME)
public class WelcomeActivity extends BaseActivity<ActivityWelcomeLayoutBinding, WelcomeViewModel> {
    private static final String TAG = "WelcomeActivity";
    /**
     * 闪屏适配器
     */
    private ISplashAdAdapter mSplashAdAdapter;

    /**
     * 提交dialog
     */
    private SubFragmentDialog mSubFragmentDialog;
    private int mStyle;

    @Override
    public void initView() {
        mStyle = mViewModel.getConfigStyle();
        LOGE("===style=>" + mStyle);
        switch (mStyle) {
            case TO_CALCULATOR_STYLE_1:
                String activityName = RouterFactory.getInstance().getPage("CalculatorActivity");
                RouterFactory.getInstance().startRouterActivity(this, activityName);
                finish();
                return;
            case TO_CALCULATOR_STYLE_2:
                activityName = RouterFactory.getInstance().getPage("Calculator2Activity");
                RouterFactory.getInstance().startRouterActivity(this, activityName);
                finish();
                return;
            default:{
                // do noting
            }
        }
        // 保存当前渠道
        AdapterCreateFactory.getInstance().cleanAllAdapter();
        ErrorCode.finishNotActivity(WelcomeActivity.class.getCanonicalName()); //
        LOGE("WelcomeActivity=>" + this.getClass().getCanonicalName());
        mSubFragmentDialog = SubFragmentDialog.newInstance("正在重新加载，请稍后！");
        mDataBing.setIsShow(false);
        mDataBing.setIsCsjShow(true);
    }

    @Override
    public void initListener() {
        if (mStyle != 0) {
            return;
        }
        mViewModel.getAdBeanMutableLiveData().observe(this, this::acceptAd);
        // 监听广告时长
        mViewModel.getTimeTaskMutableLiveData().observe(this, this::onTaskTime);
        // 跳过
        mDataBing.tvJump.setOnClickListener(this::onJumpClick);
        // 广告点击
        mDataBing.ivUrl.setOnClickListener(this::onAdImageClick);
        // 重新加载按钮
        mDataBing.btnReload.setOnClickListener(view -> {
//            if (!mSubFragmentDialog.isShow()) {
//                mSubFragmentDialog.show(getSupportFragmentManager(), getClass().getCanonicalName());
//            }
//            // 再次请求
//            mViewModel.requestService();
            LOGE("===>>",RouterFactory.getInstance().getPage("MainActivity"));
            RouterFactory.getInstance().startRouterActivity(this, RouterFactory.getInstance().getPage("MainActivity"));
        });

        if (mViewModel.isFirst()) {
            // 首次加载请求服务器地址
            mDataBing.imgLogo.setVisibility(View.VISIBLE);
            mViewModel.requestService();
        } else {
            if (!mViewModel.isOpenAd()) {
                goMainActivity();
                this.finish();
            } else {
                // 播放广告 -- 判断用本地广告还是sdk广告...
                // mViewModel.postAd(); // 本地广告播放方式
                openSdkAd();// 播放开屏广告
            }
        }
    }

    private void openSdkAd() {
        mSplashAdAdapter = AdFactory.getInstance()
                .createSplashAd(AdType.CSJ_SPLASH);

       if (mSplashAdAdapter == null) {
           LOGE(TAG, "找不到对应的广告SDK");
           goMainActivity();
           finish();
           return;
       }
        /**
         * 打开广告
         */
        boolean isOpenSuccess = mSplashAdAdapter.startSplashAd(this, AdType.CSJ_SPLASH.getAdId() , new ISplashAdAdapter.IOpenViewListener() {

            @Override
            public void openAdView(View view) {
                // 渲染成功
                mDataBing.imgLogo.setVisibility(View.GONE);
                mDataBing.setIsShow(false);
                mDataBing.setIsCsjShow(true);
                mDataBing.flGroup.removeAllViews();
                mDataBing.flGroup.addView(view);
            }

            @Override
            public void onSplashLoadFail(String msg, int code) {
                goMainActivity();
                finish();
            }

            @Override
            public void onSplashRenderFail(View view, String msg, int code) {
                goMainActivity();
                finish();
            }

            @Override
            public void onSplashAdClose(int code) {
                goMainActivity();
                finish();
            }
        });

        if (!isOpenSuccess) {
            goMainActivity();
            finish();
        }
    }

    @Override
    public void initData() {
        if (mStyle != 0) {
            return;
        }
    }

    public void onTaskTime(Integer val) {
        // 接收到广告. 开始启动倒计时
        if (!mDataBing.getIsShow()) {
            mDataBing.setIsShow(true);
        }
        if (mDataBing.imgLogo.getVisibility() == View.VISIBLE) {
            // 显示中就隐藏logo
            mDataBing.imgLogo.setVisibility(View.GONE);
        }
        mDataBing.tvJump.setText(String.format("跳过 %s", val + ""));
        if (val == 0) {
            goMainActivity();
            this.finish();
        }
    }


    public void acceptAd(TargetBean adBean) {
        if (adBean != null) {
            // 显示广告图片
            UIUtils.setImgUrl(mDataBing.ivUrl, adBean.getImage());
        } else {
            // 没有广告--
            goMainActivity();
            this.finish();
        }
    }

    /**
     * 用户点击了跳过
     *
     * @param view
     */
    public void onJumpClick(View view) {
        mViewModel.stopTask();
        if(ButtonUtils.isFastDoubleClick(view.getId())) {
            return;
        }
        goMainActivity();
        this.finish();
    }

    public void onAdImageClick(View view) {
        // 点击广告图片
        mViewModel.stopTask();
        if(ButtonUtils.isFastDoubleClick(view.getId())) {
            return;
        }
        goMainActivity();
//        TargetBean targetBean = new TargetBean();
//        targetBean.setActionType(RouterFactory.JUMP_INNER_MODULE);
//        targetBean.setTarget(RouterFactory.TO_HOME_PAGE);
        RouterFactory.getInstance().jumpToActivity(this, mViewModel.getTargetBean());
        this.finish();
    }

    @Override
    protected void handleServiceAddress(ServiceBean serviceBean) {
        if (mSubFragmentDialog != null && mSubFragmentDialog.isShow()) {
            mSubFragmentDialog.dismiss();
        }
        if (serviceBean == null || !serviceBean.isSuss()) {
            // 失败-- 显示重新加载的按钮
            mDataBing.btnReload.setVisibility(View.VISIBLE);
        } else {
            goMainActivity();
            this.finish();
        }
    }


    private void goMainActivity() {
        RouterFactory.getInstance().startRouterActivity(this, RouterFactory.getInstance().getPage("MainActivity"));
    }

    @Override
    protected void onDestroy() {
        if (mSplashAdAdapter != null) {
            // 销毁广告
            mSplashAdAdapter.onClean();
        }
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_welcome_layout;
    }
}
