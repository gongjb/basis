package com.hk.xnet;


public interface ResponseDataListener<T> {
    void success(T data);

    void fail(Throwable throwable);

    /**
     * 大部分接口不需要用到监听进度的方法。 下载文件需要用到
     *
     * @param currentSize currentSize
     * @param totalSize totalSize
     * @param progress progress
     * @param networkSpeed networkSpeed
     */
    default void upProgress(long currentSize, long totalSize, float progress, long networkSpeed){}

    /**
     * 特殊的错误码处理
     */
    void specialError(XNetSystemErrorCode code);

    /**
     * 当前数据处理模板
     * @return
     */
    default Template getTemplate() {
        // 当你请求模板变化时请重写该方法...
        return new Template();
    }
}
