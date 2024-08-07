package com.yft.zbase.server;


import static com.yft.zbase.utils.Logger.LOGE;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;
import com.yft.zbase.utils.Constant;

import java.util.Locale;

public class LanguageServer implements ILanguage {
    private MMKV mLanguageMk;

    @Override
    public String getLanguageType() {
        if (mLanguageMk.containsKey(Constant.LANGUAGE_TYPE)) {
            LOGE("======app语言类型==>>>" + mLanguageMk.decodeString(Constant.LANGUAGE_TYPE, EN_TYPE));
            return mLanguageMk.decodeString(Constant.LANGUAGE_TYPE, EN_TYPE);
        }

        return EN_TYPE;
    }

    @Override
    public boolean saveLanguage(String type) {
        return mLanguageMk.encode(Constant.LANGUAGE_TYPE, type);
    }

    @Override
    public String getString(String key) {
        return LanguageManage.getString(key);
    }

    @Override
    public void initServer(Context context) {
        mLanguageMk = MMKV.mmkvWithID(Constant.LANGUAGE, MMKV.MULTI_PROCESS_MODE);
        if (!mLanguageMk.containsKey(Constant.LANGUAGE_TYPE)) {
            resetLanguage("======当前语言, 保存系统语言==>>>");
        }
    }

    @Override
    public <T extends IServerAgent> T getServer() {
        return (T) this;
    }

    @Override
    public String serverName() {
        return LANGUAGE_SERVER;
    }

    @Override
    public void cleanInfo() {
        if (mLanguageMk != null) {
            resetLanguage("======恢复成系统语言==>>>");
        }
    }

    private void resetLanguage(String x) {
        Locale locale = Locale.getDefault();
        // 获取语言代码，例如 "en", "zh" 等
        String language = locale.getLanguage();
        LOGE(x + language);
        if (TextUtils.isEmpty(language)) {
            saveLanguage(EN_TYPE);
        } else {
            if ("en".equals(language)) {
                saveLanguage(EN_TYPE);
            } else if ("zh".equals(language)) {
                saveLanguage(CN_TYPE);
            } else {
                saveLanguage(EN_TYPE);
            }
        }
    }
}
