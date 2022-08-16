package com.hss01248.android.spi;

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
public class AndroidSpisReadUtil {

    static final String path = "androidSpis/";

   public static List<String> findClasses(Context context, Class interfaceOrSuperClass){
         List<String> configs = new ArrayList<>();
        try {
            String[] startupclasses = context.getAssets().list(path+interfaceOrSuperClass.getName());
            Log.d("AndroidSpis","try to find classes in assets/"+path+interfaceOrSuperClass.getName()+",list: "+ Arrays.toString(startupclasses));
            if(startupclasses != null && startupclasses.length >0){
                configs.addAll(Arrays.asList(startupclasses));
            }else {
                Log.w("AndroidSpis","not start up classes in assets/"+path+interfaceOrSuperClass.getName());
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
