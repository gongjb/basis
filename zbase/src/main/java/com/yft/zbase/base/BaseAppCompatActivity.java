package com.yft.zbase.base;

import static com.yft.zbase.base.BaseActivity.mapActivity;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.jaeger.library.StatusBarUtil;
import com.yft.zbase.R;
import com.yft.zbase.bean.ServiceBean;

public abstract class BaseAppCompatActivity<T extends ViewDataBinding, K extends BaseViewModel> extends AppCompatActivity {

    protected T mDataBing;
    protected K mViewModel;
    protected int[] mColors;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        getColors();
        mapActivity.put(getClass().getCanonicalName(), this);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_main));
        super.onCreate(savedInstanceState);

        mDataBing = DataBindingUtil.setContentView(this, getLayout());
        // 给viewBing注入生命周期监听
        mDataBing.setLifecycleOwner(this);
       // fullScreen();
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
//        StatusBarUtil.setLightMode(this);
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
}
