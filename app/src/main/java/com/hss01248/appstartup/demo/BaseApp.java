package com.hss01248.appstartup.demo;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 15:01
 * @Version 1.0
 */
public class BaseApp extends Application {

    //ActivityThread: Attach thread to application
    //ActivityThread: Init compatible state: true
    //ApplicationLoaders: createClassLoader zip: /data/app/com.hss01248.appstartup.demo-2L0uPAsPXeGENAA4Iyz5Qw==/base.apk librarySearchPath: /data/app/com.hss
    @Override
    protected void attachBaseContext(Context base) {
        //这里可以注入classloader:
        //https://www.twblogs.net/a/60589e89e6ac4ca98fa8a0e0
        super.attachBaseContext(base);
        Utils.init(this);
        LogUtils.i("attachBaseContext",base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("onCreate",this);
    }

    //ActivityThread: finishPreloaded preloadStatus 0
}
