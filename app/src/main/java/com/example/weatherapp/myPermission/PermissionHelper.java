package com.example.weatherapp.myPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/20
 */
public class PermissionHelper {
    private Activity activity;
    private Context context;
    private String[] allPermissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private String[] permissionDescription = {
            "需要网络权限", "需要读取手机状态权限",
            "需要精准定位权限", "需要粗定位权限"
    };

    private BasePermission basePermission;
    private int requestCode;

    public PermissionHelper(Activity activity, int requestCode) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.requestCode = requestCode;
        this.basePermission = new BasePermission(activity);
    }

    public boolean checkAllPermissionGranted(){
        for (String permission: allPermissions){
            if (!basePermission.checkPermission(permission)) return false;
        }
        return true;
    }

    public void requestAllPermission(int requestCode){
        basePermission.requestPermissions(allPermissions, requestCode);
    }

    public void startRequestPermissions(){
        boolean allGranted = checkAllPermissionGranted();
        if (allGranted) {
            Toast.makeText(context, "已获取全部权限" ,Toast.LENGTH_LONG).show();
            return;
        }
        requestAllPermission(requestCode);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != this.requestCode) {
            return;
        }

        Set<String> description = new HashSet<>();
        // 判断是否所有的权限都已经授予了
        for (int i=0; i<grantResults.length; i++) {
            int grant = grantResults[i];
            if (grant != PackageManager.PERMISSION_GRANTED) {
                description.add(permissionDescription[i]);
            }
        }

        if (description.size() == 0) {
            // 如果所有的权限都授予了, 则执行备份代码
            Toast.makeText(context, "已获取全部权限" ,Toast.LENGTH_LONG).show();

        } else {
            // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
            Toast.makeText(context, "未获取全部权限" ,Toast.LENGTH_LONG).show();
            StringBuilder message = new StringBuilder();
            Iterator<String> itt = description.iterator();
            message.append(itt.next());
            while (itt.hasNext()) {
                message.append(",").append(itt.next());
            }
            message.append("。");
            openAppDetails(message.toString());
        }
    }

    private void openAppDetails(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
