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
import com.yft.zbase.databinding.ReminderAgainPrivacyFragmentDialogBinding;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.NoLeakDialog;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

public class ReminderAgainPrivacyFragmentDialog  extends BaseFragmentDialog<ReminderAgainPrivacyFragmentDialogBinding, BaseViewModel> {

    public static ReminderAgainPrivacyFragmentDialog newInstance() {
        ReminderAgainPrivacyFragmentDialog fragment = new ReminderAgainPrivacyFragmentDialog();
        return fragment;
    }

    @Override
    public void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        //阅读完整的《用户服务协议》与《隐私政策》了解详细内容
        setCustomTextWithLinks(mDataBing.tvContent, viewModel.getString("如您不同意《用户服务协议》与《隐私政策》，我们将无法为您提供 ReelShort App的完整功能，您可以选择使用仅游览模式或直接退出应用"));
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
                dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.reminder_again_privacy_fragment_dialog;
    }

    public void setCustomTextWithLinks(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan[] clickableSpans = new ClickableSpan[3];

        int start1 = text.indexOf("《用户服务协议》");
        int end1 = start1 + "《用户服务协议》".length();
        int start2 = text.indexOf("《隐私政策》");
        int end2 = start2 + "《隐私政策》".length();
        int start3 = text.indexOf("直接退出应用");
        int end3 = start3 + "直接退出应用".length();

        clickableSpans[0] = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击《用户服务协议》时的操作
                Bundle build = new Bundle();
                build.putString("title", "用户协议");
                build.putBoolean("isWebTitle", false);
                build.putString("url", viewModel.getService().getUserAgreementUrl());
                RouterFactory.startRouterBundleActivity(getActivity(), RouterFactory.ACTIVITY_WEB, build);
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
                RouterFactory.startRouterBundleActivity(getActivity(), RouterFactory.ACTIVITY_WEB, build);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.theme_red)); // 设置颜色为蓝色
                ds.setUnderlineText(false); // 不显示下划线
            }
        };

        clickableSpans[2] = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击“直接退出应用”时的操作

                ErrorCode.exitApp();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 可以设置一个不同于其他链接的颜色，比如绿色或灰色，来区分这个链接与其他链接。
                ds.setColor(getResources().getColor(R.color.theme_red)); // 设置颜色为蓝色
                ds.setUnderlineText(false); // 不显示下划线
            }
        };

        spannableString.setSpan(clickableSpans[0], start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpans[1], start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpans[2], start3, end3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // 使 ClickableSpan 可移动
    }
}
