package com.recommend.application.bean;

public class FoodRoot {


    private String code;
    private boolean charge;
    private String msg;
    private FoodResult result;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public boolean getCharge() {
        return charge;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setResult(FoodResult result) {
        this.result = result;
    }

    public FoodResult getResult() {
        return result;
    }





}
