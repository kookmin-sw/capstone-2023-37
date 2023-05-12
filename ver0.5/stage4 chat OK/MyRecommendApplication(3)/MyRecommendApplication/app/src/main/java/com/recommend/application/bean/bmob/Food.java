package com.recommend.application.bean.bmob;



import com.recommend.application.bean.FoodProcess;

import java.io.Serializable;
import java.util.List;
import cn.bmob.v3.BmobObject;

public class Food extends BmobObject implements Serializable {

    private String name;
    private String peoplenum;
    private String preparetime;
    private String cookingtime;
    private String content;
    private String pic;
    private String tag;
    private List<FoodProcess> process;



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPeoplenum(String peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getPeoplenum() {
        return peoplenum;
    }

    public void setPreparetime(String preparetime) {
        this.preparetime = preparetime;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setFoodProcess(List<FoodProcess> process) {
        this.process = process;
    }

    public List<FoodProcess> getFoodProcess() {
        return process;
    }



}
