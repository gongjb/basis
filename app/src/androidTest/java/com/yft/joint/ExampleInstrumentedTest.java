package com.fuan.chameleon;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.hkbyte.cnbase.util.FileEncryptionHelper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.fuan.market", appContext.getPackageName());
    }

    // 示例：生成一个 Base64 编码的 AES 密钥（仅用于测试，实际中应安全存储密钥）
//    @Test
//    public void generateSecretKeyBase64() throws NoSuchAlgorithmException {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(FileEncryptionHelper.KEY_SIZE, new SecureRandom());
//        SecretKey secretKey = keyGen.generateKey();
//        Log.e("Test", Base64.getEncoder().encodeToString(secretKey.getEncoded()));
//    }
}