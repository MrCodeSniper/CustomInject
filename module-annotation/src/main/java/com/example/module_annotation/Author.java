package com.example.module_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RetentionPolicy.CLASS 编译时被编译器保留，但是运行时会被 JVM 忽略
 * Created by mac on 2018/4/12.
 * 编译时生成代码：一些处理器可以在编译时根据注解信息生成代码，比如 Java 代码，xml 代码等
 */

@Target(ElementType.TYPE)//包括接口类和枚举
@Retention(RetentionPolicy.RUNTIME) //源码级别注解 注解只保留在源码中，编译时会忽略
public @interface Author {
    String name() default "chenhong";
    String date();
}
