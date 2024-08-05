package com.yft.zbase.base;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yft.zbase.R;
import com.yft.zbase.bean.ServiceBean;

/**
 *
 * @param <T>
 * @param <K>
 */
public abstract class BaseFragment<T extends ViewDataBinding, K extends BaseViewModel> extends Fragment {
    private Context context;
    protected T mDataBing;
    protected K viewModel;
    protected int[] mColors;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBing = DataBindingUtil.inflate(inflater,getLayout(),container,false);
        // 给viewBing注入生命周期监听
        mDataBing.setLifecycleOwner(this);
        viewModel = getViewModel();
        getColors();
        initView();
        initListener();
        initData();

        // 注册服务器地址变化监听
        viewModel.getServiceLiveChangeDate().observe(getViewLifecycleOwner(), this::handleServiceAddress);
        return mDataBing.getRoot();
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

    /**
     *  初始化View
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
        return new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(vmClass);
    }

    @Override
    public void onDestroy() {
        // 移除监听
        viewModel.getServiceLiveChangeDate().removeObservers(this);
        super.onDestroy();
    }
}
