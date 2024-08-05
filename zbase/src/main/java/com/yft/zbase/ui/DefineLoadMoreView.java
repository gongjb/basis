package com.yft.zbase.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yft.zbase.R;
import com.yft.zbase.server.LanguageManage;

public class DefineLoadMoreView extends LinearLayout implements SwipeRecyclerView.LoadMoreView, View.OnClickListener {

    private TextView tvTag;

    public DefineLoadMoreView(Context context) {
        super(context);
        initView();
    }

    public DefineLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DefineLoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DefineLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.item_home_bottom_layout, this);
        tvTag = findViewById(R.id.tv_message);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoading() {
        findViewById(R.id.rl_pro).setVisibility(View.VISIBLE);
        findViewById(R.id.pro_bar).setVisibility(View.VISIBLE);
        tvTag.setText(LanguageManage.getString("正在加载，请稍后..."));
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
            tvTag.setText(LanguageManage.getString("我也是有底线的！"));
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
