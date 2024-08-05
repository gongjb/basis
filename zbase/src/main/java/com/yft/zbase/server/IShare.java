package com.yft.zbase.server;

import android.app.Activity;
import android.net.Uri;
import android.text.Spanned;
import android.view.View;

import java.io.File;

public interface IShare extends IServerAgent {
    /**
     * 打开分享
     * @param content
     */
    boolean openShareText(Activity activity, String content);


    /**
     * 打开分享
     * @param content
     */
    boolean openShareText(Activity activity, String content, String html);

    /**
     * 分享图片
     * @param file
     * @return
     */
    boolean openShareImage(Activity activity, File file);


    /**
     * 分享图片
     * @param view
     * @return File 成功返回文件对象
     */
    File openShareImage(Activity activity, View view);

    /**
     * 保存文件图片到相册
     * @param activity
     * @param file
     * @return
     */
    Uri saveImageToAlbum(Activity activity, File file);

    /**
     * 保存view图
     * @param activity
     * @param view
     * @return
     */
    Uri saveImageToAlbum(Activity activity, View view);
}
