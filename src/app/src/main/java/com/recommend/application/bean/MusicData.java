package com.recommend.application.bean;

import cn.bmob.v3.BmobObject;

public class MusicData extends BmobObject {
    private String name;
    private String url;
    private String picurl;
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
    public String getPicurl() {
        return picurl;
    }


}
