package com.yft.zbase.server;

public class LanguageManage {
   private static ILanguage iLanguage = DynamicMarketManage.getInstance().getServer(IServerAgent.LANGUAGE_SERVER);

    /**
     * 提供给XML中调用
     * @param key
     * @return
     */
    public static String getString(String key) {
        String languageType = iLanguage.getLanguageType();
        for (LanguageTo languageTo : LanguageTo.values()) {
            if (languageType.equals(ILanguage.CN_TYPE)) {
                if (key.equals(languageTo.getCn())) {
                    return languageTo.getCn(); // 如果当前语言是中文，并且key匹配中文，则直接返回中文
                }
            } else {
                if (key.equals(languageTo.getEn())) {
                    return languageTo.getEn(); // 如果当前语言非中文，并且key匹配英文，则直接返回英文
                }
            }
            // 可以在这里添加一个检查，如果找到了匹配的中文或英文，但语言类型不匹配，则直接返回对应的另一种语言的字符串
            if (languageType.equals(ILanguage.CN_TYPE) && key.equals(languageTo.getEn())) {
                return languageTo.getCn();
            } else if (!languageType.equals(ILanguage.CN_TYPE) && key.equals(languageTo.getCn())) {
                return languageTo.getEn();
            }
        }
        return key;
    }

    public static String getSysytemString(String key) {
        String languageType = iLanguage.getLanguageType();
        for (LanguageSystemTo languageTo : LanguageSystemTo.values()) {
            if (languageType.equals(ILanguage.CN_TYPE)) {
                if (key.equals(languageTo.getCn())) {
                    return languageTo.getCn(); // 如果当前语言是中文，并且key匹配中文，则直接返回中文
                }
            } else {
                if (key.equals(languageTo.getEn())) {
                    return languageTo.getEn(); // 如果当前语言非中文，并且key匹配英文，则直接返回英文
                }
            }
            // 可以在这里添加一个检查，如果找到了匹配的中文或英文，但语言类型不匹配，则直接返回对应的另一种语言的字符串
            if (languageType.equals(ILanguage.CN_TYPE) && key.equals(languageTo.getEn())) {
                return languageTo.getCn();
            } else if (!languageType.equals(ILanguage.CN_TYPE) && key.equals(languageTo.getCn())) {
                return languageTo.getEn();
            }
        }
        return key;
    }

    public static String getStringIn(String inStr, String actual) {
        String k = getString(inStr);
        return String.format(k, actual);
    }

    public static String getStringIn2Int(String inStr, int actual1, int actual2) {
        String k = getString(inStr);
        return String.format(k, actual1, actual2);
    }

    public static String getLanguageType() {
        return iLanguage.getLanguageType();
    }
}
