package com.yft.zbase.adapter;


import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.gongjiebin.latticeview.KVBean;
import com.yft.zbase.utils.Utils;

import java.util.List;

public abstract class SpecsBaseAdapter<T extends KVBean> extends DelegateAdapter.Adapter<BaseLayoutHolder> implements IOnNoticeItemSelectListener {
    protected String mTag;
    protected List<T> kvs;
    public OnItemInItemClickListener<T> onItemInItemClickListener;

    public void setOnItemInItemClickListener(OnItemInItemClickListener<T> onItemInItemClickListener) {
        this.onItemInItemClickListener = onItemInItemClickListener;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void setKvs(List<T> kvs) {
        this.kvs = kvs;
    }

    public interface OnItemInItemClickListener<T extends KVBean> {
        void onItemClick(T t);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {

    }
}
