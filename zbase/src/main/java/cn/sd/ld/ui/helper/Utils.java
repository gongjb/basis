package cn.sd.ld.ui.helper;

import android.text.TextUtils;

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
}
