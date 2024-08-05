package com.yft.user;


import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenenyu.router.annotation.Route;
import com.gongjiebin.latticeview.KVBean;
import com.yft.user.adapter.EasyAdapter;
import com.yft.user.bean.MeItemBean;
import com.yft.user.databinding.ActivityUserInformationLayoutBinding;
import com.yft.user.databinding.ItemUserinfoLayoutBinding;
import com.yft.user.model.UserViewModel;
import com.yft.zbase.base.BasePhotoActivity;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.ui.DialogPhotoFragment;
import com.yft.zbase.ui.FragmentMessageDialog;
import com.yft.zbase.ui.SubFragmentDialog;
import com.yft.zbase.utils.ToastUtils;


import java.util.LinkedList;

@Route(RouterFactory.ACTIVITY_USER_INFORMATION)
public class ActivityUserInformation extends BasePhotoActivity<ActivityUserInformationLayoutBinding, UserViewModel> {
    protected LinkedList<MeItemBean> mList;
    private EasyAdapter<MeItemBean, ItemUserinfoLayoutBinding> mEasyAdapter;
    private DialogPhotoFragment mDialogPhotoFragment;
    private SubFragmentDialog mSubDialog;
    private FragmentMessageDialog mFragmentMessageDialog;

    // 充值明细
    @Override
    public void initView() {
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.TOP_BOTTOM,
                mColors);
        mDataBing.llMain.setBackground(grad);
        mDialogPhotoFragment = DialogPhotoFragment.newInstance();
        mFragmentMessageDialog = FragmentMessageDialog.newInstance(mViewModel.getString("您的手机号为") + ":" + mViewModel.getUserServer().getUserInfo().getPhone(), mViewModel.getString("提示"), mViewModel.getString("确定"));
        mSubDialog = SubFragmentDialog.newInstance();
        mDataBing.tlt.setTitle(mViewModel.getString("个人信息"));
        mDataBing.tlt.setLeftBackImage();
        mEasyAdapter = new EasyAdapter<MeItemBean, ItemUserinfoLayoutBinding>(R.layout.item_userinfo_layout, this::onAdapterClick);
        mEasyAdapter.setToXmlPosition(true);
        mList = new LinkedList<>();

    }

    protected void initItemData() {
        mList.add(new MeItemBean(1,mViewModel.getString("头像"), "" , true, mViewModel.getUserInfo().getLogo() , false, 0));
        mList.add(new MeItemBean(2,mViewModel.getString("昵称"), mViewModel.getUserInfo().getNickname(), false, "", false, 0));
        if (!TextUtils.isEmpty(mViewModel.getUserInfo().getPhone())) {
            mList.add(new MeItemBean(4, mViewModel.getString("手机号码"), mViewModel.getUserServer().getPhoneFormat(), false, "", false, 0));
        }
        if (!TextUtils.isEmpty(mViewModel.getUserInfo().getEmail())) {
            mList.add(new MeItemBean(3, mViewModel.getString("电子邮件"),  mViewModel.getUserInfo().getEmail() , false, "", false, 0));
        }

        mList.add(new MeItemBean(5,mViewModel.getString("等级"), mViewModel.getString("等级") + mViewModel.getUserInfo().getLevel(), false, "", false, 0));
    }



    public void onAdapterClick(View view, MeItemBean meItemBean, int position) {
        if (!meItemBean.isLine()) {
            if (meItemBean.getId() == 1) {
                if (!mDialogPhotoFragment.isShow()) {
                    mDialogPhotoFragment.show(getSupportFragmentManager(), getClass().getCanonicalName());
                }
            } else if (meItemBean.getId() == 2) {
                RouterFactory.startRouterActivity(ActivityUserInformation.this, "com.yft.user.NicknameActivity");
            } else if (meItemBean.getId() == 4) {
                //RouterFactory.startRouterActivity(ActivityUserInformation.this, RouterFactory.ACTIVITY_BIND_PHONE);
                if (mFragmentMessageDialog != null && !mFragmentMessageDialog.isShow()) {
                    mFragmentMessageDialog.show(getSupportFragmentManager(), "mFragmentMessageDialog");
                }
            }
        }
    }

    @Override
    public void initListener() {

        mDialogPhotoFragment.setOnPhotoSelectListener(new DialogPhotoFragment.OnPhotoSelectListener() {
            @Override
            public void onPhotoSelect(int type) {
                if (type == DialogPhotoFragment.TYPE_CAMERA) {
                    thisRequest = CAMERA_REQUEST_CODE;
                    requestPermissionV2(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            openCamera();
                        }
                    });

                } else {
                    thisRequest = ALBUM_REQUEST_CODE;
                    requestPermissionV2(new FragmentMessageDialog.OnButtonClickListener() {
                        @Override
                        public void onButton(View view) {
                            openAlbum();
                        }
                    });
                }
            }

            @Override
            public void onDismiss(boolean isDes) {
                // noting
            }
        });
        mViewModel.getMutableSuccessLiveData().observe(this, this::onSuccess);
        mViewModel.getErrorMutableLiveData().observe(this, this::onError);
    }

    public void onSuccess(KVBean tag) {
        if (tag != null) {
            // 上传文件成功
            if (tag.getKey() == 0) {
                mEasyAdapter.getData().get(0).setImageUrl(tag.getValue());
                mEasyAdapter.notifyItemChanged(0);
                // 更新头像
                mViewModel.requestUpdateUser(tag.getValue(), "");
            } else if(tag.getKey() == 1){
                // 更新头像成功
                ToastUtils.toast(mViewModel.getString("头像更新成功"));
                closeDialogFragment();
            } else {
                closeDialogFragment();
            }
            return;
        }
        closeDialogFragment();
    }

    public void onError(String tag) {
        closeDialogFragment();
    }

    public void closeDialogFragment() {
        if (mSubDialog != null &&  mSubDialog.isShow()) {
            mSubDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        initItemData();
        mEasyAdapter.setNewData(mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDataBing.rvUser.setLayoutManager(layoutManager);
        mDataBing.rvUser.setAdapter(mEasyAdapter);
        mEasyAdapter.notifyDataSetChanged();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_user_information_layout;
    }

    @Override
    public void handlerPhoto(int type, String url) {
//        mEasyAdapter.getData().get(0).setImageUrl(url);
//        mEasyAdapter.notifyItemChanged(0);
        // 上传图片
        if (mSubDialog != null && !mSubDialog.isShow()) {
            mSubDialog.setMessage(mViewModel.getString("上传中···"));
            mSubDialog.show(getSupportFragmentManager(), "");
            mViewModel.updatePhotoFile(url);
        }
    }

    @Override
    public void selectPhotoError() {

    }

    @Override
    public String permissionDescription() {
        return LanguageManage.getString("使用该功能拍摄或存储、读取相册的照片，以便于设置用户头像等信息，请您确认授权，否则无法使用该功能。");
    }

    @Override
    public String permissionTitle() {
        return LanguageManage.getString("拍摄、存储权限说明");
    }
}
