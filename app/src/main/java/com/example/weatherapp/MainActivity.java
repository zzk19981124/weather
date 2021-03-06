package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import com.baidu.location.LocationClient;
import com.qweather.sdk.view.HeConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
   // private LocationClient mLocationClient = null;
    //private MyLocationListener locationListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //checkPermission();//检查所需权限
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null)!=null){
            Log.d("MainActivity------>", "");
            Intent intent = new Intent(this,CoolWeatherActivity.class);
            startActivity(intent);
            finish();
        }


        //initQWeather();//初始化和风天气sdk
        //mLocationClient = new LocationClient(getApplicationContext());
        //mLocationClient.registerLocationListener(s);
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
            ActivityCompat.requestPermissions(MainActivity.this, permissionss, PERMISSION_REQUEST);
        }
    }

    //初始化和风天气sdk
    private void initQWeather() {
        HeConfig.init("HE2108091349151897", "bb2f7903c86949c682fbd66095e75896");//账户初始化
        HeConfig.switchToDevService();//切换至和风的开发版服务

    }

    /**
     * 系统返回键，单击事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){

        }
            return super.onKeyDown(keyCode, event);
    }
}

