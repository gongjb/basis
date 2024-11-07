package com.hkbyte.filelibrary.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hkbyte.file.R;
import com.hkbyte.file.databinding.DialogDirFileLayoutBinding;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.UIUtils;

public class RenameFragmentDialog  extends BaseFragmentDialog<DialogDirFileLayoutBinding, BaseViewModel> {
    private final static String TAG = "CreateDirFragment";
    private RenameFragmentDialog.IRenameListener mRenameListener;
    private String mOdlName; // 旧文件名
    private int mPosition; // 修改文件下标

    public static RenameFragmentDialog newInstance(String name, int position) {
        RenameFragmentDialog fragment = new RenameFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public RenameFragmentDialog setRenameListener(IRenameListener mRenameListener) {
        this.mRenameListener = mRenameListener;
        return this;
    }

    @Override
    public void initView() {
        UIUtils.startToScale(mDataBing.vLineA);
        mDataBing.tvOrderType.setText(getString(R.string.file_rename));
        mOdlName = getArguments().getString("name");
        mPosition = getArguments().getInt("position");
        if (TextUtils.isEmpty(mOdlName)) {
            ToastUtils.toast(getString(R.string.please_select_file_rename));
            dismiss();
        }
        mDataBing.etDirName.setHint("请填写文件名称");
        mDataBing.etDirName.setText(mOdlName);
    }

    @Override
    public void initListener() {
        mDataBing.tvYes.setOnClickListener(this::onCreateDir);
        mDataBing.tvCancel.setOnClickListener(this::onCancel);
    }

    public void onCreateDir(View view) {
        String name = mDataBing.etDirName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UIUtils.showToast(getString(R.string.new_dirs_not_null_tips));
        }

        this.dismiss();
        if (mRenameListener != null) {
            mRenameListener.onRename(name, mOdlName, mPosition);
        }
    }

    public void onCancel(View view) {
        this.dismiss();
    }


    @Override
    protected float createWidth() {
        return 0.7f;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.dialog_dir_file_layout;
    }

    public interface IRenameListener {
        void onRename(String newName, String oblName, int position);
    }
}
