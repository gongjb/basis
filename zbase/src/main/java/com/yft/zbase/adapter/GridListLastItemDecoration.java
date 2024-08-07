package com.yft.zbase.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class GridListLastItemDecoration extends RecyclerView.ItemDecoration {

    protected int mColumnSpacing;// 列间距
    protected int count;

    /**
     * @param columnSpacing 列间距
     */
    public GridListLastItemDecoration(int columnSpacing, int count) {
        this.mColumnSpacing = columnSpacing;
        this.count = count;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);  //当前条目的position
//        if (position + (count-1) % count == 0) {
//            outRect.right = mColumnSpacing;
//            outRect.left = mColumnSpacing / 2;
//        } else {
//            outRect.right = mColumnSpacing;
//        }
    }
}
