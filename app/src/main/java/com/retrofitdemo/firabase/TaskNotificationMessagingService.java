package com.retrofitdemo.firabase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.retrofitdemo.GlobalElements;
import com.retrofitdemo.MainActivity;
import com.retrofitdemo.R;
import com.retrofitdemo.netUtils.MyPreferences;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by CRAFT BOX on 11/22/2016.
 */

public class TaskNotificationMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    MyPreferences myPreferences;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            myPreferences = new MyPreferences(TaskNotificationMessagingService.this);
            String user_id = remoteMessage.getData().get("user_id");
            if (myPreferences.getPreferences(MyPreferences.UID).equals("" + user_id)) {

                String image_path = remoteMessage.getData().get("notification_icon");
                String title = remoteMessage.getData().get("title");
                String desc = remoteMessage.getData().get("description");

                if (image_path.equals("")) {
                    if (myPreferences.getPreferences(MyPreferences.UID).equals("" + user_id)) {
                        Random r = new Random();
                        int notificationId = r.nextInt(80 - 65) + 65;
                        Intent viewIntent = new Intent(TaskNotificationMessagingService.this, MainActivity.class);
                        viewIntent.putExtra("type", "2");
                        viewIntent.setFlags(viewIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent viewPendingIntent = PendingIntent.getActivity(TaskNotificationMessagingService.this, notificationId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                        // todo in oreo
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            String CHANNEL_ID = "my_channel_01";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = null;
                            Notification notif = new Notification.Builder(TaskNotificationMessagingService.this, CHANNEL_ID)
                                    .setContentTitle("" + GlobalElements.fromHtml("" + title))
                                    .setStyle(new Notification.BigTextStyle().bigText("" + GlobalElements.fromHtml("" + desc.replaceAll("\n", "<br>"))))
                                    .setContentIntent(viewPendingIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(largeIcon)
                                    .setChannelId(CHANNEL_ID)
                                    .setAutoCancel(true)
                                    .build();
                            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
                            mChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                            mNotificationManager.createNotificationChannel(mChannel);
                            mNotificationManager.notify(notificationId, notif);
                        } else {
                            Notification notif = new Notification.Builder(TaskNotificationMessagingService.this)
                                    .setContentTitle("" + GlobalElements.fromHtml("" + title))
                                    .setStyle(new Notification.BigTextStyle().bigText("" + GlobalElements.fromHtml("" + desc.replaceAll("\n", "<br>"))))
                                    .setContentIntent(viewPendingIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(largeIcon)
                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                    .setAutoCancel(true)
                                    .build();
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TaskNotificationMessagingService.this);
                            notificationManager.notify(notificationId, notif);
                        }
                        //todo oreo complete
                        /*if (myPreferences.getPreferences(MyPreferences.notification_count).equals("")) {
                            myPreferences.setPreferences(MyPreferences.notification_count, "1");
                        } else {
                            myPreferences.setPreferences(MyPreferences.notification_count, "" + (Integer.parseInt("" + myPreferences.getPreferences(MyPreferences.notification_count)) + 1));
                        }*/
                        Intent intent = new Intent();
                        intent.setAction("com.samajdata.onMessageReceived");
                        sendOrderedBroadcast(intent, null);
                    }
                } else {
                    abc(image_path, title, desc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abc(String image_path, String title, String desc) {
        new generatePictureStyleNotification(TaskNotificationMessagingService.this, "" + title, "" + desc,
                "" + image_path).execute();
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        private Context mContext;
        private String title, desc, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            desc = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                Log.e("=>>", "" + e.getLocalizedMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent viewIntent = new Intent(TaskNotificationMessagingService.this, MainActivity.class);
                viewIntent.putExtra("type", "2");
                viewIntent.setFlags(viewIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent contentIntent = PendingIntent.getActivity(TaskNotificationMessagingService.this, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Random r = new Random();
                int notificationId = r.nextInt(80 - 65) + 65;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    contentIntent = PendingIntent.getActivity(TaskNotificationMessagingService.this, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationChannel notificationChannel = new NotificationChannel("" + notificationId, "" + notificationId, NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableLights(true);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                NotificationCompat.Builder notification = new NotificationCompat.Builder(TaskNotificationMessagingService.this, "" + notificationId)
                        .setContentTitle(title)
                        .setContentText(GlobalElements.fromHtml(desc))
                        .setAutoCancel(true)
                        .setLargeIcon(result)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher);
                notificationManager.notify(notificationId, notification.build());

                /*if (myPreferences.getPreferences(MyPreferences.notification_count).equals("")) {
                    myPreferences.setPreferences(MyPreferences.notification_count, "1");
                } else {
                    myPreferences.setPreferences(MyPreferences.notification_count, "" + (Integer.parseInt("" + myPreferences.getPreferences(MyPreferences.notification_count)) + 1));
                }*/
                Intent intent = new Intent();
                intent.setAction("com.samajdata.onMessageReceived");
                sendOrderedBroadcast(intent, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
