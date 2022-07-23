package com.hss01248.appstartup.api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.TreeSet;


/**
 * @Despciption todo
 * @Author hss
 * @Date 20/06/2022 10:29
 * @Version 1.0
 */
public class FirstActivityCreatedCallback implements Application.ActivityLifecycleCallbacks {




    boolean hasInit = false;
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if(!hasInit){
            hasInit = true;
            try {
                onFirstActivityCreated(activity,savedInstanceState);
            }catch (Throwable throwable){
               throwable.printStackTrace();
            }

        }
    }

    private void onFirstActivityCreated(Activity activity, Bundle savedInstanceState) {
       AppStartUpUtil.onFirstActivityCreated(activity,savedInstanceState);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
