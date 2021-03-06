package com.AudioSplitter.Task;

public class SplitTaskObject {
    private long id;
    private String userToken;
    private long begin;
    private long end;
    private String ref;
    private boolean isLocalFile=true;

    public SplitTaskObject(long id, String userToken, long begin, long end, String ref, boolean isLocalFile) {
        this.id = id;
        this.userToken = userToken;
        this.begin = begin;
        this.end = end;
        this.ref = ref;
        this.isLocalFile = isLocalFile;
    }

    public boolean isLocalFile() {
        return isLocalFile;
    }

    public void setLocalFile(boolean localFile) {
        isLocalFile = localFile;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setId(long id) {
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

    public long getId() {
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

    @Override
    public String toString() {
        return "SplitTaskObject{" +
                "id=" + id +
                ", userToken='" + userToken + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", ref='" + ref + '\'' +
                ", isLocalFile=" + isLocalFile +
                '}';
    }
}
