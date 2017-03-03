package com.heng.weather.JsonAnalyse;

import android.text.TextUtils;
import com.heng.weather.DataStore.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
}
