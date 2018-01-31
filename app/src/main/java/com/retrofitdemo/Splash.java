package com.retrofitdemo;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.retrofitdemo.netUtils.MyPreferences;
import com.retrofitdemo.netUtils.RuntimePermissionsActivity;

import java.io.File;

public class Splash extends RuntimePermissionsActivity {

    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myPreferences = new MyPreferences(Splash.this);
        /* todo 31-10-2017 create project date */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.super.requestAppPermissions(new
                                String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }, R.string.runtime_permissions_txt
                        , 20);

            }
        }, 1000);

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "" + GlobalElements.directory);
        if (!folder.exists()) {
            folder.mkdir();
        }

        Intent i = new Intent(Splash.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
