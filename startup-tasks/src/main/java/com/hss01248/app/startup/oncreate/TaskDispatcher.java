package com.hss01248.app.startup.oncreate;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;


import com.hss01248.app.startup.oncreate.sort.TaskSortUtil;
import com.hss01248.app.startup.oncreate.stat.TaskStat;
import com.hss01248.app.startup.oncreate.utils.DispatcherLog;
import com.hss01248.app.startup.oncreate.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * 启动器调用类
 */
public class TaskDispatcher {
    private long mStartTime;
    private static final int WAITTIME = 4500;//最多等4.5s，防止ANR
    private static Context sContext;
    private static boolean sIsMainProcess;
    private List<Future> mFutures = new ArrayList<>();
    private static volatile boolean sHasInit;
    private List<StartTask> mAllTasks = new ArrayList<>();
    private List<Class<? extends StartTask>> mClsAllTasks = new ArrayList<>();
    private volatile List<StartTask> mMainThreadTasks = new ArrayList<>();
    private CountDownLatch mCountDownLatch;

    /**
     * 需要等待的任务数
     */
    private AtomicInteger mNeedWaitCount = new AtomicInteger();//

    /**
     * 调用了 await 还没结束且需要等待的任务列表
     */
    private List<StartTask> mNeedWaitTasks = new ArrayList<>();

    /**
     * 已经结束的Task
     */
    private volatile List<Class<? extends StartTask>> mFinishedTasks = new ArrayList<>(100);//

    private HashMap<Class<? extends StartTask>, ArrayList<StartTask>> mDependedHashMap = new HashMap<>();

    /**
     * 启动器分析的次数，统计下分析的耗时；
     */
    private AtomicInteger mAnalyseCount = new AtomicInteger();

    private TaskDispatcher() {
    }

    public static void init(Context context) {
        if (context != null) {
            sContext = context;
            sHasInit = true;
            sIsMainProcess = Utils.isMainProcess(sContext);
        }
    }

    /**
     * 注意：每次获取的都是新对象
     *
     * @return
     */
    public static TaskDispatcher createInstance() {
        if (!sHasInit) {
            throw new RuntimeException("must call TaskDispatcher.init first");
        }
        return new TaskDispatcher();
    }

    public TaskDispatcher addTask(StartTask task) {
        if (task != null) {
            collectDepends(task);
            mAllTasks.add(task);
            mClsAllTasks.add(task.getClass());
            // 非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(task)) {
                mNeedWaitTasks.add(task);
                mNeedWaitCount.getAndIncrement();
            }
        }
        return this;
    }

    private void collectDepends(StartTask task) {
        if (task.dependsOn() != null && task.dependsOn().size() > 0) {
            for (Class<? extends StartTask> cls : task.dependsOn()) {
                if (mDependedHashMap.get(cls) == null) {
                    mDependedHashMap.put(cls, new ArrayList<StartTask>());
                }
                mDependedHashMap.get(cls).add(task);
                if (mFinishedTasks.contains(cls)) {
                    task.satisfy();
                }
            }
        }
    }

    private boolean ifNeedWait(StartTask task) {
        return !task.runOnMainThread() && task.needWait();
    }

    @UiThread
    public void start() {
        mStartTime = System.currentTimeMillis();
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("must be called from UiThread");
        }
        if (mAllTasks.size() > 0) {
            mAnalyseCount.getAndIncrement();
            printDependedMsg();
            mAllTasks = TaskSortUtil.getSortResult(mAllTasks, mClsAllTasks);
            mCountDownLatch = new CountDownLatch(mNeedWaitCount.get());

            sendAndExecuteAsyncTasks();

            DispatcherLog.i("task analyse cost-ms " + (System.currentTimeMillis() - mStartTime) + "  begin main ");
            DispatchRunnable.costMapp.put((System.currentTimeMillis() - mStartTime),"task analyse");
            executeTaskMain();
        }
        DispatcherLog.i("task analyse cost-ms  " + (System.currentTimeMillis() - mStartTime));
    }

    public void cancel() {
        for (Future future : mFutures) {
            future.cancel(true);
        }
    }

    private void executeTaskMain() {
        mStartTime = System.currentTimeMillis();
        for (StartTask task : mMainThreadTasks) {
            long time = System.currentTimeMillis();
            new DispatchRunnable(task,this).run();
            DispatcherLog.i("real main " + task.getClass().getSimpleName() + " cost-ms   " +
                    (System.currentTimeMillis() - time));
        }
        DispatcherLog.i("maintask cost-ms " + (System.currentTimeMillis() - mStartTime));
    }

    /**
     * 发送去并且执行异步任务
     */
    private void sendAndExecuteAsyncTasks() {
        for (StartTask task : mAllTasks) {
            if (task.onlyInMainProcess() && !sIsMainProcess) {
                markTaskDone(task);
            } else {
                sendTaskReal(task);
            }
            task.setSend(true);
        }
    }

    /**
     * 查看被依赖的信息
     */
    private void printDependedMsg() {
        DispatcherLog.i("needWait size : " + (mNeedWaitCount.get()));
        if (false) {
            for (Class<? extends StartTask> cls : mDependedHashMap.keySet()) {
                DispatcherLog.i("cls " + cls.getSimpleName() + "   " + mDependedHashMap.get(cls).size());
                for (StartTask task : mDependedHashMap.get(cls)) {
                    DispatcherLog.i("cls       " + task.getClass().getSimpleName());
                }
            }
        }
    }

    /**
     * 通知Children一个前置任务已完成
     */
    public void satisfyChildren(StartTask launchTask) {
        ArrayList<StartTask> arrayList = mDependedHashMap.get(launchTask.getClass());
        if (arrayList != null && arrayList.size() > 0) {
            for (StartTask task : arrayList) {
                task.satisfy();
            }
        }
    }

    public void markTaskDone(StartTask task) {
        if (ifNeedWait(task)) {
            mFinishedTasks.add(task.getClass());
            mNeedWaitTasks.remove(task);
            mCountDownLatch.countDown();
            mNeedWaitCount.getAndDecrement();
        }
    }

    /**
     * 发送任务
     */
    private void sendTaskReal(final StartTask task) {
        if (task.runOnMainThread()) {
            mMainThreadTasks.add(task);
            if (task.needCall()) {
                task.setTaskCallBack(new TaskCallBack() {
                    @Override
                    public void call() {
                        TaskStat.markTaskDone();
                        task.setFinished(true);
                        satisfyChildren(task);
                        markTaskDone(task);
                        DispatcherLog.i(task.getClass().getSimpleName() + " finish");
                        Log.i("testLog", "call");
                    }
                });
            }
        } else {
            // 直接发，是否执行取决于具体线程池
            Future future = task.runOn().submit(new DispatchRunnable(task,this));
            mFutures.add(future);
        }
    }

    public void executeTask(StartTask task) {
        if (ifNeedWait(task)) {
            mNeedWaitCount.getAndIncrement();
        }
        task.runOn().execute(new DispatchRunnable(task,this));
    }

    @UiThread
    public void await() {
        try {
            if (DispatcherLog.isDebug()) {
                DispatcherLog.i("still has " + mNeedWaitCount.get());
                for (StartTask task : mNeedWaitTasks) {
                    DispatcherLog.i("needWait: " + task.getClass().getSimpleName());
                }
            }

            if (mNeedWaitCount.get() > 0) {
                if (mCountDownLatch == null) {
                    throw new RuntimeException("You have to call start() before call await()");
                }
                mCountDownLatch.await(WAITTIME, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e){
            Log.e("TaskDispatcher", "await: NullPointerException，"+e.getMessage() );
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static boolean isMainProcess() {
        return sIsMainProcess;
    }
}
