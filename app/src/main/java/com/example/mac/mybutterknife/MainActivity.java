package com.example.mac.mybutterknife;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.module_annotation.Author;
import com.example.module_annotation.BindView;
import com.example.module_annotation.ContentView;
import com.example.module_bind.ViewBind;

@Author(date = "2018")
@ContentView(layoutId = R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindView(R.id.tv2)
    public TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewBind.bind(this);
        //实际调用了inject(MainActivity的所有信息,textview的id)
        textView.setText("xxxx");


        /**
         * 编译前的提示信息：
         * 注解可以被编译器用来发现错误，或者清除不必要的警告
         * @SuppressWarning：抑制错误
         * @Deprecated : 空注解，用于标记那些不应该被使用的代码 过期了
         * @Override ：空注解，用于标记那些覆盖父类方法的方法
           @Inherited ：当前注解是否可以继承
         * @SafeVarargs : 空注解，（varargs 可变参数）用于标记构造函数或者方法，通知编译器，这里的可变参数相关的操作保证安全
         */


        /**
         * ElementType.TYPE : 作用于任何类、接口、枚举
         ElementType.FIELD : 作用于一个域或者属性
         ElementType.METHOD : 作用于一个方法
         ElementType.PARAMTER : 作用于参数
         ElementType.CONSTRUCTOR : 作用于构造函数
         ElementType.LOCAL_VARIABLE : 作用于本地变量
         ElementType. ANNOTATION_TYPE : 作用于注解
         ElementType.PACKAGE : 作用于包

         */

    }
}
