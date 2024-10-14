package com.yft.zbase.privacy;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.yft.zbase.R;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.databinding.FragmentPrivacyDialogLayoutBinding;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.NoLeakDialog;
import com.yft.zbase.utils.UIUtils;

/**
 * 隐私权限
 */
public class PrivacyFragmentDialog extends BaseFragmentDialog<FragmentPrivacyDialogLayoutBinding, BaseViewModel> {

    private ReminderAgainPrivacyFragmentDialog mReminderAgainPrivacyFragmentDialog;
    public static PrivacyFragmentDialog newInstance() {
        PrivacyFragmentDialog fragment = new PrivacyFragmentDialog();
        return fragment;
    }

    @Override
    public void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        mReminderAgainPrivacyFragmentDialog = ReminderAgainPrivacyFragmentDialog.newInstance();
        //阅读完整的《用户服务协议》与《隐私政策》了解详细内容
        setCustomTextWithLinks(mDataBing.tvThree, "阅读完整的《用户服务协议》与《隐私政策》了解详细内容");
        UIUtils.startToScale(mDataBing.vLineA);
    }


    @Override
    protected Dialog createNoLeakDialog() {
        return new NoLeakDialog(getContext(), R.style.MyLoadingDialog, createGravity(), true).setDialogFragment(this);
    }

    @Override
    public void initListener() {
        mDataBing.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getUserServer().setPrivacy(true); // 同意啦。
                dismiss();
            }
        });

        mDataBing.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 再次提示
                mReminderAgainPrivacyFragmentDialog.show(getActivity().getSupportFragmentManager(),"mReminderAgainPrivacyFragmentDialog");
                dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_privacy_dialog_layout;
    }

    public void setCustomTextWithLinks(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan[] clickableSpans = new ClickableSpan[2];

        int start1 = text.indexOf("《用户服务协议》");
        int end1 = start1 + "《用户服务协议》".length();
        int start2 = text.indexOf("《隐私政策》");
        int end2 = start2 + "《隐私政策》".length();

        clickableSpans[0] = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击《用户服务协议》时的操作
                Bundle build = new Bundle();
                build.putString("title", "用户协议");
                build.putBoolean("isWebTitle", false);
                build.putString("url", viewModel.getService().getUserAgreementUrl());
                RouterFactory.getInstance().startRouterBundleActivity(getActivity(), RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.theme_red)); // 设置颜色为蓝色
                ds.setUnderlineText(false); // 不显示下划线
            }
        };

        clickableSpans[1] = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击《隐私政策》时的操作
                Bundle build = new Bundle();
                build.putString("title", "隐私政策");
                build.putBoolean("isWebTitle", false);
                build.putString("url", viewModel.getService().getPrivacyAgreementUrl());
                RouterFactory.getInstance().startRouterBundleActivity(getActivity(), RouterFactory.getInstance().getPage("WebYftActivity"), build);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.theme_red)); // 设置颜色为蓝色
                ds.setUnderlineText(false); // 不显示下划线
            }
        };

        spannableString.setSpan(clickableSpans[0], start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpans[1], start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // 使 ClickableSpan 可移动
    }
}
