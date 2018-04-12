package com.example.module_processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by mac on 2018/4/12.
 */

public class ProxyInfo {

    private static final String SUFFIX = "ViewInjector";
    public Map<Integer, VariableElement> mInjectElements = new HashMap<>();    //变量列表
    private TypeElement mTypeElement;    //类信息
    private String mPackageName;    //包名
    private String mProxyClassName;    //代理类名

    public ProxyInfo(final Elements elementUtils, final TypeElement typeElement) {
        mTypeElement = typeElement;
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        mPackageName = packageElement.getQualifiedName().toString();
        String className = getClassName(typeElement, mPackageName);
        mProxyClassName = className + "$$" + SUFFIX;
        System.out.println("****** " + mProxyClassName + " \n" + mPackageName);
    }

    private String getClassName(final TypeElement typeElement, final String packageName) {
        int packageLength = packageName.length() + 1;   //
        return typeElement.getQualifiedName().toString().substring(packageLength).replace('.', '$');
    }




    public String generateJavaCode() {
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder 中不要再使用 + 拼接字符串
        stringBuilder.append("// Generate code. Do not modify it !\n")
                .append("package ").append(mPackageName).append(";\n\n")
                .append("import com.example.module_bind.*;\n\n")
                //mTypeElement.getQualifiedName()完整类名
                .append("public class ").append(mProxyClassName).append(" implements ").append(SUFFIX).append("<").append(mTypeElement.getQualifiedName()).append(">").append("{\n");
        generateMethod(stringBuilder);
        stringBuilder.append("\n}\n");
        return stringBuilder.toString();
    }


    private void generateMethod(final StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return;
        }
        stringBuilder.append("@Override\n")
                .append("public void inject(").append(mTypeElement.getQualifiedName()).append(" host, Object object )").append("{\n");

        for (Integer id : mInjectElements.keySet()) {// mInjectElements中有 ID与变量信息的绑定
            VariableElement variableElement = mInjectElements.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            stringBuilder.append("if(object instanceof android.app.Activity)").append("{\n")
                    .append("host.").append(name).append(" = ")
                    .append("(").append(type).append(")((android.app.Activity)object).findViewById(").append(id).append(");")
                    .append("\n}\n")
                    .append("else").append("{\n")
                    .append("host.").append(name).append(" = ")
                    .append("(").append(type).append(")((android.view.View)object).findViewById(").append(id).append(");")
                    .append("\n}\n");
        }
        stringBuilder.append("\n}\n");
    }


    public String getProxyClassFullName() {
        return mPackageName + "." + mProxyClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }
}
