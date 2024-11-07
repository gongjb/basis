package com.yft.home.runnable;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.yft.home.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoRunnable implements Runnable {

    private PackageManager mPackageManager;
    private ILoadAppsListener mLoadAppsListener;

    public AppInfoRunnable(PackageManager packageManager, ILoadAppsListener loadAppsListener) {
        this.mPackageManager = packageManager;
        this.mLoadAppsListener = loadAppsListener;
    }

    @Override
    public void run() {
        try {
            List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            List<AppInfo> appInfos = new ArrayList<>();
            for (ApplicationInfo app : apps) {
                if((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    AppInfo appInfo = new AppInfo(app.packageName, app.loadLabel(mPackageManager).toString(), app.loadIcon(mPackageManager));
                    appInfos.add(appInfo);
                }
            }

            if (mLoadAppsListener != null) {
                mLoadAppsListener.onLoadAppsSuccess(appInfos);
            }
        } catch (Exception e) {
            if (mLoadAppsListener != null) {
                mLoadAppsListener.onFail(e);
            }
        }
    }

    public interface ILoadAppsListener {
        void onLoadAppsSuccess(List<AppInfo> appInfos);
        void onFail(Throwable throwable);
    }
}
