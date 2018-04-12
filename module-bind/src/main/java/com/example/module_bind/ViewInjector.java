package com.example.module_bind;

/**
 * Created by mac on 2018/4/12.
 */

public interface ViewInjector<T> {
    void inject(T t, Object source);
}
