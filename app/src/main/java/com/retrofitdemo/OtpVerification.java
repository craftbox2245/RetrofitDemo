package com.retrofitdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.retrofitdemo.netUtils.RetrofitClient;

public class OtpVerification extends AppCompatActivity {
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle b = intent.getExtras();
                    String sender = b.getString("sender");
                    if (sender.indexOf("" + RetrofitClient.message_pack_name) > 0) {
                        String message = b.getString("message");
                        //forgotOtp.setText("" + message.replaceAll("\\D+", ""));
                        abortBroadcast();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.setPriority(1);
            intentFilter.addAction("com.retrofitdemo.reciver.onMessageReceived");
            registerReceiver(receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();

    }
}
