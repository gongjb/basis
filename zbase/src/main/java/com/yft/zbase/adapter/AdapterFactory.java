package com.yft.zbase.adapter;

import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.vlayout.DelegateAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AdapterFactory {

    public static final String TYPE_BOTTOM = "bottom";
    // title
    public static final String TYPE_SUBTITLE = "subtitle";
    // 置顶
    public static final String TYPE_FIX_TOP = "fixTop";

    public static List<AdapterBean> stringAdapterMap = new ArrayList<>();

    public static class AdapterBean {
        // 分组id, 可根据一组数据进行删除
        public String groupId;
        public String tag;
        public DelegateAdapter.Adapter adapter;
    }

    public abstract DelegateAdapter.Adapter createAdapter(String adapterType,
                                                          FragmentActivity activity,
                                                          String tag,
                                                          float proportion,
                                                          String url);

    public abstract DelegateAdapter.Adapter createAdapter(String adapterType,
                                                          FragmentActivity activity, String tag);

    public abstract DelegateAdapter.Adapter createAdapter(String adapterType,
                                                          FragmentActivity activity, String tag, String groupId);


    public void putAdapter(AdapterBean adapter) {
        if (adapter == null) {
            return;
        }
        if (stringAdapterMap.contains(adapter)) {
            stringAdapterMap.remove(adapter);
        }
        stringAdapterMap.add(adapter);
    }


    public <T extends DelegateAdapter.Adapter> T getAdapter(int position) {
        try {
            return (T) stringAdapterMap.get(position).adapter;
        } catch (Exception e) {
            return null;
        }
    }


    public void cleanAllAdapter() {
        if (stringAdapterMap != null) {
            stringAdapterMap.clear();
        }
    }

    public <T extends DelegateAdapter.Adapter> List<T> getAdapters(String tag) {
        if (stringAdapterMap != null) {
            List<T> adapters = new ArrayList<>();
            for (AdapterBean adapter : stringAdapterMap) {
                if (tag.equals(adapter.tag)) {
                    adapters.add((T) adapter.adapter);
                }
            }
            return adapters;
        }
        return null;
    }


    public <T extends DelegateAdapter.Adapter> T getAdapter(String tag) {
        if (stringAdapterMap != null) {
            for (AdapterBean adapter : stringAdapterMap) {
                if (tag.equals(adapter.tag)) {
                    return (T) adapter.adapter;
                }
            }
        }
        return null;
    }

    /**
     * 移除指定的adapter
     * @param tag
     */
    public void removeItemAdapter(String tag) {
        if (stringAdapterMap != null && !TextUtils.isEmpty(tag)) {
            Iterator iterator = stringAdapterMap.iterator();
            while (iterator.hasNext()) {
                AdapterBean adapterBean = (AdapterBean) iterator.next();
                if (tag.equals(adapterBean.tag)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除指定的adapter
     * @param tag
     */
    public void removeItemContainsAdapter(String tag) {
        if (stringAdapterMap != null && !TextUtils.isEmpty(tag)) {
            Iterator iterator = stringAdapterMap.iterator();
            while (iterator.hasNext()) {
                AdapterBean adapterBean = (AdapterBean) iterator.next();
                if (adapterBean.tag.contains(tag)) {
                    iterator.remove();
                }
            }
        }
    }



    /**
     * 根据groupId 删除 adapter
     * @param groupId
     */
    public void removeGroupIdAdapter(String groupId) {
        if (stringAdapterMap != null && !TextUtils.isEmpty(groupId)) {
            Iterator iterator = stringAdapterMap.iterator();
            while (iterator.hasNext()) {
                AdapterBean adapterBean = (AdapterBean) iterator.next();
                if (groupId.equals(adapterBean.groupId)) {
                    iterator.remove();
                }
            }
        }
    }
}
