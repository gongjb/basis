package com.yft.zbase.ui;


import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;

import com.bumptech.glide.Glide;
import com.yft.zbase.R;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.databinding.FragmentMessageDialogLayoutBinding;

public class FragmentMessageDialog extends BaseFragmentDialog<FragmentMessageDialogLayoutBinding, BaseViewModel> {
    private OnButtonClickListener onButtonClickListener;
    private String message;
    private String title;
    private String btnText;
    private int gift;
    private Object objTag;

    public void setObjTag(Object objTag) {
        this.objTag = objTag;
    }

    public Object getObjTag() {
        return objTag;
    }

    public static FragmentMessageDialog newInstance(String msg, String title, String btnText) {
        FragmentMessageDialog fragment = new FragmentMessageDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        args.putString("btnText", btnText);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMessageDialog newInstance(String msg, String title, String btnText, int gift) {
        FragmentMessageDialog fragment = new FragmentMessageDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        args.putString("btnText", btnText);
        args.putInt("gift", gift);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMessageDialog newInstance() {
        FragmentMessageDialog fragment = new FragmentMessageDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }


    @Override
    public void initView() {
        message = !getArguments().containsKey("msg") ? message : getArguments().getString("msg");
        title = !getArguments().containsKey("title") ? title : getArguments().getString("title");
        btnText = !getArguments().containsKey("btnText") ? btnText : getArguments().getString("btnText");
        gift = !getArguments().containsKey("gift") ? 0 : getArguments().getInt("gift", 0);
        if (!TextUtils.isEmpty(message)) {
            mDataBing.tvContent.setVisibility(View.VISIBLE);
            mDataBing.tvContent.setText(message);
        } else {
            mDataBing.tvContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            mDataBing.tvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(btnText)) {
            mDataBing.tvYes.setText(btnText);
        }

        if(gift != 0) {
            mDataBing.ivGift.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(gift).into(mDataBing.ivGift);
        }
        mDataBing.tvNo.setOnClickListener(this::onButton);
        mDataBing.tvYes.setOnClickListener(this::onButton);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_message_dialog_layout;
    }

    public FragmentMessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public FragmentMessageDialog setButtonText(String text) {
        this.btnText = text;
        return this;
    }

    public void onButton(View view) {
        this.dismiss();
        if (view.getId() == R.id.tv_yes) {
            if (onButtonClickListener != null) {
                onButtonClickListener.onButton(view);
            }
        }
    }


    public FragmentMessageDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    /*
      对外暴露接口（点击事件）
     */
    public interface OnButtonClickListener {
        void onButton(View view);
    }
}
