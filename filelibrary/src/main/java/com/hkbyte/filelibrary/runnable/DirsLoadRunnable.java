package com.hkbyte.filelibrary.runnable;

import android.os.Environment;

import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.util.FileTypeDetector;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;
import com.hkbyte.filelibrary.viewmodel.IFileLoadListener;
import com.yft.zbase.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 寻找根目录下的目录
 */
public class DirsLoadRunnable implements Runnable {
    private int mType;
    private IFileLoadListener mViewModel;

    public DirsLoadRunnable(int type, IFileLoadListener viewModel) {
        this.mType = type;
        this.mViewModel = viewModel;
    }

    @Override
    public void run() {
        // 加载文件的代码
        File rootDirFile = Environment.getExternalStorageDirectory().getAbsoluteFile();
        if (rootDirFile != null && rootDirFile.exists()) {
            File chameleonFiles = mViewModel.getRootFiles(mType);

            if (chameleonFiles == null) {
                mViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_TYPE);
                return;
            }

            if (!chameleonFiles.exists()) {
                if (!chameleonFiles.mkdirs()) {
                    mViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_CREATE);
                    return;
                }
            }

            File[] files = chameleonFiles.listFiles();

            if (files == null || files.length == 0) {
                mViewModel.<FileBean>onLoadDirsFiles(new ArrayList<>());
                return;
            }

            List<File> fileArray = Arrays.asList(files);
            if (!Utils.isCollectionEmpty(fileArray)) {
                List<FileBean> fileBeanList = new ArrayList<>();
                for (File file : fileArray) {
                    if (file.isDirectory()) {
                        // 非目录的都加载出来
                        FileBean fileBean = new FileBean();
                        fileBean.setName(file.getName());
                        fileBean.setType(mType);
                        fileBean.setFileUrl(file.getAbsolutePath()); // 本地路径
                        // 寻找到原文件的
                        fileBeanList.add(fileBean);
                    }
                }

                mViewModel.<FileBean>onLoadDirsFiles(fileBeanList);
            } else {
                // 未找到文件
                mViewModel.<FileBean>onLoadDirsFiles(new ArrayList<>());
            }
        } else {
            mViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_NULL);
        }
    }


}
