package com.hkbyte.filelibrary.runnable;

import static com.yft.zbase.utils.Logger.LOGE;

import com.hkbyte.filelibrary.bean.CopyProgressBean;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;
import com.hkbyte.filelibrary.viewmodel.IFileLoadListener;
import com.hkbyte.filelibrary.viewmodel.IInsertFileListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 批量导入文件
 */
public class InsertDirsFilesRunnable implements Runnable {
    private int mType; // 文件类型， 1视频， 2图片，3音频，4文档
    private String mDirsName;// 文件复制的目标路径文件夹名称
    private List<LocalMedia> mResult; // 需要复制的文件集合
    private IInsertFileListener mFileViewModel; // 通知成功或者失败的ViewModel，主要目的是抛出主线程中运行

    public InsertDirsFilesRunnable(int type, String dirName, List<LocalMedia> result, IInsertFileListener fileViewModel) {
        this.mType = type;
        this.mDirsName = dirName;
        this.mResult = result;
        this.mFileViewModel = fileViewModel;
    }

    @Override
    public void run() {
        // 根目录
        File rootFile = mFileViewModel.getRootFiles(mType);

        if (rootFile == null) {
            mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_EXISTS);
            return;
        }
        // 目标文件
        File targetDir = new File(rootFile.getAbsolutePath() + "/" + mDirsName);
        if (!targetDir.exists()) {
            // 文件夹不存在
            mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_EXISTS);
            return;
        }

        int count = mResult.size();
        int successCount = 0;
        int failureCount = 0;

        for (LocalMedia media : mResult) {
            String sourceFilePath = media.getRealPath(); // 假设LocalMedia有一个getPath()方法返回文件路径
            File sourceFile = new File(sourceFilePath);
            if (!sourceFile.exists()) {
                failureCount++;
                continue;
            }

            String fileName = sourceFile.getName();

            String fileNameWithoutExt = "";
            String newExtension = "";
            File targetFile = null;
            switch (mType){
                case FileViewModel.FILE_DOCUMENTATION:
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf(".")); // 获取文件名（不包含后缀）
                    newExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    break;
                default:{
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf(".")); // 获取文件名（不包含后缀）
                    newExtension = mFileViewModel.getExtension(mType); // 获取自定义的新后缀
                    break;
                }
            }

            targetFile = new File(targetDir, fileNameWithoutExt + newExtension); // 创建目标文件对象

            File[] targets = targetDir.listFiles();
            // 检查目标文件夹下是否存在相同名称的文件。
            boolean whetherExists = whetherExists(targets, fileNameWithoutExt + newExtension);

            if (whetherExists) {
                // 如果存在，就将目标文件名加上 "copy" 字样
                String newFileName = "copy_" + fileNameWithoutExt + newExtension; // 新文件名以 "copy_" 开头
                targetFile = new File(targetDir, newFileName); // 创建新的目标文件对象
                boolean newFileExists = whetherExists(targetDir.listFiles(), newFileName); // 检查新文件名是否也已存在

                // 如果新文件名也已存在，则继续加上序号以确保唯一性
                int copyIndex = 1;
                while (newFileExists) {
                    newFileName = "copy_" + copyIndex + "_" + fileNameWithoutExt + newExtension; // 例如: copy_1_file.hidden
                    targetFile = new File(targetDir, newFileName);
                    newFileExists = whetherExists(targetDir.listFiles(), newFileName); // 再次检查
                    copyIndex++;
                }
            }

            try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
                 FileChannel destChannel = new FileOutputStream(targetFile).getChannel()) {
                // 使用 transferFrom 进行快速复制
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                media.setOriginalPath(targetFile.getAbsolutePath());
                successCount++;
                // 可以通过 mFileViewModel 更新进度，例如：mFileViewModel.updateProgress(successCount, mResult.size());
                CopyProgressBean copyProgressBean = new CopyProgressBean();
                copyProgressBean.setCount(count);
                copyProgressBean.setRightNow(successCount);
                mFileViewModel.pullCopyProgress(copyProgressBean);
            } catch (IOException e) {
                LOGE("InsertDirsFilesRunnable", "" + e.getMessage());
                failureCount++;
            }
        }

        // 复制完成后通知UI
        if (successCount > 0) {
            // 复制成功，
            mFileViewModel.<LocalMedia>onInsertDirsFiles(mResult);
        }

        if (failureCount > 0) {
            mFileViewModel.onFail(FileViewModel.ILoadFiles.ERROR_FILE_COPY);
        }
    }

    public boolean whetherExists(File[] targets, String fileName) {
        // 检查 targets 数组是否为 null 或者为空
        if (targets == null || targets.length == 0) {
            return false; // 没有文件可以检查
        }

        // 遍历 targets 数组中的每个 File 对象
        for (File target : targets) {
            // 检查目标文件是否存在并且名称是否匹配 fileName
            if (target.exists() && target.getName().equals(fileName)) {
                return true; // 找到文件
            }
        }

        // 如果没有文件匹配，返回 false
        return false;
    }
}
