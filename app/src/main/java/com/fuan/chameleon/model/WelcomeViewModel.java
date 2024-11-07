package com.fuan.chameleon.model;

import androidx.lifecycle.MutableLiveData;

import com.hkbyte.cnbase.Configs;
import com.hkbyte.cnbase.util.Constant;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IFreeStorage;
import com.yft.zbase.server.IServerAgent;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeViewModel extends BaseViewModel {
    private MutableLiveData<TargetBean> mAdBeanMutableLiveData = new MutableLiveData<>();
    // 广告计时器
    private MutableLiveData<Integer> mTimeTaskMutableLiveData = new MutableLiveData<>();
    private Integer mTaskIndex = 0;
    // 计数6次
    private final static Integer TASK_COUNT = 3;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private String mImageUrl = "1";
    private String mToAdUrl = "";
    private boolean isOpenAd;
    private TargetBean mTargetBean;
    private IFreeStorage mFreeStorage; //

    public boolean isOpenAd() {
        // 是否有广告
        return isOpenAd;
    }


    /**
     * 获取当前样式
     * @return
     */
    public int getConfigStyle() {
        Configs configs = mFreeStorage.getParcelable(Constant.CONFIG_INFO, Configs.class);
        if (configs == null) {
            return 0;
        }
        return configs.getStyle();
    }

    public WelcomeViewModel() {
        super();
        mFreeStorage = DynamicMarketManage.getInstance().getServer(IServerAgent.FREE_STORAGE);
        mTargetBean = getUserServer().getPreloadOpen();
        isOpenAd =(mTargetBean != null);
    }

    public TargetBean getTargetBean() {
        return mTargetBean;
    }

    public boolean isFirst() {
        if (mBaseModel.getServiceBean() == null) {
            // 实际要判断
            return true;
        }
        return false;
    }

    public MutableLiveData<TargetBean> getAdBeanMutableLiveData() {
        return mAdBeanMutableLiveData;
    }

    public MutableLiveData<Integer> getTimeTaskMutableLiveData() {
        return mTimeTaskMutableLiveData;
    }

    public String getToAdUrl() {
        return mToAdUrl;
    }

    public boolean isLogin() {
        return getUserInfo() != null;
    }

    public void postAd() {
        // 先模拟有广告，实际要从主页去获取广告内容之后
        mAdBeanMutableLiveData.postValue(mTargetBean);
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        mTimer = new Timer();
        mTimerTask = getTimerTask();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    public void stopTask() {
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                Integer val = TASK_COUNT - mTaskIndex++;
                if (val < 0) {
                    this.cancel();
                    cancel();
                    mTaskIndex = 0;
                } else {
                    getTimeTaskMutableLiveData().postValue(val);
                }
            }
        };
    }



    @Override
    protected void onCleared() {
        mTaskIndex = 0;
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        super.onCleared();
    }

    public void saveFlavor(String flavor) {
//        if (getUserServer() != null) {
//            getUserServer().saveFlavor(flavor); // 保存渠道
//        }
    }
}
