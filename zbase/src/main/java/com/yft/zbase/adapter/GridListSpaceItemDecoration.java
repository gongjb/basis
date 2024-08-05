package com.yft.zbase.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridListSpaceItemDecoration extends GridSpaceItemDecoration{
    /**
     * @param columnSpacing 列间距
     */
    public GridListSpaceItemDecoration(int columnSpacing) {
        super(columnSpacing);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mColumnSpacing;
    }
}
