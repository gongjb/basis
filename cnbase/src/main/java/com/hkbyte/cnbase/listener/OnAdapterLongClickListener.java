package com.hkbyte.cnbase.listener;

import android.view.View;

public interface OnAdapterLongClickListener<T> {
    boolean onAdapterLongClick(View view,T data, int position);
}
