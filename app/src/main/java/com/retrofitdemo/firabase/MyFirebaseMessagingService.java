/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.retrofitdemo.firabase;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.retrofitdemo.GlobalElements;
import com.retrofitdemo.MainActivity;
import com.retrofitdemo.R;
import com.retrofitdemo.netUtils.MyPreferences;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    MyPreferences myPreferences;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            myPreferences = new MyPreferences(MyFirebaseMessagingService.this);

            if (!myPreferences.getPreferences(MyPreferences.UID).equals("")) {
                String user_id = remoteMessage.getData().get("user_id");
                if (myPreferences.getPreferences(MyPreferences.UID).equals("" + user_id) || user_id.equals("0")) {
                    String type = remoteMessage.getData().get("type");

                    if (type.equals("1")) {
                        String title = remoteMessage.getData().get("title");
                        String desc = remoteMessage.getData().get("description");
                        String follow_up_id = remoteMessage.getData().get("reference_id");
                        String reference_type = remoteMessage.getData().get("reference_type");

                        Random r = new Random();
                        int notificationId = r.nextInt(80 - 65) + 65;

                        Intent viewIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("type", "1");
                        b.putString("follow_up_id", "" + follow_up_id);
                        viewIntent.putExtras(b);

                        PendingIntent viewPendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, notificationId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                        int res_sound_id = MyFirebaseMessagingService.this.getResources().getIdentifier("notification", "raw", MyFirebaseMessagingService.this.getPackageName());
                        Uri defaultSoundUri = Uri.parse("android.resource://" + MyFirebaseMessagingService.this.getPackageName() + "/" + res_sound_id);

                        Notification notif = new Notification.Builder(MyFirebaseMessagingService.this)
                                .setContentTitle("" + GlobalElements.fromHtml("" + title))
                                .setContentText("" + GlobalElements.fromHtml("" + desc))
                                .setContentIntent(viewPendingIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(largeIcon)
                                .setSound(defaultSoundUri)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                .setAutoCancel(true)
                                .build();

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                        notificationManager.notify(notificationId, notif);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}