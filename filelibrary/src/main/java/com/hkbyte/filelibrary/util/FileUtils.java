package com.hkbyte.filelibrary.util;

import static com.yft.zbase.utils.Logger.LOGE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.hkbyte.file.R;
import com.hkbyte.filelibrary.bean.FileBean;
import com.hkbyte.filelibrary.viewmodel.FileViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    // 文件头标识及其对应的扩展名
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    static {
        // 图片文件类型
        FILE_TYPE_MAP.put("FFD8FF", ".jpg");   // JPEG
        FILE_TYPE_MAP.put("89504E", ".png");   // PNG
        FILE_TYPE_MAP.put("474946", ".gif");   // GIF
        FILE_TYPE_MAP.put("000003", ".bmp");   // BMP
        FILE_TYPE_MAP.put("49492A", ".tif");   // TIFF
        FILE_TYPE_MAP.put("424D", ".bmp");     // BMP
        FILE_TYPE_MAP.put("526172", ".rar");    // RAR
        FILE_TYPE_MAP.put("504B03", ".webp");  // WEBP

        // 音频文件类型
        FILE_TYPE_MAP.put("494433", ".mp3");   // MP3
        FILE_TYPE_MAP.put("FFFB", ".mp3");      // MP3 (另一种文件头)
        FILE_TYPE_MAP.put("4D54", ".mid");      // MIDI
        FILE_TYPE_MAP.put("WAVE", ".wav");      // WAV
        FILE_TYPE_MAP.put("664C", ".flac");     // FLAC
        FILE_TYPE_MAP.put("4C61", ".aac");      // AAC

        // 视频文件类型
        FILE_TYPE_MAP.put("000000", ".mp4");    // MP4
        FILE_TYPE_MAP.put("667479", ".mov");     // MOV
        FILE_TYPE_MAP.put("1A45DFA3", ".mkv");   // MKV
        FILE_TYPE_MAP.put("3026B275", ".wmv");   // WMV
        FILE_TYPE_MAP.put("524946", ".avi");     // AVI
        FILE_TYPE_MAP.put("7B5E6E", ".mpg");      // MPG
    }

    // 方法：获取MIME类型
    public static String getMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
    }

    /**
     * 尝试预览文件。
     *
     * @param context 上下文。
     * @param filePath 文件路径。
     * @return 如果成功预览，则返回true；否则返回false。
     */
    public static void previewFile(Context context, String filePath) {
        String mimeType = getMimeType(filePath);
        File file = new File(filePath);
        String provider = context.getApplicationContext().getPackageName() + ".provider";
        Uri uri = FileProvider.getUriForFile(context, provider, file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        if (resolveInfoList.isEmpty()) {
            return;
        }
        context.startActivity(intent);
    }

    public FileUtils() {
    }

    public static void setImageId(int mType, File file, FileBean fileBean) {
        switch (mType) {
            case FileViewModel.FILE_AUDIO:
                // 音频-使用本地图片
                fileBean.setImageIntId(R.mipmap.icon_file_audio);
                break;
            case FileViewModel.FILE_DOCUMENTATION:
                String path = file.getAbsolutePath();
                if (TextUtils.isEmpty(path)) {
                    fileBean.setImageIntId(getFileToTypeImageId(""));
                } else {
                    try {
                        // 获取文件后缀
                        String eName = file.getAbsolutePath().substring(path.lastIndexOf(".") + 1, path.length());
                        // 文档-使用本地图片
                        fileBean.setImageIntId(getFileToTypeImageId(eName));
                    } catch (Exception e) {
                        // 找不到后缀
                        fileBean.setImageIntId(getFileToTypeImageId(""));
                    }
                }
                break;
            default: {
                fileBean.setImageUrl(file.getAbsolutePath());
            }
        }
    }

    /**
     * 补全这个文件以前在系统中的一些参数
     */
    public static String supplementalFields(File file) {
        return FileTypeDetector.getFileTypeByMagicNumber(file);
    }

    public static int getFileToTypeImageId(String type) {
        if (TextUtils.isEmpty(type)) {
            return droidninja.filepicker.R.drawable.icon_file_unknown;
        }

        switch (type) {
            case "zip":
            case "rar":
            case "gz":
                return droidninja.filepicker.R.drawable.icon_file_zip_fill;
            case "pdf":
                return droidninja.filepicker.R.drawable.icon_file_pdf;
            case "doc":
            case "docx":
            case "dot":
            case "dox":
                return droidninja.filepicker.R.drawable.icon_file_doc;
            case "xls":
            case "xlsx":
            case "xlsm":
            case "xlsb":
                return droidninja.filepicker.R.drawable.icon_file_xls;
            case "txt":
                return droidninja.filepicker.R.drawable.icon_file_txt;
            default: {
                return droidninja.filepicker.R.drawable.icon_file_unknown;
            }
        }
    }

    /**
     * 重命名文件或目录
     *
     * @param oldFilePath 旧文件的路径
     * @param newFilePath 新文件的路径
     * @return 如果重命名成功返回 true，否则返回 false
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        LOGE("====rename:oldFile=>>" + oldFile.getAbsolutePath() + " newFile:" + newFile.getAbsolutePath());
        // 检查旧文件是否存在且目标文件名不存在
        if (oldFile.exists() && !newFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }


    /**
     * 还原文件后缀
     *
     * @param filePath 文件路径
     * @return 还原后的文件路径
     */
    public static String getRecoverExtension(String filePath) {
        String fileHeader = getFileHeader(filePath);
        if (fileHeader == null) return "";
        // 检查文件头是否在已知文件类型中
        return FILE_TYPE_MAP.getOrDefault(fileHeader, "");
    }

    /**
     * 获取文件的文件头（前3字节的16进制字符串表示）
     *
     * @param filePath 文件路径
     * @return 文件头的16进制字符串
     */
    private static String getFileHeader(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) return null;

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] headerBytes = new byte[3];
            if (fis.read(headerBytes) != -1) {
                return bytesToHex(headerBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
