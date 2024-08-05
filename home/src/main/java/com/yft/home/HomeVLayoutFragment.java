package com.yft.home;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.chenenyu.router.annotation.Route;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yft.home.adapterutil.AdapterCreateFactory;
import com.yft.home.bean.HomeConfigBean;
import com.yft.home.databinding.HomeFragmentV2LayoutBinding;
import com.yft.home.model.HomeViewModel;
import com.yft.home.vadapter.VideoHomeBannerAdapter;
import com.yft.home.vadapter.VideoListAdapter;
import com.yft.zbase.adapter.HomeBottomAdapter;
import com.yft.zbase.adapter.SubtitleAdapter;
import com.yft.zbase.base.BaseFragment;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.DefineLoadMoreView;
import com.yft.zbase.adapter.AdapterFactory;
import com.yft.zbase.utils.Utils;
import com.yft.zbase.widget.SearchBarView;

import java.util.ArrayList;
import java.util.List;

@Route(RouterFactory.FRAGMENT_HOME_VLAYOUT)
public class HomeVLayoutFragment extends BaseFragment<HomeFragmentV2LayoutBinding, HomeViewModel> {
    private DelegateAdapter mDelegateAdapter;
    private List<DelegateAdapter.Adapter> mAdapterList;
    private DefineLoadMoreView defineLoadMoreView;
    private float height;

    @Override
    public void initView() {
        height = Utils.getScreenHeight(getContext()) * 2;
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.BOTTOM_TOP,
                mColors);
        mDataBing.llHomeMain.setBackground(grad);
        addAlphaHelp();
    }

    private void addAlphaHelp() {
        mDataBing.rlHomeMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollChange(recyclerView);
                int i1 = recyclerView.computeVerticalScrollOffset();

                int a = i1 / 5;
                if (a > 150) {
                    a = 150;
                }

                if (a <= 0) {
                    a = 0;
                }
                mDataBing.titleBar.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(com.yft.zbase.R.color.themeMainColorTwo), a));
            }
        });
    }

    private void scrollChange(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        int i1 = (position) * itemHeight - firstVisiableChildView.getTop();
        if (i1 > height) {
            if (mDataBing.flTopBack.getVisibility() != View.VISIBLE) {
                mDataBing.flTopBack.setVisibility(View.VISIBLE);
            }
        } else {
            if (mDataBing.flTopBack.getVisibility() != View.GONE) {
                mDataBing.flTopBack.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initListener() {
        viewModel.getHomeConfigBeanMutableLiveData().observe(this, this::onHomeConfigList);
        viewModel.getErrorMutableLiveData().observe(this, this::onError);
        mDataBing.titleBar.setSearchOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSearchActivity();
            }
        });

        mDataBing.flTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mDataBing.rlHomeMain.scrollToPosition(0);
            }
        });
        mDataBing.titleBar.setSearchToListener(new SearchBarView.ISearchToListener() {
            @Override
            public void onSearch(String key) {
                toSearchActivity();
            }
        });

        mDataBing.titleBar.setEditTextFocusable(false)
                .setSysStatus(View.GONE)
                .setNoticeStatus(View.GONE)
                .setSearchBtnStatus(View.GONE)
                .showLeftICON();

        mDataBing.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 是下拉刷新
                viewModel.postHomeConfig();
            }
        });
        defineLoadMoreView = new DefineLoadMoreView(requireContext());
        defineLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDataBing.rlHomeMain.addFooterView(defineLoadMoreView); // 使用默认的加载更多的View。
        mDataBing.rlHomeMain.setLoadMoreView(defineLoadMoreView);
        mDataBing.rlHomeMain.setLoadMoreListener(mLoadMoreListener); // 加载更多的监听。
    }

    private void toSearchActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "normal");
        RouterFactory.startRouterBundleActivity(HomeVLayoutFragment.this.getContext(), RouterFactory.ACTIVITY_SEARCH, bundle);
    }

    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = () -> {

    };

    @Override
    public void initData() {
        // 检查服务器地址
        viewModel.requestService();
        viewModel.postHomeConfig();
    }

    @Override
    protected void handleServiceAddress(ServiceBean serviceBean) {
        super.handleServiceAddress(serviceBean);
    }

    public void onError(String tag) {
        mDataBing.swipe.setRefreshing(false);
        mDataBing.rlHomeMain.loadMoreError(0, "请检查您的网络情况");
    }


    public void onHomeConfigList(HomeConfigBean homeConfigBean) {
        mDataBing.swipe.setRefreshing(false);
        mDataBing.rlHomeMain.loadMoreFinish(false, false);
        LOGE("已经进入homeConfigBean");
        VideoHomeBannerAdapter homeBannerAdapter = AdapterCreateFactory.getInstance().getAdapter(AdapterCreateFactory.TYPE_BANNER + "0");
        SubtitleAdapter sAdapter = AdapterCreateFactory.getInstance().getAdapter(AdapterFactory.TYPE_SUBTITLE + "4");
        VideoListAdapter vAdapter = AdapterCreateFactory.getInstance().getAdapter(AdapterCreateFactory.TYPE_VIDEO_LIST + "5");

        if (homeBannerAdapter == null) {
            // 被销毁了... 再次创建
            homeBannerAdapter = (VideoHomeBannerAdapter) AdapterCreateFactory.getInstance().createAdapter(AdapterCreateFactory.TYPE_BANNER, requireActivity(), AdapterCreateFactory.TYPE_BANNER + "0");
            sAdapter = (SubtitleAdapter) AdapterCreateFactory.getInstance().createAdapter(AdapterFactory.TYPE_SUBTITLE, requireActivity(), AdapterFactory.TYPE_SUBTITLE + "4");
            vAdapter = (VideoListAdapter) AdapterCreateFactory.getInstance().createAdapter(AdapterCreateFactory.TYPE_VIDEO_LIST, requireActivity(), AdapterCreateFactory.TYPE_VIDEO_LIST + "5");
        }

        mAdapterList = new ArrayList<>();
        if (viewModel.isExistList(homeConfigBean, AdapterCreateFactory.TYPE_BANNER)) {
            homeBannerAdapter.setBannerListBeanList(homeConfigBean.getBannerList());
            homeBannerAdapter.cleanViews();
            mAdapterList.add(homeBannerAdapter);
        }

        if (viewModel.isExistList(homeConfigBean, AdapterCreateFactory.TYPE_CLASSIFY)) {
            sAdapter.setTag(viewModel.getString("畅销榜单"));
            mAdapterList.add(sAdapter);
            vAdapter.setData(homeConfigBean.getKingkongList());
            mAdapterList.add(vAdapter);
        }


        mDataBing.rlHomeMain.getRecycledViewPool().clear();
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        mDelegateAdapter = new DelegateAdapter(virtualLayoutManager);
        mDelegateAdapter.addAdapters(mAdapterList);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 20);
        mDataBing.rlHomeMain.setRecycledViewPool(viewPool);
        mDataBing.rlHomeMain.setLayoutManager(virtualLayoutManager);
        mDataBing.rlHomeMain.setAdapter(mDelegateAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_v2_layout;
    }
}
