package com.yft.user.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.yft.user.BR;
import com.yft.zbase.adapter.CommonAdapter;
import com.yft.zbase.adapter.OnAdapterClickListener;

/**
 * 来个mvvm的万能适配器
 * @param <T>
 * @param <K>
 */
public class EasyAdapter<T, K extends ViewDataBinding> extends CommonAdapter<T, ViewDataBinding, CommonAdapter.BaseViewHolder<ViewDataBinding>> {

    private OnAdapterClickListener onItemClickListener;

    private boolean isPosition;

    private Object tag;

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    private int dataBindingCls;

    public void setToXmlPosition(boolean position) {
        isPosition = position;
    }

    public void setOnItemClickListener(OnAdapterClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EasyAdapter(int cls) {
        this(cls, null);
    }

    public EasyAdapter(int cls, OnAdapterClickListener<T> onItemClickListener) {
        this.dataBindingCls = cls;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onViewHolder(BaseViewHolder holder, int position) {
        if (getData().get(position) != null) {
            holder.mBind.setVariable(BR.DbBean, getData().get(position));
        }

        if (isPosition) {
            holder.mBind.setVariable(BR.position, position);
        }

        if (tag != null) {
            holder.mBind.setVariable(BR.tag, tag);
        }

        if (onItemClickListener != null) {
            holder.mBind.setVariable(BR.onClick, onItemClickListener);
        }
        holder.mBind.setVariable(BR.size, getData().size());
    }

    @Override
    protected K createDataBinding(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), dataBindingCls, parent,false);
    }
}
