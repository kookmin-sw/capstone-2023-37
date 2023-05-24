package com.recommend.application.bean;

import java.util.List;

public class NewsRoot {
    private int code;
    private String msg;
    private List<NewsData> data;
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

    public void setData(List<NewsData> data) {
        this.data = data;
    }
    public List<NewsData> getData() {
        return data;
    }


    @Override
    public String toString() {
        return "NewsRoot{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
