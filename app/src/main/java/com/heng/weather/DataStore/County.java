package com.heng.weather.DataStore;

/**
 * Created by heng on 2017/3/2.
 */

public class County {

    // 县的ID
    private int countyCode;

    // 县的名字
    private String countyName;

    // 县对应的天气ID
    private int weatherId;

    // 县从属的市ID
    private int cityID;

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
