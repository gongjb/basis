package com.yft.user;


import static com.yft.zbase.utils.Logger.LOGE;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenenyu.router.annotation.Route;
import com.yft.user.adapter.EasyAdapter;
import com.yft.user.bean.MeItemBean;
import com.yft.user.databinding.ActivitySetLayoutBinding;
import com.yft.user.databinding.ItemUserinfoLayoutBinding;
import com.yft.user.model.UserViewModel;
import com.yft.user.router.UserRouter;
import com.yft.user.widget.FragmentLogOffDialog;
import com.yft.zbase.BuildConfig;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.bean.DownLoadBean;
import com.yft.zbase.error.ErrorCode;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.ui.FragmentMessageDialog;
import com.yft.zbase.ui.OnZoomClickListener;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.updateapk.VersionUpdateUtil;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Route(UserRouter.ACTIVITY_SET)
public class SetActivity extends BaseActivity<ActivitySetLayoutBinding, UserViewModel> {
    private EasyAdapter<MeItemBean, ItemUserinfoLayoutBinding> mEasyAdapter;
    private List<MeItemBean> mList;
    private FragmentMessageDialog mFragmentMessageDialog;
    private FragmentLogOffDialog mFragmentLogOffDialog;
    private VersionUpdateUtil versionUpdateUtil;
    private boolean isIng;
    private boolean isReal;
    private FragmentMessageDialog mFragmentRealDialog;
    private SubFragmentDialog mSubFragmentDialog;

    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.rlMain.setBackground(grad);
        mSubFragmentDialog = SubFragmentDialog.newInstance();
        mFragmentLogOffDialog = FragmentLogOffDialog.newInstance();
        versionUpdateUtil = new VersionUpdateUtil();
        isReal = mViewModel.getUserInfo().isReal();
        mFragmentMessageDialog = FragmentMessageDialog.newInstance(mViewModel.getString("确定要退出当前账号吗？"),
                mViewModel.getString("提示"), mViewModel.getString("确定"));
        mFragmentRealDialog = FragmentMessageDialog.newInstance("您已经实名认证过了，无需再次认证！", "提示", "确定");
        mDataBing.tlt.setTitle(mViewModel.getString("设置"));
        mDataBing.tlt.setLeftBackImage();
        mEasyAdapter = new EasyAdapter<MeItemBean, ItemUserinfoLayoutBinding>(R.layout.item_userinfo_layout, this::onAdapterClick);
        mEasyAdapter.setToXmlPosition(true);
        mList = new ArrayList<>();
        initItemData();
        mEasyAdapter.setNewData(mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDataBing.rvSet.setLayoutManager(layoutManager);
        mDataBing.rvSet.setAdapter(mEasyAdapter);
        String ver = mViewModel.getString("版本");
        mDataBing.tvV.setText(ver + ": v" + BuildConfig.versionName);
    }

    protected void initItemData() {
        mList.add(new MeItemBean(1, mViewModel.getString("关于我们"), "", false, "", false, com.yft.zbase.R.mipmap.ic_launcher));
    }


    public void onAdapterClick(View view, MeItemBean meItemBean, int position) {
        if (meItemBean.getId() == 1) {
            // 跳转实名认证
        } else if (meItemBean.getId() == 2) {
            if (isIng) {
                return;
            }
            isIng = true;
            final SubFragmentDialog subFragmentDialog = SubFragmentDialog.newInstance(mViewModel.getString("请稍后..."));
            subFragmentDialog.show(getSupportFragmentManager(), "clean");
            Utils.cleanCookie(this);
            mDataBing.rvSet.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isIng = false;
                    if (subFragmentDialog.isShow()) {
                        subFragmentDialog.dismiss();
                    }
                    ToastUtils.toast(mViewModel.getString("清理完成"));
                }
            }, 3000);
        } else if (meItemBean.getId() == 3) {
            // 切换语言
        } else if (meItemBean.getId() == 4) {

        } else if (meItemBean.getId() == 5) {

        } else if (meItemBean.getId() == 6) {
            mViewModel.requestDownload();
        }
    }

    private OnZoomClickListener onZoomClickListener = new OnZoomClickListener() {
        @Override
        public void onClick(View view) {
            if (mFragmentMessageDialog != null && !mFragmentMessageDialog.isShow()) {
                mFragmentMessageDialog.show(getSupportFragmentManager(), getClass().getCanonicalName());
            }
        }
    };

    @Override
    public void initListener() {
        mDataBing.tvSubmit.setOnClickListener(onZoomClickListener);
        mDataBing.tvSubmit.setOnTouchListener(onZoomClickListener);
//        mDataBing.tvV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mFragmentLogOffDialog != null && !mFragmentLogOffDialog.isShow()) {
//                    // 注销
//                    mFragmentLogOffDialog.show(getSupportFragmentManager(), "logoff");
//                    mFragmentLogOffDialog.setOnButtonClickListener(new FragmentLogOffDialog.OnButtonClickListener() {
//                        @Override
//                        public void onButton(View view, String describe) {
//                            if (mSubFragmentDialog != null && !mSubFragmentDialog.isShow()) {
//                                mSubFragmentDialog.show(getSupportFragmentManager(), "exit");
//                            }
//                            mViewModel.userLogOff(describe);
//                        }
//                    });
//                }
//            }
//        });

        mViewModel.getDownLoadBeanMutableLiveData().observe(this, this::onDownload);
        mViewModel.getErrorMutableLiveData().observe(this, this::onError);
        mViewModel.getExitMutableLiveData().observe(this, this::onExitLogin);
        mFragmentMessageDialog.setOnButtonClickListener(new FragmentMessageDialog.OnButtonClickListener() {
            @Override
            public void onButton(View view) {
                if (mSubFragmentDialog != null && !mSubFragmentDialog.isShow()) {
                    mSubFragmentDialog.show(getSupportFragmentManager(), "exit");
                }
                mViewModel.exitLogin();
            }
        });

    }

    public void onError(String error) {
        if (mSubFragmentDialog != null && mSubFragmentDialog.isShow()) {
            mSubFragmentDialog.dismiss();
        }
    }

    public void onExitLogin(String data) {
        if (mSubFragmentDialog != null && mSubFragmentDialog.isShow()) {
            mSubFragmentDialog.dismiss();
        }
        ErrorCode.toWelcomeActivity();
        if ("0".equals(data)) {
            ToastUtils.toast(mViewModel.getString("您已经成功退出当前账号!"));
        } else {
            ToastUtils.toast(mViewModel.getString("您当前的账号已被注销!"));
        }
    }

    public void onDownload(DownLoadBean downLoadBean) {
        try {
            // 本地版本号
            String v = Utils.getNumber(BuildConfig.versionName);
            long thatVer = Long.parseLong(v);
            long serVer = Long.parseLong(Utils.getNumber(downLoadBean.getVersion()));
            if (thatVer < serVer) {
                // 发现新版本
                ArrayList<String> arrayList = new ArrayList<>();
                // 更新描述
                arrayList.add(downLoadBean.getDescription());
                LOGE("=渠道=>" + mViewModel.getDevice().getFlavor());
                // 弹出更新软件的弹出框
                versionUpdateUtil.showDownloadApkDialog(arrayList, downLoadBean.getUpdateUrl(), downLoadBean.isMustUpdate(), this.getPackageName(), this,
                        mViewModel.getDevice().getFlavor());
            } else {
                ToastUtils.toast(mViewModel.getString("当前已是最新版本!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_set_layout;
    }
}
