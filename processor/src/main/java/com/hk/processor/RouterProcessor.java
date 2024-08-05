package com.hk.processor;

import com.google.auto.service.AutoService;
import com.hk.annotation.BindPath;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    /**
     * 生成文件的工具类
     */
    private Filer filer;
    /**
     * 打印信息
     */
    private Messager messager;
    /**
     * 元素相关
     */
    private Elements elementUtils;
    private Types typeUtils;

    private Map<String, ProxyInfo> proxyInfoMap = new HashMap<>();

    /**
     * 一些初始化操作，获取一些有用的系统工具类
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "==>init");
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }


    /**
     * 设置支持的版本
     *
     * @return 这里用最新的就好
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        messager.printMessage(Diagnostic.Kind.NOTE, "==>getSupportedSourceVersion");
        return SourceVersion.latestSupported();
    }


    /**
     * 设置支持的注解类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        messager.printMessage(Diagnostic.Kind.NOTE, "==>getSupportedAnnotationTypes");
        //添加支持的注解
        HashSet<String> set = new HashSet<>();
        set.add(BindPath.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "==>process");
        //1、获取要处理的注解的元素的集合
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindPath.class);

        //process()方法会调用3次，只有第一次有效，第2，3次调用的话生成.java文件会发生异常
        if (elements == null || elements.size() < 1) {
            return true;
        }

        //2、按类来划分注解元素，因为每个使用注解的类都会生成相应的代理类
        for (Element element : elements) {
            //获取注解的参数
            String resourceValue = element.getAnnotation(BindPath.class).value();
            VariableElement variableElement = (VariableElement) element;
            //获取该元素的父元素，这里是父类
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //获取全类名
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = typeElement.getQualifiedName().toString();
            String typeName = getClassName(typeElement, packageName);
            messager.printMessage(Diagnostic.Kind.NOTE, "=>>" + className);
            JavaFile javaFile = JavaFile.builder("com.hk.proxy", generateProxyClass("HKBind", typeName, resourceValue))
                    //在文件头部添加注释
                    .addFileComment("auto generateProxyClass code,can not modify")
                    .build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 通过javapoet API生成代理类
     * @return
     */
    public static TypeSpec generateProxyClass(String proxyClassName, String typeName, String resourceValue) {

        FieldSpec resourceField = FieldSpec.builder(String.class, typeName, Modifier.PUBLIC, Modifier.FINAL)
                .initializer("$S", resourceValue)
                .build();

        //创建类
        TypeSpec typeSpec = TypeSpec.classBuilder(proxyClassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(resourceField)
                .build();
        return typeSpec;
    }

    /**
     * 获取生成的代理类的类名
     * 之所以用字符串截取、替换而没用clas.getSimpleName()的原因是为了处理内部类注解的情况，比如adapter.ViewHolder
     * 内部类反射之后的类名：例如MyAdapter$ContentViewHolder，中间是$，而不是.
     *
     * @param type
     * @param packageName
     * @return
     */
    private String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }
}