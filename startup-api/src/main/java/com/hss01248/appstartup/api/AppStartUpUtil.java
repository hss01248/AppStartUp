package com.hss01248.appstartup.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.hss01248.app.startup.oncreate.StartTask;
import com.hss01248.app.startup.oncreate.TaskDispatcher;
import com.hss01248.app.startup.oncreate.utils.DispatcherLog;

import java.io.IOException;
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


    /**
     * 需要用户主动调用, 或者使用字节码注入.
     * @param application
     * @param showLog
     */
    public static void onApplicationOnCreate(Application application,boolean showLog){
        if(application == null){
            application = Utils.getApp();
        }
        TaskDispatcher.init(application);
        DispatcherLog.setDebug(showLog);
        TaskDispatcher taskDispatcher = TaskDispatcher.createInstance();
        addTasksToDispatcher(taskDispatcher,application);
        taskDispatcher.start();
        taskDispatcher.await();

        afterTask(application);
    }
    static List<StartTask> tasks = new ArrayList<>();

    public static void addStartTaskToApplicationOnCreate(StartTask startTask){
        tasks.add(startTask);
    }

    public static void add(AppStartUpCallback callback){
        callbacks.add(callback);
    }

    private static void afterTask(Application application) {
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onFirstProviderInit");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        Collections.sort(callbackList, new Comparator<AppStartUpCallback>() {
            @Override
            public int compare(AppStartUpCallback o1, AppStartUpCallback o2) {
                return o2.orderOfAfterApplicationOnCreateTaskFinished() - o1.orderOfAfterApplicationOnCreateTaskFinished();
            }
        });
        for (AppStartUpCallback callback : callbackList) {
            callback.onAfterApplicationOnCreateTaskFinished(application);
        }
    }

    private static void addTasksToDispatcher(TaskDispatcher taskDispatcher, Application application) {
        if(callbacks == null || callbacks.isEmpty()){
            Log.v("start","callbacks is empty--onFirstProviderInit");
            return;
        }
        List<AppStartUpCallback> callbackList = new ArrayList<>(callbacks);
        for (AppStartUpCallback appStartUpCallback : callbackList) {
            StartTask task = appStartUpCallback.getApplicationOnCreateTask(application);
            if(task != null){
                taskDispatcher.addTask(task);
            }
        }
        for (StartTask task : tasks) {
            if(task != null){
                taskDispatcher.addTask(task);
            }
        }
    }

    static void fillCallbacks2(Context context){
        try {
            String[] startupclasses = context.getAssets().list("startupclasses");
            if(startupclasses != null && startupclasses.length >0){
                for (String startupclass : startupclasses) {
                    try {
                        Class clazz = Class.forName(startupclass);
                        Object o = clazz.newInstance();
                        if(o instanceof AppStartUpCallback){
                            callbacks.add((AppStartUpCallback) o);
                        }else {
                            Log.w("startup",startupclass +" not instanceof AppStartUpCallback");
                        }
                    }catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                }
            }else {
                Log.i("startup","not start up classes in assets/startupclasses");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<AppStartUpCallback> callbacks = new ArrayList<>();
    static {
        callbacks.add(new FilledCallbacks());
    }

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
