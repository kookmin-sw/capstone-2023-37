package com.recommend.application.bean;


import java.io.Serializable;

public class FoodProcess  implements Serializable {

    private String pcontent;
    private String pic;


    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }
    public String getPcontent() {
        return pcontent;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getPic() {
        return pic;
    }

}