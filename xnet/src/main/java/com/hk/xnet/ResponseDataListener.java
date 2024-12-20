package com.hk.xnet;


public interface ResponseDataListener<T> {
    // 连接超时时长
    long CONNECTION_TIME = 5000l;
    // 读取超时时长
    long READ_TIME = 5000l;
    // 写入超时时长
    long WRITE_TIME = 5000l;

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

    /**
     * timeoutPeriod 超时时长设置
     * @return
     */
    default long[] getTimeoutPeriod() {
        return new long[]{CONNECTION_TIME,READ_TIME,WRITE_TIME};
    }
}
