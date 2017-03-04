package com.heng.weather.DataStore;

import java.util.List;

/**
 * Created by heng on 2017/3/4.
 * 区域温度数据
 */

public class AreaWeatherData {

    // 区域名
    private String areaName;

    // 区域数据对应的服务器响应状态
    private String requestDesc;
    // 区域对应的当天温度数据
    private TodayWeatherData todayWeatherData;
    // 区域对应的预测天气数据表
    private List<ForecastWeatherData> ForecastWeatherList;


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }

    public TodayWeatherData getTodayWeatherData() {
        return todayWeatherData;
    }

    public void setTodayWeatherData(TodayWeatherData todayWeatherData) {
        this.todayWeatherData = todayWeatherData;
    }

    public List<ForecastWeatherData> getForecastWeatherList() {
        return ForecastWeatherList;
    }

    public void setForecastWeatherList(List<ForecastWeatherData> forecastWeatherList) {
        ForecastWeatherList = forecastWeatherList;
    }

}
