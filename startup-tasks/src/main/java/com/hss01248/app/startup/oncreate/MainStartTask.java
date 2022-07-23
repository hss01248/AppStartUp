package com.hss01248.app.startup.oncreate;

import android.app.Application;

public abstract class MainStartTask extends StartTask {

    public MainStartTask(Application app) {
        super(app);
    }

    @Override
    public boolean runOnMainThread() {
        return true;
    }

}
