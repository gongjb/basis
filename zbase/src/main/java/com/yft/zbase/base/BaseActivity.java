package com.yft.zbase.base;


import android.os.Bundle;


import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jaeger.library.StatusBarUtil;
import com.yft.zbase.R;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.error.ErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 */
public abstract class BaseActivity<T extends ViewDataBinding, K extends BaseViewModel> extends FragmentActivity {
    protected T mDataBing;
    protected K mViewModel;
    public static Map<String,FragmentActivity> mapActivity = new HashMap<>();
    protected int[] mColors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setDarkMode(this);
        getColors();
        mapActivity.put(getClass().getCanonicalName(), this);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_main));

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
     * 服务器地址发生变更
     * @param serviceBean
     */
    protected void handleServiceAddress(ServiceBean serviceBean) {
    }

    private void fullScreen() {
        //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
//        Window window = getWindow();
//        View decorView = window.getDecorView();
//        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        decorView.setSystemUiVisibility(option);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (statusBarColor() != 0) {
//            window.setStatusBarColor(statusBarColor());
//        }
//
//        //导航栏颜色也可以正常设置
//        window.setNavigationBarColor(THEME_STYLE);
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


    protected K getViewModel() {
        return createNewViewModel();
    }

    private K createNewViewModel() {
        Class<K> vmClass = BaseFind.getGenericType(getClass(), BaseViewModel.class);
        return new ViewModelProvider(this,  new ViewModelProvider.NewInstanceFactory()).get(vmClass);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // 后台销毁了我， 我必须重启保证程序正常运行.需要重启app
        ErrorCode.welcomeToJWY();
        super.onRestoreInstanceState(savedInstanceState);
    }
}
