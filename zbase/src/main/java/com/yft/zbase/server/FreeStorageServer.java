package com.yft.zbase.server;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.yft.zbase.bean.AddressBean;
import com.yft.zbase.utils.Constant;
import com.yft.zbase.utils.JsonUtil;
import com.yft.zbase.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;

public class FreeStorageServer implements IFreeStorage {
    private MMKV mStorageMk;

    @Override
    public void initServer(Context context) {
        mStorageMk = MMKV.mmkvWithID(Constant.FREE_STORAGE, MMKV.MULTI_PROCESS_MODE);
    }

    /**
     * 存对象
     *
     * @param key
     * @param parcelable
     * @param <T>
     * @return
     */
    public <T extends Parcelable> boolean saveParcelable(String key, T parcelable) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        if (parcelable == null) {
            return false;
        }
        return mStorageMk.encode(key, parcelable);
    }

    /**
     * 获取序列化对象
     * @param key
     * @param cls
     * @return
     * @param <T>
     */
    public <T extends Parcelable> T getParcelable(String key, Class<T> cls) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        if (mStorageMk.containsKey(key)) {
            return mStorageMk.decodeParcelable(key, cls);
        }
        return null;
    }

    /**
     * 存字符串
     * @param key
     * @param val
     * @return
     */
    public boolean saveString(String key, String val) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        if (TextUtils.isEmpty(val)) {
            return false;
        }
        return mStorageMk.encode(key, val);
    }

    /**
     * 获取存入的字符串
     * @param key
     * @return
     */
    public String getString(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return mStorageMk.decodeString(key, "");
    }


    /**
     * 存入集合
     * @param key
     * @param ls
     * @return
     * @param <T>
     */
    public <T extends Parcelable> boolean saveList(String key, List<T> ls) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        return mStorageMk.encode(key, JsonUtil.listToJson(ls));
    }


    /**
     * 获取集合
     * @param key
     * @param c
     * @return
     * @param <T>
     */
    public <T extends Parcelable> List<T> getList(String key,  Type c) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        if (mStorageMk.containsKey(key)) {
            String listJson = mStorageMk.decodeString(key, "");
            if (!TextUtils.isEmpty(listJson)) {
                //new TypeToken<List<T>>() {}.getType()
                return JsonUtil.parseJsonToList(listJson, c);
            }
        }

        return null;
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return IServerAgent.FREE_STORAGE;
    }

    @Override
    public void cleanInfo() {
        if (mStorageMk != null) {
            mStorageMk.clear();
            mStorageMk = null;
        }
    }
}
