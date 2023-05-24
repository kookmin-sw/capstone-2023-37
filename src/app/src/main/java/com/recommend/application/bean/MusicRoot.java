package com.recommend.application.bean;

public class MusicRoot {
    private int code;
    private MusicData data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setData(MusicData data) {
        this.data = data;
    }
    public MusicData getData() {
        return data;
    }
}
