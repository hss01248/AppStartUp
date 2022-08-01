package com.hss01248.appstartup.demo.tasks;

import android.app.Application;

import com.hss01248.app.startup.oncreate.StartTask;
import com.hss01248.app.startup.oncreate.utils.DispatcherExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 09:53
 * @Version 1.0
 */
public class CpuTask extends StartTask {
    public CpuTask(Application app) {
        super(app);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Class<? extends StartTask>> dependsOn() {
        List<Class<? extends StartTask>> tasks = new ArrayList<>();
        tasks.add(BgTask.class);
        return tasks;
    }

    @Override
    public ExecutorService runOn() {
        return DispatcherExecutor.getCPUExecutor();
    }


}
