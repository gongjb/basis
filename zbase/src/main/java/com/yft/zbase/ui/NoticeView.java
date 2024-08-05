package com.yft.zbase.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.yft.zbase.R;

import org.w3c.dom.Text;

public class NoticeView extends LinearLayout {

    private ImageView imageView;
    private TextView tvNotice;
    private TextView tvDes;

    public NoticeView(Context context) {
        super(context);
        initView(context);
    }

    public NoticeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NoticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public NoticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView (Context context) {
        View.inflate(context, R.layout.notice_view_layout,this);
        imageView = findViewById(R.id.img_notice);
        tvNotice = findViewById(R.id.tv_notice);
        tvDes = findViewById(R.id.tv_des);
    }

    public void setImg(@RawRes int imgId) {
        Glide.with(getContext()).load(imgId).into(imageView);
    }

    public void setTv(String tv) {
        tvNotice.setText(tv);
    }

    public void setTvColor(@ColorRes int color) {
        tvNotice.setTextColor(getResources().getColor(color));
    }

    public void setTvDes(String des) {
        tvDes.setText(des);
    }
}
