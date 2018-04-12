package com.example.mac.mybutterknife;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.example.module_annotation.Author;
import com.example.module_annotation.ContentView;

/**
 * Created by mac on 2018/4/12.
 */

public class BaseActivity extends Activity {


    //运行时处理注解


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationProcess();
    }

    private void annotationProcess() {//注意：需要运行时注解设置
        //运行时通过反射将注解的value放入对应的地方
        Class c = this.getClass();
        //遍历所有子类
        for (; c != Context.class; c = c.getSuperclass()) {
            //找到使用 ContentView 注解的类
            ContentView annotation_contentview = (ContentView) c.getAnnotation(ContentView.class);
            Author annotation_author=(Author) c.getAnnotation(Author.class);

            if (annotation_contentview != null&& annotation_author!=null) {
                Log.d("xxx","xxx");
                try {
                    //有可能出错的地方都要 try-catch
                    //获取 注解中的属性值，为 Activity 设置布局
                    this.setContentView(annotation_contentview.layoutId());
                    TextView tv=this.findViewById(R.id.tv);
                    tv.setText(annotation_author.name());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                return;
            }


        }


    }

}
