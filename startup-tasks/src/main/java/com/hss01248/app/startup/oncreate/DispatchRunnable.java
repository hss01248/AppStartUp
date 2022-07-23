package com.hss01248.app.startup.oncreate;

import android.os.Looper;
import android.os.Process;
import androidx.core.os.TraceCompat;


import com.hss01248.app.startup.oncreate.stat.TaskStat;
import com.hss01248.app.startup.oncreate.utils.DispatcherLog;

import java.util.Map;
import java.util.TreeMap;



/**
 * 任务真正执行的地方
 */

public class DispatchRunnable implements Runnable {

    private StartTask mTask;
    private TaskDispatcher mTaskDispatcher;

    public DispatchRunnable(StartTask task) {
        this.mTask = task;
    }

    public DispatchRunnable(StartTask task, TaskDispatcher dispatcher) {
        this.mTask = task;
        this.mTaskDispatcher = dispatcher;
    }

    @Override
    public void run() {
        TraceCompat.beginSection(mTask.getClass().getSimpleName());
        DispatcherLog.i(mTask.getClass().getSimpleName()
                + " begin run" + "  Situation  " + TaskStat.getCurrentSituation());

        Process.setThreadPriority(mTask.priority());

        long startTime = System.currentTimeMillis();

        mTask.setWaiting(true);
        mTask.waitToSatisfy();

        long waitTime = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        // 执行Task
        mTask.setRunning(true);
        mTask.run();

        // 执行Task的尾部任务
        Runnable tailRunnable = mTask.getTailRunnable();
        if (tailRunnable != null) {
            tailRunnable.run();
        }

        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            printTaskLog(startTime, waitTime);

            TaskStat.markTaskDone();
            mTask.setFinished(true);
            if (mTaskDispatcher != null) {
                mTaskDispatcher.satisfyChildren(mTask);
                mTaskDispatcher.markTaskDone(mTask);
            }
            DispatcherLog.i(mTask.getClass().getSimpleName() + " finish");
        }
        TraceCompat.endSection();
    }

    /**
     * 打印出来Task执行的日志
     *
     * @param startTime
     * @param waitTime
     */
    private void printTaskLog(long startTime, long waitTime) {
        long runTime = System.currentTimeMillis() - startTime;
        if (DispatcherLog.isDebug()) {
            DispatcherLog.i(mTask.getClass().getSimpleName() + "  wait cost-ms " + waitTime + "    run cost-ms "
                    + runTime + "   isMain " + (Looper.getMainLooper() == Looper.myLooper())
                    + "  needWait " + (mTask.needWait() || (Looper.getMainLooper() == Looper.myLooper()))
                    + "  ThreadId " + Thread.currentThread().getId()
                    + "  ThreadName " + Thread.currentThread().getName()
                    + "  Situation  " + TaskStat.getCurrentSituation()
            );
            if(Looper.getMainLooper() == Looper.myLooper()){
                costMapp.put(runTime,mTask.getClass().getSimpleName());
            }else {
                costMappBack.put(runTime, "backgroudThread ->" +mTask.getClass().getSimpleName());
            }
        }
    }

   public static TreeMap<Long,String> costMapp = new TreeMap<>();

    public static TreeMap<Long,String> costMappBack = new TreeMap<>();
    public static long attachContextTime;
    public static long applicationEndTime;
    public static long splashOnCreateEndTime;

    public static void printCost(){
        if (DispatcherLog.isDebug()) {
            StringBuilder sb =  new StringBuilder("all tasks sorted by cost on main:\n");
            for (Map.Entry<Long, String> longStringEntry : costMapp.entrySet()) {
                sb.append(longStringEntry.getValue())
                        .append(" cost-ms ")
                        .append(longStringEntry.getKey()).append("\n");
            }
            DispatcherLog.i(sb.toString());

            StringBuilder sb2 =  new StringBuilder("all tasks sorted by cost onBack:\n");
            for (Map.Entry<Long, String> longStringEntry : costMappBack.entrySet()) {
                sb2.append(longStringEntry.getValue())
                        .append(" cost-ms ")
                        .append(longStringEntry.getKey()).append("\n");
            }
            DispatcherLog.i(sb2.toString());
            DispatcherLog.i("from attachBaseContext->MainActivity.onCreate() end cost-ms: "+(System.currentTimeMillis() - attachContextTime));
        }


    }

}
