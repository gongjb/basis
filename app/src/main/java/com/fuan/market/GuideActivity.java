package com.fuan.market;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.chenenyu.router.annotation.Route;
import com.fuan.market.router.AppRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.KVBean;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.OnZoomClickListener;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import com.fuan.market.databinding.ActivityGuideActivityBinding;

import com.fuan.market.R;


/**
 * 引导页
 */
@Route(AppRouter.ACTIVITY_GUIDE)
public class GuideActivity extends BaseActivity<ActivityGuideActivityBinding, BaseViewModel> {
    @Override
    public void initView() {
        mDataBing.banner.setAdapter(new BannerImageAdapter<KVBean>(KVBean.getTestData4()) {
            @Override
            public void onBindView(BannerImageHolder holder, KVBean data, int position, int size) {
                //图片加载自己实现
                UIUtils.setImgUrl(holder.imageView, data.key);
            }
        }).setIndicator(new CircleIndicator(this));
    }

    @Override
    public void initListener() {
        OnZoomClickListener onZoomClickListener = new OnZoomClickListener() {
            @Override
            public void onClick(View view) {
                // 去登陆页面
                RouterFactory.getInstance().startRouterActivity(GuideActivity.this, RouterFactory.getInstance().getPage("MainActivity"));
            }
        };
        onZoomClickListener.setThemeColor(getResources().getColor(com.yft.zbase.R.color.theme_assistant));
        mDataBing.rounded.setCorner(360);
        mDataBing.rounded.setOnTouchListener(onZoomClickListener);
        mDataBing.rounded.setOnClickListener(onZoomClickListener);

        mDataBing.banner.setOnBannerListener((data, position) -> {

        });

        mDataBing.banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //&& mDataBing.btnReload.getVisibility() == View.GONE
                if (position >= 2 && mDataBing.rounded.getVisibility() == View.GONE) {
                    mDataBing.rounded.setVisibility(View.VISIBLE);
                    mDataBing.rounded.setClipToOutline(true);
                    mDataBing.rounded.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                createAnimator();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //
            }
        });
    }

    private void createAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.15f);
        animator.addUpdateListener(animation -> {
            if (isFinishing()) {
                return;
            }
            float progress = (float) animation.getAnimatedValue();
            final float round = 360 * progress;
            mDataBing.rounded.setCorner(round);
        });

        ValueAnimator animator1 = ValueAnimator.ofFloat(1f, 0.4f);
        animator1.addUpdateListener(valueAnimator -> {
            if (isFinishing()) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = mDataBing.rounded.getLayoutParams();
            float height = Utils.dip2px(GuideActivity.this, 90) * (float) valueAnimator.getAnimatedValue();
            layoutParams.height = (int) height;
            mDataBing.rounded.setLayoutParams(layoutParams);
        });

        ValueAnimator animator2 = ValueAnimator.ofFloat(0.6f, 1f);
        animator2.addUpdateListener(valueAnimator -> mDataBing.rounded.setAlpha((Float) valueAnimator.getAnimatedValue()));


        ValueAnimator animator3 = ValueAnimator.ofFloat(1f, 1.4f);
        animator3.addUpdateListener(valueAnimator -> {
            if (isFinishing()) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = mDataBing.rounded.getLayoutParams();
            float width = Utils.dip2px(GuideActivity.this, 90) * (float) valueAnimator.getAnimatedValue();
            layoutParams.width = (int)width;
            mDataBing.rounded.setLayoutParams(layoutParams);
        });

        //
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator1, animator2 , animator3);

        ValueAnimator animator4 = ValueAnimator.ofFloat(0f, 0.6f);
        animator4.addUpdateListener(valueAnimator -> {
            if (isFinishing()) {
                return;
            }
            mDataBing.rounded.setAlpha((Float) valueAnimator.getAnimatedValue());
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.playSequentially(animator4, set);
        animatorSet.start();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        mDataBing.rounded.clearAnimation();
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_guide_activity;
    }
}
