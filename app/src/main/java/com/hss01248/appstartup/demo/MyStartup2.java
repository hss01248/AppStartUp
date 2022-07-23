package com.hss01248.appstartup.demo;

import android.app.Application;

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
public class MyStartup2 implements AppStartUpCallback {

    @Override
    public void onAndroidXStartUpInit(Application app) {
        AppStartUpCallback.super.onAndroidXStartUpInit(app);
        LogUtils.w("onAndroidXStartUpInit---MyStartup2");
    }

    @Override
    public int orderOfAndroidXStartUpInit() {
        return 12;
    }
}
