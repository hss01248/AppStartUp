package com.hss01248.appstartup.api;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 15:27
 * @Version 1.0
 */
public class AppStartUpUtil {

    public static void add(String clazzName){

    }


    static void fillCallbacks(){
        String name = "com.hss01248.appstartup.api.AppStartUpItems";
        try {
            Object names = ReflectUtils.reflect(name)
                    .method("callbackClassNames").get();
            if(names instanceof List){
                List list = (List) names;
                callbacks.clear();
                for (Object o : list) {
                    if(o instanceof String){
                        String str = (String) o;
                        try {
                            Class clazz = Class.forName(str);
                            Object o1 = clazz.newInstance();
                            if(o1 instanceof AppStartUpCallback){
                                callbacks.add((AppStartUpCallback) o1);
                            }else {
                                Log.w("start","not AppStartUpCallback: "+ o1);
                            }
                        }catch (Throwable throwable){
                            throwable.printStackTrace();
                        }
                    }else {
                        Log.w("start","not string: "+ o);
                    }
                }
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    static List<AppStartUpCallback> callbacks = new ArrayList<>();

    static void onFirstProviderInit(){
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onFirstProviderInit");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        Collections.sort(callbackList, new Comparator<AppStartUpCallback>() {
            @Override
            public int compare(AppStartUpCallback o1, AppStartUpCallback o2) {
                return o2.orderOfFirstProviderInit() - o1.orderOfFirstProviderInit();
            }
        });
        for (AppStartUpCallback callback : callbackList) {
            callback.onFirstProviderInit(Utils.getApp());
        }
    }


    static void onAndroidXStartUpInit(){
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onAndroidXStartUpInit");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        Collections.sort(callbackList, new Comparator<AppStartUpCallback>() {
            @Override
            public int compare(AppStartUpCallback o1, AppStartUpCallback o2) {
                return o2.orderOfAndroidXStartUpInit() - o1.orderOfAndroidXStartUpInit();
            }
        });
        for (AppStartUpCallback callback : callbackList) {
            callback.onAndroidXStartUpInit(Utils.getApp());
        }
    }

    static void onBeforeApplicationOnCreate(){
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onBeforeApplicationOnCreate");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        Collections.sort(callbackList, new Comparator<AppStartUpCallback>() {
            @Override
            public int compare(AppStartUpCallback o1, AppStartUpCallback o2) {
                return o2.orderOfBeforeApplicationOnCreate() - o1.orderOfBeforeApplicationOnCreate();
            }
        });
        for (AppStartUpCallback callback : callbackList) {
            callback.onBeforeApplicationOnCreate(Utils.getApp());
        }
    }


    static void onFirstActivityCreated(Activity activity, Bundle savedInstanceState){
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onBeforeApplicationOnCreate");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        Collections.sort(callbackList, new Comparator<AppStartUpCallback>() {
            @Override
            public int compare(AppStartUpCallback o1, AppStartUpCallback o2) {
                return o2.orderOfFirstActivityCreated() - o1.orderOfFirstActivityCreated();
            }
        });
        for (AppStartUpCallback callback : callbackList) {
            callback.onFirstActivityCreated(Utils.getApp(),activity,savedInstanceState);
        }
    }
}
