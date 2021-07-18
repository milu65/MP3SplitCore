package com.Task;

public class SplitTaskObject {
    private long id;
    private String userToken;
    private long begin;
    private long end;
    private String ref;

    public SplitTaskObject(long id, String userToken, long begin, long end, String ref) {
        this.id = id;
        this.userToken = userToken;
        this.begin = begin;
        this.end = end;
        this.ref = ref;
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
}
