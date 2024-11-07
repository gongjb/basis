package com.yft.home.model;

import static com.hkbyte.cnbase.util.Constant.PACKAGES_KEY;
import static com.yft.zbase.utils.Logger.LOGE;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.UserHandle;
import android.util.Log;
import android.util.Xml;

import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.reflect.TypeToken;
import com.yft.home.ImportApkService;
import com.yft.home.bean.AppInfo;
import com.yft.home.bean.AppSaveBean;
import com.yft.home.bean.ProgressBean;
import com.yft.home.runnable.AppInfoRunnable;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IFreeStorage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.utils.ToastUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dalvik.system.DexClassLoader;

public class AppsViewModel extends BaseViewModel {
    private AppsModel mAppsModel;
    private IFreeStorage mFreeStorage;
    private MutableLiveData<List<AppInfo>> mAppInfoListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProgressBean> mCopyProgressBeanMutableLiveData = new MutableLiveData();
    private MutableLiveData<ProgressBean> mSuccessCopyApkLiveData = new MutableLiveData();
    private MutableLiveData<ProgressBean> mUninstallAppMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProgressBean> mInstallApkMutableLiveData = new MutableLiveData<>();

    private final Messenger mCopyApkMessenger = new Messenger(new CopyApkHandler()); // 用于处理 Service 发送的消息

    public AppsViewModel() {
        mAppsModel = new AppsModel();
        mFreeStorage = DynamicMarketManage.getInstance().getServer(IServerAgent.FREE_STORAGE); // 存储器
    }

    public MutableLiveData<List<AppInfo>> getAppInfoListMutableLiveData() {
        return mAppInfoListMutableLiveData;
    }

    public MutableLiveData<ProgressBean> getCopyProgressBeanMutableLiveData() {
        return mCopyProgressBeanMutableLiveData;
    }

    public MutableLiveData<ProgressBean> getSuccessCopyApkLiveData() {
        return mSuccessCopyApkLiveData;
    }

    public MutableLiveData<ProgressBean> getOnUninstallAppMutableLiveData() {
        return mUninstallAppMutableLiveData;
    }

    public MutableLiveData<ProgressBean> getInstallApkMutableLiveData() {
        return mInstallApkMutableLiveData;
    }

    /**
     * 请求本地的安装应用列表
     * @param packageManager
     */
    public void requestApps(PackageManager packageManager) {
        mAppsModel.requestApps(packageManager, new AppInfoRunnable.ILoadAppsListener() {
            @Override
            public void onLoadAppsSuccess(List<AppInfo> appInfos) {
                getAppInfoListMutableLiveData().postValue(appInfos);
            }

            @Override
            public void onFail(Throwable throwable) {
                ToastUtils.currencyToast(throwable);
                getErrorMutableLiveData().postValue("0");
            }
        });
    }




    public List<AppInfo> getAppList(PackageManager packageManager) {
        List<AppSaveBean> apps = mFreeStorage.getList(PACKAGES_KEY, new TypeToken<List<AppSaveBean>>() {}.getType());
        if (apps == null) {
            return null;
        }
        List<AppInfo> appInfos = new ArrayList<>();
        for (AppSaveBean bean : apps) {
            try {
                ApplicationInfo app = packageManager.getApplicationInfo(bean.getPackageName(), 0);
                String appName = app.loadLabel(packageManager).toString();
                Drawable appIcon = app.loadIcon(packageManager);
                appInfos.add(new AppInfo(bean.getPackageName(), appName, appIcon));
            } catch (PackageManager.NameNotFoundException e) {
                appInfos.add(new AppInfo(bean.getPackageName()));
            }
        }
        return appInfos;
    }

    /**
     * 开始复制
     * @param packageName 包名
     * @param context 上下文
     */
    public void startCopyApk(String packageName,String appName, Context context) {
        // 启动前台服务
        Intent intent = new Intent(context, ImportApkService.class);
        intent.putExtra(ImportApkService.PACKAGE_NAME, packageName);
        intent.putExtra(ImportApkService.APP_NAME, appName);
        intent.putExtra(ImportApkService.MESSENGER, mCopyApkMessenger);
        context.startService(intent);
    }


    private class CopyApkHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case ImportApkService.MSG_PROGRESS:
                        // 复制进行中
                        Bundle bundle = msg.getData();
                        ProgressBean progressBean =  bundle.getParcelable("progress");
                        getCopyProgressBeanMutableLiveData().postValue(progressBean);
                        break;
                    case ImportApkService.MSG_COMPLETE:
                        // 复制完成，
                        bundle = msg.getData();
                        progressBean =  bundle.getParcelable("progress");
                        progressBean.setProgress(100);
                        getSuccessCopyApkLiveData().postValue(progressBean);
                        break;
                    case ImportApkService.MSG_FAILED:
                        // 复制失败。
                        bundle = msg.getData();
                        progressBean = bundle.getParcelable("progress");
                        progressBean.setProgress(-1);
                        getCopyProgressBeanMutableLiveData().postValue(progressBean);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在生命周期开始， 发送给页面
     * @param apkPath 新文件路径
     */
    public void uninstallAppPost(ProgressBean apkPath) {
        getOnUninstallAppMutableLiveData().postValue(apkPath);
    }

    public void installApkPost(ProgressBean p) {
        getInstallApkMutableLiveData().postValue(p);
    }


    /**
     * 安装apk
     * @param apkFile
     * @param context
     */
    public void installApk(File apkFile, Context context) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String provider = context.getApplicationContext().getPackageName() + ".provider";
            Uri contentUri = FileProvider.getUriForFile(ZBaseApplication.get(), provider, apkFile);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(install);
    }


    public void removeAppShortcut(Context context, String packageName, String shortcutName) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setPackage(packageName));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);  // 快捷方式名称
        context.sendBroadcast(intent);
    }

    public void launchApk(Context context, File apkFile, String mainActivity, String packageName) {
        if (mainActivity != null) {
            // 使用 DexClassLoader 加载 APK
            DexClassLoader dexClassLoader = new DexClassLoader(
                    apkFile.getAbsolutePath(),
                    context.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(),
                    null,
                    context.getClassLoader()
            );

            try {
                // 加载 mainActivity 类
                Class<?> clazz = dexClassLoader.loadClass(packageName + "." + mainActivity);

                // 创建 Intent 启动该 Activity
                Intent intent = new Intent(context, clazz);
                intent.setComponent(new ComponentName(packageName, mainActivity)); // 设置目标包名和类名
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("LaunchApk", "Main activity class not found.");
        }
    }



    public String getMainActivityFromApk(File apkFile) {
        try {
            // 打开 APK 文件
            ZipFile zipFile = new ZipFile(apkFile);
            ZipEntry entry = zipFile.getEntry("AndroidManifest.xml");

            if (entry != null) {
                InputStream inputStream = zipFile.getInputStream(entry);

                // 创建 DocumentBuilderFactory 实例并解析 AndroidManifest.xml
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);

                // 获取 manifest 中的 <activity> 元素
                NodeList activities = document.getElementsByTagName("activity");

                for (int i = 0; i < activities.getLength(); i++) {
                    Element activity = (Element) activities.item(i);
                    NodeList intentFilters = activity.getElementsByTagName("intent-filter");

                    for (int j = 0; j < intentFilters.getLength(); j++) {
                        Element intentFilter = (Element) intentFilters.item(j);

                        // 查找是否包含 MAIN 和 LAUNCHER
                        NodeList actions = intentFilter.getElementsByTagName("action");
                        NodeList categories = intentFilter.getElementsByTagName("category");

                        boolean isMain = false, isLauncher = false;

                        // 查找 action = MAIN
                        for (int k = 0; k < actions.getLength(); k++) {
                            Element action = (Element) actions.item(k);
                            if ("android.intent.action.MAIN".equals(action.getAttribute("name"))) {
                                isMain = true;
                            }
                        }

                        // 查找 category = LAUNCHER
                        for (int k = 0; k < categories.getLength(); k++) {
                            Element category = (Element) categories.item(k);
                            if ("android.intent.category.LAUNCHER".equals(category.getAttribute("name"))) {
                                isLauncher = true;
                            }
                        }

                        if (isMain && isLauncher) {
                            // 找到启动页面的 Activity 名称
                            return activity.getAttribute("name");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 卸妆APK
     * @param packageName
     * @param context
     */
    public void uninstallApp(String packageName, Context context) {
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
        uninstallIntent.setData(Uri.parse("package:" + packageName));
        context.startActivity(uninstallIntent);
    }

}
