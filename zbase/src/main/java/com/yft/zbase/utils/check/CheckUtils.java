package com.yft.zbase.utils.check;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CheckUtils {
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
}
