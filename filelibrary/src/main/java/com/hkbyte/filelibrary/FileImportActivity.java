package com.hkbyte.filelibrary;

import static com.yft.zbase.utils.Logger.LOGE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chenenyu.router.annotation.Route;
import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.LatticeView;
import com.hkbyte.cnbase.listener.OnAdapterLongClickListener;
import com.hkbyte.file.R;
import com.hkbyte.file.databinding.ActivityFileImportLayoutBinding;
import com.hkbyte.filelibrary.adapter.EasyAdapter;
import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.router.FileRouter;
import com.hkbyte.filelibrary.ui.MoreFileLayout;
import com.hkbyte.filelibrary.ui.RenameFragmentDialog;
import com.hkbyte.filelibrary.util.FileUtils;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelectionPreviewModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnRecordAudioInterceptListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.permissions.PermissionResultCallback;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yft.zbase.adapter.GridListSpaceItemDecoration;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.ui.FragmentMessageDialog;
import com.yft.zbase.utils.ImageGlideLoader;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * 文件导入
 */
@Route(FileRouter.FILE_IMPORT_ACTIVITY)
public class FileImportActivity extends BaseActivity<ActivityFileImportLayoutBinding, FileViewModel> {
    // 文件类型
    private int mType;
    // 导入目标文件
    private String mDirsName;
    private EasyAdapter<FileBean, ?> mEasyAdapter;
    private LatticeView.ImageTextParams<ImageGlideLoader> mImageTextParams;
    private MoreFileLayout mDefineLoadMoreView;

    @Override
    public void initView() {
        // 功能模块视图
        mType = getIntent().getIntExtra("type", FileViewModel.FILE_VIDEO);
        mDirsName = getIntent().getStringExtra("name");
        mDataBing.tlt.setTitle(mDirsName);
        mDataBing.tlt.setLeftBackImage();
        mDataBing.tlt.setBackgroundColor(getResources().getColor(com.hkbyte.cnbase.R.color.chameleon_theme_color));
        switch (mType) {
            case FileViewModel.FILE_IMAGE:
            case FileViewModel.FILE_VIDEO:
                // 视频图片
                mEasyAdapter = new EasyAdapter(R.layout.item_file_video_layout);
                break;
            case FileViewModel.FILE_AUDIO:
            default:
                mEasyAdapter = new EasyAdapter(R.layout.item_file_dirs_video_layout);
        }

        mEasyAdapter.setTag(false);
        mEasyAdapter.setToXmlPosition(true);

        mDefineLoadMoreView = new MoreFileLayout(this);
        mDefineLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDataBing.rvVideo.addFooterView(mDefineLoadMoreView); // 使用默认的加载更多的View。
        mDataBing.rvVideo.setLoadMoreView(mDefineLoadMoreView);
        mDataBing.rvVideo.setLoadMoreListener(mLoadMoreListener); // 加载更多的监听
//        defineLoadMoreView.setVisibility(View.INVISIBLE);

        //指定列表线性布局，横向水平
        GridLayoutManager lm = new GridLayoutManager(FileImportActivity.this, 4);
        mDataBing.rvVideo.addItemDecoration(new GridListSpaceItemDecoration(Utils.dip2px(this, 10)));
        mDataBing.rvVideo.setLayoutManager(lm);
        mDataBing.rvVideo.setAdapter(mEasyAdapter);

        mImageTextParams = new BaseLatticeView.ImageTextParams<>();
        mImageTextParams.images = new Object[]{R.mipmap.icon_file_doc, R.mipmap.icon_file_audio, R.mipmap.icon_file_doc, R.mipmap.icon_file_doc};
        mImageTextParams.text = new String[]{"导出到本地", "重命名", "删除", "导入"};
        mImageTextParams.maxLine = 4; // 每一行显示的个数
        mImageTextParams.imageHigh = 55;
        mImageTextParams.imageLoader = new ImageGlideLoader();
        mImageTextParams.imageWidth = 55;
        mImageTextParams.textSize = 12; // text的字体大小
        mImageTextParams.textPaddingTop = LatticeView.dip2px(this, 4); // 字体向上给一个padding
        mImageTextParams.selectIndex = 0; // 默认第一个被选中
        mImageTextParams.textColor = com.hkbyte.cnbase.R.color.white; // 字体默认颜色
        mImageTextParams.imageType = ImageView.ScaleType.FIT_XY;
        mImageTextParams.bg_color = "#00000000";
        mDataBing.llView.setImageTextParams(mImageTextParams);
        mDataBing.llView.startView(); // 开始加载布局
    }

    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            // 该加载更多啦。
            mViewModel.loadDirsToFiles(mType, mDirsName);
        }
    };

    private void createAnimator() {
        ValueAnimator animator2 = ValueAnimator.ofFloat(0.6f, 1f);
        animator2.addUpdateListener(valueAnimator -> mDataBing.flFun.setAlpha((Float) valueAnimator.getAnimatedValue()));
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDataBing.flFun, "translationY", 300f, 0); // 向上移动300像素
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.playTogether(animator2, animator);
        animatorSet.start();
    }

    private void createAnimatorHide() {
        ValueAnimator animator2 = ValueAnimator.ofFloat(1f, 0.6f);
        animator2.addUpdateListener(valueAnimator -> mDataBing.flFun.setAlpha((Float) valueAnimator.getAnimatedValue()));

        ObjectAnimator animator = ObjectAnimator.ofFloat(mDataBing.flFun, "translationY", 0f, 300f); // 向上移动300像素

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.playTogether(animator2, animator);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (FileImportActivity.this.isFinishing()) {
                    return;
                }
                mDataBing.flFun.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
    }

    @Override
    public void initListener() {
        mViewModel.getFileBeanMutableLiveData().observe(this, this::onSuccess);
        mViewModel.getLoadFileErrorMutableLiveData().observe(this, this::onFail);
        mViewModel.getCopyMutableLiveData().observe(this, this::onCopySuccess);
        mViewModel.getCopyProgressBeanMutableLiveData().observe(this, this::onProgress);
        mViewModel.getCountCopyProgressBeanMutableLiveData().observe(this, this::onCountProgress);
        mViewModel.getDeleteNullMutableLiveData().observe(this, this::onDeleteNull);
        mDataBing.cvAdd.setOnClickListener(this::openImageSelector);
        mEasyAdapter.setOnItemClickListener((view, data, position) -> {
            if (data == null) {
                return;
            }

            if (view.getId() == R.id.check_box_fl) {
                mEasyAdapter.getData().get(position).setCheck(!data.isCheck());
                mEasyAdapter.notifyItemChanged(position);
                return;
            }

            if ((Boolean) mEasyAdapter.getTag()) {
                // 编辑模式下， 不响应点击事件
                return;
            }

            exFunViewHide();
            switch (data.getType()) {
                case FileViewModel.FILE_DOCUMENTATION:
                    try {
                        FileUtils.previewFile(this, data.getFileUrl());
                    } catch (Exception e) {
                        UIUtils.showToast(getString(R.string.find_preview_error_tips));
                    }
                    break;
                default: {
                    // 图片、音频、视频都可以使用预览模式
                    PictureSelectionPreviewModel pictureSelectionPreviewModel = PictureSelector.create(this).openPreview();
                    pictureSelectionPreviewModel.setSelectorUIStyle(getPictureSelectorStyle());
                    pictureSelectionPreviewModel.setImageEngine(GlideEngine.createGlideEngine())
                            .startActivityPreview(position, false, mViewModel.getStorageFileBeans());
                }
            }
        });

        mEasyAdapter.setOnLongItemClickListener((view, data, position) -> {
            mDataBing.flFun.setVisibility(View.VISIBLE);
            data.setCheck(true);
            // 打开编辑模式
            mEasyAdapter.setTag(true);
            mEasyAdapter.notifyDataSetChanged();
            createAnimator();
            return true;
        });

        mDataBing.llView.setOnPageItemOnClickListener(new BaseLatticeView.OnPageItemOnClickListener() {
            @Override
            public void onClick(View v, Object[] urls, int position) {
                List<FileBean> fileBeanList = null;

                if (!Utils.isCollectionEmpty(mEasyAdapter.getData())) {

                    fileBeanList = mEasyAdapter.getData()
                            .stream().filter(fileBean -> fileBean.isCheck())
                            .collect(Collectors.toList());

                    long copying = mEasyAdapter.getData().stream().filter(fileBean -> fileBean.getProgress() < 100).count();
                    if (copying > 0) {
                        // 正在copy进行中
                        ToastUtils.toast(getString(R.string.task_in_progress_tips));
                        return;
                    }
                }

                switch (position) {
                    case 0:
                        // 导出到本地
                        if (Utils.isCollectionEmpty(fileBeanList)) {
                            ToastUtils.toast(getString(R.string.select_exported_file_tips));
                            return;
                        }
                        ToastUtils.toast(getString(R.string.exporting_in_background_tips));
                        // 开始导出
                        mViewModel.insertFilesToRecover(mType, fileBeanList, FileImportActivity.this);
                        break;
                    case 1:
                        // 重命名
                        rename(fileBeanList);
                        break;
                    case 2:
                        // 删除文件
                        deleteFiles(fileBeanList);
                        break;
                    case 3:
                        // 直接调用导入的方法
                        openImageSelector(v);
                        exFunViewHide();
                        break;
                    default: {
                    }
                }
            }
        });
    }

    private void rename(List<FileBean> fileBeanList) {
        if (Utils.isCollectionEmpty(fileBeanList)) {
            ToastUtils.toast("请选择重命名的文件");
            return;
        }
        if (fileBeanList.size() != 1) {
            ToastUtils.toast("重命名只支持文件单选");
            return;
        }

        final FileBean fileBean = fileBeanList.get(0);
        if (fileBean == null) {
            return;
        }

        int mp = getMp(fileBean);
        RenameFragmentDialog.newInstance(fileBean.getName(), mp).setRenameListener(new RenameFragmentDialog.IRenameListener() {
            @Override
            public void onRename(String newName, String oblName, int position) {
                // 获取旧路径
                String odlPath = mEasyAdapter.getData().get(position).getOriginallyPath();
                File file = new File(odlPath);
                File parentFile = file.getParentFile();
                File namePath = new File(parentFile, newName);
                String newNamePath = namePath.getAbsolutePath();
                boolean isSuccess = FileUtils.renameFile(odlPath, newNamePath);
                if (isSuccess) {
                    mEasyAdapter.getData().get(position).setFileUrl(newNamePath);
                    mEasyAdapter.getData().get(position).setOriginallyPath(newNamePath);
                    mEasyAdapter.getData().get(position).setName(namePath.getName());
                    mEasyAdapter.notifyItemChanged(position);
                } else {
                    ToastUtils.toast("重命名失败了，请检查文件管理权限是否打开");
                }
            }
        }).show(getSupportFragmentManager(), "rename-dialog");
    }

    /**
     * @param fileBean
     * @return
     */
    private int getMp(FileBean fileBean) {
        int mp = 0;
        for (int i = 0; i < mEasyAdapter.getData().size(); i++) {
            FileBean bn = mEasyAdapter.getData().get(i);
            if (bn == null || TextUtils.isEmpty(bn.getOriginallyPath())) {
                continue;
            }
            if (bn.getOriginallyPath().equals(fileBean.getOriginallyPath())) {
                mp = i;
                break;
            }
        }
        return mp;
    }

    /**
     * 删除文件
     *
     * @param fileBeanList 等待删除的文件
     */
    private void deleteFiles(List<FileBean> fileBeanList) {
        // 删除
        if (Utils.isCollectionEmpty(fileBeanList)) {
            ToastUtils.toast(getString(R.string.select_files_delete_tips));
            return;
        }

        int deleteCount = mViewModel.deleteLocalFiles(fileBeanList, FileImportActivity.this, mType, false);
        if (deleteCount == fileBeanList.size()) {
            fileBeanList.stream().forEach(fileBean -> {
                Iterator<FileBean> iterator = mEasyAdapter.getData().iterator();
                while (iterator.hasNext()) {
                    FileBean bean = iterator.next();
                    if (TextUtils.isEmpty(fileBean.getOriginallyPath())) {
                        continue;
                    }
                    if (fileBean.getOriginallyPath().equals(bean.getOriginallyPath())) {
                        // 找到这些文件，一一删除
                        iterator.remove();
                    }
                }
            });

            mEasyAdapter.notifyDataSetChanged(); // 刷新视图
            // 删除完整了
            String tips = String.format(getString(R.string.delete_sure_join_tips), deleteCount);
            UIUtils.showToast(tips);
        }
    }


    public void openImageSelector(View view) {
        PictureSelector mPictureSelector = PictureSelector.create(this);
        PictureSelectionModel mSelectionModel;
        switch (mType) {
            case FileViewModel.FILE_AUDIO:
                mSelectionModel = mPictureSelector.openGallery(SelectMimeType.ofAudio());
                break;
            case FileViewModel.FILE_IMAGE:
                mSelectionModel = mPictureSelector.openGallery(SelectMimeType.ofImage());
                break;
            case FileViewModel.FILE_VIDEO:
                mSelectionModel = mPictureSelector.openGallery(SelectMimeType.ofVideo());
                break;
            default: {
                FilePickerBuilder.getInstance()
                        .setMaxCount(mViewModel.getSelectNumber(mType)) //optional
                        .addFileSupport("zip", new String[]{"zip", "rar"})
                        .pickFile(this);
                return;
            }
        }


        mSelectionModel.setMaxSelectNum(mViewModel.getSelectNumber(mType)).setSelectorUIStyle(getPictureSelectorStyle())
                .setImageEngine(GlideEngine.createGlideEngine()).setRecordAudioInterceptListener(new OnRecordAudioInterceptListener() {
                    @Override
                    public void onRecordAudio(Fragment fragment, int requestCode) {
                        String[] recordAudio = {Manifest.permission.RECORD_AUDIO};
                        if (PermissionChecker.isCheckSelfPermission(fragment.getContext(), recordAudio)) {
                            startRecordSoundAction(fragment, requestCode);
                        } else {
                            PermissionChecker.getInstance().requestPermissions(fragment,
                                    new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionResultCallback() {
                                        @Override
                                        public void onGranted() {
                                            startRecordSoundAction(fragment, requestCode);
                                        }

                                        @Override
                                        public void onDenied() {
                                        }
                                    });
                        }
                    }
                }).forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        //
                        List<FileBean> fileBeanList = mViewModel.getFileBeans(result, mType, mDirsName);
                        List<FileBean> files = mViewModel.sortReverseOrder(mEasyAdapter.getData(), fileBeanList);
                        mEasyAdapter.setNewData(files);
                        mEasyAdapter.notifyDataSetChanged();
                        //
                        mViewModel.loadPreview(mEasyAdapter.getData(), FileImportActivity.this, mType);
                        // 导入文件夹中
                        mViewModel.insertDirsToFiles(mType, mDirsName, result, FileImportActivity.this);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    public void onProgress(FileBean fileBean) {
        // copy进度回调
        File file = new File(fileBean.getOriginallyPath());
        FileUtils.setImageId(mType, file, fileBean);
        fileBean.setType(mType);
        fileBean.setName(file.getName());
        // 得到正确的文件类型
        mViewModel.setFileBeanMimeType(fileBean, mType);

        List<FileBean> fileBeanList = mEasyAdapter.getData();
        if (!Utils.isCollectionEmpty(fileBeanList)) {
            int position = -1;
            for (int i = 0; i < fileBeanList.size(); i++) {
                FileBean thatBean = fileBeanList.get(i);
                if (thatBean != null) {
                    if (!fileBean.isRecover()) {
                        if (fileBean.getOriginallyPath().equals(thatBean.getOriginallyPath())) {
                            thatBean.setProgress(fileBean.getProgress());
                            thatBean.setMimeType(fileBean.getMimeType());
                            position = i;
                            break;
                        }
                    } else {
                        if (thatBean.getOriginallyPath().equals(fileBean.getFileUrl())) {
                            thatBean.setProgress(fileBean.getProgress());
                            thatBean.setMimeType(fileBean.getMimeType());
                            position = i;
                            break;
                        }
                    }
                }
            }

            if (position > -1) {
                mEasyAdapter.notifyItemChanged(position);
                // 局部刷新
                if (fileBean.getProgress() >= 100) {
                    if (!fileBean.isRecover()) {
                        // 为了能正常的展示，将愿路径替换为目标路径（因为原路径的文件可能被删除）
                        mEasyAdapter.getData().get(position).setFileUrl(fileBean.getOriginallyPath());
                    } else {
                        mEasyAdapter.getData().remove(position);
                        mEasyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void onCountProgress(FileBean fileBean) {
        fileBean.setProgress(100);
        onProgress(fileBean);
    }


    /**
     * 启动录音意图
     *
     * @param fragment
     * @param requestCode
     */
    private static void startRecordSoundAction(Fragment fragment, int requestCode) {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(fragment.requireActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(recordAudioIntent, requestCode);
        } else {
            ToastUtils.toast("The system is missing a recording component");
        }
    }

    public PictureSelectorStyle getPictureSelectorStyle() {
        PictureSelectorStyle mPictureSelectorStyle = new PictureSelectorStyle();
        TitleBarStyle titleBarStyle = new TitleBarStyle();
        //  titleBarStyle.setHideCancelButton(true);
        // titleBarStyle.setTitleCancelBackgroundResource();
        titleBarStyle.setTitleBackgroundColor(getResources().getColor(com.hkbyte.cnbase.R.color.chameleon_theme_color));
        mPictureSelectorStyle.setTitleBarStyle(titleBarStyle);
        return mPictureSelectorStyle;
    }

    /**
     * 目录列表/文件列表加载成功
     *
     * @param fileBeanList
     */
    public void onSuccess(List<FileBean> fileBeanList) {
        LOGE("Thread=>" + Thread.currentThread().getName());
        //
        mDataBing.rvVideo.loadMoreFinish(false, true);

        // 拿到文件集合
        mEasyAdapter.addData(fileBeanList);

        // 提前加载好预览信息
        mViewModel.loadPreview(mEasyAdapter.getData(), this, mType);
        mEasyAdapter.notifyDataSetChanged();

        boolean isLastLoading = (mViewModel.getThatPage() <= 1);
        LOGE("=====>>继续加载下一页" + mViewModel.getThatPage());
        if (isLastLoading) {
            mViewModel.loadDirsToFiles(mType, mDirsName);
        }
    }


    private void onDeleteNull(Integer integer) {
        LOGE("=====>>onDeleteNull" + integer);
        // 当前页遇到空页了.继续向下加载（）
        if (integer != null && integer == 1) {
            // 最后一页被删除干净了
            mDataBing.rvVideo.loadMoreFinish(true, false);
        }
        mViewModel.loadDirsToFiles(mType, mDirsName); //
    }

    /**
     * 插入成功会调用-主要做保存到存储文件的
     *
     */
    public void onCopySuccess(final boolean isRecover) {
        if (!isRecover) {
            ToastUtils.toast(getString(R.string.files_copy_success));

            // 全面刷新
            if (!Utils.isCollectionEmpty(mEasyAdapter.getData())) {
                for (FileBean bean : mEasyAdapter.getData())
                    bean.setProgress(100);// 复制完成， 所有的进度调整为100
                mEasyAdapter.notifyDataSetChanged();
            }

            // 追加预览
            mViewModel.loadPreview(mEasyAdapter.getData(), this, mType);

            // 得到缓存文件--
            List<FileBean> cacheFileBeanList = mViewModel.getStorageFile(mType);

            if (cacheFileBeanList == null) {
                cacheFileBeanList = new ArrayList<>();
            }

            final List<FileBean> copyFileBean = new ArrayList<>();

            // 插入成功， 将文件存入
            for (LocalMedia localMedia : mViewModel.getServiceMediaList()) {
                FileBean fileBean = new FileBean();
                fileBean.setFileUrl(localMedia.getRealPath()); // 原路径
                fileBean.setOriginallyPath(localMedia.getOriginalPath()); //目标路径
                fileBean.setType(mType);
                fileBean.setMimeType(localMedia.getMimeType()); // 文件具体类型要保存好
                cacheFileBeanList.add(fileBean);
                copyFileBean.add(fileBean);
            }

            mViewModel.saveFileInfo(mType, cacheFileBeanList);

            mViewModel.cleanServiceMediaList();

            String tips = String.format(getString(R.string.delete_the_original_files_tips), copyFileBean.size());
            FragmentMessageDialog mFragmentMessageDialog = FragmentMessageDialog.newInstance(tips, getString(R.string.prompt), getString(R.string.sure));
            mFragmentMessageDialog.show(getSupportFragmentManager(), "deleteFile");

            mFragmentMessageDialog.setOnButtonClickListener(new FragmentMessageDialog.OnButtonClickListener() {
                @Override
                public void onButton(View view) {
                    //点击确定--开始删除--删除本地路径
                    int count = mViewModel.deleteLocalFiles(copyFileBean, FileImportActivity.this, mType, true);
                    if (count == copyFileBean.size()) {
                        // 删除完整了
                        String tips = String.format(getString(R.string.delete_sure_tips), count);
                        UIUtils.showToast(tips);
                    } else {
                        int remainder = copyFileBean.size() - count;
                        String tips = String.format(getString(R.string.delete_error_tips), count, remainder);
                        UIUtils.showToast(tips);
                    }
                }
            });
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                mViewModel.getServiceMediaList().stream().forEach(localMedia -> {
                    Iterator<FileBean> iterator = mEasyAdapter.getData().iterator();
                    while (iterator.hasNext()) {
                        FileBean fileBean = iterator.next();
                        if (localMedia.getRealPath().equals(fileBean.getOriginallyPath())) {
                            // 真删除
                            iterator.remove();
                            break;
                        }
                    }
                });
                // 刷新
                mEasyAdapter.notifyDataSetChanged();
            }
        }
    }


    public void onFail(Integer tag) {
        if (tag == null) {
            return;
        }
        switch (tag) {
            case FileViewModel.ILoadFiles.ERROR_FILE_CREATE:
                ToastUtils.toast(getString(R.string.files_dirs_create_error));
                break;
            case FileViewModel.ILoadFiles.ERROR_FILE_NULL:
                LOGE("空文件");
                break;
            case FileViewModel.ILoadFiles.ERROR_FILE_EXISTS:
                ToastUtils.toast(getString(R.string.files_dirs_create_exists_error));
                break;
            case FileViewModel.ILoadFiles.ERROR_FILE_COPY:
                ToastUtils.toast(getString(R.string.files_copy_error_tips));
                break;
            case FileViewModel.ILoadFiles.LAST_PAGE:
                // 最后一页。
                mDataBing.rvVideo.loadMoreFinish(true, false);
                break;
            default: {
                //do noting
            }
        }
    }

    /**
     * 文档选择回调
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> photoPaths = new ArrayList<>();
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                    if (Utils.isCollectionEmpty(photoPaths)) {
                        return;
                    }

                    ArrayList<LocalMedia> fileBeanList = mViewModel.uriToFileBean(this, photoPaths);
                    List<FileBean> fileBeans = mViewModel.getFileBeans(fileBeanList, mType, mDirsName);
                    fileBeans = mViewModel.sortReverseOrder(mEasyAdapter.getData(), fileBeans);
                    mEasyAdapter.setNewData(fileBeans);
                    mEasyAdapter.notifyDataSetChanged();
                    mViewModel.insertDirsToFiles(mType, mDirsName, fileBeanList, FileImportActivity.this);
                }
                break;
            default: {
            }
        }
    }

    @Override
    public void initData() {
        mDataBing.rvVideo.loadMoreFinish(false, true);
        mViewModel.loadDirsToFiles(mType, mDirsName);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_file_import_layout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (exFunViewHide()) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean exFunViewHide() {
        if (mDataBing.flFun.getVisibility() == View.VISIBLE) {
            mEasyAdapter.setTag(false);
            if (!Utils.isCollectionEmpty(mEasyAdapter.getData())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mEasyAdapter.getData().stream().forEach(fileBean -> fileBean.setCheck(false));
                }
            }
            mEasyAdapter.notifyDataSetChanged();
            createAnimatorHide();
            return true;
        }
        return false;
    }
}
