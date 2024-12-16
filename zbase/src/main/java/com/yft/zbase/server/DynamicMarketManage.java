package com.yft.zbase.server;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理类（用户，设备、地址、分享、支付）
 */
public class DynamicMarketManage {
    private static DynamicMarketManage instance;
    private ConcurrentHashMap<String, IServerAgent> mServerAgent;

    private DynamicMarketManage() {
        mServerAgent = new ConcurrentHashMap<>();
        mServerAgent.put(IServerAgent.USER_SERVER, new UserImplServer());
        mServerAgent.put(IServerAgent.DEVICE_SERVER, new DeviceImplServer());
        mServerAgent.put(IServerAgent.ADDRESS_SERVER, new AddressImplServer());
        mServerAgent.put(IServerAgent.SHARE_SERVER, new ShareImplServer());
        mServerAgent.put(IServerAgent.PAY_SERVER, new PayImplServer());
        mServerAgent.put(IServerAgent.LANGUAGE_SERVER, new LanguageServer());
        mServerAgent.put(IServerAgent.FREE_STORAGE, new FreeStorageServer());
    }

    public static synchronized DynamicMarketManage getInstance() {
        if (instance == null) {
            synchronized (DynamicMarketManage.class) {
                if (instance == null) {
                    instance = new DynamicMarketManage();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        Set<?> set = mServerAgent.entrySet();
        Context cox = context instanceof Application ? context : context.getApplicationContext();
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            IServerAgent iServerAgent = (IServerAgent) mapentry.getValue();
            // 初始化全部服务
            iServerAgent.initServer(cox);
        }
    }

    public <T extends IServerAgent> T getServer(String serverName) {
        if (mServerAgent.containsKey(serverName)) {
            return mServerAgent.get(serverName).getServer();
        }
        return null;
    }

    public void cleanInfoAll() {
        Set<?> set = mServerAgent.entrySet();
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            IServerAgent iServerAgent = (IServerAgent) mapentry.getValue();
            iServerAgent.cleanInfo();
        }
    }
}
