package com.fuan.market;


import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chenenyu.router.annotation.Route;
import com.fuan.market.router.AppRouter;
import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.LatticeView;

import com.fuan.market.databinding.ActivityMainBinding;
import com.fuan.market.model.MainViewModel;

import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.bean.DownLoadBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.privacy.PrivacyFragmentDialog;
import com.yft.zbase.router.RouterFactory;

import com.yft.zbase.server.ILanguage;
import com.yft.zbase.updateapk.VersionUpdateUtil;
import com.yft.zbase.utils.Constant;
import com.yft.zbase.utils.ImageGlideLoader;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

import java.util.ArrayList;

/**
 * 首页
 */

@Route(AppRouter.ACTIVITY_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private LatticeView.ImageTextParams<ImageGlideLoader> mImageTextParams;
    private int pageIndex;
    private VersionUpdateUtil versionUpdateUtil;
    private PrivacyFragmentDialog mPrivacyFragmentDialog;

    /**
     * 存放fragment页面路径
     */
    private String[] mPageTags;

    @Override
    public void initView() {
        //UserFragment
        mPageTags = new String[]{RouterFactory.getPage("HomeVLayoutFragment"),
                RouterFactory.getPage("UserFragment")};

        mPrivacyFragmentDialog = PrivacyFragmentDialog.newInstance();
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.BOTTOM_TOP,
                mColors);
        mDataBing.viewLine.setBackground(grad);

        versionUpdateUtil = new VersionUpdateUtil();
        mImageTextParams = new LatticeView.ImageTextParams();
        // 未被选中图片
        mImageTextParams.images = new Integer[] {
                com.yft.zbase.R.mipmap.icon_home_off,
                com.yft.zbase.R.mipmap.icon_me_off};


        // 选中之后应该展示的图片
        mImageTextParams.selectImages = new Integer[] {
                com.yft.zbase.R.mipmap.icon_home_on,
                com.yft.zbase.R.mipmap.icon_me_on};

        mImageTextParams.text = new String[]{mViewModel.getString("首页"), mViewModel.getString("我的")};
        mImageTextParams.maxLine = mImageTextParams.text.length; // 每一行显示的个数
        mImageTextParams.imageLoader = new ImageGlideLoader();
        mImageTextParams.imageHigh = 20;
        mImageTextParams.imageWidth = 20;
        mImageTextParams.textSize = 10; // text的字体大小
        //mImageTextParams.textPaddingTop = LatticeView.dip2px(this, 4); //字体向上给一个padding
        mImageTextParams.selectIndex = 0; // 默认第一个被选中
        mImageTextParams.textColor = com.yft.zbase.R.color.sd_white; // 字体默认颜色
        mImageTextParams.textSelectColor = com.yft.zbase.R.color.theme_red;
        mImageTextParams.imageType = ImageView.ScaleType.FIT_XY;
        // imageTextParams.textSelectColor = com.yft.zbase.R.color.theme_text_color; // 字体被选中的颜色
        mImageTextParams.bg_color = "#222432";
        //imageTextParams.animation = createAnimation();
        mDataBing.llView.setImageTextParams(mImageTextParams);
        mDataBing.llView.startView(); // 开始加载布局
        Fragment home = RouterFactory.getFragment(this, mPageTags[0]);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        String initPage = getIntent().getStringExtra("initPage");
        if (TextUtils.isEmpty(initPage)) {
            mFragmentTransaction.add(R.id.frame_container, home, mPageTags[0]);
            mFragmentTransaction.commit();
        } else {
            initPage();
        }

        if (!mViewModel.getUserServer().isPrivacy()) {
            if (ILanguage.EN_TYPE.equals(mViewModel.getLanguageType())) {
                // 英文环境下， 不用弹出隐私权限
                return;
            }
            mPrivacyFragmentDialog.show(getSupportFragmentManager(), Constant.PRIVACY);
        }
    }

    @Override
    protected void getColors() {
        mColors = new int[]{
                ColorUtils.setAlphaComponent(getResources().getColor(com.yft.zbase.R.color.themeMainColorTwo), 250),
                ColorUtils.setAlphaComponent(getResources().getColor(com.yft.zbase.R.color.themeMainColorTwo), 100),
                ColorUtils.setAlphaComponent(getResources().getColor(com.yft.zbase.R.color.themeMainColorTwo), 0)};
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 记录当前的position
        super.onSaveInstanceState(outState);
        LOGE("=====>>onSaveInstanceState");
    }

    @Override
    public void initListener() {
        mViewModel.getMutableLiveData().observe(this, this::onSuccess);
        mViewModel.getPreloadMutableLiveData().observe(this, this::onOpenStart);
        mViewModel.getDownLoadBeanMutableLiveData().observe(this, this::onDownload);
        mDataBing.llView.setOnPageItemOnClickListener(new BaseLatticeView.OnPageItemOnClickListener() {
            @Override
            public void onClick(View v, Object[] urls, int position) {
            }

            @Override
            public void onClick(View v, ImageView imageView, Object[] urls, int position) {
                if (position == 1) {
                    if (!mViewModel.isLogin()) {
                        RouterFactory.startRouterActivity(MainActivity.this, RouterFactory.getPage("LoginActivity"));
                        mImageTextParams.selectIndex = pageIndex;
                        mDataBing.llView.removeViews();
                        mDataBing.llView.startView();
                        return;
                    }
                } else {
                    pageIndex = position;
                }
                selectTab(position);
            }
        });
    }

    private void initPage() {
        String initPage = getIntent().getStringExtra("initPage");
        if (!TextUtils.isEmpty(initPage)) {
            if (!mViewModel.isLogin()) {
                // 未登录， 不做跳转
                return;
            }
            switch (initPage) {
                case RouterFactory.TO_HOME_PAGE:
                    onSuccess(0);
                    break;
                case RouterFactory.TO_HOME_APPRAISE:
                    onSuccess(1);
                    break;
                case RouterFactory.TO_HOME_NEWS:
                    onSuccess(2);
                    break;
                case RouterFactory.TO_HOME_SHOPCAR:
                    onSuccess(3);
                    break;
                case RouterFactory.TO_HOME_MINE:
                    onSuccess(4);
                    break;
                default:{}
            }
        }
    }


    public void onSuccess(Integer integer) {
        selectTab(integer);
        mImageTextParams.selectIndex = integer;
        mDataBing.llView.removeViews();
        mDataBing.llView.startView();
    }

    public void onOpenStart(TargetBean targetBean) {
        // 预加载图片
        UIUtils.preload(this, targetBean);
    }

    public void onDownload(DownLoadBean downLoadBean) {
        try {
            // 本地版本号
            String v = Utils.getNumber(BuildConfig.VERSION_NAME);
            long thatVer = Long.parseLong(v);
            long serVer = Long.parseLong(Utils.getNumber(downLoadBean.getVersion()));
            if (thatVer < serVer) {
                // 发现新版本
                ArrayList<String> arrayList = new ArrayList<>();
                // 更新描述
                arrayList.add(downLoadBean.getDescription());
                // 弹出更新软件的弹出框
                versionUpdateUtil.showDownloadApkDialog(arrayList, downLoadBean.getUpdateUrl(), downLoadBean.isMustUpdate(),BuildConfig.APPLICATION_ID ,
                        this, BuildConfig.FLAVOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectTab(int position) {
        Fragment fragment = mFragmentManager.findFragmentByTag(mPageTags[position]);
        if (fragment == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            for (Fragment fragment1 : mFragmentManager.getFragments()) {
                // 隐藏之前的页面
                mFragmentTransaction.hide(fragment1);
            }
            mFragmentTransaction.add(R.id.frame_container,
                    RouterFactory.getFragment(MainActivity.this, mPageTags[position]), mPageTags[position]);
            mFragmentTransaction.commit();
            return;
        }
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.show(fragment);
        for (Fragment fragment1 : mFragmentManager.getFragments()) {
            if (fragment1 != fragment) {
                mFragmentTransaction.hide(fragment1);
            }
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initData() {
        mViewModel.requestDownload();
        // 请求开屏广告信息
        mViewModel.requestOpenStart();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }


    // 预加载h5, 让后续h5页面加载更丝滑
    private void preloadSetting(WebView dWebView) {

    }

    @Override
    protected void onDestroy() {
        // 清除WebView缓存
        Utils.cleanCookie(this);
        super.onDestroy();
    }
}