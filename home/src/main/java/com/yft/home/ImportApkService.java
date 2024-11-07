package com.yft.home;

import static com.hkbyte.cnbase.util.Constant.PACKAGES_KEY;
import static com.yft.zbase.utils.Logger.LOGE;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yft.home.bean.AppSaveBean;
import com.yft.home.bean.ProgressBean;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IFreeStorage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.ManageThreadPoolService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ImportApkService extends Service {
    private static final String TAG = "ImportApkService";

    // 传递进度和结果的消息类型
    public static final int MSG_PROGRESS = 1;
    public static final int MSG_COMPLETE = 2;
    public static final int MSG_FAILED = 3;

    // 用于与 Activity 通信的 Messenger
    private Messenger mMessenger;
    public final static String MESSENGER = "MESSENGER";
    public final static String PACKAGE_NAME = "PACKAGE_NAME";
    public final static String APP_NAME = "APP_NAME";
    private IFreeStorage mFreeStorage;
    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不提供绑定服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取从 Activity 传递的 Messenger
        mMessenger = intent.getParcelableExtra(MESSENGER);
        // 获取 APK 包名
        String packageName = intent.getStringExtra(PACKAGE_NAME);
        String appName = intent.getStringExtra(APP_NAME);
        if (packageName != null) {
            // 开始执行 APK 文件复制任务
            ManageThreadPoolService.getInstance().executeRemote(new Thread(() -> copyApkToStorage(packageName,appName)));
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFreeStorage = DynamicMarketManage.getInstance().getServer(IServerAgent.FREE_STORAGE);
    }

    private void copyApkToStorage(final String packageName, final String appName) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            // 获取安装的应用的 APK 路径
            String apkPath = getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
            File sourceFile = new File(apkPath);

            // 获取源文件的文件名（不包括路径）
            String sourceFileName = sourceFile.getName();

            // 目标路径：将 APK 文件复制到你的应用私有存储目录，使用源文件名作为目标文件名
            File destFile = new File(getFilesDir(), packageName+".apk");

            String destFilePath = destFile.getAbsolutePath();
            // 如果目标文件已经存在，先删除
            if (destFile.exists()) {
                destFile.delete();
            }


            try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
                 FileChannel destChannel = new FileOutputStream(destFile).getChannel()) {

                long totalSize = sourceChannel.size();
                long transferred = 0;
                long bufferSize = 1024 * 1024;  // 每次传输 1MB

                while (transferred < totalSize) {
                    long bytesTransferred = sourceChannel.transferTo(transferred, bufferSize, destChannel);
                    transferred += bytesTransferred;
                    // 更新通知中的进度条
                    int progress = (int) ((transferred * 100) / totalSize);
                    LOGE("==当前进度=>" + progress);
                    // 将进度发送给 Activity
                    sendMessageToActivity(MSG_PROGRESS, progress, packageName, destFilePath,appName);
                }
            }

            saveAppPackage(packageName);
            // 复制完成后发送消息
            sendMessageToActivity(MSG_COMPLETE, -1, packageName,destFilePath,appName);  // -1 表示复制完成
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy APK: " + e.getMessage());
            // 复制失败时发送失败消息
            sendMessageToActivity(MSG_FAILED, -1, packageName, "",appName);
        } catch (PackageManager.NameNotFoundException e) {
            sendMessageToActivity(MSG_FAILED, -1, packageName, "",appName);
        } finally {
            try {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 任务完成后，停止服务
            stopSelf();
        }
    }

    public boolean saveAppPackage(String packageName) {
        // 获取已保存的应用包名列表
        List<AppSaveBean> apps = mFreeStorage.getList(PACKAGES_KEY, new TypeToken<List<AppSaveBean>>() {}.getType());

        // 如果列表为空，初始化一个新列表
        if (apps == null) {
            apps = new ArrayList<>();
        }

        // 检查包名是否已经存在
        for (AppSaveBean appSaveBean : apps) {
            if (appSaveBean.getPackageName().equals(packageName)) {
                return false; // 如果包名存在，返回 false
            }
        }

        // 插入到列表的第一个位置
        apps.add(0, new AppSaveBean(packageName));
        // 保存更新后的列表
        return mFreeStorage.saveList(PACKAGES_KEY, apps); // 记得更新存储
    }

    private void sendMessageToActivity(int what, int arg1, String packageName, String filePath,String appName) {
        if (mMessenger != null) {
            try {
                Message message = Message.obtain();
                message.what = what;
                Bundle bundle = new Bundle();
                bundle.putParcelable("progress", new ProgressBean(packageName, arg1, filePath, appName));
                message.setData(bundle);
                mMessenger.send(message);
            } catch (Exception e) {
                Log.e(TAG, "Error sending message to Activity: " + e.getMessage());
            }
        }
    }
}
