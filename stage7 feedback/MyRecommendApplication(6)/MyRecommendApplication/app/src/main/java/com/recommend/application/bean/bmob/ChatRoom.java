package com.recommend.application.bean.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class ChatRoom extends BmobObject {
    private String name;
    private User createUser;
    private String remake;
    private int maxNum; //最大人数
    /**
     * 一对多关系：用于存储所有用户
     */
    private BmobRelation users;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }


    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public BmobRelation getUsers() {
        return users;
    }

    public void setUsers(BmobRelation users) {
        this.users = users;
    }
}
