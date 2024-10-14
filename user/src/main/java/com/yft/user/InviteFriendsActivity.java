package com.yft.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.chenenyu.router.annotation.Route;
import com.hkbyte.bsbase.router.BasisJumpRouter;
import com.yft.user.databinding.ActivityInviteFriendLayoutBinding;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.server.IShare;
import com.yft.zbase.ui.ButtonUtils;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;
import com.yft.zbase.utils.ZxingUtils;


@Route(UserRouter.ACTIVITY_INVITE_FRIEND)
public class InviteFriendsActivity extends BaseActivity<ActivityInviteFriendLayoutBinding, BaseViewModel> {
    private IShare mShare;

    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.llMain.setBackground(grad);
        mShare = DynamicMarketManage.getInstance().getServer(IServerAgent.SHARE_SERVER);
        mDataBing.tlt.setTitle("邀请好友");
        mDataBing.tlt.setLeftBackImage();
        mDataBing.tvDes.setText("快邀请您的好友加入" + getResources().getString(com.yft.zbase.R.string.app_name));

        mDataBing.setUser(mViewModel.getUserInfo());
        mDataBing.ivCode.post(new Runnable() {
            @Override
            public void run() {
                if (InviteFriendsActivity.this.isFinishing()) {
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.yft.zbase.R.mipmap.ic_launcher);
                String pmc = mViewModel.getUserInfo().getRecommendCode();
                String url = "/short/h5/#/pages/register/register?pmc="+pmc+"&"
                        + mViewModel.getDevice().getAppAlias() + "=1&actionType="
                        + BasisJumpRouter.JUMP_INVITE_FRIEND;

                //mViewModel.getUserServer().getShareURL() + url
                Bitmap bitmap1 = ZxingUtils.createImage(mViewModel.getUserServer().getShareURL() + url,  mDataBing.ivCode.getWidth(), mDataBing.ivCode.getWidth(), bitmap);
                mDataBing.ivCode.setImageBitmap(bitmap1);
            }
        });


        mDataBing.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestPermission(10001)) {
                    if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                        return;
                    }
                    mShare.openShareImage(InviteFriendsActivity.this, mDataBing.clMain);
                }
            }
        });

        mDataBing.llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestPermission(10002)) {
                    if(ButtonUtils.isFastDoubleClick(view.getId(), 2000)) {
                        return;
                    }
                    saveToAlbum();
                }
            }
        });


    }

    public void saveToAlbum() {
       Uri uri =  mShare.saveImageToAlbum(InviteFriendsActivity.this, mDataBing.clMain);
       if (uri != null) {
           ToastUtils.toast("保存成功");
       }
    }

    @Override
    public void initListener() {

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
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001 && permissions != null && permissions.length == 2) {
            // 直接分享
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                mShare.openShareImage(InviteFriendsActivity.this, mDataBing.clMain);
            }
        } else if (requestCode == 10002 && permissions != null && permissions.length == 2) {
            // 保存图片
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                saveToAlbum();
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_invite_friend_layout;
    }
}
