package com.fuan.market.model;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.yft.zbase.base.BaseModel;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.KVBean;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.utils.Constant;
import com.yft.zbase.xnet.InterfacePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.fuan.market.bean.AdBean;
import cn.sd.ld.ui.helper.Logger;

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

    public boolean isOpenAd() {
        // 是否有广告
        return isOpenAd;
    }

    public WelcomeViewModel() {
        super();
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
        if (getUserServer() != null) {
            getUserServer().saveFlavor(flavor); // 保存渠道
        }
    }
}
