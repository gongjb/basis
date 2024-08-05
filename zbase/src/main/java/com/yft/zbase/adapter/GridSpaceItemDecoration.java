package com.yft.zbase.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    protected int mColumnSpacing;// 列间距

    /**
     * @param columnSpacing 列间距
     */
    public GridSpaceItemDecoration(int columnSpacing) {
        this.mColumnSpacing = columnSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = mColumnSpacing;
    }
}
