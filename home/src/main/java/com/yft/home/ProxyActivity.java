package com.yft.home;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.yft.home.bean.AppInfo;

public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfo appInfo = getIntent().getParcelableExtra("appInfo");

    }
}
