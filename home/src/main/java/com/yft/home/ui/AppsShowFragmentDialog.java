package com.yft.home.ui;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;


import androidx.recyclerview.widget.GridLayoutManager;

import com.yft.home.R;
import com.yft.home.bean.AppInfo;
import com.yft.home.databinding.FragmentAppsShowDialogLayoutBinding;
import com.yft.home.databinding.ItemAppsLayoutBinding;
import com.yft.home.model.AppsViewModel;
import com.yft.home.vadapter.EasyAdapter;
import com.yft.zbase.adapter.GridListSpaceItemDecoration;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.utils.Utils;

import java.util.List;

public class AppsShowFragmentDialog extends BaseFragmentDialog<FragmentAppsShowDialogLayoutBinding, AppsViewModel> {

    private IApkSelectListener mApkSelectListener;

    public static AppsShowFragmentDialog getInstance() {
        AppsShowFragmentDialog appsShowFragmentDialog = new AppsShowFragmentDialog();
        return appsShowFragmentDialog;
    }

    public void setApkSelectListener(IApkSelectListener mApkSelectListener) {
        this.mApkSelectListener = mApkSelectListener;
    }

    private EasyAdapter<AppInfo, ItemAppsLayoutBinding> mEasyAdapter;
    private PackageManager packageManager;

    @Override
    public void initView() {
        packageManager = getActivity().getPackageManager();

        mEasyAdapter = new EasyAdapter<>(R.layout.item_apps_layout);
        mEasyAdapter.setToXmlPosition(true);
        GridLayoutManager lm = new GridLayoutManager(getContext(), 3);
        //指定列表线性布局，横向水平
        mDataBing.rvAppsDialog.addItemDecoration(new GridListSpaceItemDecoration(Utils.dip2px(getContext(), 10)));
        mDataBing.rvAppsDialog.setLayoutManager(lm);
        mDataBing.rvAppsDialog.setAdapter(mEasyAdapter);
        mEasyAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        viewModel.getAppInfoListMutableLiveData().observe(this, this::onAppList);
        viewModel.getErrorMutableLiveData().observe(this, this::onFail);
        mEasyAdapter.setOnItemClickListener(this::onAdapterClick);
    }

    public void onAdapterClick(View view, AppInfo data, int position) {
        // 开始导入
        createShortcut(data);
        dismiss();
    }



    private void createShortcut(AppInfo app) {
        if (mApkSelectListener != null) {
            mApkSelectListener.onClickApk(app);
        }
    }

    private Bitmap getAppIcon(Drawable drawable) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void onAppList(List<AppInfo> list) {
        mDataBing.flProgress.setVisibility(View.GONE);
        mEasyAdapter.setNewData(list);
        mEasyAdapter.notifyDataSetChanged();
    }


    private void onFail(String fail) {
        mDataBing.flProgress.setVisibility(View.GONE);
        dismiss();
    }


    @Override
    public void initData() {
        // 获取本地apps
        viewModel.requestApps(packageManager);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_apps_show_dialog_layout;
    }

    public interface IApkSelectListener {
        void onClickApk(AppInfo appInfo);
    }
}
