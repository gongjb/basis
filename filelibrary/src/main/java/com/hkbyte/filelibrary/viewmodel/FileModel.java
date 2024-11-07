package com.hkbyte.filelibrary.viewmodel;

import com.hkbyte.filelibrary.runnable.DirsLoadRunnable;
import com.hkbyte.filelibrary.runnable.DirsToFilesRunnable;
import com.hkbyte.filelibrary.runnable.InsertDirsFilesRunnable;
import com.luck.picture.lib.entity.LocalMedia;
import com.yft.zbase.base.BaseModel;
import com.yft.zbase.server.ManageThreadPoolService;

import java.util.ArrayList;

public class FileModel extends BaseModel {


    /**
     * 寻找目录
     *
     * @param type 文件类型
     * @return
     */
    public void loadDirFiles(int type, IFileLoadListener iFileLoadListener) {
        ManageThreadPoolService.getInstance().executeRemote(new DirsLoadRunnable(type, iFileLoadListener));
    }

    /**
     * 寻找目录下的文件
     *
     * @param type
     * @param dirsName
     */
    public void loadDirsToFiles(int type, String dirsName, IFileLoadListener iFileLoadListener, int thatPage) {
        ManageThreadPoolService.getInstance().executeRemote(new DirsToFilesRunnable(type, dirsName, iFileLoadListener, thatPage));
    }

    /**
     * 插入文件夹中
     *
     * @param type
     * @param dirsName
     * @param result
     */
    public void insertDirsToFiles(int type, String dirsName, ArrayList<LocalMedia> result, IInsertFileListener iInsertFileListener) {
        ManageThreadPoolService.getInstance().executeRemote(new InsertDirsFilesRunnable(type, dirsName, result, iInsertFileListener));
    }
}
