package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.w3c.dom.Text;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView mScrollView;

    private TextView titleCity;

    private TextView titleUpdate;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;
    private static final String TAG = "WeatherActivity";
    private List<GeoBean.LocationBean> locationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();//初始化各控件
        initQWeather();//初始化和风天气sdk
    }


    private void initView() {
        mScrollView = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_text);
        titleUpdate = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        QWeather.getGeoCityLookup(this, "北京", Range.CN, 10, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: "+throwable);
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                if (geoBean!=null)
                locationBean = geoBean.getLocationBean();
            }
        });
    }

    //初始化和风天气sdk
    private void initQWeather() {
        HeConfig.init("HE2108091349151897", "bb2f7903c86949c682fbd66095e75896");//账户初始化
        HeConfig.switchToDevService();//切换至和风的开发版服务
        QWeather.getWeatherNow(WeatherActivity.this, "CN101010100", Lang.ZH_HANS, Unit.METRIC,
                new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: "+throwable);
                    }

                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherNowBean));
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK == weatherNowBean.getCode()) {
                            WeatherNowBean.NowBaseBean now = weatherNowBean.getNow();
                        } else {
                            //在此查看返回数据失败的原因
                            Code code = weatherNowBean.getCode();
                            Log.i(TAG, "failed code: " + code);
                        }
                    }
                });
    }
}