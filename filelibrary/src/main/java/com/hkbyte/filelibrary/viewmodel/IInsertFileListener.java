package com.hkbyte.filelibrary.viewmodel;


import com.hkbyte.filelibrary.bean.CopyProgressBean;

import java.util.List;

public interface IInsertFileListener<T> extends IFileLoadListener<T> {
    /**
     * 插入文件copy
     * @param list
     */
    void onInsertDirsFiles(List<T> list);

    /**
     *
     * @param type
     * @return
     */
    default String getExtension(int type){
        switch (type) {
            case FileViewModel.FILE_VIDEO:
                return ".xuf";
            case FileViewModel.FILE_IMAGE:
                return ".bh";
            case FileViewModel.FILE_AUDIO:
                return ".ch";
            case FileViewModel.FILE_DOCUMENTATION:
                return ".dh";
            default: {
                return ".hidde";
            }
        }
    }

    default void pullCopyProgress(CopyProgressBean progressBean) {
    }

}
