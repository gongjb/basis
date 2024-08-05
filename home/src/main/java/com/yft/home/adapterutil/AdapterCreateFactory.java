package com.yft.home.adapterutil;

import android.graphics.Color;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.yft.home.vadapter.VideoListAdapter;
import com.yft.home.vadapter.VideoHomeBannerAdapter;
import com.yft.home.vadapter.ScrollFixLayoutTopAdapter;
import com.yft.zbase.adapter.HomeBottomAdapter;
import com.yft.zbase.adapter.AdapterFactory;
import com.yft.zbase.adapter.SubtitleAdapter;
import com.yft.zbase.utils.Utils;

public class AdapterCreateFactory extends AdapterFactory {

    // top banner
    public static final String TYPE_BANNER = "banner";
    // 分类
    public static final String TYPE_CLASSIFY = "classify";
    // 影视列表
    public static final String TYPE_VIDEO_LIST = "home_video_list";
    // 底部标签-也可以是加载更多

    private static AdapterCreateFactory instance;

    private AdapterCreateFactory() {

    }

    public static synchronized AdapterCreateFactory getInstance() {
        if (instance == null) {
            synchronized (AdapterCreateFactory.class) {
                if (instance == null) {
                    instance = new AdapterCreateFactory();
                }
            }
        }
        return instance;
    }


    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag, float proportion, String url) {
        DelegateAdapter.Adapter adapter;
        AdapterBean adapterBean = new AdapterBean();
        adapterBean.tag = tag;
        adapterBean.groupId = activity.getClass().getCanonicalName();
        switch (adapterType) {
            case TYPE_VIDEO_LIST:
                // 视频
                LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
                linearLayoutHelper.setMarginLeft(Utils.dip2px(activity, 12));
                linearLayoutHelper.setMarginRight(Utils.dip2px(activity, 12));
                linearLayoutHelper.setMarginTop(Utils.dip2px(activity, 12));
                adapter = new VideoListAdapter(activity, linearLayoutHelper);
                adapterBean.adapter = adapter;
                putAdapter(adapterBean);
                return adapter;

            case TYPE_SUBTITLE:
                // 标题
                linearLayoutHelper = new LinearLayoutHelper();
                adapter = new SubtitleAdapter(linearLayoutHelper);
                adapterBean.adapter = adapter;
                putAdapter(adapterBean);
                return adapter;
            default: {
                return null;
            }
        }
    }

    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag) {
        return createAdapter(adapterType, activity, tag, activity.getClass().getCanonicalName());
    }


    @Override
    public DelegateAdapter.Adapter createAdapter(String adapterType, FragmentActivity activity, String tag, String groupId) {
        DelegateAdapter.Adapter adapter;
        AdapterBean adapterBean = new AdapterBean();
        adapterBean.tag = tag;
        adapterBean.groupId = groupId;
        switch (adapterType) {
            case TYPE_BANNER:
                SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
                singleLayoutHelper.setMarginTop(Utils.dip2px(activity, 10));
                adapter = new VideoHomeBannerAdapter(singleLayoutHelper, activity);
                //
                activity.getLifecycle().addObserver((VideoHomeBannerAdapter)adapter);
                adapterBean.adapter = adapter;
                putAdapter(adapterBean);
                return adapter;
            case TYPE_BOTTOM:
                singleLayoutHelper = new SingleLayoutHelper();
                singleLayoutHelper.setMarginTop(Utils.dip2px(activity, 10));
                adapter = new HomeBottomAdapter(singleLayoutHelper);
                adapterBean.adapter = adapter;
                putAdapter(adapterBean);
                return adapter;
            case TYPE_FIX_TOP:
                //第一个参数 是固定的位置  后面两个参数是固定之后的偏移量
                ScrollFixLayoutHelper scrollFixLayoutHelper = new ScrollFixLayoutHelper(ScrollFixLayoutHelper.BOTTOM_RIGHT,10,10);
                // 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
                scrollFixLayoutHelper.setPadding(20, 20, 20, 20);
                // 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
                scrollFixLayoutHelper.setMargin(20, 20, 20, 20);
                scrollFixLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
                //重要参数  显示类型   一直显示SHOW_ALWAYS  滑动到位置开始位置显示SHOW_ON_ENTER  滑动到结束位置显示SHOW_ON_LEAVE 后面啷个参数一开始是不显示的
                scrollFixLayoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_ENTER);
                adapter = new ScrollFixLayoutTopAdapter(scrollFixLayoutHelper, activity);
                adapterBean.adapter = adapter;
                putAdapter(adapterBean);
                return adapter;
            default: {
                return createAdapter(adapterType, activity, tag, 0, "");
            }
        }
    }
}
