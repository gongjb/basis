package com.yft.zbase.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtil {

    private static Gson gson = new Gson();
    /**
     * 把一个map变成json字符串
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     * @param json
     * @return
     */
    public static JsonObject parseJsonToJsonObj(String json) {
        try {
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     * @param json
     * @param cls
     * @return
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {

        }
        return t;
    }

    /**
     * 把一个json字符串变成对象
     * @param cls
     * @return
     */
    public static String parseBeanToJson(Object cls) {
        try {
            return gson.toJson(cls);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     *
     * @param json
     * @param jsonType  new TypeToken<BaseResultBean<ResultBean2>>() {}.getType()
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToBean1(String json,Type  jsonType){
        return new Gson().fromJson(json, jsonType);

    }

    /**
     * list转json
     * @param list
     * @param <T>
     * @return
     */
    public static<T> String listToJson(List<T> list){
        String s = null;
        try {
            s = gson.toJson(list);
        }catch (Exception e){

        }
        return s;
    }

    /**
     * 把json字符串变成map
     * @param json
     * @return
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type  new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static <T> List<T> parseJsonToList(String json, Type type) {
        List<T> list = gson.fromJson(json, type);
        return list;
    }

    /**
     *
     * 获取json串中某个字段的值，注意，只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 格式化json
     * @param uglyJSONString
     * @return
     */
    public static String jsonFormatter(String uglyJSONString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public static String getMapToString(Map<String, String> map) {
        try {
            Set<?> set = map.entrySet();
            Iterator<?> iterator = set.iterator();
            JSONObject jsonObject = new JSONObject();
            while (iterator.hasNext()) {
                Map.Entry<String,String> mapentry = (Map.Entry) iterator.next();
                jsonObject.put(mapentry.getKey(), mapentry.getValue());
            }
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}