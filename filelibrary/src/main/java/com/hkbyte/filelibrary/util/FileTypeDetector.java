package com.hkbyte.filelibrary.util;

import static com.yft.zbase.utils.Logger.LOGE;

import com.luck.picture.lib.config.PictureMimeType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeDetector {

    // 文件头与文件类型的映射表
    private static final Map<String, String> fileTypeMap = new HashMap<>();

    static {
        // 图片文件
        fileTypeMap.put("FFD8FFE0", PictureMimeType.ofJPEG());
        fileTypeMap.put("FFD8FFE1", PictureMimeType.ofJPEG());
        fileTypeMap.put("89504E47", PictureMimeType.ofPNG());
        fileTypeMap.put("47494638", PictureMimeType.ofGIF());
        //52494646
        fileTypeMap.put("52494646", PictureMimeType.ofWEBP());

        // 文档文件
        fileTypeMap.put("25504446", "PDF");
        fileTypeMap.put("504B0304", "ZIP Archive or Office Document (DOCX, XLSX, PPTX)");

        // 压缩文件
        fileTypeMap.put("52617221", "RAR");
        fileTypeMap.put("377ABCAF", "7-Zip");

        // 音频文件
        fileTypeMap.put("494433", PictureMimeType.of3GP());
        fileTypeMap.put("52494646", PictureMimeType.of3GP());

        // 视频文件
        fileTypeMap.put("00000018", PictureMimeType.ofMP4());
        fileTypeMap.put("000000", PictureMimeType.ofMP4());
        fileTypeMap.put("52494646", PictureMimeType.ofAVI());

        // 文本文件（没有固定的文件头）
        //fileTypeMap.put("TXT", "Plain Text File");
    }

    public static final String NOW = "Unknown Type";

    // 通过文件头判断文件类型
    public static String getFileTypeByMagicNumber(File file) {
        if (file == null || !file.exists()) {
            return NOW;
        }
        byte[] header = new byte[8]; // 一般读取前 8 个字节
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(header, 0, header.length);
            String fileHeader = bytesToHex(header);
            LOGE("==>" + fileHeader);
            // 截取文件头并与映射表进行匹配
            for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
                if (fileHeader.startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return NOW;
        } catch (IOException e) {
            e.printStackTrace();
            return NOW;
        }
    }

    // 将字节数组转换为 16 进制字符串的方法
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
