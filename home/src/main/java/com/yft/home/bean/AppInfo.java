package com.yft.home.bean;

import android.graphics.drawable.Drawable;

/**
 * 应用信息 （展示用）
 */
public class AppInfo {
    private String packageName;
    private String name;
    private Drawable icon;
    private int progress = 100;

    public AppInfo(String packageName, String name, Drawable icon) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
    }

    public AppInfo(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
