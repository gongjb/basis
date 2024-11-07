package com.hkbyte.filelibrary.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hkbyte.file.R;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

public class MoreFileLayout extends LinearLayout implements SwipeRecyclerView.LoadMoreView, View.OnClickListener {

    private TextView tvTag;

    public MoreFileLayout(Context context) {
        super(context);
        initView();
    }

    public MoreFileLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MoreFileLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MoreFileLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.more_button_style_layout, this);
        tvTag = findViewById(R.id.tv_message);
        tvTag.setTextColor(getContext().getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoading() {
        findViewById(R.id.rl_pro).setVisibility(View.VISIBLE);
        findViewById(R.id.pro_bar).setVisibility(View.VISIBLE);
        tvTag.setText("正在加载，请稍后...");
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        if (hasMore) {
            // 还有更多
            findViewById(R.id.pro_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_pro).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_pro).setVisibility(View.VISIBLE);
            findViewById(R.id.pro_bar).setVisibility(View.GONE);
            tvTag.setText("我也是有底线的！");
        }
    }

    @Override
    public void onWaitToLoadMore(SwipeRecyclerView.LoadMoreListener loadMoreListener) {

    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        findViewById(R.id.rl_pro).setVisibility(View.VISIBLE);
        findViewById(R.id.pro_bar).setVisibility(View.GONE);
        tvTag.setText(errorMessage);
    }
}
