package com.hss01248.appstartup.api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public interface AppStartUpCallback {

    default void onFirstProviderInit(Application app){}

    default void onAndroidXStartUpInit(Application app){}

    default void onBeforeApplicationOnCreate(Application app){}

    default void onFirstActivityCreated(Application app, Activity activity, Bundle savedInstanceState){}


   default int orderOfFirstProviderInit(){
       return 0;
   }

    default int orderOfAndroidXStartUpInit(){
       return 0;
    }

    default int orderOfBeforeApplicationOnCreate(){
       return 0;
    }

    default int orderOfFirstActivityCreated(){
       return 0;
    }
}
