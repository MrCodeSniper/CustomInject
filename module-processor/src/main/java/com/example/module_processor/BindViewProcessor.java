package com.example.module_processor;

import com.example.module_annotation.BindView;
import com.google.auto.service.AutoService;


import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//如果不使用后面两个注解，就需要重写 getSupportedAnnotationTypes() 和 getSupportedSourceVersion 方法：

@AutoService(Processor.class) //帮我们生成 META-INF 信息
@SupportedAnnotationTypes("com.example.module_annotation.BindView") //要处理的注解类型
@SupportedSourceVersion(SourceVersion.RELEASE_7)    //支持的源码版本
public class BindViewProcessor extends AbstractProcessor {
    private Elements mElementUtils; //基于元素进行操作的工具方法
    private Filer mFileCreator;     //代码创建者
    private Messager mMessager;     //日志，提示者，提示错误、警告

    private Map<String, ProxyInfo> mProxyMap = new HashMap<>();

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        //ProcessingEnvironment注解处理环境
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mFileCreator = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    /**
     * 检查 element 类型是否规范
     *
     * @param element
     * @param clazz
     * @return
     */
    private boolean checkAnnotationValid(final Element element, final Class<?> clazz) {
        if (element == null || element.getKind() != ElementKind.FIELD) {//必须是变量
            error(element, "%s must be declared on field !", clazz.getSimpleName());
            return false;
        }
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {//必须为public
            error(element, "%s must be public !", element.getSimpleName());
            return false;
        }
        return true;
    }


    private void error(final Element element, String msg, final Object... args) {
        if (args != null && args.length > 0) {
            msg = String.format(msg, args);
            mMessager.printMessage(Diagnostic.Kind.ERROR, msg, element);
        }
    }


    //处理注解信息 并生成相应的接口实现代理类
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //1.收集信息

        mMessager.printMessage(Diagnostic.Kind.NOTE, "process...");//打日志

        mProxyMap.clear();//避免生成重复的代理类

        //得到被bindview修饰的元素 得到一个不重复的集合
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //1.收集信息
        for (Element element : elements) {
            if (!checkAnnotationValid(element, BindView.class)) {    //去除不合格的元素
                continue;
            }

            //类中的成员变量
            VariableElement variableElement = (VariableElement) element;
            //类或者接口
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //完整的名称
            String qualifiedName = typeElement.getQualifiedName().toString();


            ProxyInfo proxyInfo = mProxyMap.get(qualifiedName);//根据名称 得到对应代理信息类
            if (proxyInfo == null) {//没找到就创建好 放入map
                //将该类或接口中被注解修饰的变量加入到 ProxyInfo 中
                proxyInfo = new ProxyInfo(mElementUtils, typeElement);
                mProxyMap.put(qualifiedName, proxyInfo);
            }

            //找到代理信息类 就拿到注解类的实例
            BindView annotation = variableElement.getAnnotation(BindView.class);
            if (annotation != null) {
                int id = annotation.value();//取到id
                proxyInfo.mInjectElements.put(id, variableElement);//将id和变量对应在map里
            }

        }

        //2.生成代理类


        for (String key : mProxyMap.keySet()) {//将mProxyMap的所有key遍历 即所有注解与value的绑定遍历
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                //创建文件对象
                JavaFileObject sourceFile = mFileCreator.createSourceFile(//写字节码
                        proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());//包名.类名  类信息
                Writer writer = sourceFile.openWriter();//打开写通道
                writer.write(proxyInfo.generateJavaCode());     //写入文件
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                error(proxyInfo.getTypeElement(), "Unable to write injector for type %s: %s", proxyInfo.getTypeElement(), e.getMessage());
            }
        }

        return true;

    }
}