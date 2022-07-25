package com.hss01248.appstart.test_kotlin

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.hss01248.appstartup.api.AppStartUpCallback
import com.hss01248.startup.annotation.AppStartUpItem

@AppStartUpItem
class KotlinStartupImpl : AppStartUpCallback {

    override fun onBeforeApplicationOnCreate(app: Application?) {
        super.onBeforeApplicationOnCreate(app)
        LogUtils.d("onBeforeApplicationOnCreate","KotlinStartupImpl")
    }
}