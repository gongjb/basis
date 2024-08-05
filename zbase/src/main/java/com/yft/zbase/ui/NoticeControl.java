package com.yft.zbase.ui;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;

import com.yft.zbase.R;
import com.yft.zbase.server.LanguageManage;

public class NoticeControl {

    /**
     * 点击事件必须在枚举中配置才会响应 {@link com.yft.zbase.ui.NoticeControl.NoticeState}
     */
    protected OnNoticeClickListener mOnNoticeClickListener;

    public enum NoticeState {
        // 提示
        ERROR_EMPTY("这里什么都没有哦！", false, R.mipmap.gift),
        ERROR_EMPTY_RELOAD("这里什么都没有哦！点击页面刷新", true, R.mipmap.gift),
        ORDER_ALL_EMPTY_RELOAD("暂无订单。", true, R.mipmap.gift),
        ORDER_UNPAID_EMPTY_RELOAD("暂无待付款订单。", true, R.mipmap.gift),
        ORDER_UNSHIPPED_EMPTY_RELOAD("暂无待发货订单。", true, R.mipmap.gift),
        ORDER_UNRECEIVED_GOODS_EMPTY_RELOAD("暂无待收货订单。", true, R.mipmap.gift),
        ORDER_EVALUATE_GOODS_EMPTY_RELOAD("暂无待评价订单。", true, R.mipmap.gift),
        ORDER_REFUND_GOODS_EMPTY_RELOAD("暂无退款订单", true, R.mipmap.gift),
        // 数据丢失-服务器异常
        ERROR_SERVICE("抱歉，数据好像丢失了！", false, 0),

        // 当前无网络
        ERROR_NETWORK("好像没有网络哦！",false, 0),
        ERROR_NETWORK_RELOAD("好像没有网络哦！点击页面刷新!",true, R.mipmap.net_error),

        // webview 网页丢失
        ERROR_PAGE("网页好像丢失了!", false, 0),
        ERROR_URL("您访问的网址不存在哦！", false, 0),
        ERROR_PAGE_RELOAD("网页好像丢失了，点击页面刷新！", true, 0),
        ERROR_EMPTY_ADDRESS("还没有地址，请点击下方按钮添加收货地址", false, 0),
        CART_NULL_RELOAD("您还未添加商品至购物车！", true, R.mipmap.gift),
        GOODS_NULL_RELOAD("商品被外星人劫走啦！", false, R.mipmap.ic_waixinren),
        SUCCESS("",false,0);

        private String prompt;
        private boolean isClick;
        private int drawable;

        NoticeState(String prompt, boolean isClick, int drawable) {
            this.prompt = prompt;
            this.isClick = isClick;
            this.drawable = drawable;
        }

        public String getPrompt() {
            return prompt;
        }

        public boolean isClick() {
            return isClick;
        }

        public int getDrawable() {
            return drawable;
        }
    }

    public interface OnNoticeClickListener {
        void onClickNoticeView(NoticeView view, NoticeState noticeState);
    }

    public NoticeControl setOnNoticeClickListener(OnNoticeClickListener mOnNoticeClickListener) {
        this.mOnNoticeClickListener = mOnNoticeClickListener;
        return this;
    }


    public NoticeControl showNoticeState(final NoticeView noticeView, ViewGroup rv, final NoticeState state) {
        this.showNoticeState(noticeView, rv, state, mOnNoticeClickListener);
        return this;
    }

    /**
     * 更换文本颜色
     * @param noticeView
     * @param rv
     * @param state
     * @param color
     * @param onNoticeClickListener
     * @return
     */
    public NoticeControl showNoticeStateTextColor(final NoticeView noticeView, ViewGroup rv, final NoticeState state, @ColorRes int color, final OnNoticeClickListener onNoticeClickListener) {
        if (color != 0) {
            noticeView.setTvColor(color);
        }
        return showNoticeState(noticeView,rv,state,onNoticeClickListener);
    }


    public NoticeControl showNoticeState(final NoticeView noticeView, ViewGroup rv, final NoticeState state,final OnNoticeClickListener onNoticeClickListener) {
        if (noticeView != null && state != null) {
            if (state == NoticeState.SUCCESS) {
                noticeView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                return this;
            }
            noticeView.setVisibility(View.VISIBLE);
            if (rv != null) {
                rv.setVisibility(View.GONE);
            }
            noticeView.setTv(LanguageManage.getString(state.getPrompt()));
            if (state.getDrawable() != 0) {
                noticeView.setImg(state.getDrawable());
            }
            noticeView.setOnClickListener(view -> {
                if (onNoticeClickListener != null) {
                    if (state.isClick) {
                        onNoticeClickListener.onClickNoticeView(noticeView, state);
                    }
                }
            });
        }
        return this;
    }


    public NoticeControl showNoticeState(final NoticeView noticeView, ViewGroup rv, final NoticeState state,final OnNoticeClickListener onNoticeClickListener, String des) {
        if (noticeView != null && state != null) {
            if (!TextUtils.isEmpty(des)) {
                noticeView.setTvDes(des);
            }
            if (state == NoticeState.SUCCESS) {
                noticeView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                return this;
            }
            noticeView.setVisibility(View.VISIBLE);
            if (rv != null) {
                rv.setVisibility(View.GONE);
            }
            noticeView.setTv(LanguageManage.getString(state.getPrompt()));
            if (state.getDrawable() != 0) {
                noticeView.setImg(state.getDrawable());
            }
            noticeView.setOnClickListener(view -> {
                if (onNoticeClickListener != null) {
                    if (state.isClick) {
                        onNoticeClickListener.onClickNoticeView(noticeView, state);
                    }
                }
            });
        }
        return this;
    }
}
