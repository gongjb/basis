package com.hk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS) //注解生命周期是编译期，存活于.class文件，当jvm加载class时就不在了
@Target(ElementType.TYPE) //目标对象是类
public @interface BindPath {
    String value();
}