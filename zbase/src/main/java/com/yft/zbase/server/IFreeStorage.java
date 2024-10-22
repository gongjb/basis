package com.yft.zbase.server;

import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public interface IFreeStorage extends IServerAgent {

    /**
     * 存对象
     * @param key
     * @param parcelable
     * @return
     * @param <T>
     */
    <T extends Parcelable> boolean saveParcelable(String key, T parcelable);

    /**
     * 取对象
     * @param key
     * @param cls
     * @return
     * @param <T>
     */
    <T extends Parcelable> T getParcelable(String key, Class<T> cls);

    /**
     * 保存字符串
     * @param key
     * @param val
     * @return
     */
    boolean saveString(String key, String val);

    /**
     * 获取字符串
     * @param key
     * @return
     */
    String getString(String key);


    /**
     * 存入集合
     * @param key
     * @param ls
     * @return
     * @param <T>
     */
    <T extends Parcelable> boolean saveList(String key, List<T> ls);

    /**
     * 获取集合
     * @param key
     * @param ls
     * @return
     * @param <T>
     */
    <T extends Parcelable> List<T> getList(String key, Type ls);
}
