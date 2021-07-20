package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.weatherapp.myPermission.PermissionHelper;

public class MainActivity extends AppCompatActivity {
    private PermissionHelper permissionHelper;
    private final int MY_PERMISSION_REQUEST_CODE = 404;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查需要满足的动态权限，如果没有打开权限，会弹窗提示需要用户打开哪些权限
        permissionHelper = new PermissionHelper(this, MY_PERMISSION_REQUEST_CODE);
        permissionHelper.startRequestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}