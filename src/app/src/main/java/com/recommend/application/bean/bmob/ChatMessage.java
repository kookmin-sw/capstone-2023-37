package com.recommend.application.bean.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ChatMessage extends BmobObject {
    private String content;
    private BmobFile bmobFile;

    /**
     * 所属聊天室
     */
    private ChatRoom chatRoom;

    /**
     * 发布者
     */
    private User author;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
