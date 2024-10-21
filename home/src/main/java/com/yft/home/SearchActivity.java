package com.yft.home;


import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import com.chenenyu.router.annotation.Route;
import com.gongjiebin.latticeview.AutoLineDeleteView;
import com.gongjiebin.latticeview.AutoLineLayout;
import com.gongjiebin.latticeview.KVBean;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yft.home.adapterutil.AdapterCreateFactory;
import com.yft.home.databinding.ActivitySearchLayoutBinding;
import com.yft.home.model.HomeViewModel;

import com.yft.home.router.HomeRouter;
import com.yft.zbase.base.BaseActivity;

import com.yft.zbase.utils.Utils;
import com.yft.zbase.widget.SearchBarView;

import java.util.ArrayList;
import java.util.List;


/**
 * 搜索activity
 */
@Route(HomeRouter.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchLayoutBinding, HomeViewModel> {
    private boolean mDel;

    @Override
    public void initView() {

    }

    /**
     * 加载本地的历史搜索记录
     */
    private void loadKeys(boolean isDel) {
        this.mDel = isDel;
        mDataBing.auto.removeViews();
        AutoLineLayout.AutoEditParams autoEditParams = new AutoLineDeleteView.AutoEditParams();
        //选中与未选中背景颜色变化
        autoEditParams.auto_bg_color = com.yft.zbase.R.drawable.plan_auto_no_background;

        autoEditParams.select_bg_color = com.yft.zbase.R.drawable.plan_auto_no_background;

        // 选中与未选中字体大小设置
        autoEditParams.textSize = 12;
        autoEditParams.margin_lr = 18;
        autoEditParams.textSelectSize = 12;
        // 选中与未选中字体颜色变化
        autoEditParams.textColor = (com.yft.zbase.R.color.sd_b_white);
        autoEditParams.textSelectColor = (com.yft.zbase.R.color.sd_b_white);

        // AutoLineLayout.TYPE_RADIO 单选  AutoLineLayout.TYPE_GROUP 复选
        autoEditParams.sel_type = AutoLineLayout.TYPE_NOT;
        if (isDel) {
            autoEditParams.isShowDelImg = true;
        } else {
            autoEditParams.isShowDelImg = false;
        }

        autoEditParams.width = (int) Utils.getBodyWidth(SearchActivity.this);
        List<KVBean> list = new ArrayList<>();

        String[] keys = mViewModel.getKeys();
        if (keys != null) {
            KVBean kvBean = null;
            for (String str : keys) {
                kvBean = new KVBean();
                kvBean.setValue(str);
                kvBean.isSel = true;
                list.add(kvBean);
            }

            if (kvBean != null) {
                mDataBing.tvSh.setVisibility(View.VISIBLE);
                mDataBing.ivDel.setVisibility(View.VISIBLE);
            } else {
                mDataBing.tvSh.setVisibility(View.GONE);
                mDataBing.ivDel.setVisibility(View.GONE);
            }
        } else {
            this.mDel = false;
            mDataBing.ivDel.setText("删除");
            mDataBing.tvSh.setVisibility(View.GONE);
            mDataBing.ivDel.setVisibility(View.GONE);
            mDataBing.tvAllDel.setVisibility(View.GONE);
        }

        mDataBing.auto.setEditParams(autoEditParams);
        mDataBing.auto.setViews(list);
        mDataBing.auto.startView();

        mDataBing.auto.setOnItemClickListener(new AutoLineLayout.OnItemClickListener() {
            @Override
            public void onClickItem(View view, KVBean kvBean) {
                mDataBing.titleBar.setSearchKey(kvBean.getValue());
                // 触发刷新
                onSearchToListener.onSearch(kvBean.getValue());
            }
        });

        mDataBing.auto.setOnDelectTagListener(new AutoLineDeleteView.OnDelectTagListener() {
            @Override
            public void onDel(View v, KVBean bean) {
                if (bean != null) {
                    mViewModel.removeKeys(bean.getValue());
                    loadKeys(true);
                }
            }
        });
    }


    @Override
    public void initListener() {
        mDataBing.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = mDataBing.tvAllDel.getVisibility();
                if (v == View.GONE) {
                    mDataBing.ivDel.setText("完成");
                    mDataBing.tvAllDel.setVisibility(View.VISIBLE);
                    loadKeys(true);
                } else {
                    mDataBing.ivDel.setText("删除");
                    mDataBing.tvAllDel.setVisibility(View.GONE);
                    loadKeys(false);
                }
            }
        });

        mDataBing.tvAllDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBing.tvAllDel.setVisibility(View.GONE);
                // 全部删除
                mViewModel.getUserServer().saveSearchKey(""); // 清除所有的缓存
                loadKeys(false);
            }
        });

        mDataBing.flTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBing.rvView.scrollToPosition(0);
            }
        });

    }

    private SearchBarView.ISearchToListener onSearchToListener = key -> {
        if (!TextUtils.isEmpty(key)) {
            // 保存关键字
            mViewModel.getUserServer().saveSearchKey(key);
            loadKeys(this.mDel);
            hideKeyboard();
        } else {
           // 搜索为空的处理
        }
    };

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            // 该加载更多啦。
            String key = mDataBing.titleBar.getSearchKey();
            // 请求数据
        }
    };



    @Override
    public void initData() {
        mDataBing.swipeContent.post(new Runnable() {
            @Override
            public void run() {
               //预加载数据
            }
        });

        loadKeys(false);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 屏幕尺寸发生变化
    }



    @Override
    public int getLayout() {
        return R.layout.activity_search_layout;
    }

    @Override
    protected void onDestroy() {
        AdapterCreateFactory.getInstance().removeGroupIdAdapter(getClass().getCanonicalName());
        super.onDestroy();
    }
}
