package com.heng.weather;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.heng.weather.Config.GlobalConfig;
import com.heng.weather.DataStore.AreaWeatherData;
import com.heng.weather.DataStore.ForecastWeatherData;
import com.heng.weather.DataStore.TodayWeatherData;
import com.heng.weather.JsonAnalyse.JsonHandle;
import com.heng.weather.Network.HttpUtil;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    // 滑动视图的布局
    private ScrollView weatherScrollView;

    // 城市（县）标题
    private TextView titleTextView;

    // 今天的温度
    private TextView temperatureTextView;
    // 今天的天气状况
    private TextView weacherTypeTextView;
    // 今天天气的健康提示
    private TextView healthTipTextView;

    // 预报天气所对应的布局
    private LinearLayout forecastLayout;


    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        String areaName = getIntent().getStringExtra("area_name");

        fetchWeatherData(areaName);

    }

    // 初始化控件
    public void initView(){
        weatherScrollView = (ScrollView)findViewById(R.id.weather_scrollview);
        titleTextView = (TextView)findViewById(R.id.title_city);
        temperatureTextView = (TextView)findViewById(R.id.today_text_temperature);
        weacherTypeTextView = (TextView)findViewById(R.id.today_text_weatherType);
        healthTipTextView = (TextView)findViewById(R.id.today_text_healthTip);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
    }

    // 获取对应于该城市的天气数据
    public void fetchWeatherData (String areaName){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("area_weather",null);
        if (weatherString != null) {
            // 有缓存则从本地获取天气数据
            AreaWeatherData areaWeatherData = JsonHandle.handleAreaWeatherResponse(weatherString,areaName);
            showWeacherInfo(areaWeatherData);

        }else {
            // 无缓存则从服务器获取天气数据
            forecastLayout.setVisibility(View.INVISIBLE);
            requestWeacherDataWithAreaName(areaName);
        }
    }


   // 从服务器获取天气数据
    public void requestWeacherDataWithAreaName (final String areaName) {
        showProgressDialog();
        String address = GlobalConfig.GCURLCityWeather + areaName;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(WeatherActivity.this,"从服务器获取天气数据失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final AreaWeatherData areaWeatherData = JsonHandle.handleAreaWeatherResponse(responseText,areaName);
                final String desc = areaWeatherData.getRequestDesc();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        if (desc != null && desc.equals("OK")) {
                            // 保存数据至本地
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("area_weather",responseText);
                            editor.apply();
                            showWeacherInfo(areaWeatherData);
                        }
                        else {
                            Toast.makeText(WeatherActivity.this,"从服务器获取天气数据失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // 显示天气数据
    public void showWeacherInfo(AreaWeatherData areaWeatherData) {
        // 设置城市标题
        titleTextView.setText(areaWeatherData.getAreaName());

        TodayWeatherData todayWeatherData = areaWeatherData.getTodayWeatherData();
        // 设置今天的气温数据内容
        temperatureTextView.setText(todayWeatherData.getTemperature() + "℃");
        weacherTypeTextView.setText(todayWeatherData.getWeatherType());
        healthTipTextView.setText(todayWeatherData.getHealthTips());

        forecastLayout.removeAllViews();
        for (ForecastWeatherData forecastWeatherData : areaWeatherData.getForecastWeatherList()) {
            View forecastView = LayoutInflater.from(this).inflate(R.layout.forecast_weather_item,forecastLayout,false);

            /// 配置预报天气内容
            // 日期
            TextView dateTextView = (TextView)forecastView.findViewById(R.id.weather_item_date);
            dateTextView.setText(forecastWeatherData.getDate());

            // 天气类型
            TextView typeTextView = (TextView)forecastView.findViewById(R.id.weather_item_type);
            typeTextView.setText(forecastWeatherData.getWeatherType());

            // 高温
            TextView highTextView = (TextView)forecastView.findViewById(R.id.weather_item_high);
            highTextView.setText(forecastWeatherData.getHighTemperature());

            // 低温
            TextView lowTextView = (TextView)forecastView.findViewById(R.id.weather_item_low);
            lowTextView.setText(forecastWeatherData.getLowTemperatur());

            // 风向
            TextView windDirectionTextView = (TextView)forecastView.findViewById(R.id.weather_item_windDirection);
            windDirectionTextView.setText(forecastWeatherData.getWindDirection());

            // 风力
            TextView windPowerTextView = (TextView)forecastView.findViewById(R.id.weather_item_windPower);
            windPowerTextView.setText(forecastWeatherData.getWindPower());

            forecastLayout.addView(forecastView);
        }

        forecastLayout.setVisibility(View.VISIBLE);

    }


    // 显示加载指示符
    private void showProgressDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null){
                    dialog = new ProgressDialog(WeatherActivity.this);
                    dialog.setMessage(WeatherActivity.this.getResources().getString(R.string.progress_hint));
                    dialog.setCanceledOnTouchOutside(false);
                }
                dialog.show();
            }
        });
    }

}
