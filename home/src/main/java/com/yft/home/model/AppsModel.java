package com.yft.home.model;

import android.content.pm.PackageManager;

import com.yft.home.runnable.AppInfoRunnable;
import com.yft.zbase.base.BaseModel;
import com.yft.zbase.server.ManageThreadPoolService;

public class AppsModel extends BaseModel {

    public void requestApps(PackageManager packageManager, AppInfoRunnable.ILoadAppsListener loadAppsListener) {
        ManageThreadPoolService.getInstance().executeRemote(new AppInfoRunnable(packageManager, loadAppsListener));
    }

    public void importApk(PackageManager packageManager) {

    }
}
