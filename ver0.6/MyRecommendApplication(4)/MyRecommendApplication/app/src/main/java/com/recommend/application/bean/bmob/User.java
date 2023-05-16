package com.recommend.application.bean.bmob;


import android.text.TextUtils;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject implements Serializable {
    private String nickName;
    private String phone;
    private String pwd;
    private int gender;
    private String headPicture;
    private String introduction;

    public String getNickName() {
        if (TextUtils.isEmpty(nickName))
            nickName = "No nickname";
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
