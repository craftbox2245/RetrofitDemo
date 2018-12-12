package com.retrofitdemo.reciver;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.retrofitdemo.GlobalElements;
import com.retrofitdemo.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * Created by CRAFT BOX on 1/1/2018.
 */

public class DownloadService extends IntentService {

    String file_name, file_url;
    Handler mHandler;
    String CHANNEL_ID = "my_channel_01";
    CharSequence name = "abc";// The user-visible name of the channel.

    public DownloadService() {
        super("Download Service");
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // Don't let this service restart automatically if it has been stopped by the OS.
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mHandler.post(new DisplayToast1(this, "Hello World!"));
            file_url = intent.getStringExtra("file_url").replaceAll(" ", "%20");
            File folder = new File(file_url);
            file_name = folder.getName();
            FileDownloadMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FileDownloadMethod() {
        new DownloadFileFromURL("" + file_url, "" + file_name).execute();
    }

    class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> {
        int notificationId;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        String file_success = "";

        private String file_url, file_name;

        public DownloadFileFromURL(String file_url, String file_name) {
            super();
            this.file_url = file_url;
            this.file_name = file_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Random r = new Random();
            notificationId = r.nextInt(80 - 65) + 65;
            Intent viewIntent = new Intent();
            viewIntent.setAction(Intent.ACTION_VIEW);
            File folder = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            //File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "" + GlobalElements.directory_document);
            String file_path = folder + "/" + file_name;
            String extension_file = "jpg";
            try {
                String[] extension = file_name.split("\\.");
                extension_file = extension[1];
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri contentUri = null;
            if (GlobalElements.getVersionCheck()) {
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(DownloadService.this, "" + GlobalElements.fileprovider_path, new File("" + file_path));
            } else {
                contentUri = Uri.fromFile(new File("" + file_path));
            }

            viewIntent.setDataAndType(contentUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("" + extension_file));  // you can also change jpeg to other types
            PendingIntent viewPendingIntent = PendingIntent.getActivity(DownloadService.this, notificationId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotifyManager = (NotificationManager) DownloadService.this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder = new NotificationCompat.Builder(DownloadService.this, CHANNEL_ID);
                mBuilder.setContentTitle("Download")
                        .setContentText("Download in progress")
                        .setContentIntent(viewPendingIntent)
                        .setChannelId("my_channel_01")
                        .setOnlyAlertOnce(true)
                        .setSmallIcon(R.drawable.ic_file_download_black_24dp);

                mBuilder.setProgress(100, 0, false);
                mBuilder.setAutoCancel(true);
                mNotifyManager.notify(notificationId, mBuilder.build());

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = null;
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mNotifyManager.createNotificationChannel(mChannel);
                mNotifyManager.notify(notificationId, mBuilder.build());
            } else {
                mBuilder = new NotificationCompat.Builder(DownloadService.this, CHANNEL_ID);
                mBuilder.setContentTitle("Download")
                        .setContentText("Download in progress")
                        .setContentIntent(viewPendingIntent)
                        .setChannelId("my_channel_01")
                        .setOnlyAlertOnce(true)
                        .setSmallIcon(R.drawable.ic_file_download_black_24dp);
                mBuilder.setProgress(100, 0, false);
                mBuilder.setAutoCancel(true);
                mNotifyManager.notify(notificationId, mBuilder.build());
            }

        }

        @Override
        protected Integer doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(file_url);
                URLConnection conection = url.openConnection();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                conection.connect();

                conection.setConnectTimeout(5 * 60000);
                conection.setReadTimeout(5 * 60000);
                // this will be useful so that you can show a tipical 0-100% progress bar
               /* File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "" + GlobalElements.directory_document);
                if (!folder.exists()) {
                    folder.mkdir();
                }*/
                File folder = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(folder + "/" + file_name);
                byte data[] = new byte[1024];
                for (int i = 0; i <= 100; i += 5) {
                    publishProgress(Math.min(i, 100));
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                file_success = "success";
            } catch (Exception e) {
                e.printStackTrace();
                file_success = "Server Time Out";
            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(Integer... values) {
            // setting progress percentage
            mBuilder.setProgress(100, values[0], false);
            mNotifyManager.notify(notificationId, mBuilder.build());
            super.onProgressUpdate(values);
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Integer file_url) {
            try {
                mBuilder.setContentText("Download complete");
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(notificationId, mBuilder.build());
                if (file_success.equals("success")) {
                    Toast.makeText(DownloadService.this, "Download Successfully In Your Download Folder", Toast.LENGTH_LONG).show();
                } else {
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationId);
                    Toast.makeText(DownloadService.this, "" + file_success, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DisplayToast1 implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast1(Context mContext, String text) {
            this.mContext = mContext;
            mText = text;
        }

        public void run() {
            Toast.makeText(mContext, "Your download in progress please check notification bar for progress", Toast.LENGTH_LONG).show();
        }
    }
}
