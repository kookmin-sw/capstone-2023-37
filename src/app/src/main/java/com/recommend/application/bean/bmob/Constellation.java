package com.recommend.application.bean.bmob;

import cn.bmob.v3.BmobObject;

public class Constellation extends BmobObject {
    private String date;
    private String name;
    private String health;
    private String all;
    private String love;
    private String money;
    private String work;
    private int month;
    private String resultcode;


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setHealth(String health) {
        this.health = health;
    }
    public String getHealth() {
        return health;
    }

    public void setAll(String all) {
        this.all = all;
    }
    public String getAll() {
        return all;
    }

    public void setLove(String love) {
        this.love = love;
    }
    public String getLove() {
        return love;
    }

    public void setMoney(String money) {
        this.money = money;
    }
    public String getMoney() {
        return money;
    }

    public void setWork(String work) {
        this.work = work;
    }
    public String getWork() {
        return work;
    }


    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }
    public String getResultcode() {
        return resultcode;
    }


}
