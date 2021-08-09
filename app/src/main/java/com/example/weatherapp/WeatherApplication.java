package com.example.weatherapp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * @author hello word
 * @desc 提供一种全局获取context的方式
 * @date 2021/8/9
 */
public class WeatherApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


}
