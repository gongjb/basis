package com.yft.zbase.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.yft.zbase.R;
import com.yft.zbase.bean.SpecssBean;
import com.yft.zbase.databinding.ItemSubtitleStyleLayoutBinding;

import java.util.List;

public class SubtitleAdapter extends SpecsBaseAdapter {

    private LinearLayoutHelper layoutHelper;

    public SubtitleAdapter(LinearLayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @NonNull
    @Override
    public BaseLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BaseLayoutHolder(layoutInflater.inflate(R.layout.item_subtitle_style_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemSubtitleStyleLayoutBinding subtitleStyleLayoutBinding = (ItemSubtitleStyleLayoutBinding) holder.viewDataBinding;
        subtitleStyleLayoutBinding.setSubtitle(mTag);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onNoticeItemChange(List<SpecssBean.ItemsBean> noClickItems) {

    }

    @Override
    public int getItemSpecsCount() {
        return 0;
    }
}
