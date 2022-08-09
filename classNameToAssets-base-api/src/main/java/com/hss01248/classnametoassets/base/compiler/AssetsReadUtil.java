package com.hss01248.classnametoassets.base.compiler;

import android.content.Context;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Despciption todo
 * @Author hss
 * @Date 09/08/2022 10:03
 * @Version 1.0
 */
public class AssetsReadUtil {

    static final String path = "annotationClasses/";

   public static List<String> findClasses(Context context, Class<? extends Annotation> annotaionClass){
         List<String> configs = new ArrayList<>();
        try {
            String[] startupclasses = context.getAssets().list(path+annotaionClass.getSimpleName());
            if(startupclasses != null && startupclasses.length >0){
                configs.addAll(Arrays.asList(startupclasses));
            }else {
                Log.i("AssetsRead","not start up classes in assets/"+path+annotaionClass.getSimpleName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return configs;
    }

  public   static    Object newInstance2(Class tClass){
        try {
            Method getInstance = tClass.getDeclaredMethod("getInstance");
            Object invoke =  getInstance.invoke(tClass);
            return invoke;
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                return tClass.newInstance();
            } catch (Throwable ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
