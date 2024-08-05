package com.yft.zbase.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

public class NoLeakBackDialog extends Dialog {

    private WeakReference<DialogFragment> weakReference;

    private int gravity;

    public NoLeakBackDialog setDialogFragment(DialogFragment dialogFragment) {
        weakReference = new WeakReference<>(dialogFragment);
        return this;
    }

    public NoLeakBackDialog(Context context) {
        super(context);
    }

    public NoLeakBackDialog(Context context, int themeResId, int gravity){
        super(context,themeResId);
        this.gravity = gravity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //透明背景
        Window window = getWindow();
        //设置宽度
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.gravity = this.gravity;
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * 0.9);//设置dialog的宽度为当前手机屏幕的宽度*0.8
        window.setAttributes(p);


        // 设置弹窗外部的背景透明度，需要配合FLAG_DIM_BEHIND一起使用。
        // dimAmount取值范围为0f~1f，0f表示完全透明，1f表示完全不透明。
        p.dimAmount = 0.0f;
        // 让该window覆盖的所有页面显示都变暗淡
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(p);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setOnShowListener(OnShowListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setCancelMessage(Message msg) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setDismissMessage(Message msg) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (weakReference != null && weakReference.get() != null) {
            weakReference.get().dismissAllowingStateLoss();
        }
    }
}
