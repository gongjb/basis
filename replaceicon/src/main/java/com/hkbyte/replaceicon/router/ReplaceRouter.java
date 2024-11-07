package com.hkbyte.replaceicon.router;

import com.yft.zbase.router.IRouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class ReplaceRouter implements IRouter {

    // 计算器伪装页面1
    public static final String CALCULATOR_ACTIVITY = "com/hkbyte/replaceicon/calculator/CalculatorActivity";
    // 计算器伪装页面2 Calculator2Activity
    //ReplaceIconActivity
    // 计算器伪装页面2
    public static final String CALCULATOR_2_ACTIVITY = "com/hkbyte/replaceicon/calculator/Calculator2Activity";

    // 更换页面
    public static final String REPLACE_ICON_ACTIVITY = "com/hkbyte/replaceicon/calculator/ReplaceIconActivity";
    @Override
    public ConcurrentMap<String, String> initPages() {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap();
        // 配置页面跳转
        concurrentMap.put("CalculatorActivity", CALCULATOR_ACTIVITY);
        concurrentMap.put("Calculator2Activity", CALCULATOR_2_ACTIVITY);
        //ReplaceIconActivity
        concurrentMap.put("ReplaceIconActivity", REPLACE_ICON_ACTIVITY);
        return concurrentMap;
    }
}
