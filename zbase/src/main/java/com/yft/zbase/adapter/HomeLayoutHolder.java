package com.yft.zbase.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class HomeLayoutHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public T viewDataBinding;
    public HomeLayoutHolder(@NonNull View itemView) {
        super(itemView);
        viewDataBinding = DataBindingUtil.bind(itemView);
    }
}
