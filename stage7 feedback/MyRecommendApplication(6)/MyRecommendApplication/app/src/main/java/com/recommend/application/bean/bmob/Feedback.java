package com.recommend.application.bean.bmob;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject {
    private String content;
    private User user;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
