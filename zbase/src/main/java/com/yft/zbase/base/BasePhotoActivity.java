package com.yft.zbase.base;

import static com.yft.zbase.base.BaseActivity.mapActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.core.os.EnvironmentCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jaeger.library.StatusBarUtil;
import com.yft.zbase.R;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.ui.FragmentMessageDialog;
import com.yft.zbase.utils.Logger;
import com.yft.zbase.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class BasePhotoActivity<T extends ViewDataBinding, K extends BaseViewModel> extends FragmentActivity {
    protected T mDataBing;
    protected K mViewModel;
    protected int[] mColors;
    //用于保存拍照图片的uri
    protected Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    //如果不加static，当跳转之后再回来，这个变量就为null了,但不是长久之计
    protected static String mCameraImagePath;
    // 是否是Android 10以上手机
    protected boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    protected static final int CAMERA_REQUEST_CODE = 200;
    protected static final int ALBUM_REQUEST_CODE = 201;
    protected int thisRequest = CAMERA_REQUEST_CODE;
    private FragmentMessageDialog mFragmentMessageDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        mFragmentMessageDialog = FragmentMessageDialog.newInstance(permissionDescription(), permissionTitle(),"确定");
        getColors();
        mapActivity.put(getClass().getCanonicalName(), this);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_main));
        super.onCreate(savedInstanceState);

        mDataBing = DataBindingUtil.setContentView(this, getLayout());
        // 给viewBing注入生命周期监听
        mDataBing.setLifecycleOwner(this);
        //fullScreen();
        mViewModel = getViewModel();
        initView();
        initListener();
        initData();
        mViewModel.getServiceLiveChangeDate().observe(this, this::handleServiceAddress);
    }

    protected void getColors() {
        mColors = new int[]{getResources().getColor(R.color.themeMainColor),getResources().getColor(R.color.themeMainColor)};
    }

    /**
     * 调起相机拍照
     */
    protected void openCamera() {
        try {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断是否有相机
            File photoFile = null;
            Uri photoUri = null;
            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            } else {
                UIUtils.showToast("检测到设备异常，无法启用相机");
                selectPhotoError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            selectPhotoError();
        }
    }


    /**
     * 调起相册选择相片
     */
    protected void openAlbum() {
        try {
            Intent captureIntent = new Intent(Intent.ACTION_PICK, null);
            captureIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(captureIntent, ALBUM_REQUEST_CODE);
        } catch (Exception e) {
            selectPhotoError();
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    private boolean requestPermission() {
        // 用于没有授予权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限 11 以下授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 10001);
            return false;
        }
        mViewModel.getUserServer().setCameraStoragePermissions(true);
        return true;
    }


    protected void requestPermissionV2(final FragmentMessageDialog.OnButtonClickListener onButtonClickListener) {
        // 用于没有授予权限
        boolean isStoragePermissions = mViewModel.getUserServer().isCameraStoragePermissions();
        if (isStoragePermissions) {
            // 用户已经同意了权限- 可能只同意了当前使用， 还是需要再去判断下权限。
            if (onButtonClickListener != null) {
                boolean requestPermission = requestPermission();
                if (requestPermission) {
                    onButtonClickListener.onButton(null);
                }
            }
        } else {
            if (mFragmentMessageDialog != null && !mFragmentMessageDialog.isShow()) {
                mFragmentMessageDialog.setOnButtonClickListener(new FragmentMessageDialog.OnButtonClickListener() {
                    @Override
                    public void onButton(View view) {
                        // 请求权限
                        boolean requestPermission = requestPermission();
                        if (requestPermission) {
                            onButtonClickListener.onButton(view);
                        }
                    }
                });
                mFragmentMessageDialog.show(getSupportFragmentManager(), "requestPermissionV2");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001 && permissions != null && permissions.length == 3) {
            if (permissions[0].equals(Manifest.permission.CAMERA)
                    && permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissions[2].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                mViewModel.getUserServer().setCameraStoragePermissions(true); // 用户同意了权限。
                if (thisRequest == CAMERA_REQUEST_CODE) {
                    openCamera();
                } else {
                    openAlbum();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            // 相机
            if (resultCode == RESULT_OK) {
                //将拍照后的图片设置给imageview
                if (isAndroidQ) {
                    try {
                        File file = getFile(this, mCameraUri);
                        Logger.LOGE("======>" + file.getPath());
                        if (!TextUtils.isEmpty(file.getPath())) {
                            handlerPhoto(CAMERA_REQUEST_CODE, file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    handlerPhoto(CAMERA_REQUEST_CODE, mCameraImagePath);
                }
            }
        } else if (requestCode == ALBUM_REQUEST_CODE) {
            //相册
            if (resultCode == RESULT_OK) {
                String path;
                //判断手机的版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上的系统使用的这个方法处理图片
                    path = handleImageOnKitKat(data);
                } else {
                    //4.4以下的系统使用的图片处理方法
                    path = handleImageBeforeKitKat(data);
                }
                handlerPhoto(ALBUM_REQUEST_CODE, path);
            }
        }
    }

    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://" +
                        "downloads//public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是普通content类型的uri，则使用普通的方法处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果使用file类型的uri，直接获取图片的路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(externalContentUri,
                null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    /**
     * 服务器地址发生变更
     *
     * @param serviceBean
     */
    protected void handleServiceAddress(ServiceBean serviceBean) {

    }

    private void fullScreen() {

    }


    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Logger.LOGE("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    protected int statusBarColor() {
        return 0;
    }

    /**
     * 初始化View
     */
    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    public abstract int getLayout();

    public abstract void handlerPhoto(int type, String url);

    public abstract void selectPhotoError();

    /**
     * 新增权限说明（华为应用市场）
     * @return
     */
    public abstract String permissionDescription();


    /**
     * 新增权限说明（华为应用市场）
     * @return
     */
    public abstract String permissionTitle();


    protected K getViewModel() {
        return createNewViewModel();
    }

    private K createNewViewModel() {
        Class<K> vmClass = BaseFind.getGenericType(getClass(), BaseViewModel.class);
        return new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(vmClass);
    }

    @Override
    protected void onDestroy() {
        mapActivity.remove(getClass().getCanonicalName());
        if (mViewModel != null) {
            mViewModel.getServiceLiveChangeDate().removeObservers(this);
        }
        // 移除监听
        super.onDestroy();
    }

}
