package com.heng.weather.DataStore;

/**
 * Created by heng on 2017/3/3.
 * 预测天气
 */

public class ForecastWeatherData {

    // 风向
    private String windDirection;
    // 风力
    private String windPower;
    // 高温
    private String highTemperature;
    // 低温
    private String lowTemperatur;
    // 天气类型
    private String weatherType;
    // 日期
    private String date;

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        this.highTemperature = highTemperature;
    }

    public String getLowTemperatur() {
        return lowTemperatur;
    }

    public void setLowTemperatur(String lowTemperatur) {
        this.lowTemperatur = lowTemperatur;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
