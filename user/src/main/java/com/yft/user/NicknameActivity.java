package com.yft.user;

import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;

import com.chenenyu.router.annotation.Route;
import com.gongjiebin.latticeview.KVBean;
import com.yft.user.databinding.ActivityNicknameLayoutBinding;
import com.yft.user.model.UserViewModel;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.OnZoomClickListener;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.utils.UIUtils;

@Route(UserRouter.ACTIVITY_NICKNAME)
public class NicknameActivity extends BaseActivity<ActivityNicknameLayoutBinding, UserViewModel> {

    private SubFragmentDialog mSubDialog;
    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.llMain.setBackground(grad);
        mDataBing.tlt.setTitle("修改昵称");
        mDataBing.tlt.setLeftBackImage();
        mDataBing.tvNick.setText(mViewModel.getUserInfo().getNickname());
        mSubDialog = SubFragmentDialog.newInstance();
    }

    @Override
    public void initListener() {
        mDataBing.tvSubmit.setOnClickListener(mOnZoomClickListener);
        mDataBing.tvSubmit.setOnTouchListener(mOnZoomClickListener);
        mViewModel.getMutableSuccessLiveData().observe(this, this::onSuccess);
        mViewModel.getErrorMutableLiveData().observe(this, this::onError);
    }

    public void onSuccess(KVBean kvBean) {
        closeDialog();
        if (kvBean != null) {
            if (kvBean.getKey() == 1) {
                // 保存昵称成功
                this.finish();
            }
        }
    }

    public void closeDialog() {
        if (mSubDialog != null && mSubDialog.isShow()) {
            mSubDialog.dismiss();
        }
    }

    public void onError(String tag) {
        closeDialog();
    }

    private OnZoomClickListener mOnZoomClickListener = new OnZoomClickListener() {
        @Override
        public void onClick(View view) {
            String nick = mDataBing.tvNick.getText().toString();
            if (TextUtils.isEmpty(nick)) {
                UIUtils.showToast("请填写昵称，昵称不能为空");
                return;
            }

            if (nick.length() > 12) {
                UIUtils.showToast("昵称的长度不能大于12个字符");
                return;
            }

            if (mSubDialog != null && !mSubDialog.isShow()) {
                mSubDialog.show(getSupportFragmentManager(),"-->");
                mViewModel.requestUpdateUser("", nick);
            }
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_nickname_layout;
    }
}
