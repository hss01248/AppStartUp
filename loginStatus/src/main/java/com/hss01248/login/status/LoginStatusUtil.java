package com.hss01248.login.status;


import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;


/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 11:18
 * @Version 1.0
 */
public class LoginStatusUtil {



    static List<LoginLogoutObserver> observers = new ArrayList<>();
    static List<LoginLogoutObserver> onceObservers = new ArrayList<>();

    public static   <T> void addObserver(LoginLogoutObserver<T> listener){
       addObserver(false,null,listener);
    }

    public static   <T> void addOnceObserver(LoginLogoutObserver<T> listener){
      addObserver(true,null,listener);
    }

    /**
     *
     * @param once  是否仅一次
     * @param lifecycleOwner 是否仅当前页面
     * @param observer
     * @param <T>
     */
    public static   <T> void addObserver(boolean once,LifecycleOwner lifecycleOwner,LoginLogoutObserver<T> observer){
        if(observer != null){
            if(lifecycleOwner != null){
                lifecycleOwner.getLifecycle().addObserver(observer);
            }
            if(once){
                onceObservers.add(observer);
            }else {
                observers.add(observer);
            }

        }
    }

    public static <T> void onLogin(T userInfo){
        for (LoginLogoutObserver listener : observers) {
            try {
                listener.login(userInfo);
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
        }
        for (LoginLogoutObserver onceObserver : onceObservers) {
            try {
                onceObserver.login(onceObserver);
            }catch (Exception th){
                th.printStackTrace();
            }
        }
        onceObservers.clear();
    }

    public static void onLogout(boolean fromLoginPage){
        for (LoginLogoutObserver listener : observers) {
            try {
                listener.logout(fromLoginPage);
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
        }
        for (LoginLogoutObserver onceObserver : onceObservers) {
            try {
                onceObserver.logout(fromLoginPage);
            }catch (Exception th){
                th.printStackTrace();
            }
        }
        onceObservers.clear();
    }
}
