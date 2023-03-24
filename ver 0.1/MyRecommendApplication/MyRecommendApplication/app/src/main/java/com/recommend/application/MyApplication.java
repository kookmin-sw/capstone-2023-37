package com.recommend.application;

import android.app.Application;

import com.recommend.application.bean.User;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    private static MyApplication instance;
    private User user;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        Bmob.resetDomain("http://bmobsdk3.2user3.15456.cn/8/");
        Bmob.initialize(this, "163378b82dd2d8a1cc650b36f2619ec6");

    }

    public User getUser() {
        return user;
    }

    public void setLoginUser(User user) {
        this.user = user;
    }
}
