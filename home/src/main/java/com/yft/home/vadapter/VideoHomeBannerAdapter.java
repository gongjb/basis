package com.yft.home.vadapter;


import static com.yft.zbase.utils.Logger.LOGE;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.yft.home.R;
import com.yft.home.VideoBannerPageView;
import com.yft.home.bean.HomeConfigBean;
import com.yft.home.databinding.ItemHomeTopBannerBinding;
import com.yft.zbase.adapter.BaseLayoutHolder;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGAViewPager;

public class VideoHomeBannerAdapter extends DelegateAdapter.Adapter<BaseLayoutHolder>  implements LifecycleEventObserver {
    private LayoutHelper layoutHelper;
    private FragmentActivity mContext;
    private List<HomeConfigBean.BannerListBean> mBannerListBeanList;
    private BGABanner mBannerView;

    private List<View> views = new ArrayList<>();

    public void cleanViews() {
        VideoBannerPageView.cleanMediaSource();
        views.clear();
    }

    public VideoHomeBannerAdapter(LayoutHelper layoutHelper, FragmentActivity context) {
        this.layoutHelper = layoutHelper;
        this.mContext = context;
    }

    public void setBannerListBeanList(List<HomeConfigBean.BannerListBean> mBannerListBeanList) {
        this.mBannerListBeanList = mBannerListBeanList;
    }

    @NonNull
    @Override
    public BaseLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BaseLayoutHolder(layoutInflater.inflate(R.layout.item_home_top_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseLayoutHolder holder, int position) {
        ItemHomeTopBannerBinding itemHomeTopCardBinding = (ItemHomeTopBannerBinding) holder.viewDataBinding;
        float height = (Utils.getBodyWidth(mContext) - Utils.dip2px(mContext,52) * 2) / 0.567f;
        mBannerView = itemHomeTopCardBinding.banner;
        ViewGroup.LayoutParams layoutParams = itemHomeTopCardBinding.banner.getLayoutParams();
        layoutParams.height = (int)height;
        if (!Utils.isCollectionEmpty(mBannerListBeanList)) {
            if (mBannerListBeanList.size() == 1) {
                //banner_pointAutoPlayAble 设置为不自动播放
                itemHomeTopCardBinding.banner.setAutoPlayAble(false);
            }
            itemHomeTopCardBinding.banner.setLayoutParams(layoutParams);
            if (isNotUpdate()) return;
            for (HomeConfigBean.BannerListBean k : mBannerListBeanList){
                VideoBannerPageView view = new VideoBannerPageView(mContext);
                view.setData(k);
                view.setTag(k);
                views.add(view);
            }
            itemHomeTopCardBinding.banner.setData(views);
            itemHomeTopCardBinding.banner.setAdapter(null);
            itemHomeTopCardBinding.banner.setIndicatorVisibility(false);
            itemHomeTopCardBinding.banner.setAdapter(new BGABanner.Adapter<View, HomeConfigBean.BannerListBean>() {
                @Override
                public void fillBannerItem(BGABanner banner, View itemView, HomeConfigBean.BannerListBean model, int position) {
                    itemView.setClipToOutline(true);
                    itemView.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, itemView.getWidth(), itemView.getHeight(), Utils.dip2px(mContext, 10));
                        }
                    });

                    VideoBannerPageView view = (VideoBannerPageView) itemView;
                    view.play();
                }
            });
            itemHomeTopCardBinding.banner.setDelegate(new BGABanner.Delegate<View, HomeConfigBean.BannerListBean>() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, @Nullable HomeConfigBean.BannerListBean data, int position) {
                    HomeConfigBean.BannerListBean bannerListBean = (HomeConfigBean.BannerListBean) itemView.getTag();
                    RouterFactory.jumpToActivity(mContext, bannerListBean);
                }
            });

            BGAViewPager viewPager = itemHomeTopCardBinding.banner.getViewPager();
            /*
             android:clipChildren="false"
    android:clipToPadding="false"
             */
            viewPager.setClipChildren(false);
            viewPager.setClipToPadding(false);
            viewPager.setPageMargin(Utils.dip2px(mContext,15));
            viewPager.setOffscreenPageLimit(3);
            RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
            l.leftMargin = Utils.dip2px(mContext,40);
            l.rightMargin = Utils.dip2px(mContext,40);
            viewPager.setLayoutParams(l);
            itemHomeTopCardBinding.banner.getViewPager().setPageTransformer(true, new PeekPageTransformer());
        }
    }

    private boolean isNotUpdate() {
        // If either collection is empty, assume an update is needed
        if (Utils.isCollectionEmpty(views) || Utils.isCollectionEmpty(mBannerListBeanList)) {
            return false;
        }

        int count = 0;
        // Use a Set to quickly check if a banner ID exists in mBannerListBeanList
        Set<String> bannerIdSet = new HashSet<>();
        for (HomeConfigBean.BannerListBean bannerListBean : mBannerListBeanList) {
            bannerIdSet.add(bannerListBean.getId());
        }

        // Iterate over views to count matching banner IDs
        for (View view : views) {
            VideoBannerPageView pageView = (VideoBannerPageView) view;
            if (pageView != null && pageView.getBannerBean() != null) {
                String bannerId = pageView.getBannerBean().getId();
                // If the banner ID is in the set, increment the count
                if (bannerIdSet.contains(bannerId)) {
                    count++;
                }
            }
        }

        // If the count matches the size of views or mBannerListBeanList, no update is needed
        return count == views.size() && count == mBannerListBeanList.size();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        public static final float MIN_SCALE = 0.8f;//原图片缩小0.8倍
        private static final float MIN_ALPHA = 0.6f;//透明度

        public void transformPage(View page, float position) {
            if (position < -1) {//[-Infinity,-1)左边显示出半个的page
                page.setAlpha(MIN_ALPHA);//设置page的透明度
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) {
                if (position < 0) {//(0,-1] 第一页向左移动
                    if (position < -0.2f)//最大缩小到0.8倍
                        position = -0.2f;
                    page.setAlpha(1f + position*2);
                    page.setScaleY(1f + position);
                    page.setScaleX(1f + position);
                } else {//[1,0] 第二页向左移动 成currentItem
                    if (position > 0.2)
                        position = 0.2f;
                    page.setAlpha(1f -position*2);
                    page.setScaleY(1f - position);
                    page.setScaleX(1f - position);
                }
            } else {//(1,+Infinity]右边显示出半个的page
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            }
        }
    }

    public class IZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public class PeekPageTransformer implements ViewPager.PageTransformer {

        private static final float DEFAULT_MIN_SCALE = 0.85f;
        private float mMinScale = DEFAULT_MIN_SCALE;//view缩小值
        public static final float DEFAULT_CENTER = 0.5f;
        private int pageWidth;
        private int pageHeight;
        @Override
        public void transformPage(View view, float position) {
            pageWidth = view.getWidth();
            pageHeight = view.getHeight();

            view.setPivotY(pageHeight / 2);
            view.setPivotX(pageWidth / 2);
            if (position < -1.0f) {
                // [-Infinity,-1)
                // view移动到最左边，在屏幕之外
                handleInvisiblePage(view, position);
            } else if (position <= 0.0f) {
                // [-1,0]
                // view移动到左边
                handleLeftPage(view, position);
            } else if (position <= 1.0f) {
                // view移动到右边
                handleRightPage(view, position);
            } else {
                // (1,+Infinity]
                //  view移动到右边，在屏幕之外
                view.setPivotX(0);
                view.setScaleX(mMinScale);
                view.setScaleY(mMinScale);
            }

        }

        public void handleInvisiblePage(View view, float position) {
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
            view.setPivotX(pageWidth);
        }

        public void handleLeftPage(View view, float position) {
            float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));
        }

        public void handleRightPage(View view, float position) {

            float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));

        }
    }


    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                //onCreate();
                break;
            case ON_START:
                //onStart();

                break;
            case ON_RESUME:
                //onResume();
                if (mBannerView != null) {
                    LOGE("==>", "banner 开始播放");
                    mBannerView.startAutoPlay();
                    VideoBannerPageView.restart();
                }
                break;
            case ON_PAUSE:
                //onPause();
                if (mBannerView != null) {
                    LOGE("==>", "banner 停止播放");
                    mBannerView.stopAutoPlay();
                    VideoBannerPageView.stop();
                }
                break;
            case ON_STOP:
                //onStop();
                break;
            case ON_DESTROY:
                //onDestroy();
                VideoBannerPageView.clean();
                break;
            case ON_ANY:
                break;
        }
    }

    public void onHide(boolean isHide) {
        if (isHide) {
            mBannerView.stopAutoPlay();
            VideoBannerPageView.stop();
        } else {
            VideoBannerPageView.restart();
            VideoBannerPageView.restart();
        }
    }
}
