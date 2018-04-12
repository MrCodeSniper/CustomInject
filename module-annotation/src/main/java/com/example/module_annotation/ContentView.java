package com.example.module_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mac on 2018/4/12.
 */

@Target(ElementType.TYPE)//包括接口类和枚举
@Retention(RetentionPolicy.RUNTIME)//RetentionPolicy.RUNTIME : 最高级，运行时会被保留，可以被运行时访问
public @interface ContentView {
    int layoutId();
}
