package com.yft.home.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.yft.home.bean.AppInfo;

public class HomeUIUtils {

    @BindingAdapter(value = {"imageApps"})
    public static void imageApps(ImageView iv, AppInfo appInfo){
        // 导入
        if (iv != null && appInfo != null) {
            if (appInfo.getIcon() != null) {
                iv.setImageDrawable(appInfo.getIcon());
            }
        }
    }

    @BindingAdapter(value = {"itemProgress"})
    public static void itemProgress(TextView tv,  AppInfo appInfo) {
        if (appInfo == null || appInfo.getProgress() == 100) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(appInfo.getProgress()+"%");
        }
    }
}
