package com.hss01248.app.startup.oncreate.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Utils {

    private static String sCurProcessName = null;

    public static boolean isMainProcess(Context context) {
        String processName = getCurProcessName(context);
        if (processName != null && processName.contains(":")) {
            return false;
        }
        return (processName != null && processName.equals(context.getPackageName()));
    }

    public static String getCurProcessName(Context context) {
        String processName = sCurProcessName;
        if (!TextUtils.isEmpty(processName)) {
            return processName;
        }
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName;
                    return sCurProcessName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sCurProcessName = getCurProcessNameFromProc();
        return sCurProcessName;
    }

    private static String getCurProcessNameFromProc() {
        BufferedReader cmdlineReader = null;
        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(
                            "/proc/" + android.os.Process.myPid() + "/cmdline"),
                    "iso-8859-1"));
            int c;
            StringBuilder processName = new StringBuilder();
            while ((c = cmdlineReader.read()) > 0) {
                processName.append((char) c);
            }
            return processName.toString();
        } catch (Throwable e) {
            // ignore
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return null;
    }

    /**
     * 获取手机存储的剩余空间
     */
    public static String getAvailableStorageSize() {
        long bytes = 0;
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            //可用块数量
            long availableCount = statFs.getAvailableBlocks();
            //块大小
            long blockSize = statFs.getBlockSize();
            bytes = blockSize * availableCount;
        } catch (Throwable ignored) {
        }
        //格式化存储大小
        return ConvertUtils.byte2FitMemorySize(bytes).replace(",", ".");
    }

    /**
     * 获取手机存储的剩余空间
     */
    public static String getAllBlockStorageSize() {
        long bytes = 0;
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            //存储块总数量
            long blockCount = statFs.getBlockCount();
            //块大小
            long blockSize = statFs.getBlockSize();
            bytes = blockSize * blockCount;
        } catch (Throwable ignored) {
        }
        //格式化存储大小
        return ConvertUtils.byte2FitMemorySize(bytes).replace(",", ".");
    }


    /**
     * 获取app缓存目录大小
     */
    public static String getCacheAllSize() {
        long allSize = 0;
        try {
            File cacheParentFile = com.blankj.utilcode.util.Utils.getApp().getCacheDir().getParentFile();
            File externalCacheDirParentFile = com.blankj.utilcode.util.Utils.getApp().getExternalCacheDir().getParentFile();
            allSize = FileUtils.getLength(cacheParentFile) + FileUtils.getLength(externalCacheDirParentFile);
        } catch (Throwable ignored) {
        }
        return ConvertUtils.byte2FitMemorySize(allSize);
    }

    /** 获取APP缓存目录list，取前十的 */
    public static List<CacheFileInfo> getCacheFolderList() {
        List<CacheFileInfo> tempFiles = new ArrayList<>();
        try {
            File cacheParentFile = com.blankj.utilcode.util.Utils.getApp().getCacheDir().getParentFile();
            File externalCacheDirParentFile = com.blankj.utilcode.util.Utils.getApp().getExternalCacheDir().getParentFile();
            tempFiles.addAll(getSubFiles(cacheParentFile));
            tempFiles.addAll(getSubFiles(externalCacheDirParentFile));
            Collections.sort(tempFiles);
        } catch (Throwable ignored) {
        }
        return tempFiles;
    }


    private static List<CacheFileInfo> getSubFiles(File parentFile) {
        List<CacheFileInfo> destFiles = new ArrayList<>();
        if (parentFile != null) {
            File[] files = parentFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    //过滤文件
                    if (FileUtils.isFile(file)) {
                        continue;
                    }
                    //过滤占用大小为0的文件夹
                    if (file.getAbsolutePath().endsWith("/cache") || file.getAbsolutePath().endsWith("/files")) {
                        //cache和files文件夹的目录需要再次遍历
                        destFiles.addAll(getSubFiles(file));
                    } else {
                        CacheFileInfo cacheFileInfo = new CacheFileInfo();
                        cacheFileInfo.setAbsPath(file.getAbsolutePath());
                        cacheFileInfo.setLength(FileUtils.getLength(file));
                        destFiles.add(cacheFileInfo);
                    }
                }
            }
        }
        return destFiles;
    }


    /**
     * 获取缓存目录指定文件夹下的文件
     *
     * @param folderName 文件夹名称
     */
    public static List<CacheFileInfo> getFileFilerList(String folderName) {
        List<CacheFileInfo> tempFiles = new ArrayList<>();
        try {
            File cacheParentFile = com.blankj.utilcode.util.Utils.getApp().getCacheDir().getParentFile();
            File dbFile = FileUtils.getFileByPath(cacheParentFile + "/" + folderName);
            if (dbFile != null) {
                File[] files = dbFile.listFiles();
                if (files != null) {
                    for (File file : files) {
                        CacheFileInfo cacheFileInfo = new CacheFileInfo();
                        cacheFileInfo.setAbsPath(file.getName());
                        cacheFileInfo.setLength(FileUtils.getLength(file));
                        tempFiles.add(cacheFileInfo);
                    }
                }
            }
            Collections.sort(tempFiles);
        } catch (Throwable ignored) {
        }
        return tempFiles;
    }


}
