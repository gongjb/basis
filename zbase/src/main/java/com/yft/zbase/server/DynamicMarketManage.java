package com.yft.zbase.server;

import android.app.Application;
import android.content.Context;

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
        IServerAgent userImplServer = new UserImplServer();
        IServerAgent deviceImplServer = new DeviceImplServer();
        IServerAgent addressImplServer = new AddressImplServer();
        IServerAgent shareImplServer = new ShareImplServer();
        IServerAgent payImplServer = new PayImplServer();
        IServerAgent languageServer = new LanguageServer();
        mServerAgent.put(IServerAgent.USER_SERVER, userImplServer);
        mServerAgent.put(IServerAgent.DEVICE_SERVER, deviceImplServer);
        mServerAgent.put(IServerAgent.ADDRESS_SERVER, addressImplServer);
        mServerAgent.put(IServerAgent.SHARE_SERVER, shareImplServer);
        mServerAgent.put(IServerAgent.PAY_SERVER, payImplServer);
        mServerAgent.put(IServerAgent.LANGUAGE_SERVER, languageServer);
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
