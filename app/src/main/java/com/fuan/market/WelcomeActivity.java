package com.fuan.market;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.text.TextUtils;
import android.view.View;

import com.chenenyu.router.annotation.Route;
import com.fuan.market.router.AppRouter;
import com.yft.home.adapterutil.AdapterCreateFactory;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.bean.KVBean;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.ButtonUtils;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.utils.UIUtils;

import com.fuan.market.model.WelcomeViewModel;

import com.fuan.market.databinding.ActivityWelcomeLayoutBinding;

import com.fuan.market.R;


@Route(AppRouter.ACTIVITY_WELCOME)
public class WelcomeActivity extends BaseActivity<ActivityWelcomeLayoutBinding, WelcomeViewModel> {
    /**
     * 提交dialog
     */
    private SubFragmentDialog mSubFragmentDialog;

    @Override
    public void initView() {
        // 保存当前渠道
        mViewModel.saveFlavor(BuildConfig.FLAVOR);
        AdapterCreateFactory.getInstance().cleanAllAdapter();
        ErrorCode.finishNotActivity(WelcomeActivity.class.getCanonicalName()); //
        LOGE("WelcomeActivity=>" + this.getClass().getCanonicalName());
        mSubFragmentDialog = SubFragmentDialog.newInstance("正在重新加载，请稍后！");
        mDataBing.setIsShow(false);
    }

    @Override
    public void initListener() {
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
            LOGE("===>>",RouterFactory.getPage("MainActivity"));
            RouterFactory.startRouterActivity(this, RouterFactory.getPage("MainActivity"));
        });

        if (mViewModel.isFirst()) {
            // 首次加载请求服务器地址
            mViewModel.requestService();
        } else {
            if (!mViewModel.isOpenAd()) {
                goMainActivity();
                this.finish();
            } else {
                // 播放广告
                mViewModel.postAd();
            }
        }
    }

    @Override
    public void initData() {

    }

    public void onTaskTime(Integer val) {
        // 接收到广告. 开始启动倒计时
        if (!mDataBing.getIsShow()) {
            mDataBing.setIsShow(true);
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
        RouterFactory.jumpToActivity(this, mViewModel.getTargetBean());
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
        RouterFactory.startRouterActivity(this, RouterFactory.getPage("MainActivity"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_welcome_layout;
    }
}
