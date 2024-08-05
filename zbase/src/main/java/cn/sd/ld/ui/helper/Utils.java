package cn.sd.ld.ui.helper;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaDrm;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.yft.zbase.ZBaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import cn.sd.ld.ui.helper.check.CheckHook;
import cn.sd.ld.ui.helper.check.CheckRoot;
import cn.sd.ld.ui.helper.check.CheckVirtual;
import cn.sd.ld.ui.helper.check.EmulatorDetector;

public class Utils {

    static {
        System.loadLibrary("ang");
    }

    static public native String getMD5KeyByRequest(long requestTimestamp,String requestTimestampStr, String requestId, String token,boolean isToken, String random);

    public static native String getMD5KeyByDecrypt(long requestTimestamp,String requestTimestampStr, String requestId, String token, String random);

    static public native String getPositionBaseUrl(int position);

    public static String getMD5KeyByRequest(long requestTimestamp,String requestTimestampStr, String requestId, String token, String random) {
        boolean isToken = false;
        if(TextUtils.isEmpty(token)) {
            isToken = true;
            token = "";
        }
        return getMD5KeyByRequest(requestTimestamp,requestTimestampStr,requestId,token,isToken,random);
    }

    public static boolean isCollectionEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**	 * 检查网络是否可用	 * 	 * @param context	 * @return	 */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }


    public static String getUUID() {
        String serial = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            serial = getDeviceOther();
            return serial; // 随便一个初始化;
        }
        UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        MediaDrm wvDrm = null;
        try {
            wvDrm = new MediaDrm(wideVineUuid);
            byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            //optional encoding to convert the array in string.
            return Base64.encodeToString(wideVineId, Base64.NO_WRAP);
        } catch (Exception e) {
            return getDeviceOther();
        } finally {
            if (wvDrm != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    wvDrm.close();
                } else {
                    wvDrm.release();
                }
            }
        }
    }

    public static String getDeviceOther() {
        String deviceId = getDeviceOtherStr();
        try {
            return Md5Encryption.parseStrToMd5U32(deviceId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDeviceOtherStr() {
        String androidID = Settings.Secure.getString(ZBaseApplication.get().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = androidID + Build.SERIAL;
        return  androidID + deviceId;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 判断当前是否为真机
     * @param context
     * @return false 为
     */
    public static boolean isFalsePhone(Context context){
//        String cpuInfo = readCpuInfo();
//        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
//            return true;
//        }

        // EmulatorDetector.isEmulator(context) 模拟器检测
        // CheckVirtual.isRunInVirtual() 多开检测
        // CheckHook.isHook(context) Hook检测
        // CheckRoot.isDeviceRooted(); root检测
        long a = System.currentTimeMillis();

        boolean isFalsePhone = EmulatorDetector.isEmulator(context)
                || CheckVirtual.isRunInVirtual() || CheckRoot.isDeviceRooted() || CheckHook.isHook(context);
        long b = System.currentTimeMillis();
        long c = (b - a) / 1000;
        return isFalsePhone;

    }

    /*
     *根据CPU是否为电脑来判断是否为模拟器(子方法)
     *返回:String
     */
    public static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
        }
        return result;
    }

    /**
     * 时间转换
     * @param dateTime
     * @return
     * @throws java.text.ParseException
     */
    public static long parseTimeLong(String dateTime) throws java.text.ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
        return calendar.getTimeInMillis();
    }

    /**
     * 将毫秒转换为具体时间
     * @param millisecond
     * @return
     */
    public static String parseTimeStr(long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        return format.format(date);
    }

    public static String getStringTime(long cnt){
        long hour = cnt / 3600;
        long min = cnt % 3600 / 60;
        long second = cnt % 60;
        return String.format(Locale.CHINA,"%02d:%02d:%02d",hour,min,second);
    }

    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    public static boolean copyStr(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) ZBaseApplication.get().getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
