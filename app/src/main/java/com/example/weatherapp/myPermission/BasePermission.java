package com.example.weatherapp.myPermission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/20
 */
public class BasePermission {
    private Activity activity;
    public BasePermission(Activity activity){
        this.activity = activity;
    }

    public Boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public Boolean alreadyRequest(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
