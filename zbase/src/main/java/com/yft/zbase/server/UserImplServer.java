package com.yft.zbase.server;


import static com.yft.zbase.utils.Logger.LOGE;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;
import com.yft.zbase.bean.AuthenticationBean;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.utils.Constant;
import com.yft.zbase.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UserImplServer implements IUser {
    private MMKV mUserMk;
    private ServiceBean mServiceBean;
    private UserInfo mUserInfo;

    @Override
    public UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        if (mUserMk.containsKey(Constant.USER_INFO)) {
            mUserInfo = mUserMk.decodeParcelable(Constant.USER_INFO, UserInfo.class);
            return mUserInfo;
        }
        return null;
    }

    @Override
    public boolean isLogin() {
        return getUserInfo() != null;
    }

    @Override
    public boolean sveaUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        boolean isUser = mUserMk.encode(Constant.USER_INFO, userInfo);
        LOGE("用户信息保存 is suss=>" + isUser + "=" + userInfo.toString());
        return isUser;
    }

    @Override
    public ServiceBean getServiceUrl() {
        if (mServiceBean != null) {
            return mServiceBean;
        }
        if (mUserMk.containsKey(Constant.SERVICE_ADDRESS)) {
            mServiceBean = mUserMk.decodeParcelable(Constant.SERVICE_ADDRESS, ServiceBean.class);
            return mServiceBean;
        }
        return null;
    }

    @Override
    public boolean sveaServiceUrl(ServiceBean serviceBean) {
        this.mServiceBean = serviceBean;
        return mUserMk.encode(Constant.SERVICE_ADDRESS, serviceBean);
    }

    @Override
    public String getAppletURL() {
//        if (BuildConfig.DEBUG) {
//            return "http://175.178.180.81";
//        } else {
//            return "http://ms.jwruihe.com";
//        }
        if (getServiceUrl() != null && !TextUtils.isEmpty(getServiceUrl().getH5Server())) {
            // h5地址
            return getServiceUrl().getH5Server();
        }

        return "";
    }

    @Override
    public String getShareURL() {
        if (getServiceUrl() != null && !TextUtils.isEmpty(getServiceUrl().getShareServer())) {
            // h5地址
            return getServiceUrl().getShareServer();
        }
        return "";
    }

    @Override
    public String getPhoneFormat() {
        try {
            if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getPhone())) {
                String phone = mUserInfo.getPhone();
                //{"account":"63165303","amount":0.0,"amountComing":0.0,"hasPassword":false,"level":0,"nickname":"Reel Short","rechargeCount":0,"recommendCode":"63165303"}
                //{"account":"41011532","amount":476.0,"amountComing":1060.6,"hasPassword":true,"level":1,"nickname":"RS8104","phone":"13800138104","rechargeCount":0,"recommendCode":"41011532"}
                return Utils.phoneFormat(phone);
            }
        } catch (Exception e) {
            return mUserInfo != null ? mUserInfo.getPhone() : "";
        }
        return "";
    }

    @Override
    public boolean savePreloadOpen(TargetBean targetBean) {
        if (mUserMk == null) {
            return false;
        }

        if (targetBean == null) {
            // 清除
            mUserMk.remove(Constant.SERVICE_OPEN);
            return false;
        }

        return mUserMk.encode(Constant.SERVICE_OPEN, targetBean);
    }

    @Override
    public TargetBean getPreloadOpen() {
        if (mUserMk == null) {
            return null;
        }

        if (!mUserMk.containsKey(Constant.SERVICE_OPEN)) {
            return null;
        }

        // 获取信息
        return mUserMk.decodeParcelable(Constant.SERVICE_OPEN, TargetBean.class);
    }

    /**
     * 保存实名信息
     *
     * @param authenticationBean
     * @return
     */
    @Override
    public boolean saveAuthentication(AuthenticationBean authenticationBean) {
        if (mUserMk == null) {
            return false;
        }

        if (authenticationBean == null) {
            // 清除
            mUserMk.remove(Constant.AUTHENTICATION);
            return false;
        }

        return mUserMk.encode(Constant.AUTHENTICATION, authenticationBean);
    }

    @Override
    public AuthenticationBean getAuthentication() {
        if (mUserMk == null) {
            return null;
        }

        if (!mUserMk.containsKey(Constant.AUTHENTICATION)) {
            return null;
        }
        // 获取信息
        return mUserMk.decodeParcelable(Constant.AUTHENTICATION, AuthenticationBean.class);
    }

    @Override
    public String[] getSearchHistory() {
        if (mUserMk == null) {
            return null;
        }

        if (!mUserMk.containsKey(Constant.SEARCH_KEY)) {
            return null;
        }

        String jsonArray = mUserMk.decodeString(Constant.SEARCH_KEY, "");
        if (!TextUtils.isEmpty(jsonArray)) {
            try {
                JSONArray arrayObj = new JSONArray(jsonArray);
                int size = arrayObj.length();
                List<String> keys = new ArrayList<>();
                for (int i =0 ;i < size; i ++) {
                    keys.add(arrayObj.optString(i));
                }

                if (Utils.isCollectionEmpty(keys)) {
                    return null;
                }
                return keys.toArray(new String[keys.size()]);
            } catch (JSONException e) {
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    @Override
    public boolean saveSearchKey(String key) {
        if (mUserMk == null) {
            return false;
        }

        if (TextUtils.isEmpty(key)) {
            // 清除
            mUserMk.remove(Constant.SEARCH_KEY);
            return false;
        }
        String[] array = getSearchHistory();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(key);

        if (array != null) {
            for (int i = 0; i < array.length; i ++) {
                if (i <= 8 && !key.equals(array[i])) {
                    jsonArray.put(array[i]);
                }
            }
        }

        return mUserMk.encode(Constant.SEARCH_KEY, jsonArray.toString());
    }

    @Override
    public boolean removeSearchHistoryKey(String key) {
        if (mUserMk == null) {
            return false;
        }

        if (TextUtils.isEmpty(key)) {
            return false;
        }

        String[] array = getSearchHistory();
        JSONArray jsonArray = new JSONArray();

        if (array != null) {
            for (int i = 0; i < array.length; i ++) {
                if (!key.equals(array[i])) {
                    jsonArray.put(array[i]);
                }
            }
        }

        return mUserMk.encode(Constant.SEARCH_KEY, jsonArray.toString());
    }

    @Override
    public void setPrivacy(boolean isPrivacy) {
        mUserMk.encode(Constant.PRIVACY, isPrivacy);
    }

    @Override
    public boolean isPrivacy() {
        if (!mUserMk.containsKey(Constant.PRIVACY)) {
            return false;
        }
        return mUserMk.decodeBool(Constant.PRIVACY, false);
    }

    @Override
    public void setCameraStoragePermissions(boolean isPermissions) {
        // STORAGE_PERMISSIONS
        mUserMk.encode(Constant.CAMERA_STORAGE_PERMISSIONS, isPermissions);
    }

    @Override
    public boolean isCameraStoragePermissions() {
        if (!mUserMk.containsKey(Constant.CAMERA_STORAGE_PERMISSIONS)) {
            return false;
        }
        return mUserMk.decodeBool(Constant.CAMERA_STORAGE_PERMISSIONS, false);
    }

    @Override
    public void setCameraPermissions(boolean isCameraPermissions) {
        mUserMk.encode(Constant.CAMERA_PERMISSIONS, isCameraPermissions);
    }

    @Override
    public boolean isCameraPermissions() {
        if (!mUserMk.containsKey(Constant.CAMERA_PERMISSIONS)) {
            return false;
        }
        return mUserMk.decodeBool(Constant.CAMERA_PERMISSIONS, false);
    }

    @Override
    public void setStoragePermissions(boolean isCameraPermissions) {
        mUserMk.encode(Constant.STORAGE_PERMISSIONS, isCameraPermissions);
    }

    @Override
    public boolean isStoragePermissions() {
        if (!mUserMk.containsKey(Constant.STORAGE_PERMISSIONS)) {
            return false;
        }
        return mUserMk.decodeBool(Constant.STORAGE_PERMISSIONS, false);
    }

    @Override
    public String getFlavor() {
        if (!mUserMk.containsKey(Constant.FLAVOR_KEY)) {
            return Versions.OFFICIAL_VERSION_NAME.getName();
        }
        return mUserMk.decodeString(Constant.FLAVOR_KEY);
    }

    @Override
    public boolean saveFlavor(String flavor) {
        if (TextUtils.isEmpty(flavor)) {
            return false;
        }
        return mUserMk.encode(Constant.FLAVOR_KEY, flavor);
    }

    @Override
    public UserLevelType getUserLevel() {
        if (mUserInfo == null) {
            return UserLevelType.LEVE0;
        }
        return Utils.getUserLevel(mUserInfo.getLevel());
    }

    public String getBastServiceURL() {
        return mServiceBean == null ? "" : mServiceBean.getAssignUri();
    }

    @Override
    public void cleanUserInfo() {
        if (mUserMk != null) {
            mUserInfo = null;
            mUserMk.remove(Constant.USER_INFO);
        }
    }

    @Override
    public void initServer(Context context) {
        mUserMk = MMKV.mmkvWithID(Constant.USER, MMKV.MULTI_PROCESS_MODE);
        if (mServiceBean == null) {
            // 在deeplink 跳转时由于未经过欢迎界面和主页面，导致网络异常请求， 所以必须优先拿出历史serviceBean
            mServiceBean = getServiceUrl();
        }
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return USER_SERVER;
    }

    @Override
    public void cleanInfo() {
        mUserInfo = null;
        mServiceBean = null;
        mUserMk.clear();
    }
}
