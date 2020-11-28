package com.example.closet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class SplashActivity extends Activity {

    private final int MY_PERMISSION_REQUEST = 100;
    /**
     * Application permission 목록, android build target 23
     */

    public static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            checkPermission(MANDATORY_PERMISSIONS);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, navigationdrawer.class);
            startActivity(intent);
            finish();
        }
    }

    //권한 허용 묻기
    @TargetApi(23)
    private void checkPermission(String[] permissions) {
        requestPermissions(permissions, MY_PERMISSION_REQUEST);
    }

    /**
     * Application permission, android build target 23
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                int cnt = permissions.length;
                for (int i = 0; i < cnt; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                }
                break;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, navigationdrawer.class);
        startActivity(intent);
        finish();
    }
}
