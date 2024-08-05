package com.yft.zbase.updateapk;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yft.zbase.R;
import com.yft.zbase.server.LanguageManage;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * 展示更新内容的dialog
 */

public class DialogUpData extends AlertDialog implements View.OnClickListener {
    private ListView lv;//新版本功能的更新内容列表
    // 取消更新、马上更新、下载进度、下载中的textview
    private TextView tvCancle, tvUpData, tvProgress, tvLoading,tvMsg;
    // 下载箭头
    private ImageView imgDown;
    private View view;
    // 下载进度的布局
    private RelativeLayout rlProgress;
    // 取消更新和马上更新所在的布局，开始下载后要隐藏
    private LinearLayout llButton;
    // 更新内容适配器
    private AdapterUpData adapterUpData;
    // 点击取消更新和马上更新的回调事件
    private UpdataDialogCallback callBack;
    // 是否强制更新
    private boolean isMustUpdate;
    // 更新内容的数据
    private ArrayList<String> list = new ArrayList<>();

    /**
     * @param context
     * @param list     更新内容列表
     * @param callBack 点击取消更新和马上更新的回调事件
     */
    public DialogUpData(Context context, ArrayList<String> list, boolean isMustUpdate, UpdataDialogCallback callBack) {
        super(context);
        this.list.clear();
        if (list != null)
            this.list.addAll(list);
        this.callBack = callBack;
        this.isMustUpdate = isMustUpdate;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去除背景
        Window win = getWindow();
        win.setBackgroundDrawable(new BitmapDrawable());
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_updata);
        initView();
        addListener();
    }


    /**
     *  设置消息
     * @param message
     */
    public void settvMsg(String message){
        if(tvMsg!=null){
            tvMsg.setText(message);
        }
    }

    public void initView() {
        lv = findViewById(R.id.lv);
        tvCancle = findViewById(R.id.tvCancle);
        view = findViewById(R.id.v_line);
        tvCancle.setVisibility(isMustUpdate ? View.GONE : View.VISIBLE);
        view.setVisibility(isMustUpdate ? View.GONE : View.VISIBLE);
        tvUpData = findViewById(R.id.tvUpData);
        tvProgress = findViewById(R.id.tvProgress);
        rlProgress = findViewById(R.id.rlProgress);
        imgDown = findViewById(R.id.imgDown);
        llButton = findViewById(R.id.llButton);
        tvLoading = findViewById(R.id.tvLoading);
        tvMsg = findViewById(R.id.tvMsg);
        if (list == null || list.size() == 0) {
            list.add(LanguageManage.getString("检测到最新版本Apk，是否更新？"));
        }
        tvLoading.setText(LanguageManage.getString("下载中..."));
        tvMsg.setText(LanguageManage.getString("版本更新"));
        tvCancle.setText(LanguageManage.getString("稍后更新"));
        tvUpData.setText(LanguageManage.getString("马上更新"));
        adapterUpData = new AdapterUpData(getContext(), list);
        lv.setAdapter(adapterUpData);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMustUpdate) {
                return false;
            }
            callBack.cancle(this);
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void addListener() {
        tvCancle.setOnClickListener(this);
        tvUpData.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus)
            return;
        //  设置dialog最大宽度为屏幕宽度的0.8倍
        //  设置dialog最大高度为屏幕高度的0.4倍
        Window window = getWindow();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.5)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.4);
        }
        attributes.width = (int) (displayMetrics.widthPixels * 0.8);
        window.setAttributes(attributes);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvCancle) {
            // 取消更新
            dismiss();
            if (callBack != null)
                callBack.cancle(this);
        } else if (i == R.id.tvUpData) {
            // 开始更新
            imgDown.setVisibility(View.GONE);
            rlProgress.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.GONE);
            tvLoading.setVisibility(View.VISIBLE);
            if (callBack != null)
                callBack.updata(this);

        }
    }

    /**
     * 设置进度
     * 开始下载apk的时候，需要不间断调用这个方法，更新dialog内容，显示当前下载的进度
     *
     * @param max      文件大小
     * @param progress 当前下载大小
     */
    public void setProgress(float max, float progress) {
        // 根据文件大小和当前下载大小，计算当前下载的百分比
        String pro = new DecimalFormat("#.00").format(progress / max);
        pro = pro.replace(".", "");
        if (pro.charAt(0) == '0') {
            pro = pro.substring(1);
        }
        setProgress(pro);
    }

    /**
     * 设置百分比进度
     *
     * @param percent
     */
    public void setProgress(String percent) {
        tvProgress.setText(percent + "%");
    }

}
