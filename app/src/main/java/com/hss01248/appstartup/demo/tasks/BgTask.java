package com.hss01248.appstartup.demo.tasks;

import android.app.Application;

import com.hss01248.app.startup.oncreate.StartTask;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 09:53
 * @Version 1.0
 */
public class BgTask extends StartTask {
    public BgTask(Application app) {
        super(app);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
