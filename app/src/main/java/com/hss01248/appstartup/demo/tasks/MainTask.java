package com.hss01248.appstartup.demo.tasks;

import android.app.Application;

import com.hss01248.app.startup.oncreate.StartTask;
import com.hss01248.app.startup.oncreate.utils.DispatcherExecutor;

import java.util.concurrent.ExecutorService;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 09:50
 * @Version 1.0
 */
public class MainTask extends StartTask {
    public MainTask(Application app) {
        super(app);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean runOnMainThread() {
        return true;
    }


}
