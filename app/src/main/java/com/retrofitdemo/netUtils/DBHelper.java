package com.retrofitdemo.netUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;


import com.retrofitdemo.GlobalElements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "test.db";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;

    public static String TEST = "test";
    DecimalFormat doubleFormat;
    MyPreferences myPreferences;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        myPreferences = new MyPreferences(context);
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        doubleFormat = new DecimalFormat("#");
        doubleFormat.setMinimumFractionDigits(0);
    }

    public void exportDB(Context con) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh;mm a");
        String Datetime = sdf.format(c.getTime());

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + DB_NAME;
        String backupDBPath = "" + GlobalElements.directory + DB_NAME + " " + Datetime;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            /*try {
                encryptfile(""+backupDB, "154",con);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }*/
            Toast.makeText(con, "DB Exported in Sdcard or Inter Storage!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    public void importDB(File currentDB, String db_name) {
        // TODO Auto-generated method stub
        try {
            decrypt("" + currentDB, "154", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sagar/database/" + db_name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + DB_NAME;
                String backupDBPath = GlobalElements.directory + "" + db_name;
                File backupDB = new File(data, currentDBPath);
                File currentDB1 = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB1).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                /*   */
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + GlobalElements.directory + db_name);
                boolean deleted = file.delete();
                Toast.makeText(context, "Import successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void encryptfile(String path, String Pass, Context con) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        try {
            String attachment = "" + path.concat(".crypt");
            String salt = "t784";
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(path.concat(".crypt"));
            byte[] key = (salt + Pass).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
            cos.close();
            fis.close();
            File file = new File(path);
            boolean deleted = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String path, String password, String outPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String salt = "t784";
        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream(outPath);
        byte[] key = (salt + password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        //------------------------------------------------------------
        PackageInfo pinfo = null;
        if (!dbExist) {
            getReadableDatabase();
            copyDataBase();
        } else {
            System.out.print("");
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery(Query, null);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* todo delete table*/
    public boolean DeleteTable(String table_name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db.delete(table_name, "1", null) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* todo delete perticuler Row */
    public boolean DeleteRow(String table_name, String column_name, String id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(table_name, "" + column_name + "=" + id, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String rp_getvalue(String table_name, String column_name, String where) {
        try {
            Cursor c2 = getData("select * from " + table_name + " where " + where + " ");
            if (c2.getCount() > 0) {
                if (c2.moveToNext()) {
                    String value = "" + c2.getString(c2.getColumnIndex("" + column_name));
                    c2.close();
                    db.close();
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return "0";
        }
        return "0";
    }

    public void addTest(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", "1");
        values.put("name", "hardip");

        long _id = -1;
        if (id.equals("" + rp_getvalue(TEST, "id", "id=" + id))) {
            _id = db.update(TEST, values, "server_id=" + id, new String[]{});
            id = "" + rp_getvalue(TEST, "id", "server_id=" + id);
        } else {
            _id = db.insert(TEST, null, values);
            id = "" + id;
        }
    }

}