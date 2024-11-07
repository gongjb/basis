package com.hkbyte.filelibrary.viewmodel;

import com.hkbyte.filelibrary.bean.CopyProgressBean;
import com.hkbyte.filelibrary.bean.FileBean;

import java.io.File;
import java.util.List;

public interface IFileLoadListener<T> {

    /**
     * 文件与目录加载成功
     * @param list
     */
    default void onLoadDirsFiles(List<T> list) {}

    /**
     * 出现空文件夹（一般是删除把里面的文件删除干净了）
     */
    default void onLoadDirsDeleteNull(int thatPage){}

    /**
     * 加载失败
     * @param errorCode
     */
    void onFail(int errorCode);


    File getRootFiles(int type);

    void setFileBeanMimeType(FileBean fileBean, int type);


}
