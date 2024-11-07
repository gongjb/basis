package com.hkbyte.filelibrary.router;

import com.yft.zbase.router.IRouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class FileRouter implements IRouter {
    // 文件目录页面
    public static final String FILE_VIDEO_ACTIVITY = "com/hkbyte/filelibrary/FileVideoActivity";
    // 文件导入页面
    public static final String FILE_IMPORT_ACTIVITY = "com/hkbyte/filelibrary/FileImportActivity";

    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap();
        // 配置页面跳转
        concurrentMap.put("FileVideoActivity", FILE_VIDEO_ACTIVITY);
        concurrentMap.put("FileImportActivity", FILE_IMPORT_ACTIVITY);
        return concurrentMap;
    }
}
