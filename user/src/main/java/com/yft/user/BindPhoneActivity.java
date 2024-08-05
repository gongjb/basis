package com.yft.user;

import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;

import com.chenenyu.router.annotation.Route;
import com.yft.user.databinding.ActivityBindPhoneLayoutBinding;
import com.yft.user.model.LoginViewModel;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.OnZoomClickListener;
import com.yft.zbase.utils.UIUtils;

@Route(RouterFactory.ACTIVITY_BIND_PHONE)
public class BindPhoneActivity extends BaseActivity<ActivityBindPhoneLayoutBinding, LoginViewModel> {

    @Override
    public void initView() {
        mDataBing.tlt.setTitle("更换手机号");
        mDataBing.tlt.setLeftBackImage();
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.loginBackground.setBackground(grad);

    }

    public boolean isLoginCheck() {
        String phone = mDataBing.edPhone.getText().toString();
        String checkNumber = mDataBing.edCheck.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast("请输入电话号码");
            return false;
        }

        if (!phone.startsWith("1") || phone.length() != 11) {
            UIUtils.showToast("请填写正确的电话号码!");
            return false;
        }

        if (TextUtils.isEmpty(checkNumber)) {
            UIUtils.showToast("请填写验证码！");
            return false;
        }

        if (checkNumber.length() < 4 || checkNumber.length() > 8) {
            UIUtils.showToast("请填写正确的验证码！");
            return false;
        }

        return true;
    }

    @Override
    public void initListener() {
        onZoomClickListener.setThemeColor(getResources().getColor(com.yft.zbase.R.color.btn_color));
        mDataBing.rlLgoin.setOnClickListener(onZoomClickListener);
        mDataBing.rlLgoin.setOnTouchListener(onZoomClickListener);
        mViewModel.getTimeMutableLiveData().observe(this, this::timer);
        mDataBing.tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mDataBing.edPhone.getText().toString();
                if (!mViewModel.isStart()) {
                    UIUtils.showToast("正在发送验证码，请稍后！");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    UIUtils.showToast("请输入电话号码");
                    return;
                }

                if (!phone.startsWith("1") || phone.length() != 11) {
                    UIUtils.showToast("请填写正确的电话号码!");
                    return;
                }

                mViewModel.requestCheckNum("changephone" ,phone);
            }
        });
    }

    public void timer(Integer count) {
        if (count > 0) {
            mDataBing.tvCheck.setText(count + "s");
        } else if (count <= 0) {
            mDataBing.tvCheck.setText("获取验证码");
        }
    }

    private OnZoomClickListener onZoomClickListener = new OnZoomClickListener() {
        @Override
        public void onClick(View view) {
            ErrorCode.toLoginActivity();
            if (!isLoginCheck()) {
                return;
            }
            String phone = mDataBing.edPhone.getText().toString();
            String checkNumber = mDataBing.edCheck.getText().toString();
            // 请求登录
            // mViewModel.requestLogin(phone, checkNumber);
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_bind_phone_layout;
    }
}
