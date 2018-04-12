package com.example.module_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mac on 2018/4/12.
 */
@Target(ElementType.FIELD)//作用域为 变量
@Retention(RetentionPolicy.CLASS)//编译时生成代码
public @interface BindView {
    int value();
}
