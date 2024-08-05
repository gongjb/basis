package com.yft.zbase.updateapk;

import android.app.Dialog;

/**
 * 版本升级dialog点击
 * 取消更新和马上更新的按钮的回调事件
 */

public interface UpdataDialogCallback {
    /**
     * 点击了马上更新
     * @param dialog 把dialog本身传递过去，用于销毁本身
     */
    void updata(Dialog dialog);

    /**
     * 点击了取消更新
     * @param dialog
     */
    void cancle(Dialog dialog);
}
