package com.hkbyte.filelibrary.util;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.databinding.BindingAdapter;

import com.hkbyte.filelibrary.bean.FileBean;

public class FileUIUtils {
    @BindingAdapter(value = {"selected"})
    public static void textSelected(TextView textView, boolean selected){
        // 导入
        if (textView != null) {
            textView.setSelected(selected);
        }
    }


    @BindingAdapter(value = "tvFileNumber")
    public static void tvFileNumber(TextView view, FileBean fileBean) {
        if (fileBean == null || fileBean.getProgress() == 100) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(fileBean.getProgress()+"%");
        }
    }
}
