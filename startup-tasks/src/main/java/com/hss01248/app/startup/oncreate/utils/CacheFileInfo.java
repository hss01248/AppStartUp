package com.hss01248.app.startup.oncreate.utils;

/**
 * @author chendx
 * @version 1.0
 * @since 2021/9/24
 */
public class CacheFileInfo implements Comparable<CacheFileInfo> {
    private String absPath;
    private long length;

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public int compareTo(CacheFileInfo o) {
        return (int) (o.getLength() - this.length);
    }
}
