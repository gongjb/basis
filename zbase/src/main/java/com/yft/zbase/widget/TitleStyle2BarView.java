package com.yft.zbase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yft.zbase.R;

public class TitleStyle2BarView extends TitleBarView {


    public TitleStyle2BarView(Context context) {
        super(context);
    }

    public TitleStyle2BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void findLayoutView(Context context) {
        View.inflate(context, R.layout.item_title_style2_layout, this);
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        TextView textView = findViewById(R.id.tv_title);
        textView.getPaint().setStrokeWidth(8);
    }


    public void showAddress(String address, View.OnClickListener onClickListener) {
        TextView textView = findViewById(R.id.tv_address);
        ImageView ivGo = findViewById(R.id.iv_go);
        ImageView ivAddress = findViewById(R.id.iv_address);
        textView.setVisibility(View.VISIBLE);
        ivGo.setVisibility(View.VISIBLE);
        ivAddress.setVisibility(View.VISIBLE);

        textView.setText(address);
        textView.setOnClickListener(onClickListener);
        ivGo.setOnClickListener(onClickListener);
        ivAddress.setOnClickListener(onClickListener);
    }

    public void setAddress(String address) {
        TextView textView = findViewById(R.id.tv_address);
        ImageView ivGo = findViewById(R.id.iv_go);
        ImageView ivAddress = findViewById(R.id.iv_address);
        textView.setVisibility(View.VISIBLE);
        ivGo.setVisibility(View.VISIBLE);
        ivAddress.setVisibility(View.VISIBLE);
        textView.setText(address);
    }
}
