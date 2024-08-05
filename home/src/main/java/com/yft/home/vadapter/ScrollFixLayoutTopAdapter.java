package com.yft.home.vadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.yft.home.R;
import com.yft.home.databinding.ItemFixlayoutTopAdapterLayoutBinding;
import com.yft.zbase.adapter.BaseLayoutHolder;

public class ScrollFixLayoutTopAdapter extends DelegateAdapter.Adapter<BaseLayoutHolder> {
    private LayoutHelper layoutHelper;
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public ScrollFixLayoutTopAdapter(LayoutHelper layoutHelper, Context context) {
        this.layoutHelper = layoutHelper;
        // 防止内存泄漏
        this.mContext = context.getApplicationContext();
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    public ScrollFixLayoutHelper getFixLayoutHelper() {
        return (ScrollFixLayoutHelper) layoutHelper;
    }

    @NonNull
    @Override
    public BaseLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BaseLayoutHolder(layoutInflater.inflate(R.layout.item_fixlayout_top_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {
        ItemFixlayoutTopAdapterLayoutBinding mDataBing = (ItemFixlayoutTopAdapterLayoutBinding) holder.viewDataBinding;
        mDataBing.flTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(view);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return 1;
    }
}
