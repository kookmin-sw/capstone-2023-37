package com.recommend.application.bean;

import java.util.List;

public class WeatherJsonRootBean {


    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private long dt;
    private int timezone;
    private long id;
    private String name;
    private int cod;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }



    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }



    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getCod() {
        return cod;
    }


    public class Coord {

        private double lon;
        private double lat;

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLon() {
            return lon;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLat() {
            return lat;
        }

    }


    public class Weather {

        private int id;
        private String main;
        private String description;
        private String icon;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getMain() {
            return main;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

    }

    public class Main {

        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getTemp() {
            return temp;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getPressure() {
            return pressure;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getHumidity() {
            return humidity;
        }

    }


    public class Wind {

        private double speed;
        private int deg;

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }

        public int getDeg() {
            return deg;
        }

    }
}
