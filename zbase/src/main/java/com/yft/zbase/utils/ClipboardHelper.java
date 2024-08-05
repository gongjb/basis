package com.yft.zbase.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardHelper {
    private ClipboardManager clipboardManager;

    public ClipboardHelper(Context context) {
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static ClipboardHelper getInstance(Context context){
        return new ClipboardHelper(context);
    }

    /**
     * 将文本复制到剪贴板
     *
     * @param text 要复制的文本
     */
    public void copyText(String text) {
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);
    }

    /**
     * 从剪贴板获取文本
     *
     * @return 剪贴板中的文本
     */
    public String getCopiedText() {
        if (clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).getText();
                return text.toString();
            }
        }
        return null;
    }
}
