package com.heng.weather.DataStore;

/**
 * Created by heng on 2017/3/2.
 */

public class City {

    // ID字段
    private int id;

    // 城市名
    private String cityName;

    // 城市编号
    private int cityCode;

    // 城市所属的省份ID
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
