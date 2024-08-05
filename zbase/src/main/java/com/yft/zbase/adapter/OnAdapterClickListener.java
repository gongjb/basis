package com.yft.zbase.adapter;

import android.view.View;

/**
 * 提供给adapter/view使用的点击事件
 * @param <T>
 */
public interface OnAdapterClickListener<T> {
    void onAdapterClick(View view,T data, int position);
}

