package com.yft.zbase.ui;

import static com.yft.zbase.utils.UIUtils.startToScale;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yft.zbase.R;
import com.yft.zbase.databinding.DialogPhotoSelectLayoutBinding;

public class DialogPhotoFragment extends DialogFragment {
    public static final int TYPE_ALBUM = 0;
    public static final int TYPE_CAMERA = 1;
    private OnPhotoSelectListener mOnPhotoSelectListener;
    private boolean isShow;
    public boolean isShow() {
        return isShow;
    }
    private DialogPhotoSelectLayoutBinding dialogBinding;
    private boolean isClick;

    public static DialogPhotoFragment newInstance() {
        DialogPhotoFragment fragment = new DialogPhotoFragment();
        return fragment;
    }

    public void setOnPhotoSelectListener(OnPhotoSelectListener onPhotoSelectListener) {
        this.mOnPhotoSelectListener = onPhotoSelectListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new NoLeakDialog(getContext(), R.style.MyLoadingDialog, Gravity.BOTTOM).setDialogFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialogBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_photo_select_layout ,container,false);
        dialogBinding.tvCancel.setOnClickListener((v)->{dismiss(true);});
        dialogBinding.tvEmallType.setOnClickListener((v) -> {
            if (mOnPhotoSelectListener != null) {
                mOnPhotoSelectListener.onPhotoSelect(TYPE_CAMERA);
            }
            isClick = true;
            dismiss(false);
        });
        dialogBinding.tvOrderType.setOnClickListener((v) -> {
            if (mOnPhotoSelectListener != null) {
                mOnPhotoSelectListener.onPhotoSelect(TYPE_ALBUM);
            }
            isClick = true;
            dismiss(false);
        });
        startToScale(dialogBinding.vLineA);
        return dialogBinding.getRoot();
    }

    @Override
    public void onPause() {
        if (mOnPhotoSelectListener != null) {
            mOnPhotoSelectListener.onDismiss(isClick ? false : true);
        }
        super.onPause();
        isShow = false;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        isClick = false;
        isShow = true;
        super.show(manager, tag);
    }

    public void dismiss(boolean isDis) {
        if (mOnPhotoSelectListener != null) {
            mOnPhotoSelectListener.onDismiss(isDis);
        }
        isShow = false;
        super.dismiss();
    }

    @Override
    public void dismiss() {
        if (mOnPhotoSelectListener != null) {
            mOnPhotoSelectListener.onDismiss(isClick ? false : true);
        }
        isShow = false;
        super.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (mOnPhotoSelectListener != null) {
            mOnPhotoSelectListener.onDismiss(isClick ? false : true);
        }
        super.onDismiss(dialog);
        isShow = false;

    }

    @Override
    public void onDestroy() {
        if (mOnPhotoSelectListener != null) {
            mOnPhotoSelectListener.onDismiss(isClick ? false : true);
        }
        super.onDestroy();
        isShow = false;
    }

    public interface OnPhotoSelectListener {
        void onPhotoSelect(int type);

        void onDismiss(boolean i);
    }
}
