package com.Task;

public class SplitTaskObject {
    private int id;
    private String userToken;
    private long begin;
    private long end;
    private boolean isLocalFile;
    private String ref;

    public SplitTaskObject(int id, String userToken, long begin, long end, boolean isLocalFile, String ref) {
        this.id = id;
        this.userToken = userToken;
        this.begin = begin;
        this.end = end;
        this.isLocalFile = isLocalFile;
        this.ref = ref;
    }

    public boolean isLocalFile() {
        return isLocalFile;
    }

    public void setLocalFile(boolean localFile) {
        this.isLocalFile = localFile;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public String getUserToken() {
        return userToken;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }
}
