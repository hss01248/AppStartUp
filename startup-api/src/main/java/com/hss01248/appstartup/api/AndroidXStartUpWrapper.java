package com.hss01248.appstartup.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/22 11:32
 * @Version 1.0
 */
public class AndroidXStartUpWrapper implements Initializer<String> {
    @NonNull
    @Override
    public String create(@NonNull Context context) {
        AppStartUpUtil.onAndroidXStartUpInit();
        return "AndroidXStartUpWrapper";
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
