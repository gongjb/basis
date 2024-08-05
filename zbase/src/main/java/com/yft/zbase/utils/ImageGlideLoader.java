package com.yft.zbase.utils;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gongjiebin.latticeview.ImageLoader;

public class ImageGlideLoader implements ImageLoader {

    public ImageGlideLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path != null) {
            Glide.with(context).load(path).into(imageView);
        } else {
            LOGE("======> path is null");
        }
    }

    @Override
    public void displayDrawableImage(Context context, Integer path, ImageView imageView) {

    }
}
