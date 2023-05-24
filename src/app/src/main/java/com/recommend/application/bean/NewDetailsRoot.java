package com.recommend.application.bean;

public class NewDetailsRoot {

    private int code;
    private String msg;
    private NewDetailsData data;
    private long time;
    private int usage;
    private String log_id;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(NewDetailsData data) {
        this.data = data;
    }
    public NewDetailsData getData() {
        return data;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public long getTime() {
        return time;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }
    public int getUsage() {
        return usage;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
    public String getLog_id() {
        return log_id;
    }
}
