package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.weatherapp.gson.Weather;
import com.example.weatherapp.util.HttpUtil;
import com.example.weatherapp.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CoolWeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST = 1;
    private LocationClient mLocationClient = null;
    //private double latitude;//纬度
    //private double longitude;//经度
    //private MyLocationListener mLocationListener = new MyLocationListener();

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefreshLayout;

    private Button navBtn;

    private ImageView bingPicImg;

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

    private String weatherId;//经纬度数据

    private static final String APIKey = "f3773b3c742e43738f6bfc0f28be3a07";

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

        initBaiDuLocation();//初始化百度sdk , 将所需城市信息放入SharedPreferences中

        //SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);

        SharedPreferences pref1 = this.getSharedPreferences("weather",MODE_PRIVATE);  //天气
        SharedPreferences pref2 = this.getSharedPreferences("location",MODE_PRIVATE);   //经纬度

        //SharedPreferences prefs = this.getPreferences("")
        String weatherString = pref1.getString("weather", null);

        if (weatherString != null) {
            //有缓存时，直接解析天气数据
            //Weather
            Weather weather = Utility.handleWeatherResponse(weatherString);
        } else {
            //无天气信息时，去服务器查询天气
            weatherId = pref2.getString("location", "");
            //滚动条设置为不可见的
            //weatherLayout.setVisibility(View.INVISIBLE);  // 会使布局消失
            requestWeather(weatherId);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(CoolWeatherActivity.this, "更新天气数据中~", Toast.LENGTH_SHORT).show();
                requestWeather(weatherId);
                //bingPicImg.setVisibility(View.INVISIBLE);//关闭壁纸
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
        //插入bing每日一图放入imageView
        String bingPic = pref1.getString("bingPic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 根据天气id，请求城市天气信息
     * <p>
     * https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/114.30425,22.745263/weather.json
     * <p>
     * 实况天气接口 ：
     * https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/114.30425,22.745263/realtime.json
     * <p>
     * https://api.caiyunapp.com/v2.5/ybYlZVR7znW79DfN/114.30425,22.745263/forecast.json
     * <p>
     * 和风天气 实时天气预报
     * https://devapi.qweather.com/v7/weather/now?key=f3773b3c742e43738f6bfc0f28be3a07&location=114.30425,22.745263   √
     * <p>
     * 逐小时天气预报：
     * https://devapi.qweather.com/v7/weather/24h?key=f3773b3c742e43738f6bfc0f28be3a07&location=114.30425,22.745263
     */
    public void requestWeather(final String locationId) {
        if (locationId == null) {
            Log.d(TAG, "requestWeather: --->未获取到经纬度信息");
            return;
        }
        String weatherUrl = "https://devapi.qweather.com/v7/weather/now?key=" + APIKey + "&location=" + locationId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string(); // 这个只能调用一次
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "200".equals(weather.code)) {
                            //if (weather!=null ){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(CoolWeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                        } else {
                            Toast.makeText(CoolWeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false); // 关闭下拉刷新效果
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CoolWeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        //关闭下拉刷新控件
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        bingPicImg = findViewById(R.id.bing_pic_img);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_text);
        titleUpdate = findViewById(R.id.title_update_time);
        //dataUpdate = findViewById(R.id.update_text);//暂时不用
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

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor edi = PreferenceManager.getDefaultSharedPreferences(CoolWeatherActivity.this).edit();
                edi.putString("bingPic", bingPic);
                edi.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(CoolWeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    //初始化百度sdk
    private void initBaiDuLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);//注册监听函数
        LocationClientOption option = new LocationClientOption(); //配置定位sdk的参数
        option.setScanSpan(0);//仅定位一次
        option.setOpenGps(true);//使用GPS
        option.setIsNeedAddress(true);//设置为需要地址信息
        option.setLocationNotify(true);//GPS 1s/1次
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //获取到当前位置的经纬度，需要开启GPS。
    public BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = bdLocation.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = bdLocation.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息

            //使用sharedPreferences,存储获取到的定位数据
            SharedPreferences.Editor editor = getSharedPreferences("location", 0).edit();
            //.putString("longitude",String.valueOf(longitude));
            //editor.putString("latitude",String.valueOf(latitude));
            //editor.putString("city",city);
            // editor.putString("district",district);
            //editor.putString("street",street);
            String locationData = longitude + "," + latitude;
            editor.putString("location", locationData);
            editor.apply();
            Log.d(TAG, "百度sdk获取经纬度 --->"+locationData);  //打印出了很多行
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_button:
                drawerLayout.openDrawer(GravityCompat.START);//显示侧滑菜单
                break;
            default:
                break;
        }
    }

    /**
     * 处理并展示weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName;
        String updateTime;
        String degree = "℃";
        String weatherInfo;
    }
}













































