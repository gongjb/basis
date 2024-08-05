package com.yft.zbase.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yft.zbase.utils.Utils;

public class ListSpaceItemDecoration extends RecyclerView.ItemDecoration{
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = Utils.dip2px(parent.getContext(),8);
    }
}
