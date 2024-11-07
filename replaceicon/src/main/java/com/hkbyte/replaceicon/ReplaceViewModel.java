package com.hkbyte.replaceicon;

import com.hkbyte.cnbase.Configs;
import com.hkbyte.cnbase.util.Constant;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IFreeStorage;
import com.yft.zbase.server.IServerAgent;

public class ReplaceViewModel extends BaseViewModel {

    private IFreeStorage mFreeStorage; //

    public ReplaceViewModel() {
        mFreeStorage = DynamicMarketManage.getInstance().getServer(IServerAgent.FREE_STORAGE);
    }

    public void saveConfigStyle(int style) {
        Configs configs = mFreeStorage.getParcelable(Constant.CONFIG_INFO, Configs.class);
        if (configs == null) {
            configs = new Configs(style);
        } else {
            configs.setStyle(style);
        }

        mFreeStorage.saveParcelable(Constant.CONFIG_INFO, configs);
    }
}
