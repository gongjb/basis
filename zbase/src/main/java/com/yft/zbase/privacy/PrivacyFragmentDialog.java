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
    private IOnPrivacyClick mOnPrivacyClick;
    public static PrivacyFragmentDialog newInstance() {
        PrivacyFragmentDialog fragment = new PrivacyFragmentDialog();
        return fragment;
    }

    public void setOnPrivacyClick(IOnPrivacyClick mOnPrivacyClick) {
        this.mOnPrivacyClick = mOnPrivacyClick;
    }

    @Override
    public void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        mReminderAgainPrivacyFragmentDialog = ReminderAgainPrivacyFragmentDialog.newInstance();
        //阅读完整的《用户服务协议》与《隐私政策》了解详细内容
        setCustomTextWithLinks(mDataBing.tvThree, "阅读完整的《用户服务协议》与《隐私政策》了解详细内容");
        mDataBing.vLineA.setBackgroundColor(getLineColor());
        UIUtils.startToScale(mDataBing.vLineA);
        mDataBing.clPrivacyA.setBackground(getClPrivacy());
        mDataBing.tvContent.setTextColor(getTextContentColor());
        mDataBing.tvTwo.setTextColor(getTextContentColor());
        mDataBing.tvThree.setTextColor(getTextContentColor());
        mDataBing.tvNo.setTextColor(getTextNoColor());
        mDataBing.tvYes.setTextColor(getTextYesColor());
        mDataBing.tvTitle.setTextColor(getTextTitleColor());
    }

    private int getTextTitleColor() {
        return getResources().getColor(R.color.sd_white);
    }

    /**
     * 容器背景（可自定义）
     * @return
     */
    protected Drawable getClPrivacy() {
        return getResources().getDrawable(R.drawable.item_dialog_background);
    }

    /**
     * title line
     * @return
     */
    protected int getLineColor() {
        return getResources().getColor(R.color.ui_dialog_text_color);
    }

    /**
     * 内容文本颜色
     * @return
     */
    protected int getTextContentColor() {
        return getResources().getColor(R.color.sd_b_white);
    }

    /**
     * no text color
     * @return
     */
    protected int getTextNoColor() {
        return getResources().getColor(R.color.sd_d_white);
    }

    /**
     * yes text color
     * @return
     */
    protected int getTextYesColor() {
        return getResources().getColor(R.color.btn_color);
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
                if (mOnPrivacyClick != null) {
                    mReminderAgainPrivacyFragmentDialog.setOnPrivacyClick(mOnPrivacyClick);
                }
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
