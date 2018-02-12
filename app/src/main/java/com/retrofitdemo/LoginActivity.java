package com.retrofitdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.retrofitdemo.reciver.DownloadService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*try {
            Intent intent = new Intent(AccountActivity.this, DownloadService.class);
            intent.putExtra("file_url", "" + pdffile);
            intent.putExtra("file_name", "" + filename);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
