package com.yft.zbase.ui;

import android.text.TextUtils;

import com.chenenyu.router.annotation.Route;
import com.yft.zbase.R;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.databinding.ActivityTextLayoutBinding;
import com.yft.zbase.router.RouterFactory;

@Route("com.yft.zbase.ui.TextActivity")
public class TextActivity extends BaseActivity<ActivityTextLayoutBinding, BaseViewModel> {

    @Override
    public void initView() {
        String text = getIntent().getStringExtra("text");
        if (!TextUtils.isEmpty(text)) {
            mDataBing.tvContent.setText(text);
        }
        mDataBing.tlt.setTitle("扫描结果");
        mDataBing.tlt.setLeftBackImage();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_text_layout;
    }
}
