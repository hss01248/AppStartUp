package com.hss01248.appstartup.demo;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.Utils;
import com.hss01248.android.spi.AndroidSpisReadUtil;
import com.hss01248.appstartup.api.AppStartUpCallback;
import com.hss01248.appstartup.api.AppStartUpUtil;
import com.hss01248.appstartup.demo.tasks.BgTask;
import com.hss01248.appstartup.demo.tasks.CpuTask;
import com.hss01248.appstartup.demo.tasks.MainTask;
import com.hss01248.classnametoassets.base.compiler.AssetsReadUtil;
import com.hss01248.test_annotation.AppStartUpItem3333;

import java.util.List;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 15:01
 * @Version 1.0
 */
public class BaseApp extends MultiDexApplication {

    //ActivityThread: Attach thread to application
    //ActivityThread: Init compatible state: true
    //ApplicationLoaders: createClassLoader zip: /data/app/com.hss01248.appstartup.demo-2L0uPAsPXeGENAA4Iyz5Qw==/base.apk librarySearchPath: /data/app/com.hss
    @Override
    protected void attachBaseContext(Context base) {
        //这里可以注入classloader:
        //https://www.twblogs.net/a/60589e89e6ac4ca98fa8a0e0
        super.attachBaseContext(base);
        Utils.init(this);
        LogUtils.i("attachBaseContext",base, ProcessUtils.getCurrentProcessName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("onCreate",this, ProcessUtils.getCurrentProcessName());
        AppStartUpUtil.addStartTaskToApplicationOnCreate(new CpuTask(this));
        AppStartUpUtil.addStartTaskToApplicationOnCreate(new MainTask(this));
        AppStartUpUtil.addStartTaskToApplicationOnCreate(new BgTask(this));
       // AppStartUpUtil.onApplicationOnCreate(this,BuildConfig.DEBUG);

        List<String> classes = AssetsReadUtil.findClasses(this, AppStartUpItem3333.class);
        LogUtils.w("classes of AppStartUpItem3333",classes);

        AndroidSpisReadUtil.findClasses(this, AppStartUpCallback.class);
    }

    //ActivityThread: finishPreloaded preloadStatus 0
}
