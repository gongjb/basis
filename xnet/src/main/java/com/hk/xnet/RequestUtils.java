package com.hk.xnet;

import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


public class RequestUtils {

    private static Random random = new Random();

    /**
     *
     * @param values 页面中传递而来的产生
     * @param iParameter 公共参数
     * @return
     */
    public static Map<String, String> getRequestParameter(TreeMap<String, String> values, IParameter iParameter) {
        values.putAll(getRequestParameter(iParameter));
        return values;
    }

    /**
     * 空参数调用
     * @param iParameter
     * @return
     */
    public static Map<String, String> getRequestParameter(IParameter iParameter) {
        TreeMap<String, String> values = new TreeMap<>();
        long requestTimestamp = System.currentTimeMillis();
        String uuid =  iParameter.getUUID(); // 每次获取的不一致
        values.put("version", iParameter.getVersion());
        values.put("language", iParameter.getLanguageType());
        values.put("serialNumber", iParameter.getDeviceId());
        values.put("requestTimestamp", String.valueOf(requestTimestamp));
        values.put("requestId", uuid);
        //clientType
        values.put("clientType", iParameter.getAndroid());
        values.put("promoteChannel",iParameter.getPromoteChannel());
        //clientModel
        values.put("clientModel", iParameter.getClientModel());
        // 获取key
        String token = iParameter.getToken();
        String key = iParameter.getKey(requestTimestamp, uuid, token, getRandom());
        if(!TextUtils.isEmpty(token)) {
            values.put("token", iParameter.getToken());
        }
        // 将其他值，转换成服务器能够识别的key
        values.put("sign", getDigest(values, key));
        return values;
    }

    private static String getDigest(TreeMap<String, String> map, String key) {
        StringBuilder sb = new StringBuilder();
        int in = 0;
        for (Map.Entry entry : map.entrySet()) {
            in++;
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if ((in + 1) <= map.size()) {
                // 最后一个不拼接
                sb.append("&");
            }
        }
        sb.append(key);
        String sign = "";
        try {
            sign = Md5Encryption.parseStrToMd5L32(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String getRandom() {
        long num = random.nextLong();
        return String.valueOf(num);
    }
}
