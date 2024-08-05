package com.yft.zbase.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseFind {

    public static <T> Class<T> getGenericType(Class<?> srcClass, Class<?> superClass) {
        Type generic = srcClass.getGenericSuperclass();
        Class<T> targetClass = null;
        if(generic instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) generic;
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type type : types) {
                Class genericC = (Class) type;
                if(superClass.isAssignableFrom(genericC)){
                    targetClass = genericC;
                    break;
                }
            }
        }
        return targetClass;
    }
}
