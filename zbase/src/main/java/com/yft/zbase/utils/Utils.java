package com.yft.zbase.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.yft.zbase.server.UserLevelType;
import com.yft.zbase.bean.KVBean;
import com.yft.zbase.bean.WeChatPayParams;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isCollectionEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f); // 四舍五入取整
    }

    public static float getScreenWidth(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            if (screenWidth <= 0) {
                return dip2px(context, 360);
            }
            return screenWidth;
        } catch (Exception e) {
            return dip2px(context, 360);
        }
    }

    public static float getScreenHeight(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenHeight = size.y;
            if (screenHeight <= 0) {
                return dip2px(context, 920);
            }
            return screenHeight;
        } catch (Exception e) {
            return dip2px(context, 920);
        }
    }

    /**
     * 清除
     * @param context
     */
    public static void cleanCookie(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
        } else {
            if (context == null) {
                return;
            }
            Context ctx = context.getApplicationContext();
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(ctx);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieSyncMngr.stopSync();
        }
    }

    public static float getBodyWidth(Context mContext) {
        float screenWidth = getScreenWidth(mContext);
        if (screenWidth <= 0.0f) {
            // 给一个默认的值, 屏幕宽度为360
            screenWidth =  Utils.dip2px(mContext, 360);
        }
        return screenWidth - (Utils.dip2px(mContext,12) * 2);
    }

    // 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); // 四舍五入取整
    }

    /**
     * 判断请求是否失败
     * @param kvBean
     * @return
     */
    public static boolean requestIsFail(KVBean kvBean) {
       return (kvBean == null || TextUtils.equals(kvBean.key, KVBean.FAIL));
    }


    public static boolean isLink(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    public static String getPriceDouble(double value) {
        if (value % 1 == 0) {
            try {
                return String.valueOf((int)value);
            } catch (Exception e) {
                //do noting
            }
        }
        return new Formatter().format("%.2f", value).toString();
    }

    public static String getPriceYuan(double a) {
        return getPriceDouble(a / 100d);
    }


    public static String format(double value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.toString();
        } catch (Exception e) {
            return "";
        }
    }

    //FLOOR
    public static float formatFloor(double value) {
        // 将double值转换为BigDecimal，以便进行高精度的数学运算
        BigDecimal bd = new BigDecimal(Double.toString(value));
        // 使用FLOOR舍入模式将BigDecimal值保留两位小数
        // FLOOR模式总是向负无穷大方向舍入
        bd = bd.setScale(2, RoundingMode.FLOOR);
        // 将BigDecimal转换为float类型
        // 注意：这里可能会发生精度损失，因为float类型的精度有限
        return bd.floatValue();
    }

    public static String format6(double value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(6, RoundingMode.HALF_UP);
            return bd.toString();
        } catch (Exception e) {
            return "";
        }
    }


    public static String formatStr1(double value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            return bd.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static double formatTo2(double value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            return value;
        }
    }


    public static Map<String, String> getUrlParameters(String url) {
        try {
            Map<String, String> params = urlRequest(url);
            return params;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> urlRequest(String URL)
    {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit=null;

        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null)
        {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");

            //解析出键值
            if(arrSplitEqual.length>1)
            {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            }
            else
            {
                if(arrSplitEqual[0]!="")
                {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL)
    {
        String strAllParam=null;
        String[] arrSplit=null;

        strURL=strURL.trim();

        arrSplit=strURL.split("[?]");
        if(strURL.length()>1)
        {
            if(arrSplit.length>1)
            {
                if(arrSplit[1]!=null)
                {
                    strAllParam=arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    public static String phoneFormat(String phone) {
        try {
            if (TextUtils.isEmpty(phone)) {
                return phone;
            }
            String reStr = phone.substring(phone.length() - 4, phone.length());
            String preStr = phone.substring(0, phone.length() - 8);
            StringBuilder sb = new StringBuilder();
            sb.append(preStr).append("****").append(reStr);
            return sb.toString();
        } catch (Exception e) {
            return phone;
        }
    }

    public static boolean isStringPureNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        // 使用正则表达式匹配纯数字字符串
        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String formatCreateDate(String createDate) {
        try {
            long longTime = Long.parseLong(createDate);
            Date date = new Date(longTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);
            return formattedDate;
        } catch (Exception e) {
            return createDate;
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } else {
            // 如果没有获取到ConnectivityManager对象，则默认为已连接
            return true;
        }
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String versionName = packInfo.versionName;
        //String versionCode = packInfo.versionCode;
        return versionName;
    }

    public static String getNumber(String str) {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        return builder.toString();
    }

    /**
     * 格式化积分
     * @param integral 积分
     * @return
     */
    public static String getIntegral(double integral) {
        if (integral <= 0.0d) {
            return "";
        }
        BigDecimal org = new BigDecimal(integral);
        BigDecimal dest = org.setScale(5, BigDecimal.ROUND_HALF_UP);
        String iStr =  dest.toString();
        if (!TextUtils.isEmpty(iStr) && iStr.endsWith("0")) {
            // 去除数字后面的0
            iStr = iStr.replaceAll("0+$", "");
        }
        return iStr;
    }

    /**
     * 判断是否存在某个app
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasApplication(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                boolean isEx = false;
                Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
                if (!cn.sd.ld.ui.helper.Utils.isCollectionEmpty(list)) {
                    for (int i = 0,count=list.size(); i <count ; i++) {
                        if(list.get(i).activityInfo.applicationInfo.packageName.equalsIgnoreCase(packageName)) {
                            isEx = true;
                            break;
                        }
                    }
                }

                if (!isEx) {
                    List<PackageInfo> listPackageInfo = packageManager.getInstalledPackages(0);
                    if(!cn.sd.ld.ui.helper.Utils.isCollectionEmpty(listPackageInfo)) {
                        for (int i = 0; i < listPackageInfo.size(); i++) {
                            if (listPackageInfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                                isEx = true;
                                break;
                            }
                        }
                    }
                }
                return isEx;
            } else {
                //获取系统中安装的应用包的信息。 6.0以下的不会太关心。 找不到就找不到吧
                List<PackageInfo> listPackageInfo = packageManager.getInstalledPackages(0);
                if (cn.sd.ld.ui.helper.Utils.isCollectionEmpty(listPackageInfo)) {
                    return false;
                }
                for (int i = 0; i < listPackageInfo.size(); i++) {
                    if (listPackageInfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return  false;
        }
        return false;
    }

    public static String base64ToPlain(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return new String(decodedBytes);
    }

    public static String plainToBase64(String plainText) {
        return Base64.encodeToString(plainText.getBytes(), Base64.DEFAULT);
    }

    public static WeChatPayParams parseParams(String queryString) {
        // 初始化参数映射
        Map<String, String> params = new HashMap<>();

        // 按'&'分割字符串，得到各个参数
        String[] pairs = queryString.split("&");

        // 遍历每个参数对
        for (String pair : pairs) {
            // 按'='分割键和值
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                // 将参数名和参数值添加到映射中
                params.put(keyValue[0], keyValue[1]);
            }
        }

        // 从映射中提取参数值并创建WeChatPayParams对象
        return new WeChatPayParams(
                params.get("appId"),
                params.get("partnerId"),
                params.get("prepayId"),
                params.get("packageValue"),
                params.get("nonceStr"),
                params.get("timeStamp"),
                params.get("sign")
        );
    }

    public static UserLevelType getUserLevel(int level) {
        for (UserLevelType userLevelType : UserLevelType.values()) {
            if (userLevelType.getLevel() == level) {
                return userLevelType;
            }
        }
        return null;
    }

    public static float formatString(String value) {
        try {
            // 将字符串解析为BigDecimal
            BigDecimal bdValue = new BigDecimal(value);
//            System.out.println("Original BigDecimal: " + bdValue); // 打印原始值

            // 设置保留两位小数，并指定舍入模式（这里使用向下舍入）
            BigDecimal roundedValue = bdValue.setScale(2, RoundingMode.FLOOR);
//            System.out.println("Rounded BigDecimal: " + roundedValue); // 打印舍入后的值
            // 将BigDecimal转换为float
            float floatValue = roundedValue.floatValue();
//            System.out.println("Rounded BigDecimal: " + roundedValue); // 打印舍入后的值
            return floatValue;
        } catch (NumberFormatException e) {
            // 如果字符串不能解析为数值，则打印异常堆栈并返回一个默认值
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static String formatString2(String value) {
        try {
            // 将字符串解析为BigDecimal
            BigDecimal bdValue = new BigDecimal(value);
            // 设置保留两位小数，并指定舍入模式（这里使用向下舍入）
            BigDecimal roundedValue = bdValue.setScale(2, RoundingMode.FLOOR);
            // 将BigDecimal转换为float
            return roundedValue.toString();
        } catch (Exception e) {
            // 如果字符串不能解析为数值，则打印异常堆栈并返回一个默认值
            e.printStackTrace();
            return value;
        }
    }
}
