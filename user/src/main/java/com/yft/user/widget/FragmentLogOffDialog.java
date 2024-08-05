package com.yft.user.widget;

import android.text.TextUtils;
import android.view.View;

import com.yft.user.databinding.FragmentLogOffDialogLayoutBinding;
import com.yft.zbase.R;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.utils.ToastUtils;

public class FragmentLogOffDialog extends BaseFragmentDialog<FragmentLogOffDialogLayoutBinding, BaseViewModel> {
    private OnButtonClickListener onButtonClickListener;
    private String describe;

    public static FragmentLogOffDialog newInstance() {
        FragmentLogOffDialog fragment = new FragmentLogOffDialog();
        return fragment;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void initView() {
        mDataBing.tvNo.setOnClickListener(this::onButton);
        mDataBing.tvYes.setOnClickListener(this::onButton);
        mDataBing.llOne.setOnClickListener(this::onClick);
        mDataBing.llTwo.setOnClickListener(this::onClick);
        mDataBing.llThree.setOnClickListener(this::onClick);
        mDataBing.llFour.setOnClickListener(this::onClick);
        mDataBing.llFive.setOnClickListener(this::onClick);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return com.yft.user.R.layout.fragment_log_off_dialog_layout;
    }

    public void onClick(View view) {
        if (view.getId() == com.yft.user.R.id.ll_one) {
            describe = "安全隐私顾虑";
            mDataBing.ivOne.setImageResource(R.mipmap.icon_in);
            mDataBing.ivTwo.setImageResource(R.mipmap.icon_on);
            mDataBing.ivThree.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFour.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFive.setImageResource(R.mipmap.icon_on);
        } else if (view.getId() == com.yft.user.R.id.ll_two) {
            describe = "重复多余账号";
            mDataBing.ivOne.setImageResource(R.mipmap.icon_on);
            mDataBing.ivTwo.setImageResource(R.mipmap.icon_in);
            mDataBing.ivThree.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFour.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFive.setImageResource(R.mipmap.icon_on);
        } else if (view.getId() == com.yft.user.R.id.ll_three) {
            describe = "购物遇到困难";
            mDataBing.ivOne.setImageResource(R.mipmap.icon_on);
            mDataBing.ivTwo.setImageResource(R.mipmap.icon_on);
            mDataBing.ivThree.setImageResource(R.mipmap.icon_in);
            mDataBing.ivFour.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFive.setImageResource(R.mipmap.icon_on);
        } else if (view.getId() == com.yft.user.R.id.ll_four) {
            describe = "无法修改个人信息";
            mDataBing.ivOne.setImageResource(R.mipmap.icon_on);
            mDataBing.ivTwo.setImageResource(R.mipmap.icon_on);
            mDataBing.ivThree.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFour.setImageResource(R.mipmap.icon_in);
            mDataBing.ivFive.setImageResource(R.mipmap.icon_on);
        } else if (view.getId() == com.yft.user.R.id.ll_five) {
            describe = "其它原因";
            mDataBing.ivOne.setImageResource(R.mipmap.icon_on);
            mDataBing.ivTwo.setImageResource(R.mipmap.icon_on);
            mDataBing.ivThree.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFour.setImageResource(R.mipmap.icon_on);
            mDataBing.ivFive.setImageResource(R.mipmap.icon_in);
        }
    }


    public void onButton(View view) {
        this.dismiss();
        if (view.getId() == R.id.tv_yes) {
            if (TextUtils.isEmpty(describe)) {
                ToastUtils.toast("请选择注销原因");
                return;
            }

            if (onButtonClickListener != null) {
                onButtonClickListener.onButton(view, describe);
            }
        }
    }


    /*
      对外暴露接口（点击事件）
     */
    public interface OnButtonClickListener {
        void onButton(View view, String mark);
    }
}
