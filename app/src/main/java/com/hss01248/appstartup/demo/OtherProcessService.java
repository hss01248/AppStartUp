package com.hss01248.appstartup.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @Despciption todo
 * @Author hss
 * @Date 29/07/2022 18:00
 * @Version 1.0
 */
public class OtherProcessService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
