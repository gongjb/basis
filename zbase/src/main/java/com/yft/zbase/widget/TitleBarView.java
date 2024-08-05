package com.yft.zbase.widget;

import static com.yft.zbase.utils.Constant.PARAMETER;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.R;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IDevice;
import com.yft.zbase.utils.Constant;

public class TitleBarView extends LinearLayout implements View.OnClickListener {
    private int type = 0;
    public MMKV mMmkv;
    protected RoundRelativeLayout roundRelativeLayout;
    private IDevice mDevice;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attributeSet) {
        mDevice = DynamicMarketManage.getInstance().getServer(IDevice.DEVICE_SERVER);
        findLayoutView(context);
        roundRelativeLayout = findViewById(R.id.rounded_layout);
        setStatusBar(context);
    }

    /**
     * 初始化view
     * @param context
     */
    protected void findLayoutView(Context context) {
        View.inflate(context, R.layout.view_title_bar_layout, this);
        setBackClick();
    }

    public RoundRelativeLayout getRoundRelativeLayout() {
        return roundRelativeLayout;
    }

    protected void setStatusBar(Context context) {
        mMmkv = MMKV.mmkvWithID(PARAMETER, MMKV.MULTI_PROCESS_MODE);
        View view = findViewById(R.id.v_top);
        ViewGroup.LayoutParams marginLayoutParams = view.getLayoutParams();
        marginLayoutParams.height = mDevice.getStatusBarHi();
        view.setLayoutParams(marginLayoutParams);
    }


    public void setTopViewHide(int viewHide) {
        findViewById(R.id.v_top).setVisibility(viewHide);
    }

    protected void setBackClick() {
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    // 自定义左边的按钮
    public void showLeftImage(int imageId) {
        showLeftImage(imageId, null);
    }

    public void showLeftImage(int imageId, OnIconClickListener onIconClickListener) {
        ImageView imageView = findViewById(R.id.iv_back);
        imageView.setVisibility(View.VISIBLE);
        Glide.with(this).load(imageId).into(imageView);
        if (onIconClickListener != null) {
            imageView.setOnClickListener(v -> {
                onIconClickListener.onLeftClick(v);
            });
        }
    }

    // 自定义左边的按钮
    public void showRightImage(int imageId) {
        showRightImage(imageId, null);
    }

    public void showRightImage(int imageId, OnIconClickListener onIconClickListener) {
        ImageView imageView = findViewById(R.id.iv_right);
        imageView.setVisibility(View.VISIBLE);
        Glide.with(this).load(imageId).into(imageView);
        if (onIconClickListener != null) {
            imageView.setOnClickListener(v -> {
                onIconClickListener.onRightClick(v);
            });
        }
    }

    /**
     */
    public void showRightImage() {
        ImageView ri = findViewById(R.id.iv_right);
        ri.setVisibility(View.VISIBLE);
    }

    /**
     */
    public void hideRightImage() {
        ImageView ri = findViewById(R.id.iv_right);
        ri.setVisibility(View.INVISIBLE);
    }

    public interface OnIconClickListener {
        void onLeftClick(View v);

        default void onRightClick(View v){}

        default void onMiddleClick(View v) {}
    }

    public int getType() {
        return type;
    }

    public void setTitle(String title) {
        TextView tvTlt = findViewById(R.id.tv_title);
        if (tvTlt != null) {
            tvTlt.setVisibility(View.VISIBLE);
            tvTlt.setText(title);
        }
    }

    public void setTitle(String title, int color) {
        TextView tvTlt = findViewById(R.id.tv_title);
        tvTlt.setVisibility(View.VISIBLE);
        tvTlt.setTextColor(color);
        tvTlt.setText(title);
    }

    public void setTitle(int title) {
        TextView tvTlt = findViewById(R.id.tv_title);
        if (tvTlt != null)
            tvTlt.setText(title);
    }

    public void setRightText(String rightText) {
        TextView tvTlt = findViewById(R.id.tv_right);
        if (tvTlt != null)
            tvTlt.setVisibility(View.VISIBLE);
        tvTlt.setText(rightText);
    }

    public void setRightText(String rightText, OnClickListener onClickListener) {
        TextView tvTlt = findViewById(R.id.tv_right);
        if (tvTlt != null)
            tvTlt.setVisibility(View.VISIBLE);
        tvTlt.setText(rightText);
        tvTlt.setOnClickListener(onClickListener);
    }

    public void setRightTextColor(int color) {
        TextView tvTlt = findViewById(R.id.tv_right);
        if (tvTlt != null)
            tvTlt.setVisibility(View.VISIBLE);
        tvTlt.setTextColor(color);
    }

    /**
     * @param rv              图片资源
     * @param onClickListener 点击时间
     */
    public void setRightImage(int rv, OnClickListener onClickListener) {
        ImageView ri = findViewById(R.id.iv_right);
        ri.setVisibility(View.VISIBLE);
        ri.setOnClickListener(onClickListener);
        Glide.with(this).load(rv).into(ri);
    }


    public void setRedShow(int vg) {
        findViewById(R.id.v_red).setVisibility(vg);
    }

    public void setLeftBackImage() {
        findViewById(R.id.iv_back)
                .setVisibility(View.VISIBLE);
    }

    public void setLeftBackImageInvisible() {
        findViewById(R.id.iv_back)
                .setVisibility(View.INVISIBLE);
    }

    public void setLeftBackImage(View.OnClickListener onClickListener) {
        View view = findViewById(R.id.iv_back);
        view .setVisibility(View.VISIBLE);
        view.setOnClickListener(onClickListener);
    }


    public void onBackClick() {
        if (getContext() instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) getContext();
            activity.finish();
            return;
        }

        if (getRootView().getContext() instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) getContext();
            activity.finish();
            return;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackClick();
        }
    }
}
