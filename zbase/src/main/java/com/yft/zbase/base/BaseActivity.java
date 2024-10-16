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
        // 默认使用黑暗模式
        statusBarModel(true);
        getColors();
        mapActivity.put(getClass().getCanonicalName(), this);

        mDataBing = DataBindingUtil.setContentView(this, getLayout());
        // 给viewBing注入生命周期监听
        mDataBing.setLifecycleOwner(this);

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

    protected void statusBarModel(boolean isDarkMode) {
        if (isDarkMode) {
            StatusBarUtil.setDarkMode(this);
        } else {
            StatusBarUtil.setLightMode(this);
        }
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
