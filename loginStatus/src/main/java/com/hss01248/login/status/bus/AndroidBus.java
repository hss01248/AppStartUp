package com.hss01248.login.status.bus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 14:05
 * @Version 1.0
 */
public class AndroidBus {

    static   Map<Class, List<BusObserver>> map = new ConcurrentHashMap<>();
    static List<BusObserver> onceObservers = new CopyOnWriteArrayList<>();
    static Handler mainHandler;

    public static <T> void post(T obj){
        Class<?> aClass = obj.getClass();
        if(map.containsKey(aClass)){
            //必须完全匹配,不能是子类.
            List<BusObserver> busObservers = map.get(aClass);
            List<BusObserver> onceObserversToRemove = new ArrayList<>();
            if(busObservers != null){
                for (BusObserver busObserver : busObservers) {
                    if(onceObservers.contains(busObserver)){
                        onceObserversToRemove.add(busObserver);
                    }
                    if(busObserver.switchToUIThread()){
                        if(mainHandler == null){
                            mainHandler = new Handler(Looper.getMainLooper());
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    busObserver.observer(obj);
                                }catch (Throwable throwable){
                                    throwable.printStackTrace();
                                }
                            }
                        });
                    }else {
                        try {
                            busObserver.observer(obj);
                        }catch (Throwable throwable){
                            throwable.printStackTrace();
                        }
                    }
                }
                //移除一次性的监听器
                if(!onceObserversToRemove.isEmpty()){
                    try {
                        for (BusObserver busObserver : onceObserversToRemove) {
                            busObservers.remove(busObserver);
                            onceObservers.remove(busObserver);
                        }
                    }catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                }
            }
        }else {
            Log.w("bus","no observer for :"+obj);
        }
    }

    public static <T> void removeObserverMannually(BusObserver<T> observer){
        try {
            if(onceObservers.contains(observer)){
                onceObservers.remove(observer);
            }
            for (Map.Entry<Class, List<BusObserver>> classListEntry : map.entrySet()) {
                List<BusObserver> value = classListEntry.getValue();
                if(value.contains(observer)){
                    value.remove(observer);
                }
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }



    public static <T> void observer(boolean once, LifecycleOwner lifecycleOwner,BusObserver<T> observer){
        Type type = observer.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class aClass = ClassUtil.getClass(type,0);
            List<BusObserver> busObservers = null;
            if(map.containsKey(aClass)){
                busObservers = map.get(aClass);
            }else {
                busObservers = new ArrayList<>();
            }
            busObservers.add(observer);
            if(lifecycleOwner != null){
                lifecycleOwner.getLifecycle().addObserver(observer);
            }
            if(once){
                onceObservers.add(observer);
            }
        }else {
            Log.w("bus","not impl of  BusObserver<T>");
        }
    }


}
