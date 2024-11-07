package com.hkbyte.cnbase.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class FileEncryptionHelper {

    // AES 密钥长度
    private static final int KEY_SIZE = 256;
    // 用于存储密钥的 Base64 编码
    private static final String SECRET_KEY = "your-secret-base64-encoded-key-here"; // 替换为你的 Base64 编码密钥

    // 初始化密钥
    private static SecretKey getSecretKey() throws Exception {
        byte[] decodedKey  = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // 加密文件的一部分
    public static void encryptPartOfFile(File file, long startByte, long length, File outputFile) throws Exception {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            // 读取未加密部分
            raf.seek(0);
            byte[] buffer = new byte[(int) startByte];
            raf.readFully(buffer);
            fos.write(buffer);

            // 读取要加密的部分
            raf.seek(startByte);
            buffer = new byte[(int) length];
            raf.readFully(buffer);

            // 加密部分数据
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encryptedData = cipher.doFinal(buffer);

            // 写入加密部分
            fos.write(encryptedData);

            // 如果文件还有其他未加密部分，继续读取并写入
            long remainingBytes = file.length() - (startByte + length);
            if (remainingBytes > 0) {
                raf.seek(startByte + length);
                buffer = new byte[(int) remainingBytes];
                raf.readFully(buffer);
                fos.write(buffer);
            }
        }
    }

    // 解密文件的一部分并合并未加密部分
    public static void decryptPartOfFile(File encryptedFile, long encryptedStartByte, long encryptedLength, File outputFile) throws Exception {
        try (RandomAccessFile raf = new RandomAccessFile(encryptedFile, "r");
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            // 读取未加密部分（如果有）
            long unencryptedSize = encryptedStartByte;
            byte[] buffer = new byte[(int) unencryptedSize];
            raf.readFully(buffer);
            fos.write(buffer);

            // 读取加密部分
            raf.seek(encryptedStartByte);
            buffer = new byte[(int) encryptedLength];
            raf.readFully(buffer);

            // 解密部分数据
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] decryptedData = cipher.doFinal(buffer);

            // 写入解密部分
            fos.write(decryptedData);

            // 如果文件还有其他未加密部分，继续读取并写入
            long remainingBytes = encryptedFile.length() - (encryptedStartByte + encryptedLength);
            if (remainingBytes > 0) {
                raf.seek(encryptedStartByte + encryptedLength);
                buffer = new byte[(int) remainingBytes];
                raf.readFully(buffer);
                fos.write(buffer);
            }
        }
    }

    // 示例：生成一个 Base64 编码的 AES 密钥（仅用于测试，实际中应安全存储密钥）
    public static String generateSecretKeyBase64() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static void test() {
        File inputFile = new File("/path/to/your/video.mp4");
        File encryptedFile = new File("/path/to/your/encrypted_video.mp4");
        File decryptedFile = new File("/path/to/your/decrypted_video.mp4");

        long startByteToEncrypt = 1024 * 1024; // 例如，从 1 MB 处开始加密
        long lengthToEncrypt = 1024 * 1024; // 加密 1 MB 数据

        try {
            // 加密文件的一部分
            FileEncryptionHelper.encryptPartOfFile(inputFile, startByteToEncrypt, lengthToEncrypt, encryptedFile);

            // 解密文件的一部分并合并未加密部分
            FileEncryptionHelper.decryptPartOfFile(encryptedFile, startByteToEncrypt, lengthToEncrypt, decryptedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}