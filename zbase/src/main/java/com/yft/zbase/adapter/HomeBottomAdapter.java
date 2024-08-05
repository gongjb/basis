package com.yft.zbase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.yft.zbase.R;
import com.yft.zbase.databinding.HomeBottomLayoutBinding;
import com.yft.zbase.databinding.ItemHomeBottomLayoutBinding;
import com.yft.zbase.utils.Utils;

public class HomeBottomAdapter extends DelegateAdapter.Adapter<HomeLayoutHolder> {
    private LayoutHelper layoutHelper;
    private boolean isLoad;
    private boolean isLastPage;
    private float proportion;
    public HomeBottomAdapter(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    public HomeBottomAdapter(LayoutHelper layoutHelper, float proportion) {
        this.layoutHelper = layoutHelper;
        this.proportion = proportion;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @NonNull
    @Override
    public HomeLayoutHolder<HomeBottomLayoutBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new HomeLayoutHolder(layoutInflater.inflate(R.layout.item_home_bottom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeLayoutHolder holder, int position) {
        ItemHomeBottomLayoutBinding homeBottomLayoutBinding = (ItemHomeBottomLayoutBinding) holder.viewDataBinding;
        if (proportion != 0.0f) {
            int i = (int) (Utils.dip2px(homeBottomLayoutBinding.getRoot().getContext(), 64) / proportion);
            ViewGroup.LayoutParams layoutParams =  homeBottomLayoutBinding.rlPro.getLayoutParams();
            layoutParams.height = i;
            homeBottomLayoutBinding.rlPro.setLayoutParams(layoutParams);
        }

        if (isLoad) {
            // 占位置的隐藏掉
            if (isLastPage) {
                // 最后一页
                homeBottomLayoutBinding.rlPro.setVisibility(View.VISIBLE);
                homeBottomLayoutBinding.proBar.setVisibility(View.GONE);
                homeBottomLayoutBinding.tvMessage.setText("已经触碰到我的底线了！");
            } else {
                homeBottomLayoutBinding.rlPro.setVisibility(View.VISIBLE);
                homeBottomLayoutBinding.proBar.setVisibility(View.VISIBLE);
                homeBottomLayoutBinding.tvMessage.setText("正在加载，请稍等...");
            }
        } else {
            homeBottomLayoutBinding.rlPro.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
