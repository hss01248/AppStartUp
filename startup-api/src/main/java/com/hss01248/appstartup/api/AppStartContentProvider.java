package com.hss01248.appstartup.api;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.Utils;

import java.io.FileNotFoundException;



/**
 * @Despciption 非常优秀的博客 Android 多个 ContentProvider 初始化顺序: https://sivanliu.github.io/2017/12/16/provider%E5%88%9D%E5%A7%8B%E5%8C%96/
 * 提供一个与firebase一样测量app启动时间的工具,initOrder=102, 比firebase的FirebasePerfProvider还更前初始化
 * firebase的说明: https://firebase.google.com/docs/perf-mon/app-start-foreground-background-traces?hl=en&authuser=0&platform=android
 * @Author hss
 * @Date 27/04/2022 10:57
 * @Version 1.0
 */
public class AppStartContentProvider extends ContentProvider {
    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return super.openAssetFile(uri, mode);
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if (context instanceof Application) {
            Application application = (Application) context;
            Utils.init(application);
        }
    }

    @Override
    public boolean onCreate() {
        init2();
        return false;
    }

    private void init2() {
        AppStartUpUtil.fillCallbacks2(getContext());
        if (ProcessUtils.isMainProcess()) {
            Utils.getApp().registerActivityLifecycleCallbacks(new FirstActivityCreatedCallback());
            AppStartUpUtil.onFirstProviderInit();
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
