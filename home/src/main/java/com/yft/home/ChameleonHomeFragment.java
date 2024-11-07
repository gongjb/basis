package com.yft.home;

import static com.yft.zbase.utils.Logger.LOGE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chenenyu.router.annotation.Route;
import com.hkbyte.cnbase.router.ChameleonJumpRouter;
import com.yft.home.bean.AppInfo;
import com.yft.home.bean.HomeConfigBean;
import com.yft.home.bean.ProgressBean;
import com.yft.home.databinding.FragmentChameleonHomeLayoutBinding;
import com.yft.home.databinding.ItemAppsLayoutBinding;
import com.yft.home.databinding.ItemRvClassifBinding;
import com.yft.home.model.AppsViewModel;
import com.yft.home.router.HomeRouter;
import com.yft.home.ui.AppsShowFragmentDialog;
import com.yft.home.vadapter.EasyAdapter;
import com.yft.zbase.adapter.GridListSpaceItemDecoration;
import com.yft.zbase.adapter.OnAdapterClickListener;
import com.yft.zbase.base.BaseFragment;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页只提供展示入口功能， 个自的能力包装在model中完成
 */
@Route(HomeRouter.FRAGMENT_CHAMELEON_HOME)
public class ChameleonHomeFragment extends BaseFragment<FragmentChameleonHomeLayoutBinding, AppsViewModel> {

    private EasyAdapter<HomeConfigBean.KingkongListBean, ItemRvClassifBinding> mEasyAdapter;
    private EasyAdapter<AppInfo, ItemAppsLayoutBinding> mAppEasyAdapter;
    @Override
    public void initView() {
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewModel.getDevice().getStatusBarHi());
        mDataBing.vTop.setLayoutParams(params);
        mEasyAdapter = new EasyAdapter(R.layout.item_rv_classif, new OnAdapterClickListener<HomeConfigBean.KingkongListBean>() {
            @Override
            public void onAdapterClick(View view, HomeConfigBean.KingkongListBean data, int position) {
                ChameleonJumpRouter.getInstance().jumpToActivity(getContext(), data);
            }
        });

        mEasyAdapter.setToXmlPosition(true);
        mEasyAdapter.setNewData(HomeConfigBean.KingkongListBean.getKingkongListBeans());
        //指定列表线性布局，横向水平
        GridLayoutManager lm = new GridLayoutManager(getContext(), 5) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mDataBing.rvCore.addItemDecoration(new GridListSpaceItemDecoration(Utils.dip2px(getContext(), 10)));
        mDataBing.rvCore.setLayoutManager(lm);
        mDataBing.rvCore.setAdapter(mEasyAdapter);
        mEasyAdapter.notifyDataSetChanged();


        mAppEasyAdapter = new EasyAdapter<>(R.layout.item_apps_layout);
        mAppEasyAdapter.setToXmlPosition(true);
        //指定列表线性布局，横向水平
        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 4);
        mDataBing.rvApps.addItemDecoration(new GridListSpaceItemDecoration(Utils.dip2px(getContext(), 10)));
        mDataBing.rvApps.setLayoutManager(manager2);
        mDataBing.rvApps.setAdapter(mAppEasyAdapter);
        mAppEasyAdapter.setNewData(viewModel.getAppList(getActivity().getPackageManager()));
        mAppEasyAdapter.notifyDataSetChanged();
    }


    @Override
    public void initListener() {
        mEasyAdapter.setOnItemClickListener(this::onAdapterClick);
        mAppEasyAdapter.setOnItemClickListener(this::onAppAdapterClick);
        mDataBing.ivAddApp.setOnClickListener(this::onAddClick);
        viewModel.getCopyProgressBeanMutableLiveData().observe(this, this::onProgress);
        viewModel.getSuccessCopyApkLiveData().observe(this, this::onCopySuccess);
        viewModel.getOnUninstallAppMutableLiveData().observe(this, this::onUninstallSuccess);
        viewModel.getInstallApkMutableLiveData().observe(this, this::installSuccess);
    }


    public void onAddClick(View v) {
        AppsShowFragmentDialog appsShowFragmentDialog = AppsShowFragmentDialog.getInstance();
        appsShowFragmentDialog.setApkSelectListener(this::onClickApk);
        appsShowFragmentDialog.show(getChildFragmentManager(), "appsDialog");
    }

    /**
     * apk导入进度与结果
     * @param progressBean
     */
    private void onProgress(ProgressBean progressBean) {
        if (progressBean != null) {
          // 更新进度
            int position = -1;
            for ( int i = 0; i < mAppEasyAdapter.getData().size(); i ++ ) {
                if (mAppEasyAdapter.getData().get(i).getPackageName().equals(progressBean.getPackageName())) {
                    position = i;
                    break;
                }
            }

            if (position != -1)  {
                if (progressBean.getProgress() == -1) {
                    ToastUtils.toast("导入失败！");
                    mAppEasyAdapter.getData().remove(position);
                    mAppEasyAdapter.notifyDataSetChanged();
                    return;
                }

                LOGE("====>" + progressBean.getProgress());
                // 找到了更新进度
                mAppEasyAdapter.getData().get(position).setProgress(progressBean.getProgress());
                mAppEasyAdapter.notifyItemChanged(position);
            }
        }
    }

    private void onCopySuccess(final ProgressBean progressBean) {
        onProgress(progressBean);

//        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
//        filter.addDataScheme("package");
//        getContext().registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
//                    getContext().unregisterReceiver(this); // 注销掉， 不然会重复收到卸载消息
//                    Uri data = intent.getData();
//                    if (data != null) {
//                        String packageName = data.getSchemeSpecificPart();
//                        LOGE("UninstallReceiver", "Application uninstalled: " + packageName);
//                        // 处理卸载事件，可能需要更新本地数据库或者 UI
//                        viewModel.uninstallAppPost(progressBean);
//                    }
//                }
//            }
//        }, filter);
//
//        viewModel.uninstallApp(progressBean.getPackageName(), getContext()); // 卸载


       //String s =  viewModel.getMainActivityFromApk(new File(progressBean.getFilePath()));
       //LOGE("11111=========>" + s);

        viewModel.launchApk(getContext(), new File(progressBean.getFilePath()), "WelcomeActivity", "cn.sd.ld.ui");
    }


    private void onUninstallSuccess(final ProgressBean s) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    // 应用安装成功
                    //Log.d("InstallReceiver", "APK installed successfully.");
                    viewModel.installApkPost(s);
                } else if (Intent.ACTION_PACKAGE_INSTALL.equals(action)) {
                    // 安装失败
                    //Log.d("InstallReceiver", "APK installation failed.");
                }
            }
        }, filter);

        viewModel.installApk(new File(s.getFilePath()), getContext());

    }

    private void installSuccess(ProgressBean progressBean) {
        // 安装成功
        viewModel.removeAppShortcut(getContext(), progressBean.getPackageName(), progressBean.getApkName());
    }

    /**
     * 选中apk了
     * @param appInfo
     */
    private void onClickApk(AppInfo appInfo) {
        // 预插入
        appInfo.setProgress(0);
        if (mAppEasyAdapter.getData() != null ) {
            mAppEasyAdapter.getData().add(0, appInfo);
        } else {
            List<AppInfo> appInfos = new ArrayList<>();
            appInfos.add(appInfo);
            mAppEasyAdapter.setNewData(appInfos);
        }

        mAppEasyAdapter.notifyDataSetChanged();
        // 开始导入
        viewModel.startCopyApk(appInfo.getPackageName(), appInfo.getName(), getContext());
    }


    private void onAppAdapterClick(View view, AppInfo data, int position) {
        // 打开app
        launchApp(data.getPackageName());
    }

    private void launchApp(String packageName) {
        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
    }

    public void onAdapterClick(View view, HomeConfigBean.KingkongListBean data, int position) {
        if (TextUtils.isEmpty(data.getActionType())) {
            return;
        }
        String activityName = "";

        Bundle bundle = new Bundle();

        switch (data.getActionType()) {
            case ChameleonJumpRouter.TO_FILE_VIDEO:
                activityName = RouterFactory.getInstance().getPage("FileVideoActivity");
                bundle.putInt("type", 1);
                break;
            case ChameleonJumpRouter.TO_FILE_IMAGE:
                activityName = RouterFactory.getInstance().getPage("FileVideoActivity");
                bundle.putInt("type", 2);
                break;
            case ChameleonJumpRouter.TO_FILE_AUDIO:
                activityName = RouterFactory.getInstance().getPage("FileVideoActivity");
                bundle.putInt("type", 3);
                break;
            case ChameleonJumpRouter.TO_FILE_DOCUMENTATION:
                activityName = RouterFactory.getInstance().getPage("FileVideoActivity");
                bundle.putInt("type", 4);
                break;
            case ChameleonJumpRouter.TO_CALCULATOR_CAMOUFLAGE:
                activityName = RouterFactory.getInstance().getPage("ReplaceIconActivity");
                break;
            default:{
                return;
            }
        }

        if (TextUtils.isEmpty(activityName)) {
            return;
        }

        RouterFactory.getInstance().startRouterBundleActivity(getContext(), activityName, bundle);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_chameleon_home_layout;
    }
}
