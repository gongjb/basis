package com.yft.user.model;


import static com.yft.zbase.utils.Logger.LOGE;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.reflect.TypeToken;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.bean.UserToken;
import com.yft.zbase.server.DynamicMarketManage;
import com.yft.zbase.server.IAddress;
import com.yft.zbase.server.IServerAgent;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;
import com.yft.zbase.xnet.InterfacePath;
import com.yft.zbase.xnet.ResponseDataListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginViewModel extends BaseViewModel {
    private Timer mTimer;
    private TimerTask mTimerTask;
    private MutableLiveData<Integer> mTimeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<UserInfo> mUserInfoMutableLiveData = new MutableLiveData<>();

    private final int countTime = 60;
    private volatile int count = countTime;
    private volatile boolean isStart;
    private LoginModel mLoginModel;
    private IAddress mAddress;

    public LoginViewModel() {
        super();
        mLoginModel = new LoginModel();
        mAddress = DynamicMarketManage.getInstance().getServer(IServerAgent.ADDRESS_SERVER);
    }

    public boolean isStart() {
        return isStart;
    }

    public MutableLiveData<Integer> getTimeMutableLiveData() {
        return mTimeMutableLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoMutableLiveData() {
        return mUserInfoMutableLiveData;
    }

    // 请求验证码
    public void requestCheckNum(String action, String phone) {
        mLoginModel.sendCode(action, phone, new ResponseDataListener<String>() {
            @Override
            public void success(String data) {
                ToastUtils.toast("验证码发送成功");
                startTime();
            }

            @Override
            public void fail(Throwable throwable) {
                ToastUtils.currencyToast(throwable);
                getErrorMutableLiveData().postValue(throwable.getMessage());
            }
        });
    }

    /**
     * 注册
     * @param phone 电话号码
     * @param code 验证码
     * @param invitationCode 邀请码
     */
    public void requestReg(String phone, String code, String invitationCode) {
        mLoginModel.postUserReg(phone, code, invitationCode, new ResponseDataListener<UserToken>() {
            @Override
            public void success(UserToken data) {
                if (data != null) {
                    UserInfo userInfo = data.getUser();
                    userInfo.setToken(data.getToken());
                    // 保存起用户信息
                    mLoginModel.saveUserInfo(userInfo);
                    // 发送user信息给登录页面， 处理跳转逻辑
                    mUserInfoMutableLiveData.postValue(userInfo);

                    // 同步地址信息。
                    //saveAddressList();
                }
            }

            @Override
            public void fail(Throwable throwable) {
                ToastUtils.currencyToast(throwable);
                getErrorMutableLiveData().postValue(throwable.getMessage());
            }
        });
    }

    /**
     * 请求登录
     * @param phone 电话号码
     * @param code 验证码
     */
    public void requestLogin(String phone, String code) {
        mLoginModel.postUserLogin(phone, code, new ResponseDataListener<UserToken>() {
            @Override
            public void success(UserToken data) {
                if (data != null) {
                    UserInfo userInfo = data.getUser();
                    userInfo.setToken(data.getToken());
                    LOGE("LoginViewModel","==>" + userInfo.toString());
                    // 保存起用户信息
                    mLoginModel.saveUserInfo(userInfo);
                    // 发送user信息给登录页面， 处理跳转逻辑
                    mUserInfoMutableLiveData.postValue(userInfo);
                    // 保存地址信息
                    //saveAddressList();
                }
            }

            @Override
            public void fail(Throwable throwable) {
                ToastUtils.currencyToast(throwable);
                getErrorMutableLiveData().postValue(throwable.getMessage());
            }
        });
    }

    private void saveAddressList() {
        mLoginModel.requestAddressCoddList(new ResponseDataListener<List<AddressBean>>() {
            @Override
            public void success(List<AddressBean> data) {
                if (!Utils.isCollectionEmpty(data)) {
                    mAddress.saveAddressList(data);
                }
            }

            @Override
            public void fail(Throwable throwable) {

            }
        });
    }

    public void startTime() {
        stopTime();
        isStart = true;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                count --;
                if (count != -1){
                    getTimeMutableLiveData().postValue(count);
                }
                if (count == 0) {
                    isStart = false;
                    stopTime();
                }
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);
    }



    public void stopTime() {
        count = countTime;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTime();
    }
}
