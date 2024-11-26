package com.yft.zbase.privacy;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yft.zbase.BuildConfig;
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

    private IOnPrivacyClick mOnPrivacyClick;

    public static ReminderAgainPrivacyFragmentDialog newInstance() {
        ReminderAgainPrivacyFragmentDialog fragment = new ReminderAgainPrivacyFragmentDialog();
        return fragment;
    }

    public void setOnPrivacyClick(IOnPrivacyClick mOnPrivacyClick) {
        this.mOnPrivacyClick = mOnPrivacyClick;
    }

    @Override
    public void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        //阅读完整的《用户服务协议》与《隐私政策》了解详细内容
        String tips = String.format("如您不同意《用户服务协议》与《隐私政策》，我们将无法为您提供 %s App的完整功能，您可以选择使用仅游览模式或直接退出应用", getAppName());
        setCustomTextWithLinks(mDataBing.tvContent, tips);


        initLineColor(mDataBing.vLineA);
        UIUtils.startToScale(mDataBing.vLineA);
        initClPrivacy(mDataBing.clPrivacy);
        initTextContentColor(mDataBing.tvContent);
        initTextNoColor(mDataBing.tvNo);
        initTextYesColor(mDataBing.tvYes);
        initTextTitleColor(mDataBing.tvTitle);
    }

    /**
     * app name
     * @return
     */
    protected String getAppName() {
        // 复写此方法
        return "Gong-jb";
    }

    protected void initTextTitleColor(TextView tvTitle) {
        tvTitle.setTextColor(getResources().getColor(R.color.sd_white));
    }

    /**
     * 容器背景（可自定义）
     * @return
     */
    protected void initClPrivacy(ConstraintLayout constraintLayout) {
        constraintLayout.setBackground(getResources().getDrawable(R.drawable.item_dialog_background));
    }

    /**
     * title line
     * @return
     */
    protected void initLineColor(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.ui_dialog_text_color));
    }

    /**
     * 内容文本颜色
     * @return
     */
    protected void initTextContentColor(TextView...tvs) {
        if (tvs == null) return;
        for (int i = 0; i < tvs.length; i ++) {
            tvs[i].setTextColor(getResources().getColor(R.color.sd_b_white));
        }
    }

    /**
     * no text color
     * @return
     */
    protected void initTextNoColor(TextView tvNo) {
        tvNo.setTextColor(getResources().getColor(R.color.sd_d_white));
    }

    /**
     * yes text color
     * @return
     */
    protected void initTextYesColor(TextView tvYes) {
        tvYes.setTextColor(getResources().getColor(R.color.btn_color));
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
                if (mOnPrivacyClick != null){
                    mOnPrivacyClick.onPrivacyClick(IOnPrivacyClick.consent);
                }
                dismiss();
            }
        });

        mDataBing.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 再次提示
                if (mOnPrivacyClick != null) {
                    mOnPrivacyClick.onPrivacyClick(IOnPrivacyClick.not);
                }
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
