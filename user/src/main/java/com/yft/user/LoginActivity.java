package com.yft.user;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;


import com.chenenyu.router.annotation.Route;
import com.yft.user.databinding.ActivityLoginActivityBinding;
import com.yft.user.model.LoginViewModel;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.ButtonUtils;
import com.yft.zbase.ui.OnZoomClickListener;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

@Route(UserRouter.ACTIVITY_USER_LOGIN)
public class LoginActivity extends BaseActivity<ActivityLoginActivityBinding, LoginViewModel> {
    private int mGroupHigh;
    private int mGroupWidth;
    // 该参数携带值，代表是来请求登录的
    private String mCallbackFunction;
    private SubFragmentDialog mDialog;
    private String pmc;

    @Override
    public void initView() {
        mDialog = SubFragmentDialog.newInstance(mViewModel.getString("正在请求，请稍后···"));
        mCallbackFunction = getIntent().getStringExtra("function");
        mGroupHigh = Utils.dip2px(this, 44);

        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.loginBackground.setBackground(grad);
        pmc = getIntent().getStringExtra("pmc");

        OnZoomClickListener onZoomClickListener = new OnZoomClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSel = (boolean) mDataBing.ivIcc.getTag();
                if (!isSel) {
                    UIUtils.showToast(mViewModel.getString("请阅读用户服务协议与隐私协议"));
                    return;
                }

                if (!isLoginCheck()) {
                    return;
                }
                mDialog.show(getSupportFragmentManager(), "mLogin");
                String phone = mDataBing.edPhone.getText().toString();
                String checkNumber = mDataBing.edCheck.getText().toString();
                // 请求登录
                mViewModel.requestLogin(phone, checkNumber);
            }
        };

        OnZoomClickListener onZoomClickListener1 = new OnZoomClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSel = (boolean) mDataBing.ivIcc.getTag();
                if (!isSel) {
                    UIUtils.showToast(mViewModel.getString("请阅读用户服务协议与隐私协议"));
                    return;
                }
                boolean isReg = isRegCheck();
                if (isReg) {
                    mDialog.show(getSupportFragmentManager(), "mReg");
                    String phone = mDataBing.edPhone.getText().toString();
                    String code = mDataBing.edCheck.getText().toString();
                    String invitationCode = mDataBing.edInv.getText().toString();
                    if (TextUtils.isEmpty(invitationCode)) {
                        // 不选默认填8个7
                        invitationCode = "77777777";
                    }
                    mViewModel.requestReg(phone, code, invitationCode);
                }
            }
        };
        mDataBing.rlLgoin.setOnClickListener(onZoomClickListener);
        mDataBing.rlLgoin.setOnTouchListener(onZoomClickListener);

        mDataBing.rlRegister.setOnClickListener(onZoomClickListener1);
        mDataBing.rlRegister.setOnTouchListener(onZoomClickListener1);

        ViewGroup.LayoutParams layoutParams = mDataBing.rlInv.getLayoutParams();
        layoutParams.height = 1;
        layoutParams.width = 1;
        mDataBing.rlInv.setLayoutParams(layoutParams);

        mDataBing.tvRegister.setTag(false); // 是注册
        mDataBing.tvRegister.setOnClickListener(view -> {
            // 前一个状态
            boolean isRegister = (Boolean) mDataBing.tvRegister.getTag();
            mDataBing.tvRegister.setText(isRegister ? mViewModel.getString("立即注册") : mViewModel.getString("立即登录"));
            mDataBing.edInv.setVisibility(isRegister ? View.GONE : View.VISIBLE);
            mDataBing.tvRegister.setTag(!isRegister);

            if (isRegister) {
                // 改变为登录状态
                createInvAnimator(true, 1, 0);
                mDataBing.rlRegister.setVisibility(View.INVISIBLE);
                mDataBing.rlLgoin.setVisibility(View.VISIBLE);
            } else {
                createInvAnimator(false, 0, 1);
                mDataBing.rlRegister.setVisibility(View.VISIBLE);
                mDataBing.rlLgoin.setVisibility(View.INVISIBLE);
            }
        });

        mDataBing.rlLgoin.post(() -> mGroupWidth = mDataBing.rlLgoin.getWidth());
    }

    public boolean isLoginCheck() {
        String phone = mDataBing.edPhone.getText().toString();
        String checkNumber = mDataBing.edCheck.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(mViewModel.getString("请输入电话号码或者邮箱！"));
            return false;
        }

        if (!phone.contains("@")) {
            if (!phone.startsWith("1") || phone.length() != 11) {
                UIUtils.showToast(mViewModel.getString("请填写正确的电话号码！"));
                return false;
            }
        }

        if (TextUtils.isEmpty(checkNumber)) {
            UIUtils.showToast(mViewModel.getString("请填写验证码！"));
            return false;
        }

        if (checkNumber.length() < 4 || checkNumber.length() > 8) {
            UIUtils.showToast(mViewModel.getString("请填写正确的验证码！"));
            return false;
        }
        return true;
    }

    public boolean isRegCheck() {
        boolean reg = isLoginCheck();
        if (reg) {
            return true;
        } else {
            return false;
        }
    }

    public void createInvAnimator(boolean isLogin, float a, float b) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDataBing.rlInv, "alpha", a, b);


        ValueAnimator animatort = ValueAnimator.ofFloat(a, b);
        animatort.addUpdateListener(animation -> {
            if (isFinishing()) {
                return;
            }
            float progress = (float) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = mDataBing.rlInv.getLayoutParams();
            int high = (int) (mGroupHigh * progress);
            int width = (int) (mGroupWidth * progress);
            if (high <= 0) {
                high = 1;
            }
            if (width <= 0) {
                width = 1;
            }
            layoutParams.height = high;
            layoutParams.width = width;
            mDataBing.rlInv.setLayoutParams(layoutParams);
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animatort);
        set.start();

//        set = new AnimatorSet();
//        set.setDuration(1000);
//        set.playTogether(animators, objectAnimator);
//        set.start();
    }

    public void onError(String tag) {
        if (mDialog != null && mDialog.isShow()) {
            // 关闭dialog
            mDialog.dismiss();
        }
    }

    @Override
    public void initListener() {
        mViewModel.getTimeMutableLiveData().observe(this, this::checkNumber);
        mViewModel.getErrorMutableLiveData().observe(this, this::onError);
        mDataBing.ivIcc.setTag(false);
        mDataBing.ivIcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSel = (boolean) mDataBing.ivIcc.getTag();
                if (!isSel) {
                    mDataBing.ivIcc.setImageResource(com.yft.zbase.R.mipmap.icon_in);
                } else {
                    mDataBing.ivIcc.setImageResource(com.yft.zbase.R.mipmap.icon_on);
                }
                mDataBing.ivIcc.setTag(!isSel);
            }
        });

        mDataBing.tvCheck.setOnClickListener(view -> {
            if (ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                return;
            }
            String phone = mDataBing.edPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                //请输入电话号码或者邮箱！
                UIUtils.showToast(mViewModel.getString("请输入电话号码或者邮箱！"));
                return;
            }

            if (!phone.contains("@")) {
                if (!phone.startsWith("1") || phone.length() != 11) {
                    UIUtils.showToast(mViewModel.getString("请填写正确的电话号码！"));
                    return;
                }
            }


            if (!mViewModel.isStart()) {
                boolean isRegister = (Boolean) mDataBing.tvRegister.getTag();
                mViewModel.requestCheckNum(isRegister ? "reg" : "login",phone);
            } else {
                UIUtils.showToast(mViewModel.getString("正在获取验证码，请稍等"));
            }
        });

        mViewModel.getUserInfoMutableLiveData().observe(this, this::userInfo);

        if (!TextUtils.isEmpty(pmc)) {
            mDataBing.edInv.setText(pmc);
            mDataBing.tvRegister.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }
                    // 触发点击
                    mDataBing.tvRegister.performClick();
                }
            },1000);
        }

        mDataBing.tvXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle build = new Bundle();
                build.putString("title", mViewModel.getString("用户协议"));
                build.putBoolean("isWebTitle", false);
                build.putString("url", mViewModel.getUserServer().getServiceUrl().getUserAgreementUrl());
                RouterFactory.getInstance().startRouterBundleActivity(LoginActivity.this, RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }
        });

        mDataBing.tvYinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle build = new Bundle();
                build.putString("title", mViewModel.getString("隐私政策"));
                build.putBoolean("isWebTitle", false);
                build.putString("url", mViewModel.getUserServer().getServiceUrl().getPrivacyAgreementUrl());
                RouterFactory.getInstance().startRouterBundleActivity(LoginActivity.this, RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }
        });
    }

    public void userInfo(UserInfo userInfo) {
        if (userInfo != null) {
            ToastUtils.toast(mViewModel.getString("登录成功"));
        }

        if (!TextUtils.isEmpty(mCallbackFunction)) {
            Intent intent = new Intent();
            intent.putExtra("function", mCallbackFunction);
            intent.putExtra("userData", userInfo);
            this.setResult(100, intent);
            this.finish();
        } else {
            // 登录成功
            this.finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(mCallbackFunction)) {
                Intent intent = new Intent();
                intent.putExtra("function", mCallbackFunction);
                this.setResult(101, intent);
                this.finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void checkNumber(Integer count) {
        if (count > 0) {
            mDataBing.tvCheck.setText(count + "s");
        } else if (count <= 0) {
            mDataBing.tvCheck.setText(mViewModel.getString("获取验证码"));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_login_activity;
    }
}
