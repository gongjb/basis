package com.hkbyte.filelibrary.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.hkbyte.file.R;
import com.hkbyte.file.databinding.FragmentProgressDialogBinding;
import com.yft.zbase.base.BaseFragmentDialog;
import com.yft.zbase.base.BaseViewModel;

public class FragmentProgressDialog extends BaseFragmentDialog<FragmentProgressDialogBinding, BaseViewModel> {

    private String mMessage;

    public static FragmentProgressDialog newInstance(String msg) {
        FragmentProgressDialog fragment = new FragmentProgressDialog();
        Bundle args = new Bundle();
        args.putString("msg",msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        mMessage = getArguments().getString("msg");
        getDialog().setCanceledOnTouchOutside(false);
        if(!TextUtils.isEmpty(mMessage)){
            setMessage(mMessage);
        }
    }

    public void setMessage(String message) {
        if(mDataBing!=null){
            mDataBing.tvText.setText(message);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onPause() {
        super.onPause();
        setShow(true);
    }



    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_progress_dialog;
    }
}
