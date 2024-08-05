package com.yft.zbase.server;

public interface ILanguage extends IServerAgent{
    String CN_TYPE = "cn";
    String EN_TYPE = "en";

    /**
     * 获取当前的语言环境
     * @return {@link com.yft.zbase.server.ILanguage#CN_TYPE}
     */
    String getLanguageType();

    /**
     * 保存语言类型
     * @param type
     * @return
     */
    boolean saveLanguage(String type);

    /**
     * 根据语言类型返回字符串
     * @param key 字符串对应的key
     * @return
     */
    String getString(String key);
}
