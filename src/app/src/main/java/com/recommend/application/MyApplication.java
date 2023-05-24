package com.recommend.application;

import android.app.Activity;
import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.recommend.application.bean.WeatherBean;
import com.recommend.application.bean.bmob.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;

public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;
    private User user;
    private Set<Activity> activitySet = new HashSet<>();
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

    public void addActivity(Activity baseActivity) {
        activitySet.add(baseActivity);
    }

    public void finishActivity(){
        for (Activity a:activitySet){
            a.finish();
        }
        System.exit(0);
    }

    public String getToken() {
        return "LwExDtUWhF3rH5ib";
    }

    public String getApiKey() {
        return "da39dce4f8aa52155677ed8cd23a6470";
    }

    public List<WeatherBean> getWeatherList() {
        List<WeatherBean> weatherBeanList = new ArrayList<>();
        weatherBeanList.add(new WeatherBean("Seoul","37.57786","126.96350"));
        weatherBeanList.add(new WeatherBean("Tokyo","35.73670","139.78661"));
        weatherBeanList.add(new WeatherBean("BeiJing","39.87773","116.38471"));
        weatherBeanList.add(new WeatherBean("ShangHai","31.26287 ","121.50970"));

        return weatherBeanList;
    }

    public List<String> getConstellationList() {
        List<String> stringList = new ArrayList<>();
        //创建12星座数据
        stringList.add("Aquarius      (1/20-2/18)");
        stringList.add("Pisces        (2/19-3/20)");
        stringList.add("Aries         (3/21-4/20)");
        stringList.add("Taurus        (4/21-5/20)");
        stringList.add("Gemini        (5/21-6/21)");
        stringList.add("Cancer        (6/22-7/22)");
        stringList.add("Leo           (7/23-8/22)");
        stringList.add("Virgo         (8/23-9/22)");
        stringList.add("Libra         (9/23-10/23)");
        stringList.add("Scorpio       (10/24-11/22)");
        stringList.add("Sagittarius   (11/23-12/21)");
        stringList.add("Capricorn     (12/22-1/20)");
        return stringList;
    }
}
