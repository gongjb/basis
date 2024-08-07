package com.yft.zbase.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.graphics.ColorUtils;


public abstract class OnZoomClickListener implements View.OnClickListener,View.OnTouchListener {
    // 按钮的主题颜色
    private int themeColor;
    // 按下去的颜色
    private int downColor;

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        float[] floats = new float[3];
        ColorUtils.colorToHSL(themeColor, floats);
        // 色彩饱和度下降百分之20
        floats[2] = floats[2] * 0.4f;
        downColor = ColorUtils.HSLToColor(floats);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                if (themeColor != 0) {
                    view.setBackgroundColor(themeColor);
                }
                createUp(view);
                break;
            case MotionEvent.ACTION_DOWN:
                // 记录按下去的点
                createDown(view);
                if (downColor != 0) {
                    view.setBackgroundColor(downColor);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }

    public void createDown(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator1);
        set.setDuration(300);
        set.start();
    }

    public void createUp(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        AnimatorSet set = new AnimatorSet();

        set.playTogether(animator, animator1);
        set.setDuration(300);
        set.start();
    }


}
