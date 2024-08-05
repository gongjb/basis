package com.yft.zbase.adapter;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.vlayout.DelegateAdapter;

/**
 * 不提供任何create方法， 只用于管理adapter类
 */
public class AdapterFactoryManage extends AdapterFactory {

    private static AdapterFactoryManage instance;

    private AdapterFactoryManage() {
    }

    public static synchronized AdapterFactoryManage getInstance() {
        if (instance == null) {
            synchronized (AdapterFactoryManage.class) {
                if (instance == null) {
                    instance = new AdapterFactoryManage();
                }
            }
        }
        return instance;
    }

    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag, float proportion, String url) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag, String groupId) {
        throw new RuntimeException("Stub!");
    }
}
