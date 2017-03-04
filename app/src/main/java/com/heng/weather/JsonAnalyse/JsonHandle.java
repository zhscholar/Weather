package com.heng.weather.JsonAnalyse;

import android.text.TextUtils;
import com.heng.weather.DataStore.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heng on 2017/3/3.
 */

public class JsonHandle {
    // 解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray provincesArray = new JSONArray(response);
                for (int i = 0; i < provincesArray.length();i++) {
                    JSONObject jsonObject = provincesArray.getJSONObject(i);
                    Province province = new Province();
                    // 省的代号
                    province.setProvinceCode(jsonObject.getInt("id"));
                    // 省的名字
                    province.setProvinceName(jsonObject.getString("name"));

                    // 保存数据
                    DataStoreHelper.saveProvince(province);
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 解析和处理服务器返回的城市数据
    public static boolean handleCityResponse(String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray cityArray = new JSONArray(response);
                for (int i = 0; i < cityArray.length();i++) {
                    JSONObject jsonObject = cityArray.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);

                    DataStoreHelper.saveCity(city);
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse (String response,int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try{
                JSONArray countyArray = new JSONArray(response);
                for (int i = 0;i < countyArray.length();i++) {
                    JSONObject jsonObject = countyArray.getJSONObject(i);
                    County county = new County();
                    county.setCityID(cityId);
                    county.setCountyCode(jsonObject.getInt("id"));
                    county.setCountyName(jsonObject.getString("name"));

                    DataStoreHelper.saveCounty(county);
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    // 解析服务器返回的温度Json数据
    public static AreaWeatherData handleAreaWeatherResponse (String response,String areaName) {
        if (!TextUtils.isEmpty(response)) {
            try{
                // 区域天气数据对象；
                AreaWeatherData areaWeatherData = new AreaWeatherData();
                // 当天天气数据对象
                TodayWeatherData todayWeatherData = new TodayWeatherData();
                // 预报天气数组
                List<ForecastWeatherData> forecastList = new ArrayList<ForecastWeatherData>();

                // 配置天气数据
                JSONObject sourceJsonObject = new JSONObject(response);
                String desc = sourceJsonObject.getString("desc");
                areaWeatherData.setRequestDesc(desc);

                if (desc.equals("OK")) {
                    JSONObject weatherData = sourceJsonObject.getJSONObject("data");
                    // 保存今天的天气数据
                    String todayTemperature = weatherData.getString("wendu");
                    String todayWeatherHint = weatherData.getString("ganmao");
                    areaWeatherData.setAreaName(weatherData.getString("city"));

                    todayWeatherData.setTemperature(todayTemperature);
                    todayWeatherData.setHealthTips(todayWeatherHint);

                    JSONArray sourceJsonArray = weatherData.getJSONArray("forecast");
                    for (int i = 0; i < sourceJsonArray.length();i++) {
                        ForecastWeatherData forecastWeatherData = new ForecastWeatherData();
                        JSONObject jsonObject = sourceJsonArray.getJSONObject(i);
                        if (i == 0) {
                            todayWeatherData.setWeatherType(jsonObject.getString("type"));
                            continue;
                        }
                        // 风向
                        forecastWeatherData.setWindDirection(jsonObject.getString("fengxiang"));
                        // 风力
                        forecastWeatherData.setWindPower(jsonObject.getString("fengli"));
                        // 高温
                        forecastWeatherData.setHighTemperature(jsonObject.getString("high"));
                        // 天气类型
                        forecastWeatherData.setWeatherType(jsonObject.getString("type"));
                        // 低温
                        forecastWeatherData.setLowTemperatur(jsonObject.getString("low"));
                        // 日期
                        forecastWeatherData.setDate(jsonObject.getString("date"));

                        forecastList.add(forecastWeatherData);
                    }

                    areaWeatherData.setTodayWeatherData(todayWeatherData);
                    areaWeatherData.setForecastWeatherList(forecastList);
                }
                return areaWeatherData;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
