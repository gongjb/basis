package com.yft.home.vadapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.yft.zbase.adapter.BaseLayoutHolder;
import com.yft.zbase.bean.CommodityBean;

import java.util.List;

public class CommodityBaseAdapter<T extends BaseLayoutHolder> extends DelegateAdapter.Adapter<T>{

    protected List<CommodityBean> data;

    public void addData(List<CommodityBean> data) {
        if (this.data != null) {
            this.data.addAll(data);
        } else {
            setNewData(data);
        }
    }

    public void setNewData(List<CommodityBean> data) {
        this.data = data;
    }

    public List<CommodityBean> getData() {
        return data;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        throw new RuntimeException("Stub!");
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getItemCount() {
        throw new RuntimeException("Stub!");
    }
}
