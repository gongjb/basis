package com.yft.zbase.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.yft.zbase.R;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.ui.DialogPhotoFragment;
import com.yft.zbase.ui.NoLeakDialog;

/**
 *
 * @param <T>
 * @param <K>
 */
public abstract class BaseFragmentDialog<T extends ViewDataBinding, K extends BaseViewModel> extends DialogFragment {
    private Context context;
    protected T mDataBing;
    protected K viewModel;
    protected int[] mColors;
    private boolean isShow;
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createNoLeakDialog();
    }

    protected Dialog createNoLeakDialog() {
        return new NoLeakDialog(getContext(), R.style.MyLoadingDialog, createGravity(), createWidth()).setDialogFragment(this);
    }

    /**
     * dialog的显示位置
     * @return
     */
    protected float createWidth() {
        return 0.9f;
    }

    /**
     * dialog的显示位置
     * @return
     */
    protected int createGravity() {
        return Gravity.CENTER;
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
        isShow = false;
        viewModel.getServiceLiveChangeDate().removeObservers(this);
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        isShow = false;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        isShow = true;
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }
}
