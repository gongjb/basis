package com.yft.zbase.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

public class NoLeakDialog extends Dialog {

    private WeakReference<DialogFragment> weakReference;

    private int gravity;

    private boolean isMustUpdate;

    private float width = 0.9f;

    public NoLeakDialog setDialogFragment(DialogFragment dialogFragment) {
        weakReference = new WeakReference<>(dialogFragment);
        return this;
    }

    public NoLeakDialog setDialogWidth(float width) {
        this.width = width;
        return this;
    }


    public NoLeakDialog(Context context) {
        super(context);
    }

    public NoLeakDialog(Context context, int themeResId, int gravity){
        super(context,themeResId);
        this.gravity = gravity;
    }

    public NoLeakDialog(Context context, int themeResId, int gravity, float width){
        super(context,themeResId);
        this.gravity = gravity;
        setDialogWidth(width);
    }

    public NoLeakDialog(Context context, int themeResId, int gravity, boolean isMustUpdate){
        super(context,themeResId);
        this.gravity = gravity;
        this.isMustUpdate = isMustUpdate;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMustUpdate) {
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明背景
        Window window = getWindow();
        //设置宽度
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.gravity = this.gravity;
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * (this.width));//设置dialog的宽度为当前手机屏幕的宽度*0.8
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
