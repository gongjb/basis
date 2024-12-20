package com.hk.xnet;


import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.upload.UploadListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class XNetImpl implements IXNet {

    /**
     * 私有实例 volatile
     */
    private volatile static XNetImpl instance;


    /**
     * 私有构造方法
     */
    private XNetImpl() {
    }

    /**
     * 唯一公开获取实例的方法（静态工厂方法）
     *
     * @return
     */
    public static XNetImpl getInstance() {
        if (instance == null) {
            // 加锁
            synchronized (XNetImpl.class) {
                if (instance == null) {
                    instance = new XNetImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void initHttp(Application application, boolean isDebug) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setPrintLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);        //log打印级别，决定了log显示的详细程度
        //loggingInterceptor.setColorLevel(Level.INFO);
        loggingInterceptor.setColorLevel(isDebug ? Level.INFO : Level.OFF);

        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();

        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(application)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        //.addCommonHeaders(headers)                      //全局公共头
        //.addCommonParams(params);                       //全局公共参数
    }

    /**
     * 抛出去进行初始化
     * @param application
     * @param isDebug
     * @param builder
     * @param cacheMode
     * @param cacheEntity
     */
    @Override
    public void initHttp(Application application, boolean isDebug, OkHttpClient.Builder builder, CacheMode cacheMode, long cacheEntity) {
        if (builder == null) {
            // 配置
            initHttp(application, isDebug);
            return;
        }

        OkGo.getInstance().init(application)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(cacheMode)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(cacheEntity)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    @Override
    public <T> IXNet easyPost(final String url, final Map<String, String> kv, final ResponseDataListener<T> responseDataListener,final Class<?> cls) {
        easyPost(url, kv, responseDataListener, cls, "");
        return this;
    }


    @Override
    public <T extends String> IXNet easyPostAny(final String url,final Map<String, String> kv,final ResponseDataListener<T> responseDataListener) {
        setTimeoutPeriod(responseDataListener);
        //LOGE("XNet", "h5请求路径=>" + url);
        Set<?> set = kv.entrySet();/// 返回此映射所包含的映射关系的 Set 视图。
        Iterator<?> iterator = set.iterator();
        PostRequest<String> request = OkGo.<String>post(url).isMultipart(true);
        request.tag(this);
        while (iterator.hasNext()) {
            Map.Entry<String,String> mapentry = (Map.Entry) iterator.next();
            request.params((String) mapentry.getKey(), (String) mapentry.getValue());        //
        }

        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
               // LOGE("XNet", "h5请求结果=>" + ((T) response.body()));
                responseDataListener.success((T) response.body());
            }

            @Override
            public void onError(Response<String> response) {
                if(responseDataListener != null) {
                    responseDataListener.fail(response.getException());
                }
            }
        });
        return this;
    }

    @Override
    public <T> IXNet easyPost(final String url, final Map<String, String> kv, final ResponseDataListener<T> responseDataListener, final Type list) {
        easyPost(url, kv, responseDataListener, list, "");
        return this;
    }

    @Override
    public IXNet cancelAllRequest() {
        // 取消全部网络请求。
        OkGo.getInstance().cancelAll();
        return this;
    }

    @Override
    public IXNet cancelTagRequest(String tag) {
        OkGo.getInstance().cancelTag(tag);
        return this;
    }

    @Override
    public <T> IXNet easyPost(final String url,final Map<String, String> kv,final ResponseDataListener<T> responseDataListener,final Class<?> cls, String tag) {
        setTimeoutPeriod(responseDataListener);
        Set<?> set = kv.entrySet();
        Iterator<?> iterator = set.iterator();
        PostRequest<String> request = OkGo.<String>post(url).isMultipart(true);
       // LOGE("XNet", " 请求路径=>" + url);
        if (TextUtils.isEmpty(tag)) {
            request.tag(url);
        } else {
            request.tag(tag);
        }

        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            request.params((String) mapentry.getKey(), (String) mapentry.getValue());        //
        }

        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                responseJson(responseDataListener, response.body(), cls);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if(responseDataListener != null) {
                    responseDataListener.fail(response.getException());
                }
            }
        });
        return this;
    }

    @Override
    public <T> IXNet easyPost(final String url, final Map<String, String> kv,final ResponseDataListener<T> responseDataListener,final Type list, final String tag) {
        setTimeoutPeriod(responseDataListener);
        Set<?> set = kv.entrySet();/// 返回此映射所包含的映射关系的 Set 视图。
        Iterator<?> iterator = set.iterator();
        PostRequest<String> request = OkGo.<String>post(url).isMultipart(true);
        if (TextUtils.isEmpty(tag)) {
            request.tag(url);
        } else {
            request.tag(tag);
        }
        //LOGE("XNet", " 请求路径=>" + url);
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            request.params((String) mapentry.getKey(), (String) mapentry.getValue());        //
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                responseJson(responseDataListener, response.body(), list);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if(responseDataListener != null) {
                    responseDataListener.fail(response.getException());
                }
            }
        });
        return this;
    }

    @Override
    public <T> IXNet easyGet(final String url,final ResponseDataListener<T> responseDataListener,final Class<?> cls) {
        setTimeoutPeriod(responseDataListener);
        GetRequest<String> getRequest = OkGo.<String>get(url);//
        getRequest.tag(url);//
        getRequest.converter(new StringConvert())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        responseJson(responseDataListener, response.body(), cls);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        responseDataListener.fail(response.getException());
                    }
                });
        return this;
    }

    @Override
    public <T> IXNet easyGet(final String url,final ResponseDataListener<T> responseDataListener,final Type list) {
        setTimeoutPeriod(responseDataListener);
        GetRequest<String> getRequest = OkGo.<String>get(url);//
        getRequest.tag(url);//
        getRequest.converter(new StringConvert())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        responseJson(responseDataListener, response.body(), list);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        responseDataListener.fail(response.getException());
                    }
                });
        return this;
    }





    @Override
    public <T> IXNet updateFile(final String path, final Map<String, String> kv, final File files, final ResponseDataListener listener) {
        if(OkUpload.getInstance().getTask(path) != null) {
            return this;
        }
        PostRequest<String> postRequest = OkGo.<String>post(path);//
        Set<?> set = kv.entrySet();/// 返回此映射所包含的映射关系的 Set 视图。
        Iterator<?> iterator = set.iterator();
        // LOGE("XNetImpl", " 请求路径=>" + path);
        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            postRequest.params((String) mapentry.getKey(), (String) mapentry.getValue());        //
        }
        postRequest.params("file", files);
        postRequest .converter(new StringConvert());
        OkUpload.request(files.getAbsolutePath(), postRequest)//
                .extra1(path)//
                .save()
                .register(new DesUpLoadListener(listener))
                .start();
        return this;
    }


    public IXNet downLoadFile(final String path, final ResponseDataListener listener) {
        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
        if(OkDownload.getInstance().getTask(path) != null) {
            return this;
        }
        GetRequest<File> request = OkGo.<File>get(path).tag(path);
        OkDownload.request(path, request)//
                //.folder(ZBaseApplication.get().getFilesDir().toString())
                .save()//
                .register(new DesListener(listener, path))
                .start();
        return this;
    }


    private class DesUpLoadListener extends UploadListener{
        private ResponseDataListener listener;
        public DesUpLoadListener(ResponseDataListener tag) {
            super(tag);
            this.listener = tag;
        }

        @Override
        public void onStart(Progress progress) {

        }

        @Override
        public void onProgress(Progress progress) {
            this.listener.upProgress(progress.totalSize, progress.totalSize, progress.currentSize / progress.totalSize, 0);
        }

        @Override
        public void onError(Progress progress) {
            this.listener.fail(progress.exception);
        }

        @Override
        public void onFinish(Object o, Progress progress) {
            //LOGE("XNetImpl", "==>" + o.toString());
            this.listener.success(o.toString());
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }


    private class DesListener extends DownloadListener {
        private ResponseDataListener listener;
        private String task;
        DesListener(ResponseDataListener listener, String taskPath) {
            super(listener);
            this.listener = listener;
            this.task = taskPath;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            try {
                this.listener.upProgress(progress.currentSize, progress.totalSize, 0, 0);
            } catch (Exception e) {
            }
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //LOGE("下载完成=》" + file.getAbsolutePath());
            this.listener.success(file.getAbsolutePath());
            if (OkDownload.getInstance().getTask(this.task) != null) {
                // 移除掉
                OkDownload.getInstance().removeTask(this.task);
            }
        }

        @Override
        public void onRemove(Progress progress) {
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
            listener.fail(throwable);

            if (OkDownload.getInstance().getTask(this.task) != null) {
                // 移除掉
                OkDownload.getInstance().removeTask(this.task);
            }
        }
    }



    /**
     * 解析请求过来的数据
     *
     * @param listener
     * @param responseJson
     */
    private void responseJson(final ResponseDataListener listener, final String responseJson, final Class<?> cls) {
        try {
            //LOGE("XNet", " 请求结果=>" + responseJson);
            JSONObject jsonObject = new JSONObject(responseJson);
            String code = jsonObject.optString(listener.getTemplate().getCode());
            if (TextUtils.isEmpty(code) || !code.equals(listener.getTemplate().successCode())) {
                XNetSystemErrorCode xNetSystemErrorCode = isSystemErrorCode(code);
                if (xNetSystemErrorCode != null) {
                    // 处理公共的错误码。
                    listener.specialError(xNetSystemErrorCode);
                    return;
                }
                listener.fail(new WebServiceThrowable().setErrorCode(code).setErrorMsg(jsonObject.optString(listener.getTemplate().getMessage())));
                return;
            }

            if (listener != null) {
                String dataJson = jsonObject.optString(listener.getTemplate().getBody());
                Gson gson = new Gson();
                listener.success(gson.fromJson(dataJson, cls));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.fail(e);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            listener.fail(e);
        } catch (Exception e) {
            e.printStackTrace();
            listener.fail(e);
        }
    }

    /**
     * 解析请求过来的数据
     *
     * @param listener
     * @param responseJson
     */
    private void responseJson(final ResponseDataListener listener,final String responseJson,final Type type) {
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            String code = jsonObject.optString(listener.getTemplate().getCode());
            if (TextUtils.isEmpty(code) || !code.equals(listener.getTemplate().successCode())) {
                XNetSystemErrorCode xNetSystemErrorCode = isSystemErrorCode(code);
                if (xNetSystemErrorCode != null) {
                    // 处理公共的错误码。
                    listener.specialError(xNetSystemErrorCode);
                    return;
                }
                listener.fail(new WebServiceThrowable().setErrorCode(code).setErrorMsg(jsonObject.optString(listener.getTemplate().getMessage())));
                return;
            }

            if (listener != null) {
                String dataJson = jsonObject.optString(listener.getTemplate().getBody());
                Gson gson = new Gson();
                listener.success(gson.fromJson(dataJson, type));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.fail(e);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            listener.fail(e);
        } catch (Exception e) {
            e.printStackTrace();
            listener.fail(e);
        }
    }

    /**
     * 是否出现系统级别的错误
     * @param code 错误码
     * @return 是否是系统级别错误吗
     */
    public XNetSystemErrorCode isSystemErrorCode(final String code) {
        if (TextUtils.isEmpty(code)) {
            return null;
        }
        for (XNetSystemErrorCode errorCode : XNetSystemErrorCode.values()) {
            if(errorCode.isSystemErrorCode(code)) {
                return errorCode;
            }
        }
        return null;
    }

    /**
     * 设置新的超时时间
     * @param responseDataListener
     */
    public void setTimeoutPeriod(ResponseDataListener responseDataListener) {
        // 获取OKGo实例
        OkHttpClient okHttpClient = OkGo.getInstance().getOkHttpClient();
        // 创建OkHttpClient的新实例，并设置超时时间
        OkHttpClient newOkHttpClient = okHttpClient.newBuilder()
                .connectTimeout(responseDataListener.getTimeoutPeriod()[0], TimeUnit.MILLISECONDS)       // 设置连接超时时间为10秒
                .readTimeout(responseDataListener.getTimeoutPeriod()[1], TimeUnit.SECONDS)          // 设置读取超时时间为30秒
                .writeTimeout(responseDataListener.getTimeoutPeriod()[2], TimeUnit.SECONDS)         // 设置写入超时时间为30秒
                .build();
        // 将新的OkHttpClient设置到OKGo实例中
        OkGo.getInstance().setOkHttpClient(newOkHttpClient);
    }
}
