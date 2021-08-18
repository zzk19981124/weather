package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.example.weatherapp.gson.Weather;
import com.example.weatherapp.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CoolWeatherActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
    private LocationClient mLocationClient = null;
    //private double latitude;//纬度
    //private double longitude;//经度
    //private MyLocationListener mLocationListener = new MyLocationListener();

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdate;

    private TextView dataUpdate;//当用户进入首页时，“正在获取数据”，获取到了之后，“获取成功”，然后消失

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private static final String TAG = "CoolWeatherActivity";

    //private CityInformation cityInfo = new CityInformation();//放置经纬度、城市、街道信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();//检查所需权限
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            //顶部窗口视图
            View decorView = getWindow().getDecorView();
            //设置状态栏的可见性
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            //设置状态栏的颜色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        initView();//初始化各控件

        newSharedPreferences();//存储天气信息
    }

    private void newSharedPreferences() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时，直接解析天气数据
            //Weather
        } else {
            //无天气信息时，去服务器查询天气

        }
    }

    /**
     * 根据天气id，请求城市天气信息
     * <p>
     * https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/114.30425,22.745263/weather.json
     * <p>
     * 实况天气接口 ：
     * https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/114.30425,22.745263/realtime.json
     */
    public void requestWeather(final String locationId) {
        String weatherUrl = "https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/" +
                locationId + "/realtime.json";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CoolWeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void initView() {
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_text);
        titleUpdate = findViewById(R.id.title_update_time);
        dataUpdate = findViewById(R.id.update_text);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
    }

    //检查app权限,如果判断app没有获取到该权限，添加到list，然后跳转到系统权限申请界面，让用户去手动打开权限
    private void checkPermission() {
        List<String> mPermissionList = new ArrayList<>();
        String[] permissions = {Manifest.permission.INTERNET,//访问网络，定位需要上网
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                mPermissionList.add(permission);
        }
        if (!mPermissionList.isEmpty()) {
            String[] permissionss = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(CoolWeatherActivity.this, permissionss, PERMISSION_REQUEST);
        }
    }
}