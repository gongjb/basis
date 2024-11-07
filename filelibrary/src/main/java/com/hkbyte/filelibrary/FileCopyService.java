package com.hkbyte.filelibrary;

import static com.yft.zbase.utils.Logger.LOGE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.core.app.NotificationCompat;

import com.hkbyte.file.R;
import com.yft.zbase.server.ManageThreadPoolService;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileCopyService extends Service {
    private static final String CHANNEL_ID = "FileCopyChannel";
    private static final int NOTIFICATION_ID = 1;
    private volatile boolean mCopying = false; // 标志位，表示是否正在复制
    private List<FileCopyTask> mTasks; // 批量复制任务列表
    public static final int FILE_COPY_COMPLETE = 1; // 消息标识符
    public static final int FILE_COPY_COMPLETE_SUCCESS = 2; // 消息标识符
    public static final int FILE_COPY_COMPLETE_PROGRESS= 3; // 消息
    public static final int MAX_NOTICE = 5;  // 任务大于等于5个，就不再显示通知了（会很卡）
    public boolean mRecover; // 当前是否是恢复文件（导出）
    private Messenger mClientMessenger; // 用于与 Activity 通信的 Messenger

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 从 Intent 中获取文件路径列表
        ArrayList<String> sourcePaths = intent.getStringArrayListExtra("sourcePaths");
        ArrayList<String> destPaths = intent.getStringArrayListExtra("destPaths");
        mClientMessenger = intent.getParcelableExtra("messenger");
        mRecover = intent.getBooleanExtra("recover", false);

        if (sourcePaths == null || destPaths == null || sourcePaths.size() != destPaths.size()) {
            // 检查路径列表是否有效
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        if (mCopying) {
            // 如果正在复制，直接忽略这个请求或提示用户
            ToastUtils.toast(getString(R.string.task_not_over_tips));
            return START_NOT_STICKY;
        }

        mCopying = true; // 标志位设置为 true，表示任务正在执行
        mTasks = new ArrayList<>();

        // 创建文件复制任务列表
        for (int i = 0; i < sourcePaths.size(); i++) {
            mTasks.add(new FileCopyTask(sourcePaths.get(i), destPaths.get(i)));
        }

        if (mTasks.size() < MAX_NOTICE) {
            // 启动前台通知
            startForeground(NOTIFICATION_ID, createNotification(0, 0, mTasks.size(), mRecover));
        }

        // 在新线程中执行批量文件复制
        ManageThreadPoolService.getInstance().executeRemote(new ReplicationThreads(this, mTasks, mClientMessenger, mRecover));

        return START_STICKY;
    }


    private static class ReplicationThreads implements Runnable {
        private FileCopyService mFileCopyService;
        private List<FileCopyTask> mTasks;
        private Messenger clientMessenger;
        private boolean mRecover;

        public ReplicationThreads(FileCopyService fileCopyService, List<FileCopyTask> tasks, Messenger clientMessenger, boolean recover) {
            this.mFileCopyService = fileCopyService;
            this.mTasks = tasks;
            this.clientMessenger = clientMessenger;
            this.mRecover = recover;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < mTasks.size(); i ++) {
                    FileCopyTask task = mTasks.get(i);
                    copyFile(task.sourcePath, task.destPath,  i, mTasks.size(), this.mRecover);
                }
                if (this.mRecover) {
                    List<String> listDestPath = new ArrayList<>();
                    List<String> listSourcePath = new ArrayList<>();
                    for (FileCopyTask fileCopyTask : mTasks) {
                        listDestPath.add(fileCopyTask.destPath);
                        listSourcePath.add(fileCopyTask.sourcePath);
                    }

                    if (!listDestPath.isEmpty()) {
                        // 通知更新相册
                        mFileCopyService.update(mFileCopyService, listDestPath.toArray(new String[0]));
                    }
                    // 直接本地文件删除文件
                    deleteFiles(listSourcePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 无论成功还是失败，任务完成后重置标志位
                mFileCopyService.mCopying = false;
                if (mTasks.size() < MAX_NOTICE) {
                    mFileCopyService.stopForeground(true);
                }
                mFileCopyService.stopSelf();
            }
        }


        public void deleteFiles(List<String> listSourcePath) {
            for (String filePath : listSourcePath) {
                File file = new File(filePath);
                if (file.exists()) {
                    boolean deleted = file.delete();
                }
            }
        }


        private void copyFile(final String sourcePath, final String destPath,final int position, int count, boolean recover) throws Exception {
            File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);

            if (!destFile.exists()) {
                destFile.createNewFile();
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
                    if (clientMessenger != null) {
                        String obj = sourcePath + "," + destPath + "," + recover;
                        Message msg = Message.obtain(null, FILE_COPY_COMPLETE_PROGRESS, obj);
                        msg.arg1 = progress;
                        // 发送当前文件拷贝进度
                        clientMessenger.send(msg);
                    }
                    if (mTasks.size() < MAX_NOTICE) {
                        Notification notification = mFileCopyService.createNotification(progress, position, count, this.mRecover);
                        mFileCopyService.startForeground(NOTIFICATION_ID, notification);
                    }
                }
            }


            if (clientMessenger != null) {
                String obj = sourcePath + "," + destPath + "," + recover;
                Message msg = Message.obtain(null, FILE_COPY_COMPLETE, obj);
                clientMessenger.send(msg);
                if ((position+1) == mTasks.size()) {
                    Message msg1 = Message.obtain(null, FILE_COPY_COMPLETE_SUCCESS, obj);
                    clientMessenger.send(msg1);
                }
            }
        }
    }


    private Notification createNotification(int progress, int position, int count, boolean recover) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(droidninja.filepicker.R.drawable.icon_file_unknown)
                .setContentTitle(recover ? getString(R.string.exporting_tips) : getString(R.string.importing_tips))
                .setContentText((position+1)+ "/" + count)
                .setVibrate(null)  // 取消震动
                .setSound(null)    // 取消声音
                .setLights(0, 0, 0) // 取消闪光灯
                .setProgress(100, progress, false);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, getString(R.string.file_copy_channel), NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private static class FileCopyTask {
        String sourcePath;
        String destPath;
        FileCopyTask(String sourcePath, String destPath) {
            this.sourcePath = sourcePath;
            this.destPath = destPath;
        }
    }

    public void update(Context context, String...strings) {
        MediaScannerConnection.scanFile(context, strings, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                // 扫描完成后的回调，可以在这里做一些处理
                LOGE("====>>通知相册成功");
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
