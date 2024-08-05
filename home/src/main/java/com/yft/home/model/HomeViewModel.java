package com.yft.home.model;

import androidx.lifecycle.MutableLiveData;

import com.yft.home.adapterutil.AdapterCreateFactory;
import com.yft.home.bean.HomeConfigBean;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;
import com.yft.zbase.xnet.ResponseDataListener;

public class HomeViewModel extends BaseViewModel {

    private MutableLiveData<HomeConfigBean> mHomeConfigBeanMutableLiveData = new MutableLiveData<>();
    private HomeModel homeModel = new HomeModel();

    public MutableLiveData<HomeConfigBean> getHomeConfigBeanMutableLiveData() {
        return mHomeConfigBeanMutableLiveData;
    }

    // 请求首页配置
    public void postHomeConfig() {
        homeModel.postHomeConfig(new ResponseDataListener<HomeConfigBean>() {
            @Override
            public void success(HomeConfigBean data) {
                getHomeConfigBeanMutableLiveData().postValue(data);
            }

            @Override
            public void fail(Throwable throwable) {
                ToastUtils.currencyToast(throwable); // 如果需要toast提示需要加上这句话
                getErrorMutableLiveData().postValue("0"); // 页面处理
            }
        });
    }

    /**
     * 获取历史搜索记录
     *
     * @return
     */
    public String[] getKeys() {
        return getUserServer().getSearchHistory();
    }

    /**
     * 获取历史搜索记录
     *
     * @return
     */
    public boolean removeKeys(String key) {
        return getUserServer().removeSearchHistoryKey(key);
    }

    public boolean isExistList(HomeConfigBean data, String type) {
        if (data == null) {
            return false;
        }
        switch (type) {
            case AdapterCreateFactory.TYPE_CLASSIFY:
                return !Utils.isCollectionEmpty(data.getKingkongList());
            case AdapterCreateFactory.TYPE_BANNER:
                return !Utils.isCollectionEmpty(data.getBannerList());
            default: {
                return false;
            }
        }
    }
}
