package com.hss01248.appstartup.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import com.blankj.utilcode.util.LogUtils;
import com.hss01248.appstartup.api.AppStartUpCallback;
import com.hss01248.startup.annotation.AppStartUpItem;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 15:04
 * @Version 1.0
 */
@AppStartUpItem
public class MyStartup1 implements AppStartUpCallback {

    @Override
    public void onFirstProviderInit(Application app) {
        LogUtils.d("onFirstProviderInit",app);
        Looper.getMainLooper().setMessageLogging(new BlockPrinter());
    }

    @Override
    public void onBeforeApplicationOnCreate(Application app) {
        LogUtils.d("onBeforeApplicationOnCreate",app);
    }

    @Override
    public void onFirstActivityCreated(Application app, Activity activity, Bundle savedInstanceState) {
        LogUtils.d("onFirstActivityCreated",app,activity,savedInstanceState);
    }

    @Override
    public void onAndroidXStartUpInit(Application app) {
        LogUtils.d("onAndroidXStartUpInit",app);
    }
}
