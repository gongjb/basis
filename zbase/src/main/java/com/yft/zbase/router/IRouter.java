package com.yft.zbase.router;

import java.util.concurrent.ConcurrentMap;

public interface IRouter {
    ConcurrentMap<String, String> initPages(); // 初始化页面， 返回最终模块的页面字典
}
