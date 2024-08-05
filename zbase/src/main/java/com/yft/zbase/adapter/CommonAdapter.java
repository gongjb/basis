package com.yft.zbase.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.yft.zbase.base.BaseFind;
import com.yft.zbase.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 本项目所有adapter的父类
 *
 * @param <T>  数据源
 * @param <DT> dataBing
 * @param <BV> viewHolder
 */
public abstract class CommonAdapter<T, DT extends ViewDataBinding, BV extends CommonAdapter.BaseViewHolder> extends RecyclerView.Adapter<BV> {

    private List<T> data;

    public void setNewData(List<T> data) {
        this.data = data;
    }

    public void addData(List<T> data) {
        if (Utils.isCollectionEmpty(this.data)) {
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public void onBindViewHolder(BV holder, int position) {
        onViewHolder(holder, position);
        holder.mBind.executePendingBindings();
    }

    @Override
    public BV onCreateViewHolder(ViewGroup parent, int viewType) {
        DT dataBind = createDataBinding(parent, viewType);
        return getViewHolder(dataBind);
    }

    /**
     * 子类可重写该方法，可实现复杂布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected DT createDataBinding(ViewGroup parent, int viewType) {
        return getDataBinding(parent);
    }

    protected DT getDataBinding(ViewGroup parent) {
        Class dataBindClass = BaseFind.getGenericType(getClass(), ViewDataBinding.class);
        try {
            Method method = dataBindClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            return (DT) method.invoke(null, LayoutInflater.from(parent.getContext()), parent, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void onViewHolder(BV holder, int position);

    protected BV getViewHolder(DT dt) {
        return getBaseViewHolder(dt);
    }

    /**
     * 子类复写此方法可自己实现ViewHolder
     * @param dt
     * @return
     */
    protected BV getBaseViewHolder(DT dt) {
        return (BV) new BaseViewHolder(dt);
    }

    @Override
    public int getItemCount() {
        return Utils.isCollectionEmpty(data) ? 0 : data.size();
    }

    public static class BaseViewHolder<DT extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public DT mBind;

        public BaseViewHolder(DT mBind) {
            super(mBind.getRoot());
            this.mBind = mBind;
        }

        public <M extends DT> DT getBind() {
            M m = (M) mBind;
            return m;
        }
    }
}
