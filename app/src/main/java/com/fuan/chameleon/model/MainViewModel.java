package com.fuan.chameleon.model;





import static com.hkbyte.cnbase.router.ChameleonJumpRouter.TO_HOME_MINE;
import static com.hkbyte.cnbase.router.ChameleonJumpRouter.TO_HOME_PAGE;
import static com.hkbyte.cnbase.router.ChameleonJumpRouter.TO_HOME_TOOLS;
import static com.hkbyte.cnbase.router.ChameleonJumpRouter.TO_HOME_VIP;

import androidx.lifecycle.MutableLiveData;

import com.hk.xnet.WebServiceThrowable;
import com.hkbyte.cnbase.router.ChameleonJumpRouter;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.router.IToHomePageListener;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.xnet.ResponseDataListener;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<TargetBean> mPreloadMutableLiveData = new MutableLiveData<>();
    private WelcomeModel mWelcomeModel;

    public MutableLiveData<Integer> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<TargetBean> getPreloadMutableLiveData() {
        return mPreloadMutableLiveData;
    }

    public MainViewModel() {
        super();
        mWelcomeModel = new WelcomeModel();
        // 注入jump跳转代码代码
        ChameleonJumpRouter.getInstance().addToHomePageListener(iToHomePageListener);
    }


    private IToHomePageListener iToHomePageListener = new IToHomePageListener() {
        @Override
        public void onToHomePage(TargetBean homeListBean) {
            switch (homeListBean.getTarget()) {
                case TO_HOME_PAGE:
                    getMutableLiveData().postValue(0);
                    break;
                case TO_HOME_TOOLS:
                    getMutableLiveData().postValue(1);
                    break;
                case TO_HOME_VIP:
                    getMutableLiveData().postValue(2);
                    break;
                case TO_HOME_MINE:
                    getMutableLiveData().postValue(3);
                    break;
                default:{}
            }

        }
    };

    /**
     * 请求开屏广告
     */
    public void requestOpenStart() {
        mWelcomeModel.postOpenStart(new ResponseDataListener<TargetBean>() {
            @Override
            public void success(TargetBean data) {
                // 加载到本地
                getUserServer().savePreloadOpen(data);
                getPreloadMutableLiveData().postValue(data);
            }

            @Override
            public void fail(Throwable throwable) {
                if (throwable instanceof WebServiceThrowable) {
                    getUserServer().savePreloadOpen(null);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ChameleonJumpRouter.getInstance().removeToHomePageListener(iToHomePageListener);
    }
}
