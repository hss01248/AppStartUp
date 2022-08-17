package com.hss01248.classnametoassets.base.compiler;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @Despciption todo
 * @Author hss
 * @Date 09/08/2022 10:20
 * @Version 1.0
 */
public abstract class AbstractWriteClassNamesToAssetsProcessor extends AbstractProcessor {
    protected Elements elementUtils;
    static String TAG = "WriteClassNamesToAssets";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        //System.out.println("--------->AppStartUpItem elementUtils:  " + processingEnv.getFiler().toString());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(getAnnotationClass().getCanonicalName());
        //System.out.println("process annotation   ---> "+ getAnnotationClass().getName());
        return types;
    }

    protected String getLogTag(){
        return getClass().getSimpleName()+"-"+getAnnotationClass().getSimpleName();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println( getLogTag()+"-----process annotation:  "+ getAnnotationClass().getName()+", " + annotations+",roundEnvironment:"+roundEnvironment);
        if(annotations == null || annotations.isEmpty()){
            return false;
        }
        //搜集到AppStartUpCallback的所有子类,然后添加到list, 然后调用
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(getAnnotationClass());
        List<String> classNames = new ArrayList<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            Name name = typeElement.getQualifiedName();
            //typeElement.getInterfaces()
            //typeElement.getSuperclass();
            System.out.println(getLogTag()+"--------->:  classname " + typeElement);
            classNames.add(name.toString());

            try {
                 FileObject fo = null;
                String temFilePath = "";
                try {
                     fo = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", name.toString());
                    temFilePath = fo.toUri().getPath();
                     //.processing.FilerException: Attempt to reopen a
                    // file for path /Users/hss/github2/AppStartUpDemo/testlib/build/intermediates/javac/debug/classes/com.hss01248.startup.testlib.MyStartup3
                }catch (Throwable throwable){
                    //throwable.printStackTrace();
                    String msg = throwable.getMessage();
                    temFilePath = msg.substring(msg.indexOf("for path ")+"for path ".length());
                    //fo = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", name.toString());
                }

                //String temFilePath = fo.toUri().getPath();
                System.out.println(getLogTag()+"--------->:  temFilePath " + temFilePath);
                //java apt
                ///Users/hss/github2/AppStartUpDemo/testlib/build/intermediates/javac/debug/classes/com.hss01248.startup.testlib.MyStartup3
                //kotlin apt
                ///Users/hss/xx/module-offline-pay/Module-Offline-Pay/build/tmp/kapt3/classes/debug/
                int idx = temFilePath.indexOf("/build/intermediates/");
                if(idx < 0){
                    idx = temFilePath.indexOf("/build/tmp/");
                }
                String outputPath = temFilePath.substring(0, idx);
                outputPath = outputPath + "/src/main/assets/annotationClasses/"+ getAnnotationClass().getSimpleName()+"/";
                System.out.println(getLogTag()+"--------->outputPath " + outputPath);
                File dir = new File(outputPath);
                dir.mkdirs();
                File target = new File(dir,name.toString());
                System.out.println(getLogTag()+"--------->  write file name:  " + target.getAbsolutePath());
                target.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //或者把字符串写到propertis文件中,放到本模块的main/assets/startup/目录下
        //然后启动时解析出assets/startup/目录下所有配置
        //https://blog.csdn.net/haizishiwo/article/details/75213053
        return true;
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();
}
