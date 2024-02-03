package com.aws.demo;

import java.time.Instant;
public class S3ObjectInfo {

    private String key;
    private long size;
    private Instant lastModified;

    public S3ObjectInfo(String key, long size, Instant lastModified) {
        this.key = key;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    // Optional: Override toString for convenient logging or debugging
    @Override
    public String toString() {
        return "S3ObjectInfo{" +
                "key='" + key + '\'' +
                ", size=" + size +
                ", lastModified=" + lastModified +
                '}';
    }
}

