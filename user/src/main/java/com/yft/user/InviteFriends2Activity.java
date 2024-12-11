package com.yft.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.chenenyu.router.annotation.Route;
import com.hkbyte.bsbase.router.BasisJumpRouter;
import com.yft.user.databinding.ActivityInviteFriendTwoLayoutBinding;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IShare;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.ui.ButtonUtils;
import com.yft.zbase.ui.FragmentMessageDialog;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;
import com.yft.zbase.utils.ZxingUtils;

@Route(UserRouter.ACTIVITY_INVITE_FRIEND_2)
public class InviteFriends2Activity extends BaseActivity<ActivityInviteFriendTwoLayoutBinding, BaseViewModel> {
    private IShare mShare;
    // 图片的宽高比是
    private float whr = 0.5222f;
    private FragmentMessageDialog mFragmentMessageDialog;
    @Override
    public void initView() {
        mFragmentMessageDialog = FragmentMessageDialog.newInstance(permissionDescription(), permissionTitle(), mViewModel.getString("确定"));
        mShare = DynamicMarketManage.getInstance().getServer(IServerAgent.SHARE_SERVER);
        mDataBing.tlt.setTitle(mViewModel.getString("邀请好友"));
        mDataBing.tlt.setLeftBackImage();
        mDataBing.setUser(mViewModel.getUserInfo());
        configUi(getResources().getColor(com.yft.zbase.R.color.themeMainColor));
        mDataBing.ivCode.post(new Runnable() {
            @Override
            public void run() {
                if (InviteFriends2Activity.this.isFinishing()) {
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_a);
                String pmc = mViewModel.getUserInfo().getRecommendCode();
                String url = "/short/h5/#/pages/register/register?pmc="+pmc+"&"
                        + mViewModel.getDevice().getAppAlias() + "=1&actionType="
                        + BasisJumpRouter.JUMP_INVITE_FRIEND;

                Bitmap bitmap1 = ZxingUtils.createImage(mViewModel.getUserServer().getShareURL() + url,  mDataBing.ivCode.getWidth(), mDataBing.ivCode.getWidth(), bitmap);
                mDataBing.ivCode.setImageBitmap(bitmap1);
            }
        });


        mDataBing.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewModel.getUserServer().isStoragePermissions()) {
                    if (requestPermission(10001)) {
                        if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                            return;
                        }
                        mShare.openShareImage(InviteFriends2Activity.this, mDataBing.clMain);
                    }
                } else {
                    mFragmentMessageDialog.setOnButtonClickListener(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            if (requestPermission(10001)) {
                                if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                                    return;
                                }
                                mShare.openShareImage(InviteFriends2Activity.this, mDataBing.clMain);
                            }
                        }
                    });
                    mFragmentMessageDialog.show(getSupportFragmentManager(), "fragmentMessageDialog-invite");
                }
            }
        });

        mDataBing.llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewModel.getUserServer().isStoragePermissions()) {
                    if (requestPermission(10002)) {
                        if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                            return;
                        }
                        saveToAlbum();
                    }
                } else {
                    mFragmentMessageDialog.setOnButtonClickListener(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            if (requestPermission(10002)) {
                                if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                                    return;
                                }
                                saveToAlbum();
                            }
                        }
                    });
                    mFragmentMessageDialog.show(getSupportFragmentManager(), "fragmentMessageDialog-invite");
                }
            }
        });
    }

    public String permissionDescription() {
        return LanguageManage.getString("将图片存储到系统相册方便您邀请好友加入ReelShort，将授权存储权限，否则无法使用该功能。");
    }

    public String permissionTitle() {
        return LanguageManage.getString("存储权限说明");
    }

    public void saveToAlbum() {
        Uri uri =  mShare.saveImageToAlbum(InviteFriends2Activity.this, mDataBing.clMain);
        if (uri != null) {
            ToastUtils.toast(mViewModel.getString("保存成功"));
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configUi(getResources().getColor(com.yft.zbase.R.color.themeMainColor));
    }

    private void configUi(int color) {
        int[] colors = new int[]{
                color,
                getResources().getColor(com.yft.zbase.R.color.black_00),
                getResources().getColor(com.yft.zbase.R.color.black_00)
        };

        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        mDataBing.vMc.setBackground(grad);

        ViewGroup.LayoutParams layoutParams = mDataBing.ivShare.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = (int) ((int) Utils.getScreenWidth(this) / whr);
            mDataBing.ivShare.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void initData() {

    }

    protected boolean requestPermission(final int requestCode) {
        // 用于没有授予权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限 11 以下授权
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            return false;
        }
        //当前已经具备存储权限
        mViewModel.getUserServer().setStoragePermissions(true);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001 && permissions != null && permissions.length == 2) {
            // 直接分享
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                mViewModel.getUserServer().setStoragePermissions(true);
                mShare.openShareImage(InviteFriends2Activity.this, mDataBing.clMain);
            }
        } else if (requestCode == 10002 && permissions != null && permissions.length == 2) {
            mViewModel.getUserServer().setStoragePermissions(true);
            // 保存图片
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                saveToAlbum();
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_invite_friend_two_layout;
    }
}