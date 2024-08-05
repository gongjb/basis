package com.yft.zbase.xnet;

import static cn.sd.ld.ui.helper.Logger.LOGE;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.upload.UploadListener;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.error.WebServiceThrowable;
import com.yft.zbase.error.XNetSystemErrorCode;
import com.yft.zbase.utils.ToastUtils;
import com.yft.zbase.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

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
    public <T> IXNet easyPost(final String url, final Map<String, String> kv, final ResponseDataListener<T> responseDataListener,final Class<?> cls) {
        easyPost(url, kv, responseDataListener, cls, "");
        return this;
    }

    @Override
    public <T extends String> IXNet easyPostAny(final String url,final Map<String, String> kv,final ResponseDataListener<T> responseDataListener) {
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
        Set<?> set = kv.entrySet();
        Iterator<?> iterator = set.iterator();
        PostRequest<String> request = OkGo.<String>post(url).isMultipart(true);
       // LOGE("XNet", " 请求路径=>" + url);
        request.tag(url);
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
                if(responseDataListener != null) {
                    responseDataListener.fail(response.getException());
                }
            }
        });
        return this;
    }

    @Override
    public <T> IXNet easyPost(final String url, final Map<String, String> kv,final ResponseDataListener<T> responseDataListener,final Type list, final String tag) {
        Set<?> set = kv.entrySet();/// 返回此映射所包含的映射关系的 Set 视图。
        Iterator<?> iterator = set.iterator();
        PostRequest<String> request = OkGo.<String>post(url).isMultipart(true);
        request.tag(url);
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
                if(responseDataListener != null) {
                    responseDataListener.fail(response.getException());
                }
            }
        });
        return this;
    }

    @Override
    public <T> IXNet updateFile(final String path, final Map<String, String> kv, final File files, final ResponseDataListener listener) {
        if(OkUpload.getInstance().getTask(path) != null) {
            ToastUtils.toast("当前正在上传文件，请稍等！");
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
            ToastUtils.toast("当前正在下载，请稍等！");
            return this;
        }
        GetRequest<File> request = OkGo.<File>get(path).tag(path);
        OkDownload.request(path, request)//
                .folder(ZBaseApplication.get().getFilesDir().toString())
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

}
