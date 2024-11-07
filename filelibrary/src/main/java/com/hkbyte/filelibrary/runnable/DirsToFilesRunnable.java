package com.hkbyte.filelibrary.runnable;

import static com.yft.zbase.utils.Logger.LOGE;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.util.FileUtils;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;
import com.hkbyte.filelibrary.viewmodel.IFileLoadListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 寻找目录下的所有文件
 */
public class DirsToFilesRunnable implements Runnable {
    private int mType;
    private String mDirsName;
    private IFileLoadListener mFileViewModel;
    private int mCurrentPage; // 当前页数

    public DirsToFilesRunnable(int type, String dirName, IFileLoadListener fileViewModel, int currentPage) {
        this.mType = type;
        this.mDirsName = dirName;
        this.mFileViewModel = fileViewModel;
        this.mCurrentPage = currentPage;
    }

    @Override
    public void run() {
        LOGE("===Thread=>" + Thread.currentThread().getName());
        File rootFile = mFileViewModel.getRootFiles(mType);
        File dirsFile = new File(rootFile, mDirsName);
        if (!dirsFile.exists()) {
            mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_NULL);
            return;
        }
        // 得到文件夹总数量（总页数）
        int dirsCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dirsCount = countDirectories(dirsFile.getAbsolutePath(), true);
        } else {
            File[] countFiles = dirsFile.listFiles();
            if (countFiles != null) {
                dirsCount = countFiles.length;
            }
        }

        int thatFilePage =  dirsCount - (mCurrentPage - 1);
        if (thatFilePage <= 0) {
            // 最后一页了
            mFileViewModel.onFail(FileViewModel.ILoadFiles.LAST_PAGE);
            return;
        }
        LOGE("查找文件夹， 文件夹名称=》" + thatFilePage);
        // 根据当前页数构建分页文件夹路径
        File pageDir = new File(dirsFile, String.valueOf(thatFilePage));

         //开始寻找文件
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int count = countDirectories(pageDir.getAbsolutePath(), false);
            if (count == 0) {
                // 当前页面已经被删除干净了
                mFileViewModel.onLoadDirsDeleteNull(thatFilePage);
                return;
            }
            Path dirsPath = Paths.get(dirsFile.getAbsolutePath(), String.valueOf(thatFilePage));
            if (!Files.exists(dirsPath)) {
                mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_NULL);
                return;
            }

            List<FileBean> fileBeanList = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirsPath)) {
                for (Path entry : stream) {
                    if (Files.isRegularFile(entry)) { // 只处理普通文件
                        File file = entry.toFile();
                        FileBean fileBean = createFileBean(file);
                        fileBean.setCurrentPage(thatFilePage);
                        // 如果需要排序，可以在这里收集文件信息，稍后进行排序
                        fileBeanList.add(fileBean);
                    }
                }

                // 对文件列表进行排序（如果需要）
                if (!fileBeanList.isEmpty()) {
                    fileBeanList.sort(Comparator.comparingLong(DirsToFilesRunnable::getFileCreationTime).reversed());
                }

                mFileViewModel.<FileBean>onLoadDirsFiles(fileBeanList);
            } catch (IOException | DirectoryIteratorException ex) {
                mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_IO);
                ex.printStackTrace();
            }
        }
    }

    private FileBean createFileBean(File file) {
        FileBean fileBean = new FileBean();
        fileBean.setName(file.getName());
        fileBean.setType(mType);
        fileBean.setFileUrl(file.getAbsolutePath());
        fileBean.setOriginallyPath(file.getAbsolutePath());
        FileUtils.setImageId(mType, file, fileBean);
        mFileViewModel.setFileBeanMimeType(fileBean, mType);
        return fileBean;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int countDirectories(String dirPath, boolean isDirectory) {
        int directoryCount = 0;
        Path path = Paths.get(dirPath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (isDirectory) {
                    if (Files.isDirectory(entry)) {
                        directoryCount++;
                    }
                } else {
                    if (!Files.isDirectory(entry)) {
                        directoryCount++;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directoryCount;
    }

    // 获取文件的创建时间（修正为返回创建时间）
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static long getFileCreationTime(FileBean fileBean) {
        File file = new File(fileBean.getFileUrl());
        try {
            Path filePath = file.toPath();
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            return attrs.lastAccessTime().toMillis(); // 获取创建时间的毫秒数
        } catch (IOException e) {
            e.printStackTrace();
            return Long.MAX_VALUE; // 如果获取失败，则放在最后
        }
    }
}
