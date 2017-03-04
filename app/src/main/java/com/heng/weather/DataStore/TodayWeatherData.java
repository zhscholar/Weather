package com.heng.weather.DataStore;

/**
 * Created by heng on 2017/3/3.
 * 今日天气
 */

public class TodayWeatherData {

    // 温度
    private String temperature;

    // 天气类型
    private String weatherType;

    // 健康提示
    private String healthTips;


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getHealthTips() {
        return healthTips;
    }

    public void setHealthTips(String healthTips) {
        this.healthTips = healthTips;
    }
}
