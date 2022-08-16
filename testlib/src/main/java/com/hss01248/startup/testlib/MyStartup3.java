package com.hss01248.startup.testlib;

import android.app.Application;

import androidx.annotation.Keep;

import com.blankj.utilcode.util.LogUtils;
import com.hss01248.appstartup.api.AppStartUpCallback;
import com.hss01248.startup.annotation.AppStartUpItem;
import com.hss01248.test_annotation.AppStartUpItem3333;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 15:04
 * @Version 1.0
 */
@Keep
@AppStartUpItem3333
@AppStartUpItem
public class MyStartup3 implements AppStartUpCallback {

    @Override
    public void onAndroidXStartUpInit(Application app) {
        AppStartUpCallback.super.onAndroidXStartUpInit(app);
        LogUtils.w("onAndroidXStartUpInit---MyStartup3");
    }

    @Override
    public int orderOfAndroidXStartUpInit() {
        return -2;
    }
}
