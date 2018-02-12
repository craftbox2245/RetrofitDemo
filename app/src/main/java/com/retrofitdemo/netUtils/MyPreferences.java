package com.retrofitdemo.netUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CRAFT BOX on 1/23/2017.
 */

public class MyPreferences {

    Context context;

    public static String PreferenceName = "Demo";
    public static String EncraptionKey = "Demo@xyz12364";

    public static String UID = "u_id",refreshedtoken="refreshedtoken";



    public MyPreferences(Context context) {
        this.context = context;
    }

    String value = "";

    public String getPreferences(String key) {
        value = "";
        try {
            SharedPreferences channel = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            value = AESCrypt.decrypt("" + EncraptionKey, channel.getString("" + key, "").toString());
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
            return value;
        }
        return value;
    }

    public void setPreferences(String key, String value) {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putString("" + key, AESCrypt.encrypt("" + EncraptionKey, "" + value));
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearPreferences() {
        try {
            SharedPreferences settings = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            return settings.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
