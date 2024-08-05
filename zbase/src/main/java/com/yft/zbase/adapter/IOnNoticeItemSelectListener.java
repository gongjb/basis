package com.yft.zbase.adapter;

import com.yft.zbase.bean.SpecssBean;

import java.util.List;

/**
 * 监听item点击发生的变化
 */
public interface IOnNoticeItemSelectListener {
    /**
     * item发生了变化
     * @param noClickItems
     */
    void onNoticeItemChange(List<SpecssBean.ItemsBean> noClickItems);

    // 得到当前选中的规格
    int getItemSpecsCount();
}
