package com.hss01248.startup.compile;

import com.google.auto.service.AutoService;
import com.hss01248.startup.annotation.AppStartUpItem;
import com.squareup.javapoet.JavaFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 14:15
 * @Version 1.0
 */
@AutoService(Processor.class)
public class StartUpProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AppStartUpItem.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println("-----process---->AppStartUpItem:  " + annotations+",roundEnvironment:"+roundEnvironment);
        if(annotations == null || annotations.isEmpty()){
            return false;
        }
        //搜集到AppStartUpCallback的所有子类,然后添加到list, 然后调用
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AppStartUpItem.class);
        List<String> classNames = new ArrayList<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            Name name = typeElement.getQualifiedName();
            //System.out.println("--------->AppStartUpItem:  "+ name);
            System.out.println("--------->AppStartUpItem:  " + typeElement);
            classNames.add(name.toString());
        }
        //或者把字符串写到文件中?


        String className = getName();
        JavaFile javaFile = JavaFile
                .builder("com.hss01248.appstartup.api", ClassCodeGen.generateJavaCode(classNames,className))
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }




    //不行,这个库执行时,并不知道另外一个库是否有生成某个类
    private String getName() {
        String name = "AppStartUpItems";
        int i = 0;
        while (true){
            try {
                name = "AppStartUpItems"+i;
                Class clazz = Class.forName("com.hss01248.appstartup.api."+name);
                i++;
            } catch (ClassNotFoundException e) {
                return name;
            }
        }
    }


}
