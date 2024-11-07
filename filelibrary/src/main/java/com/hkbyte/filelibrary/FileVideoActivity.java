package com.hkbyte.filelibrary;

import static com.hkbyte.filelibrary.viewmodel.FileViewModel.FILE_AUDIO;
import static com.hkbyte.filelibrary.viewmodel.FileViewModel.FILE_DOCUMENTATION;
import static com.hkbyte.filelibrary.viewmodel.FileViewModel.FILE_IMAGE;
import static com.hkbyte.filelibrary.viewmodel.FileViewModel.FILE_VIDEO;
import static com.yft.zbase.utils.Logger.LOGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chenenyu.router.annotation.Route;
import com.hkbyte.file.R;
import com.hkbyte.file.databinding.ActivityFileVideoLayoutBinding;
import com.hkbyte.file.databinding.ItemFileDirsVideoLayoutBinding;
import com.hkbyte.filelibrary.adapter.EasyAdapter;
import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.router.FileRouter;
import com.hkbyte.filelibrary.ui.CreateDirFragmentDialog;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;
import com.yft.zbase.adapter.GridListSpaceItemDecoration;
import com.yft.zbase.adapter.OnAdapterClickListener;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;

import java.util.List;

/**
 * 私密视频
 */
@Route(FileRouter.FILE_VIDEO_ACTIVITY)
public class FileVideoActivity extends BaseActivity<ActivityFileVideoLayoutBinding, FileViewModel> {
    private final static String TAG = "FileVideoActivity";
    private EasyAdapter<FileBean, ItemFileDirsVideoLayoutBinding> mEasyAdapter;
    private volatile boolean isStorageManage; // 是否具备文件管理权限
    private int mType; // 文件类型
    private CreateDirFragmentDialog mCreateDirFragment;

    @Override
    public void initView() {
        // 主题颜色用项目中的
        mCreateDirFragment = CreateDirFragmentDialog.newInstance();
        mType = getIntent().getIntExtra("type", mViewModel.FILE_VIDEO);
        mDataBing.tlt.setBackgroundColor(getResources().getColor(com.hkbyte.cnbase.R.color.chameleon_theme_color));
        mDataBing.tlt.setTitle(getTitle(mType));
        mDataBing.tlt.setLeftBackImage();

        mEasyAdapter = new EasyAdapter(R.layout.item_file_dirs_video_layout);

        mEasyAdapter.setToXmlPosition(true);
        mViewModel.wrapToAddEntities(getResources().getString(R.string.new_dirs), com.hkbyte.cnbase.R.mipmap.ic_app_add_img);
        mEasyAdapter.setNewData(mViewModel.getFileLinkList());

        //指定列表线性布局，横向水平
        GridLayoutManager lm = new GridLayoutManager(FileVideoActivity.this, 3);
        mDataBing.rvVideo.addItemDecoration(new GridListSpaceItemDecoration(Utils.dip2px(this, 10)));
        mDataBing.rvVideo.setLayoutManager(lm);
        mDataBing.rvVideo.setAdapter(mEasyAdapter);
        mEasyAdapter.notifyDataSetChanged();
    }

    public String getTitle(int type) {
        //getResources().getString(R.string.private_video)
        switch (type) {
            case FILE_VIDEO:
                return getResources().getString(R.string.private_video);
            case FILE_IMAGE:
                return getResources().getString(R.string.private_image);
            case FILE_AUDIO:
                return getResources().getString(R.string.private_audio);
            case FILE_DOCUMENTATION:
                return getResources().getString(R.string.private_documentation);
            default: {
                return "";
            }
        }
    }


    /**
     * 加载目录
     */
    public void loadDirLoad() {
        mViewModel.loadDirFiles(mType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            // 同意了权限/加载本地文件列表
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                isStorageManage = Environment.isExternalStorageManager();
            }

            LOGE(TAG, "权限====>>" + isStorageManage);
            if (isStorageManage) {
                loadDirLoad();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001 && permissions != null && permissions.length == 2) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 同意了文件存储权限
                isStorageManage = true;
                loadDirLoad();
            }
        }
    }


    @Override
    public void initListener() {
        mViewModel.getFileBeanMutableLiveData().observe(this, this::onSuccess);
        mViewModel.getLoadFileErrorMutableLiveData().observe(this, this::onFail);
        mViewModel.getCreateDirsMutableLiveData().observe(this, this::onCreateSuccess);
        mEasyAdapter.setOnItemClickListener(new OnAdapterClickListener<FileBean>() {
            @Override
            public void onAdapterClick(View view, FileBean data, int position) {
                switch (data.getType()) {
                    case FileViewModel.FILE_MORE:
                        // 点击了更多,开始请求权限
                        if (!isStorageManage) {
                            // 没有权限，去打开权限。
                            requestPermission();
                        } else {
                            if (!mCreateDirFragment.isShow()) {
                                mCreateDirFragment.show(getSupportFragmentManager(), TAG);
                            }
                        }
                        break;
                    case FILE_VIDEO:
                    case FILE_IMAGE:
                    case FILE_AUDIO:
                    case FILE_DOCUMENTATION:
                        String activityName = RouterFactory.getInstance().getPage("FileImportActivity");
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", data.getType());
                        bundle.putString("name", data.getName());
                        RouterFactory.getInstance().startRouterBundleActivity(FileVideoActivity.this, activityName, bundle);
                    default:{}
                }

            }
        });

        mCreateDirFragment.setNewDirsListener(this::onCreateDirs);
    }


    public void onCreateSuccess(String name) {
        // 创建成功
        loadDirLoad();
    }

    public void onCreateDirs(String name) {
        // 去创建目录吧
        mViewModel.createDirs(mType, name);
    }

    @Override
    public void initData() {
        // 非第一次打开，判断是否有权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isStorageManage = Environment.isExternalStorageManager();
        } else {
            isStorageManage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }

        if (!isStorageManage) {
            // 没有权限，去打开权限。
            requestPermission();
        } else {
            // 加载本地的 video 或者 image等
            loadDirLoad();
        }
    }

    public void onSuccess(List<FileBean> fileBeanList) {
        if (!Utils.isCollectionEmpty(fileBeanList)) {
            // 包装文件集合
            mViewModel.packagingFiles(fileBeanList);
            mViewModel.wrapToAddEntities(getResources().getString(R.string.new_dirs), com.hkbyte.cnbase.R.mipmap.ic_app_add_img);
            mEasyAdapter.setNewData(mViewModel.getFileLinkList());

            mEasyAdapter.notifyDataSetChanged();
        }
    }

    public void onFail(Integer tag) {
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
            default: {

            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_file_video_layout;
    }

    private void requestPermission() {
        // 用于没有授予权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // 拉起授权程序
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请权限 11 以下授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10001);
            }
        }
    }
}
