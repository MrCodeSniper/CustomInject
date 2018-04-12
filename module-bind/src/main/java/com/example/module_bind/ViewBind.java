package com.example.module_bind;

import android.app.Activity;

/**
 * Created by mac on 2018/4/12.
 */

public class ViewBind {


    private static final String SUFFIX = "$$ViewInjector";


    public static void bind(Activity activity){
        bind(activity, activity);
    }



    /**
     * 1.寻找对应的代理类
     * 2.调用接口提供的绑定方法
     *
     * @param host
     * @param root
     */
    @SuppressWarnings("unchecked")
    private static void bind(final Object host, final Object root) {
        if (host == null || root == null) {
            return;
        }

        Class<?> aClass = host.getClass();//activity的字节码

        String proxyClassFullName = aClass.getName() + SUFFIX;    //拼接生成类的名称

        try {
            Class<?> proxyClass = Class.forName(proxyClassFullName);//从编译后的字节码找类
            ViewInjector viewInjector = (ViewInjector) proxyClass.newInstance();
            if (viewInjector != null) {
                viewInjector.inject(host, root);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }






}
