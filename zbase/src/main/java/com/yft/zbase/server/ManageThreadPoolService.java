package com.yft.zbase.server;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageThreadPoolService {
    private static ManageThreadPoolService instance;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    public static synchronized ManageThreadPoolService getInstance() {
        if (instance == null) {
            synchronized (ManageThreadPoolService.class) {
                if (instance == null) {
                    instance = new ManageThreadPoolService();
                }
            }
        }
        return instance;
    }

    private ExecutorService singlePool = Executors.newSingleThreadExecutor();


    private ExecutorService remotePool = Executors.newCachedThreadPool();


    public void executeRemote(Runnable runnable) {
        remotePool.execute(runnable);
    }

    public void executeSingle(Runnable runnable) {
        singlePool.execute(runnable);
    }

    public void executeOnUiThread(Runnable command) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            command.run();
        } else {
            mainHandler.post(command);
        }
    }
}
