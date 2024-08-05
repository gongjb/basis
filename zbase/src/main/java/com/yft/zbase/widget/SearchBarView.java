package com.yft.zbase.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.yft.zbase.R;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IDevice;
import com.yft.zbase.server.ILanguage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.ui.OnCheckLoginClick;
import com.yft.zbase.utils.UIUtils;
import com.yft.zbase.utils.Utils;

/**
 * title 布局是搜索
 */
public class SearchBarView extends TitleBarView {

    private int[] mColors;
    private EditText mSearchText;
    private ImageView mIvScan;
    private FrameLayout mFlRight;
    private ISearchToListener iSearchToListener;
    private TextView btnSearch;
    private ImageView ivLogin;

    public SearchBarView(Context context) {
        super(context);
    }

    public SearchBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSearchToListener(ISearchToListener iSearchToListener) {
        this.iSearchToListener = iSearchToListener;
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        mColors = new int[] {
                context.getResources().getColor(R.color.search_btn_start_color),
                context.getResources().getColor(R.color.search_btn_end_color)
        };

//        roundRelativeLayout.setStrokeWidth(2f);
//        roundRelativeLayout.setStrokeColor(getContext().getResources().getColor(R.color.ui_search_color));

        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        findViewById(R.id.btn_search).setBackground(grad);
        UIUtils.setViewRadius(findViewById(R.id.btn_search), R.dimen.ui_16_dp);
    }

    @Override
    protected void findLayoutView(Context context) {
        View.inflate(context, R.layout.item_search_bar_draw_layout, this);
        UIUtils.setViewRadius(findViewById(R.id.v_red), R.dimen.ui_5_dp);
        ivLogin = findViewById(R.id.iv_logo);
        mSearchText =findViewById(R.id.ed_search);
        mSearchText.setHint(LanguageManage.getString("搜索你想看的热剧"));
        mIvScan = findViewById(R.id.iv_scan);
        mFlRight = findViewById(R.id.fl_right);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setText(LanguageManage.getString("搜索"));
        View viewTop = findViewById(R.id.v_top);
        IDevice iDevice = DynamicMarketManage.getInstance().getServer(IServerAgent.DEVICE_SERVER);
        ViewGroup.LayoutParams layoutParams = viewTop.getLayoutParams();
        layoutParams.height = iDevice.getStatusBarHi();
        viewTop.setLayoutParams(layoutParams);

        btnSearch.setOnClickListener(view -> {
            if (iSearchToListener != null) {
                iSearchToListener.onSearch(getSearchKey());
            }
        });

        mSearchText.setOnEditorActionListener((textView, i, keyEvent ) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (iSearchToListener != null) {
                    iSearchToListener.onSearch(getSearchKey());
                }
                return true;
            }
            return false;
        });
    }

    /**
     * 设置top view
     * @param vg
     * @return
     */
    public SearchBarView setTopVG(int vg) {
        findViewById(R.id.v_top).setVisibility(vg);
        return this;
    }

    public void setWrapConstraint() {
        try {
            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) findViewById(R.id.match_constraint).getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int se = Utils.dip2px(getContext(), 4);
            layoutParams.setMarginStart(se);
            layoutParams.setMarginEnd(se);
            findViewById(R.id.match_constraint).setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLeftBackImage() {
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this::onBackClick);
    }

    public SearchBarView showLeftICON() {
        ivLogin.setVisibility(View.VISIBLE);
        return this;
    }




    public void onBackClick(View view) {
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

//    public void openSoftKeyboard(Context context) {
//        FragmentActivity activity = null;
//        if (getContext() instanceof FragmentActivity) {
//            activity = (FragmentActivity) getContext();
//        }
//
//        if (activity == null) {
//            if (getRootView().getContext() instanceof FragmentActivity) {
//                activity = (FragmentActivity) getContext();
//            }
//        }
//
//        if (activity != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm.isActive()) {
//                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//            } else {
//                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }


    public void closeSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public SearchBarView setSearchOnClick(View.OnClickListener onClickListener) {
        mSearchText.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 搜索焦点设置
     * @param isFocusable
     */
    public SearchBarView setEditTextFocusable(boolean isFocusable) {
        mSearchText.setFocusable(isFocusable);
        return this;
    }

    public SearchBarView setSysStatus(int vg) {
        mIvScan.setVisibility(vg);
        return this;
    }

    public SearchBarView setNoticeStatus(int vg) {
        mFlRight.setVisibility(vg);
        return this;
    }

    public SearchBarView setSearchBtnStatus(int vg) {
        btnSearch.setVisibility(vg);
        return this;
    }

    public SearchBarView setScanOnClick(View.OnClickListener onClickListener) {
        mIvScan.setOnClickListener(onClickListener);
        return this;
    }

    public String getSearchKey() {
        if (mSearchText == null) {
            return "";
        }
        return mSearchText.getText().toString();
    }

    public void setSearchKey(String searchStr) {
        if (mSearchText != null && !TextUtils.isEmpty(searchStr)) {
            mSearchText.setText(searchStr);
            mSearchText.setSelection(searchStr.length());
        }
    }

    public interface ISearchToListener {
        void onSearch(String key);
    }
}
