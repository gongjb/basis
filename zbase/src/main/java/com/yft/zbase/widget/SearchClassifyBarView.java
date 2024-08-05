package com.yft.zbase.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yft.zbase.R;
import com.yft.zbase.ui.OnCheckLoginClick;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

/**
 * 分类中的搜索
 */
public class SearchClassifyBarView extends TitleBarView {

    private int[] mColors;
    private SearchBarView.ISearchToListener iSearchToListener;

    public SearchClassifyBarView(Context context) {
        super(context);
    }

    public SearchClassifyBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSearchToListener(SearchBarView.ISearchToListener iSearchToListener) {
        this.iSearchToListener = iSearchToListener;
    }

    @Override
    protected void findLayoutView(Context context) {
        View.inflate(context, R.layout.item_search_classify_layout, this);
        findViewById(R.id.ed_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iSearchToListener != null) {
                    iSearchToListener.onSearch("");
                }
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iSearchToListener != null) {
                    iSearchToListener.onSearch("");
                }
            }
        });
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        mColors = new int[] {
                context.getResources().getColor(R.color.search_btn_start_color),
                context.getResources().getColor(R.color.search_btn_end_color)
        };
        roundRelativeLayout.setStrokeWidth(2f);
        roundRelativeLayout.setStrokeColor(getContext().getResources().getColor(R.color.ui_search_color));

        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        findViewById(R.id.btn_search).setBackground(grad);
        UIUtils.setViewRadius(findViewById(R.id.btn_search), R.dimen.ui_16_dp);

        TextView textView = findViewById(R.id.tv_title);
        textView.getPaint().setStrokeWidth(8);
    }
}
