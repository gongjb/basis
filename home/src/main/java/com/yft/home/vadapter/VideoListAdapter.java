package com.yft.home.vadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.yft.home.R;
import com.yft.home.bean.HomeConfigBean;
import com.yft.home.databinding.ItemClassifyCommodityLayoutBinding;
import com.yft.zbase.adapter.BaseLayoutHolder;
import com.yft.zbase.adapter.OnAdapterClickListener;
import com.yft.zbase.bean.CommodityBean;
import com.yft.zbase.router.RouterFactory;

import java.util.List;

import cn.sd.ld.ui.helper.Utils;

/**
 * 首页的视频列表合集
 */
public class VideoListAdapter extends DelegateAdapter.Adapter<BaseLayoutHolder>{
    public LinearLayoutHelper mLinearLayoutHelper;
    public Context mContext;
    private List<HomeConfigBean.KingkongListBean> data;

    public void setData(List<HomeConfigBean.KingkongListBean> data) {
        this.data = data;
    }

    public void addData(List<HomeConfigBean.KingkongListBean> data) {
        if (!Utils.isCollectionEmpty(this.data)) {
            this.data.addAll(data);
        } else {
            this.data = data;
        }
    }

    public VideoListAdapter(Activity activity, LinearLayoutHelper linearLayoutHelper) {
        mContext = activity.getApplicationContext();
        mLinearLayoutHelper = linearLayoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLinearLayoutHelper;
    }

    @NonNull
    @Override
    public BaseLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BaseLayoutHolder(layoutInflater.inflate(R.layout.item_classify_commodity_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {
        ItemClassifyCommodityLayoutBinding commodityLayoutBinding = (ItemClassifyCommodityLayoutBinding) holder.viewDataBinding;
        commodityLayoutBinding.setBean(data.get(position));
        Paint paint = commodityLayoutBinding.tvTitle.getPaint();
        paint.setStrokeWidth(8);
        commodityLayoutBinding.setPosition(position);
        commodityLayoutBinding.setOnItemClick(new OnAdapterClickListener<HomeConfigBean.KingkongListBean>() {
            @Override
            public void onAdapterClick(View view, HomeConfigBean.KingkongListBean data, int position) {
                RouterFactory.jumpToActivity(mContext, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
