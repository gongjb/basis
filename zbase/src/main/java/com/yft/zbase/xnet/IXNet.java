package com.yft.zbase.xnet;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public interface IXNet {

    /*
      发起post请求，返回结果是一个实体
     */
    <T> IXNet easyPost(final String url, final Map<String, String> kv,final ResponseDataListener<T> responseDataListener,final Class<?> cls);

    /*
      返回一个字符串
     */
    <T extends String> IXNet easyPostAny(final String url, final Map<String, String> kv,final ResponseDataListener<T> responseDataListener);

    /**
     *  发起post请求，返回结果是一个array list
     * @param url
     * @param kv
     * @param responseDataListener
     * @param list
     * @param <T>
     * @return
     */
    <T> IXNet easyPost(final String url, final Map<String, String> kv, final ResponseDataListener<T> responseDataListener,final Type list);

    /*
      取消全部请求
     */
    IXNet cancelAllRequest();

    /**
     * 根据tag取消请求
     * @param tag
     * @return
     */
    IXNet cancelTagRequest(String tag);

    /**
     * 增加一个请求的tag, 如果有需要取消请求可根据对应的tag进行操作
     * @param url
     * @param kv
     * @param responseDataListener
     * @param cls
     * @param tag
     * @param <T>
     * @return
     */
    <T> IXNet easyPost(final String url, final Map<String, String> kv,final ResponseDataListener<T> responseDataListener,final Class<?> cls, String tag);

    /**
     * 增加一个请求的tag, 如果有需要取消请求可根据对应的tag进行操作
     * @param url
     * @param kv
     * @param responseDataListener
     * @param tag
     * @param <T>
     * @return
     */
    <T> IXNet easyPost(final String url, final Map<String, String> kv, final ResponseDataListener<T> responseDataListener,final Type list, String tag);

    /**
     * 下载文件
     * @param path
     * @param responseDataListener
     * @param <T>
     * @return
     */
    <T> IXNet downLoadFile(final String path,final ResponseDataListener<T> responseDataListener);

    /**
     * 上传文件
     * @param path
     * @param kv
     * @param files
     * @param listener
     * @param <T>
     * @return
     */
    <T> IXNet updateFile(final String path, final Map<String, String> kv, final File files, final ResponseDataListener listener);
}
