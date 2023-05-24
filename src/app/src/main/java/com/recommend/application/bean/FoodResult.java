package com.recommend.application.bean;

import com.recommend.application.bean.bmob.Food;

import java.util.List;

public class FoodResult {
    private String status;
    private String msg;
    private Result result;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public class Result {

        private String num;
        private List<Food> list;

        public void setNum(String num) {
            this.num = num;
        }

        public String getNum() {
            return num;
        }

        public void setList(List<Food> list) {
            this.list = list;
        }

        public List<Food> getList() {
            return list;
        }

    }









}
