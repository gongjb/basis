package com.hkbyte.filelibrary.ui;

import android.text.TextUtils;
import android.view.View;

import com.hkbyte.file.R;
import com.hkbyte.file.databinding.DialogDirFileLayoutBinding;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.utils.UIUtils;

/**
 * 创建文件目录
 */
public class CreateDirFragmentDialog extends BaseFragmentDialog<DialogDirFileLayoutBinding, BaseViewModel> {
    private final static String TAG = "CreateDirFragment";
    private INewDirsListener mNewDirsListener;

    public static CreateDirFragmentDialog newInstance() {
        CreateDirFragmentDialog fragment = new CreateDirFragmentDialog();
        return fragment;
    }

    public void setNewDirsListener(INewDirsListener mNewDirsListener) {
        this.mNewDirsListener = mNewDirsListener;
    }

    @Override
    public void initView() {
        UIUtils.startToScale(mDataBing.vLineA);
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
        if (mNewDirsListener != null) {
            mNewDirsListener.onCreateDirs(name);
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

    public interface INewDirsListener {
        void onCreateDirs(String name);
    }
}
