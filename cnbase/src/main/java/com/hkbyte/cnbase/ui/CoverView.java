package com.hkbyte.cnbase.ui;

import static com.yft.zbase.utils.Logger.LOGE;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


/**
 * 是否启动遮罩层，
 */
public class CoverView extends View implements LifecycleEventObserver {

    public CoverView(Context context) {
        super(context);
        initView();
    }

    public CoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        // 注册生命周期
        this.post(() -> getActivity().getLifecycle().addObserver(CoverView.this));

    }

    public FragmentActivity getActivity() {
        Context context = getContext();
        if(context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        }

        View contentView = getRootView().findViewById(android.R.id.content);
        if (contentView == null) {
            return null;
        }
        context = contentView.getContext();
        if(context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        }
        return null;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                LOGE("======>> ON_CREATE");
                //onCreate();
                break;
            case ON_START:
                LOGE("======>> ON_START");
               // setVisibility(View.GONE);
                //onStart();
                break;
            case ON_RESUME:
                LOGE("======>> ON_RESUME");
                //onResume();
                break;
            case ON_PAUSE:
                //setVisibility(View.VISIBLE);
                LOGE("======>> ON_PAUSE");
                //onPause();
                break;
            case ON_STOP:
                LOGE("======>> ON_STOP");
                //setVisibility(View.VISIBLE);
                //onStop();
                break;
            case ON_DESTROY:
                LOGE("======>> ON_DESTROY");
                //onDestroy();
                break;
            case ON_ANY:
                break;
            default:{
                LOGE("======>> " + event.name());
                // do noting
            }
        }
    }
}
