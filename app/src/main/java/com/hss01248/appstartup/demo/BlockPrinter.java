package com.hss01248.appstartup.demo;

import android.os.SystemClock;
import android.util.Log;
import android.util.Printer;

/**
 * @Despciption todo
 * @Author hss
 * @Date 25/07/2022 16:16
 * @Version 1.0
 */
public class BlockPrinter implements Printer {
    private long mBlockThresholdMillis = 100;
    private long mStartTimeMillis;
    private boolean mStartedPrinting = false;
    private long mStartThreadTimeMillis;

    @Override
    public void println(String x) {
        Log.d("MainLooper",x);
        //logging.println(">>>>> Dispatching to " + msg.target + " " +
        //                msg.callback + ": " + msg.what);
        //logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
        if (!mStartedPrinting) {
            mStartTimeMillis = System.currentTimeMillis();
            mStartThreadTimeMillis = SystemClock.currentThreadTimeMillis();
            mStartedPrinting = true;
        } else {
            final long endTime = System.currentTimeMillis();
            mStartedPrinting = false;
            if (isBlock(endTime)) {
                notifyBlockEvent(endTime-mStartTimeMillis);
            }
        }
    }

    private void notifyBlockEvent(long endTime) {
        Throwable throwable = new Throwable("main block " + endTime);
        Log.w("MainLooper",throwable);
    }

    private boolean isBlock(long endTime) {
        return endTime - mStartTimeMillis > mBlockThresholdMillis;
    }
}
