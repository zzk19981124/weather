package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
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
    private static final String TAG = "WeatherActivity";
    private List<GeoBean.LocationBean> locationBean;
    //private Boolean isGetWeather = false;
    private CityInformation cityInfo = new CityInformation();//放置经纬度、城市、街道信息

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    /*while (isGetWeather == false) {
                    }*/
                    getWeatherInfo(String.valueOf(msg.obj));
                    mLocationClient.stop();
                    break;
                case 2:
                    //titleCity.setText(String.valueOf(msg.obj));
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();//检查所需权限
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();//初始化各控件
        initBaiDuLocation();//初始化百度sdk
        initQWeather();//初始化和风天气sdk

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String location = cityInfo.getLongitude() +","+cityInfo.getLatitude();
                getWeatherInfo(location);
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
        option.setLocationNotify(true);//GPS 1s/1次
        mLocationClient.setLocOption(option);
        mLocationClient.start();
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

        /*QWeather.getGeoCityLookup(this, "北京", Range.CN, 10, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: " + throwable);
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                if (geoBean != null)
                    locationBean = geoBean.getLocationBean();
            }
        });*/

    }

    //初始化和风天气sdk
    private void initQWeather() {
        HeConfig.init("HE2108091349151897", "bb2f7903c86949c682fbd66095e75896");//账户初始化
        HeConfig.switchToDevService();//切换至和风的开发版服务

    }

    //获取天气信息    公制单位，主要是摄氏度
    private void getWeatherInfo(String location) {
        QWeather.getWeatherNow(WeatherActivity.this, location, Lang.ZH_HANS, Unit.METRIC,
                new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: " + throwable);
                    }

                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        //isGetWeather = true;
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
            ActivityCompat.requestPermissions(WeatherActivity.this, permissionss, PERMISSION_REQUEST);
        }
    }

    /**
     * 系统返回键，单击事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

        }
        return super.onKeyDown(keyCode, event);
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

            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息


            cityInfo.setLatitude(latitude);
            cityInfo.setLongitude(longitude);
            cityInfo.setDistrict(district);
            cityInfo.setStreet(street);
            if (longitude != 0 && latitude != 0) {
                Message message1 = new Message();
                message1.what = 1;
                message1.obj = longitude + "," + latitude;
                mHandler.sendMessage(message1);
            }

            if (district != null && street != null) {
                Message message2 = new Message();
                message2.what = 2;
                message2.obj = district + " " + street;
                mHandler.sendMessage(message2);
            }
        }
    };

}