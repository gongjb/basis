package com.yft.zbase.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yft.zbase.R;
import com.yft.zbase.databinding.FragmentSubDialogBinding;


/**
 * 加载狂
 */
public class SubFragmentDialog extends DialogFragment {

    private volatile boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    private FragmentSubDialogBinding dialogBinding;
    private String message;

    public static SubFragmentDialog newInstance(String msg) {
        SubFragmentDialog fragment = new SubFragmentDialog();
        Bundle args = new Bundle();
        args.putString("msg",msg);
        fragment.setArguments(args);
        return fragment;
    }

    public static SubFragmentDialog newInstance() {
        return newInstance("");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new NoLeakBackDialog(getContext(), R.style.MyLoadingDialog, Gravity.CENTER).setDialogFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_dialog,container,false);
        message = getArguments().getString("msg");
        if(!TextUtils.isEmpty(message)){
            setMessage(message);
        }
        getDialog().setCanceledOnTouchOutside(false);

//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dialogBinding.rlPro, "alpha", 0, 1);
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dialogBinding.rlPro, "translationX", -300, 0);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(objectAnimator,objectAnimator1);
//        animatorSet.setDuration(600);
//        animatorSet.start();

        return dialogBinding.getRoot();
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
            isShow = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            isShow = true;
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            isShow = false;
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        try {
            isShow = false;
            super.onDismiss(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            isShow = false;
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        if(dialogBinding!=null){
            dialogBinding.tvText.setText(message);
        }
    }
}
