package com.yft.zbase.xnet;

import com.hk.xnet.XNetSystemErrorCode;
import com.yft.zbase.error.ErrorCode;

public abstract class ResponseDataListener<T> implements com.hk.xnet.ResponseDataListener<T> {
    /**
     * 特殊的错误码处理
     */
    @Override
    public void specialError(XNetSystemErrorCode code) {
        ErrorCode.errorStartActivity(code);
    }

    @Override
    public void aPublicFail(Throwable throwable) {
        // 公共异常处理
    }
}
