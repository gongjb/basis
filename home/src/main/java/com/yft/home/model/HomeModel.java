package com.yft.home.model;

import com.yft.home.bean.HomeConfigBean;
import com.yft.home.bean.VersionBean;
import com.yft.zbase.base.BaseModel;
import com.yft.zbase.xnet.InterfacePath;
import com.yft.zbase.xnet.ResponseDataListener;


public class HomeModel extends BaseModel {

    public HomeModel() {
        super();
    }

    public void postVersion(ResponseDataListener<VersionBean> responseDataListener) {
    }

    /**
     * 请求首页配置
     *
     * @param responseDataListener
     */
    public void postHomeConfig(ResponseDataListener<HomeConfigBean> responseDataListener) {
        // 请求
        mNetWork.easyPost(mUser.getBastServiceURL() + InterfacePath.HOME_CONFIG,
                getRequestParameter(),
                responseDataListener,
                HomeConfigBean.class);
    }

}
