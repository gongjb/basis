package com.hkbyte.filelibrary.viewmodel;

import static com.yft.zbase.utils.Logger.LOGE;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.reflect.TypeToken;
import com.hkbyte.cnbase.util.Constant;
import com.hkbyte.filelibrary.FileCopyService;
import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.util.FileUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IFreeStorage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.ManageThreadPoolService;
import com.yft.zbase.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import droidninja.filepicker.utils.ContentUriUtils;

public class FileViewModel extends BaseViewModel {
    public final static int FILE_VIDEO = 1; // 视频
    public final static int FILE_IMAGE = 2; // 图片
    public final static int FILE_AUDIO = 3; // 音频
    public final static int FILE_DOCUMENTATION = 4; // 文档
    public final static int FILE_MORE = 0; // 更多
    public final static String ROOT_APP_PATH = "/.chameleon"; // 隐藏文件.用于存储相片/视频/文档等
    public final static String ROOT_APP_RECOVER_PATH = "/隐私管家恢复目录"; //
    public final static int COUNT_PAGE = 36; // 每一页的总文件数
    public volatile int thatPage = 0; //  当前页面
    private IFreeStorage mFreeStorage;
    private LinkedList<FileBean> mFileLinkList;
    // 文件加载成功
    private MutableLiveData<List<FileBean>> mFileBeanMutableLiveData = new MutableLiveData<>();
    // 失败通知
    private MutableLiveData<Integer> mLoadFileErrorMutableLiveData = new MutableLiveData<>();
    // 创建成功通知
    private MutableLiveData<String> mCreateDirsMutableLiveData = new MutableLiveData<>();
    // 复制成功通知
    private MutableLiveData<Boolean> mCopyMutableLiveData = new MutableLiveData<>();
    // 复制进度
    private MutableLiveData<FileBean> mCopyProgressBeanMutableLiveData = new MutableLiveData<>();
    // 复制进度
    private MutableLiveData<FileBean> mCountCopyProgressBeanMutableLiveData = new MutableLiveData<>();
    // s
    private MutableLiveData<Integer> mDeleteNullMutableLiveData = new MutableLiveData<>();
    //插入文件缓存
    private List<LocalMedia> mLocalMedias;
    // 预览文件缓存
    private ArrayList<LocalMedia> mStorageFileBeans;

    public List<LocalMedia> getmLocalMedias() {
        return mLocalMedias;
    }

    public ArrayList<LocalMedia> getStorageFileBeans() {
        return mStorageFileBeans;
    }

    public FileModel mFileModel;

    private final Messenger incomingMessenger = new Messenger(new IncomingHandler()); // 用于处理 Service 发送的消息

    public FileViewModel() {
        mFileModel = new FileModel();
        mFileLinkList = new LinkedList<>();
        mFreeStorage = DynamicMarketManage.getInstance().getServer(IServerAgent.FREE_STORAGE);
    }

    public IFreeStorage getFreeStorage() {
        return mFreeStorage;
    }

    public MutableLiveData<List<FileBean>> getFileBeanMutableLiveData() {
        return mFileBeanMutableLiveData;
    }

    public MutableLiveData<Boolean> getCopyMutableLiveData() {
        return mCopyMutableLiveData;
    }


    public MutableLiveData<Integer> getLoadFileErrorMutableLiveData() {
        return mLoadFileErrorMutableLiveData;
    }

    public MutableLiveData<String> getCreateDirsMutableLiveData() {
        return mCreateDirsMutableLiveData;
    }

    public MutableLiveData<FileBean> getCopyProgressBeanMutableLiveData() {
        return mCopyProgressBeanMutableLiveData;
    }

    public MutableLiveData<FileBean> getCountCopyProgressBeanMutableLiveData() {
        return mCountCopyProgressBeanMutableLiveData;
    }

    public MutableLiveData<Integer> getDeleteNullMutableLiveData() {
        return mDeleteNullMutableLiveData;
    }

    public LinkedList<FileBean> getFileLinkList() {
        return mFileLinkList;
    }

    public int getThatPage() {
        return thatPage;
    }

    private ArrayList<LocalMedia> mServiceMediaList = new ArrayList<>();

    public void wrapToAddEntities(String name, int imageUrl) {
        FileBean addEndBean = new FileBean();
        addEndBean.setType(FILE_MORE);
        addEndBean.setName(name);
        addEndBean.setImageIntId(imageUrl);
        if (!Utils.isCollectionEmpty(mFileLinkList)) {
            Iterator<FileBean> iterator = mFileLinkList.iterator();
            while (iterator.hasNext()) {
                FileBean link = iterator.next();
                if (link.getType() == FILE_MORE) { // 检查条件，这里以 null 为例
                    iterator.remove(); // 删除当前元素
                }
            }
        }
        mFileLinkList.addLast(addEndBean);
    }

    public void cleanServiceMediaList() {
        mServiceMediaList.clear();
    }

    public List<LocalMedia> getServiceMediaList() {
        return mServiceMediaList;
    }

    /**
     * 寻找目录
     *
     * @param type 文件类型
     * @return
     */
    public void loadDirFiles(int type) {
        mFileModel.loadDirFiles(type, new IFileLoadListener<FileBean>() {
            @Override
            public void onFail(int errorCode) {
                getLoadFileErrorMutableLiveData().postValue(errorCode);
            }

            @Override
            public File getRootFiles(int type) {
                return FileViewModel.this.getRootFiles(type);
            }

            @Override
            public void setFileBeanMimeType(FileBean fileBean, int type) {

            }

            @Override
            public void onLoadDirsFiles(List<FileBean> list) {
                getFileBeanMutableLiveData().postValue(list);
            }
        });
    }

    /**
     * 寻找目录下的文件
     *
     * @param type
     * @param dirsName
     */
    public void loadDirsToFiles(int type, String dirsName) {
        thatPage = thatPage + 1; //
        mFileModel.loadDirsToFiles(type, dirsName, new IFileLoadListener<FileBean>() {
            @Override
            public void onLoadDirsFiles(final List<FileBean> list) {
                getFileBeanMutableLiveData().postValue(list);
            }

            @Override
            public void onFail(int errorCode) {
                getLoadFileErrorMutableLiveData().postValue(errorCode);
            }

            @Override
            public File getRootFiles(int type) {
                return FileViewModel.this.getRootFiles(type);
            }

            @Override
            public void onLoadDirsDeleteNull(int thatPage) {
                getDeleteNullMutableLiveData().postValue(thatPage);
            }

            @Override
            public void setFileBeanMimeType(FileBean fileBean, int type) {
                FileViewModel.this.setFileBeanMimeType(fileBean, type);
            }
        }, thatPage);
    }


    /**
     * 排序逻辑 (将newlyAdded倒序插入alreadyExists)
     *
     * @param alreadyExists 已经存在的历史（被加入的集合）
     * @param newlyAdded
     * @return
     */
    public List<FileBean> sortReverseOrder(List<FileBean> alreadyExists, List<FileBean> newlyAdded) {

        // 如果 alreadyExists 为空，则返回 newlyAdded 的倒序版本
        if (Utils.isCollectionEmpty(alreadyExists)) {
            alreadyExists = new ArrayList<>();
//            if (!Utils.isCollectionEmpty(newlyAdded)) {
//                for (int i =0; i < newlyAdded.size(); i ++) {
//                    alreadyExists.add(newlyAdded.get(i)); // 将 newlyAdded 倒序添加到 alreadyExists
//                }
//            }
//            return alreadyExists;
        }

        if (Utils.isCollectionEmpty(newlyAdded)) {
            return alreadyExists;
        }

        // 从 newlyAdded 列表的最后一个元素开始插入
        for (int i = newlyAdded.size() - 1; i >= 0; i--) {
            alreadyExists.add(0, newlyAdded.get(i)); // 插入到已经存在集合的开头
        }

        return alreadyExists;
    }

    /**
     * @param result
     * @return
     */
    public List<FileBean> getFileBeans(ArrayList<LocalMedia> result, int type, String mDirsName) {
        if (Utils.isCollectionEmpty(result)) {
            return new ArrayList<>();
        }
        List<FileBean> fileBeanList = new ArrayList<>();
        ArrayList<String> sourcePaths = new ArrayList<>();
        ArrayList<String> destPaths = new ArrayList<>();
        gengeratePathsInBatchesV2(type, mDirsName, result, sourcePaths, destPaths);

        for (int i = 0; i < result.size(); i++) {
            LocalMedia media = result.get(i);
            FileBean fileBean = new FileBean();
            File f = new File(destPaths.get(i));
            fileBean.setName(f.getName());
            fileBean.setFileUrl(sourcePaths.get(i)); // 原路径
            fileBean.setOriginallyPath(destPaths.get(i)); // 目标路径
            fileBean.setType(type);
            fileBean.setProgress(0); // 初始化进度
            File file = new File(media.getRealPath());
            fileBean.setMimeType(FileUtils.supplementalFields(file));
            FileUtils.setImageId(type, file, fileBean); // 补全图片
            fileBeanList.add(fileBean);
        }
        return fileBeanList;
    }


    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case FileCopyService.FILE_COPY_COMPLETE_PROGRESS:
                        String obj = (String) msg.obj;
                        String[] objStrs = obj.split(",");
                        FileBean fileBean = new FileBean();
                        fileBean.setFileUrl(objStrs[0]); // 原来的路径
                        fileBean.setOriginallyPath(objStrs[1]); // 目标路径
                        fileBean.setRecover(Boolean.valueOf(objStrs[2]));
                        fileBean.setProgress(msg.arg1); // 进度
                        getCopyProgressBeanMutableLiveData().postValue(fileBean);
                        break;
                    case FileCopyService.FILE_COPY_COMPLETE:
                        obj = (String) msg.obj;
                        objStrs = obj.split(",");
                        LocalMedia localMedia = LocalMedia.generateLocalMedia(ZBaseApplication.get(), objStrs[0]); // 原路径
                        localMedia.setOriginalPath(objStrs[1]); // 目标路径
                        mServiceMediaList.add(localMedia);
                        FileBean fileBean1 = new FileBean();
                        fileBean1.setFileUrl(objStrs[0]); // 原来的路径
                        fileBean1.setOriginallyPath(objStrs[1]); // 目标路径
                        fileBean1.setRecover(Boolean.valueOf(objStrs[2]));
                        fileBean1.setProgress(100); // 进度
                        getCountCopyProgressBeanMutableLiveData().postValue(fileBean1);
                        break;
                    case FileCopyService.FILE_COPY_COMPLETE_SUCCESS:
                        obj = (String) msg.obj;
                        objStrs = obj.split(",");
                        boolean isRecover = Boolean.valueOf(objStrs[2]);

                        getCopyMutableLiveData().postValue(isRecover);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 插入文件夹中
     */
    public void insertDirsToFiles(int mType, String mDirsName, ArrayList<LocalMedia> mResult, Context context) {
        mLocalMedias = mResult;
        ArrayList<String> sourcePaths = new ArrayList<>();
        ArrayList<String> destPaths = new ArrayList<>();
        // 批量生成目标路径
        gengeratePathsInBatchesV2(mType, mDirsName, mResult, sourcePaths, destPaths);
        if (!Utils.isCollectionEmpty(sourcePaths)) {
            // 启动前台服务
            Intent intent = new Intent(context, FileCopyService.class);
            intent.putStringArrayListExtra("sourcePaths", sourcePaths);
            intent.putStringArrayListExtra("destPaths", destPaths);
            intent.putExtra("messenger", incomingMessenger);
            context.startService(intent);
        }
    }

    /**
     * 批量导出文件
     */
    public void insertFilesToRecover(int mType, List<FileBean> mResult, Context context) {
        ArrayList<String> sourcePaths = new ArrayList<>();
        ArrayList<String> destPaths = new ArrayList<>();
        // 批量生成目标路径
        gengerateToRecoverPathsInBatches(mType, mResult, sourcePaths, destPaths);
        if (!Utils.isCollectionEmpty(sourcePaths)) {
            // 启动前台服务
            Intent intent = new Intent(context, FileCopyService.class);
            intent.putStringArrayListExtra("sourcePaths", sourcePaths);
            intent.putStringArrayListExtra("destPaths", destPaths);
            intent.putExtra("recover", true); // 使用恢复功能
            intent.putExtra("messenger", incomingMessenger);
            context.startService(intent);
        }
    }

    /**
     * 通过ArrayList<LocalMedia> 生成原始路径与目标路径
     * @param mType 文件类型
     * @param mResult 数据源
     * @param sourcePaths 原始路径
     * @param destPaths 目标路径
     */
    public void gengerateToRecoverPathsInBatches(int mType, List<FileBean> mResult, ArrayList<String> sourcePaths, ArrayList<String> destPaths) {
        // 目标文件
        File targetDir = getRestoreDirectory(mType);
        if (!targetDir.exists()) {
            // 文件夹不存在,创建一个
            targetDir.mkdirs();
        }
        for (FileBean media : mResult) {
            String sourceFilePath = media.getOriginallyPath(); //
            File sourceFile = new File(sourceFilePath);
            if (!sourceFile.exists()) {
                continue;
            }

            String fileName = sourceFile.getName();

            String fileNameWithoutExt = "";
            String newExtension = "";
            File targetFile = null;
            switch (mType) {
                case FileViewModel.FILE_DOCUMENTATION:
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf(".")); // 获取文件名（不包含后缀）
                    newExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    break;
                default: {
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf(".")); // 获取文件名（不包含后缀）
                    newExtension = getRecoverExtension(mType, sourceFilePath); // 获取自定义的新后缀
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
            sourcePaths.add(sourceFile.getAbsolutePath());
            destPaths.add(targetFile.getAbsolutePath());
        }
    }

    /**
        还原后缀
     */
    private String getRecoverExtension(int mType, String filePath) {
        String extension = FileUtils.getRecoverExtension(filePath);
        if (TextUtils.isEmpty(extension)) {
            // 兜底
            switch (mType) {
                case FILE_IMAGE:
                    extension = ".jpeg";
                    break;
                case FILE_VIDEO:
                    extension = ".mp4";
                    break;
                case FILE_AUDIO:
                    extension = ".mp3";
                    break;
                default:{
                    extension = filePath.substring(filePath.lastIndexOf("."));
                }
            }
            return extension;
        } else {
            return extension;
        }
    }

    /**
     * 通过ArrayList<LocalMedia> 生成原始路径与目标路径(自动分页版本)
     * @param mType 文件类型
     * @param mDirsName 根目录
     * @param mResult 数据源
     * @param sourcePaths 原始路径
     * @param destPaths 目标路径
     */
    public void gengeratePathsInBatchesV2(int mType, String mDirsName, ArrayList<LocalMedia> mResult, ArrayList<String> sourcePaths, ArrayList<String> destPaths) {
        // 根目录
        File rootFile = getRootFiles(mType);
        // 目标文件根目录
        File targetRootDir = new File(rootFile.getAbsolutePath() + "/" + mDirsName);
        if (!targetRootDir.exists()) {
            targetRootDir.mkdirs(); // 创建根目录
        }

        int pageSize = COUNT_PAGE;
        int existingFilesCount = countExistingFiles(targetRootDir); // 统计文件数量
        int currentFolderIndex = existingFilesCount / pageSize + 1; // 计算当前应放入的文件夹索引
        int filesInCurrentFolder = existingFilesCount % pageSize; // 当前文件夹中已存在的文件数量

        for (LocalMedia media : mResult) {
            String sourceFilePath = media.getRealPath();
            File sourceFile = new File(sourceFilePath);
            if (!sourceFile.exists()) {
                continue; // 如果源文件不存在，跳过
            }

            String fileName = sourceFile.getName();
            String fileNameWithoutExt;
            String newExtension;

            switch (mType) {
                case FileViewModel.FILE_DOCUMENTATION:
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
                    newExtension = fileName.substring(fileName.lastIndexOf(".")); // 获取后缀
                    break;
                default:
                    fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
                    newExtension = getExtension(mType); // 获取自定义的新后缀
                    break;
            }

            // 如果当前文件夹已满，切换到下一个文件夹
            if (filesInCurrentFolder >= pageSize) {
                currentFolderIndex++;
                filesInCurrentFolder = 0; // 重置当前文件夹文件计数
            }

            // 创建目标文件夹
            File targetDir = new File(targetRootDir, String.valueOf(currentFolderIndex));
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // 创建新的目标文件夹
            }

            // 设置目标文件的路径
            File targetFile = new File(targetDir, fileNameWithoutExt + newExtension);

            // 检查目标文件夹下是否存在相同名称的文件
            boolean whetherExists = whetherExists(targetDir.listFiles(), fileNameWithoutExt + newExtension);
            if (whetherExists) {
                String newFileName = "copy_" + fileNameWithoutExt + newExtension;
                targetFile = new File(targetDir, newFileName);
                boolean newFileExists = whetherExists(targetDir.listFiles(), newFileName);

                int copyIndex = 1;
                while (newFileExists) {
                    newFileName = "copy_" + copyIndex + "_" + fileNameWithoutExt + newExtension;
                    targetFile = new File(targetDir, newFileName);
                    newFileExists = whetherExists(targetDir.listFiles(), newFileName);
                    copyIndex++;
                }
            }

            // 添加源和目标路径
            sourcePaths.add(sourceFile.getAbsolutePath());
            destPaths.add(targetFile.getAbsolutePath());

            filesInCurrentFolder++; // 当前文件夹文件数量加1
        }
    }

    // 优化后的文件统计方法
    private int countExistingFiles(File targetRootDir) {
        int existingFilesCount = 0;
        try (DirectoryStream<Path> folderStream = Files.newDirectoryStream(targetRootDir.toPath(), Files::isDirectory)) {
            for (Path folder : folderStream) {
                // 使用 DirectoryStream 统计文件数量
                try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(folder)) {
                    for (Path file : fileStream) {
                        existingFilesCount++; // 统计文件数量
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingFilesCount;
    }

    public String getExtension(int type) {
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

    /**
     * 创建目录
     *
     * @param mType
     * @param name
     */
    public void createDirs(int mType, String name) {
        File file = getRootFiles(mType);
        if (file == null) {
            // 目录创建创建
            getLoadFileErrorMutableLiveData().postValue(ILoadFiles.ERROR_FILE_NULL);
            return;
        }


        File newDirs = new File(file.getAbsoluteFile() + "/" + name);

        if (newDirs.exists()) {
            // 文件夹已存在
            getLoadFileErrorMutableLiveData().postValue(ILoadFiles.ERROR_FILE_EXISTS);
            return;
        }

        if (!newDirs.mkdirs()) {
            getLoadFileErrorMutableLiveData().postValue(ILoadFiles.ERROR_FILE_CREATE);
            return;
        }

        // 创建目录成功
        getCreateDirsMutableLiveData().postValue(name);
    }

    public void packagingFiles(List<FileBean> fileBeanList) {
        if (!Utils.isCollectionEmpty(fileBeanList)) {
            mFileLinkList.clear();
            for (FileBean fileBean : fileBeanList) {
                mFileLinkList.add(fileBean);
            }
        }
    }

    /**
     * 获取对应文件类型的根目录
     *
     * @param mType
     * @return
     */
    public File getRootFiles(int mType) {
        File rootDirFile = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File chameleonFiles = null;
        switch (mType) {
            case FILE_VIDEO:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_PATH + "/a"); // 不使用看一眼就明白单词做文件名
                break;
            case FILE_IMAGE:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_PATH + "/b");
                break;
            case FILE_AUDIO:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_PATH + "/c");
                break;
            case FILE_DOCUMENTATION:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_PATH + "/d");
                break;
            default: {
                return null;
            }
        }
        return chameleonFiles;
    }


    /**
     * 获取对应文件类型的恢复目录
     *
     * @param mType
     * @return
     */
    public File getRestoreDirectory(int mType) {
        File rootDirFile = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File chameleonFiles = null;
        switch (mType) {
            case FILE_VIDEO:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_RECOVER_PATH + "/video"); // 不使用看一眼就明白单词做文件名
                break;
            case FILE_IMAGE:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_RECOVER_PATH + "/image");
                break;
            case FILE_AUDIO:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_RECOVER_PATH + "/audio");
                break;
            case FILE_DOCUMENTATION:
                chameleonFiles = new File(rootDirFile.getAbsoluteFile() + ROOT_APP_RECOVER_PATH + "/document");
                break;
            default: {
                return null;
            }
        }
        return chameleonFiles;
    }

    /**
     * 获取文件类型
     *
     * @param mType
     * @return
     */
    public String getFileType(int mType) {
        switch (mType) {
            case FILE_VIDEO:
                return PictureMimeType.ofMP4();
            case FILE_IMAGE:
                return PictureMimeType.ofJPEG();
            case FILE_AUDIO:
                return PictureMimeType.MIME_TYPE_AUDIO_AMR; //MIME_TYPE_AUDIO_AMR
            case FILE_DOCUMENTATION:
                return "DOCX";
            default: {
                return ".hidde";
            }
        }
    }


    /**
     * 获取文件类型
     *
     * @param mType
     * @return
     */
    public int getSelectNumber(int mType) {
        switch (mType) {
            case FILE_VIDEO:
                return 30;
            case FILE_IMAGE:
                return 100;
            case FILE_AUDIO:
                return 30; //MIME_TYPE_AUDIO_AMR
            case FILE_DOCUMENTATION:
                return 30;
            default: {
                return 20;
            }
        }
    }

    public ArrayList<LocalMedia> uriToFileBean(Context context, List<Uri> arrayUri) {
        ArrayList<LocalMedia> fileBeanList = new ArrayList<>();
        if (Utils.isCollectionEmpty(arrayUri)) {
            return fileBeanList;
        }

        for (Uri uri : arrayUri) {
            try {
                String filePath = ContentUriUtils.INSTANCE.getFilePath(context, uri);
                LocalMedia localMedia = LocalMedia.generateLocalMedia(context, filePath);
                fileBeanList.add(localMedia);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return fileBeanList;
    }


    /**
     * 获取缓存文件信息
     *
     * @param type
     * @return
     */
    public List<FileBean> getStorageFile(int type) {
        switch (type) {
            case FILE_VIDEO:
                return getFreeStorage().<FileBean>getList(Constant.VIDEO_FILE_INFO_LIST, new TypeToken<List<FileBean>>() {
                }.getType());
            case FILE_IMAGE:
                return getFreeStorage().<FileBean>getList(Constant.IMAGE_FILE_INFO_LIST, new TypeToken<List<FileBean>>() {
                }.getType());
            case FILE_AUDIO:
                return getFreeStorage().<FileBean>getList(Constant.AUDIO_FILE_INFO_LIST, new TypeToken<List<FileBean>>() {
                }.getType());
            case FILE_DOCUMENTATION:
                return getFreeStorage().<FileBean>getList(Constant.DOCX_FILE_INFO_LIST, new TypeToken<List<FileBean>>() {
                }.getType());
            default: {
                return null;
            }
        }
    }

    public void addPreview(List<LocalMedia> localMedia) {
        if (!Utils.isCollectionEmpty(localMedia)) {
            if (Utils.isCollectionEmpty(mStorageFileBeans)) {
                mStorageFileBeans = new ArrayList<>();
            }
            mStorageFileBeans.addAll(localMedia);
        }
    }

    /**
     * 提前加载好预览信息
     *
     * @param f
     * @param context
     * @param mType
     */
    public void loadPreview(List<FileBean> f, Context context, int mType) {
        // 封装预览数据
        mStorageFileBeans = new ArrayList<>();
        for (FileBean fileBean : f) {
            LocalMedia localMedia = joinMimeType(fileBean, context, mType);
            mStorageFileBeans.add(localMedia);
        }
    }


    /**
     * 补全mime
     * @param f
     * @param mType
     */
    public void setFileBeanMimeType(FileBean f, int mType) {
        //1. 读本地缓存。
        List<FileBean> fileBeanList = getStorageFile(mType);
        // 2. 匹配目标路径
        if (!Utils.isCollectionEmpty(fileBeanList)) {
            for (FileBean files : fileBeanList) {
                if (TextUtils.isEmpty(files.getOriginallyPath()) || TextUtils.isEmpty(f.getOriginallyPath())) {
                    continue;
                }

                if (files.getOriginallyPath().equals(f.getOriginallyPath())) {
                    // 文件比配上了
                    LOGE("====缓存匹配成功目标路径=>" + files.getOriginallyPath() + "原路径" + files.getFileUrl() + "==type=>" + files.getMimeType());
                    f.setMimeType(files.getMimeType());
                    break;
                }
            }
        }

        if (TextUtils.isEmpty(f.getMimeType())) {
            // 还是空，优先取
            f.setMimeType(f.getMimeType());
        }


        if (TextUtils.isEmpty(f.getMimeType())) {
            // 最后的兜底
            f.setMimeType(getFileType(mType));
        }
    }


    private LocalMedia joinMimeType(FileBean f, Context context, int mType) {
        // 进过混淆之后， 已经无法判断文件的具体类型
        LocalMedia localMedia = LocalMedia.generateLocalMedia(context, f.getFileUrl());
        localMedia.setMimeType("");
        // 还原文件具体类型
        //1. 读本地缓存。
        List<FileBean> fileBeanList = getStorageFile(mType);
        // 2. 匹配目标路径
        if (!Utils.isCollectionEmpty(fileBeanList)) {
            for (FileBean files : fileBeanList) {
                if (TextUtils.isEmpty(files.getOriginallyPath()) || TextUtils.isEmpty(localMedia.getRealPath())) {
                    continue;
                }

                if (files.getOriginallyPath().equals(localMedia.getRealPath())) {
                    // 文件比配上了
                    LOGE("====缓存匹配成功目标路径=>" + files.getOriginallyPath() + "原路径" + files.getFileUrl() + "==type=>" + files.getMimeType());
                    localMedia.setMimeType(files.getMimeType());
                    break;
                }
            }
        }

        if (TextUtils.isEmpty(localMedia.getMimeType())) {
            // 还是空，优先取
            localMedia.setMimeType(f.getMimeType());
        }


        if (TextUtils.isEmpty(localMedia.getMimeType())) {
            // 最后的兜底
            localMedia.setMimeType(getFileType(mType));
        }
        return localMedia;
    }


    /**
     * 保存List集合
     *
     * @param type
     * @param fileBeanList
     * @return
     */
    public boolean saveFileInfo(int type, List<FileBean> fileBeanList) {
        String key = "";
        switch (type) {
            case FILE_VIDEO:
                key = Constant.VIDEO_FILE_INFO_LIST;
                break;
            case FILE_IMAGE:
                key = Constant.IMAGE_FILE_INFO_LIST;
                break;
            case FILE_AUDIO:
                key = Constant.AUDIO_FILE_INFO_LIST;
                break;
            case FILE_DOCUMENTATION:
                key = Constant.DOCX_FILE_INFO_LIST;
                break;
            default: {
                break;
            }
        }

        if (TextUtils.isEmpty(key)) {
            return false;
        }

        return getFreeStorage().saveList(key, fileBeanList);
    }

    /**
     * 批量删除文件
     *
     * @param localMedias
     */
    public int deleteLocalFiles(List<FileBean> localMedias, Context context, int type, boolean isFileUrl) {
        if (Utils.isCollectionEmpty(localMedias)) {
            return -1;
        }

        int count = 0;
        for (FileBean localMedia : localMedias) {
            File file;
            if (isFileUrl) {
                file = new File(localMedia.getFileUrl());
            } else {
                file = new File(localMedia.getOriginallyPath());
            }
            boolean isDelete = file.delete();
            if (isDelete) {
                count++; // 计数
                if (type == FILE_DOCUMENTATION) {
                    // 文档不需要通知
                    continue;
                }
                // 通知 MediaStore 更新
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    Uri uri = null;
                    switch (type) {
                        case FILE_VIDEO:
                            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI; // 对于视频
                            break;
                        case FILE_AUDIO:
                            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // 对于视频
                            break;
                        case FILE_IMAGE:
                            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // 对于视频
                            break;
                        default: {
                            // 文档不需要通知
                            continue;
                        }
                    }
                    // Android 10 及以上版本
                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.delete(uri, MediaStore.MediaColumns.DATA + "=?", new String[]{file.getAbsolutePath()});
                } else {
                    // Android 9 及以下版本
                    MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
                }
            }
        }

        return count;
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


public interface ILoadFiles {
    int ERROR_FILE_NULL = 0; // 空文件
    int ERROR_FILE_CREATE = 1; // 文件创建失败
    int ERROR_FILE_TYPE = 2; //文件类型错误
    int ERROR_FILE_EXISTS = 3; // 已存在
    int ERROR_FILE_COPY = 4; // 复制出错
    int ERROR_IO = 5;
    int LAST_PAGE = 6; // 最后一夜
}
}
