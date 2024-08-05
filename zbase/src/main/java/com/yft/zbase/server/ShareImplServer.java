package com.yft.zbase.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ShareImplServer implements IShare {

    @Override
    public void initServer(Context context) {
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return SHARE_SERVER;
    }

    @Override
    public void cleanInfo() {

    }

    @Override
    public synchronized boolean openShareText(Activity activity, String content) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            shareIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(shareIntent, "分享到"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean openShareText(Activity activity, String content, String html) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
            shareIntent.putExtra(html, content);
            shareIntent.setType("text/html");
            activity.startActivity(Intent.createChooser(shareIntent, "分享到"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized boolean openShareImage(Activity activity, File file) {
        try {
            Uri imageUri = saveImageToAlbum(activity, file);
            if (imageUri == null) {
                return false;
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("image/*");
            activity.startActivity(Intent.createChooser(shareIntent, "分享到"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public synchronized File openShareImage(Activity activity, View view) {
        try {
            // 创建一个与View相同大小的Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            // 将Bitmap关联到一个Canvas对象
            Canvas canvas = new Canvas(bitmap);
            // 绘制View的内容到Canvas上
            view.draw(canvas);
            // 保存Bitmap为图片文件
            long timeMillis = System.currentTimeMillis();

            File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), timeMillis + ".jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            boolean isOpen = this.openShareImage(activity, file);
            if(isOpen) {
                return file;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public synchronized Uri saveImageToAlbum(Activity activity, File file) {
        try {
            Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
            // 获取图片保存路径
            String imagePath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, file.getName(), "");
            // 获取图片的URI
            Uri imageUri = Uri.parse(imagePath);
            // 创建保存图片的Intent
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(imageUri);
            // 发送广播通知系统相册更新图片
            activity.sendBroadcast(intent);
            return imageUri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public synchronized Uri saveImageToAlbum(Activity activity, View view) {
        try {
            // 创建一个与View相同大小的Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            // 将Bitmap关联到一个Canvas对象
            Canvas canvas = new Canvas(bitmap);
            // 绘制View的内容到Canvas上
            view.draw(canvas);
            // 保存Bitmap为图片文件
            File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), SystemClock.currentThreadTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

           return saveImageToAlbum(activity, file);
        } catch (Exception e) {
            return null;
        }
    }
}
