package com.abbasmoharreri.computingaccount.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.abbasmoharreri.computingaccount.static_value.PreferencesKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


/**
 * This Class for create backup and restoring it to DB
 * @version 1.0
 * */
public class BackupAndRestore {

    PackageManager packageManager;
    PackageInfo packageInfo;
    SharedPreferences sharedPreferences;

    /**
     * This method for restoring backup to DB
     * @param context (Type = Context) context of the app
     * @param address (Type = String) address in storage for saving backup
     * */
    public void importDB(Context context, String address) {

        try {
            File sd = new File(address);
            sd.setWritable(true,false);

            if (sd.canRead()) {
                File backupDB = context.getDatabasePath(DataBase.DATABASE_NAME);
                //String backupDBPath = String.format("%s.bak", DataBase.DATABASE_NAME);
                File currentDB = new File(address);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Log.i("Backup State: ", "Import Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method for creating backup from DB
     * @param context (Type = Context) context of the app
     * */
    public void exportDB(Context context) {


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String address = sharedPreferences.getString(PreferencesKeys.BACKUP_ADDRESS, "");

        try {
            File sd = new File(address);
            sd.setWritable(true,false);

            if (sd.canWrite()) {
                String backupDBPath = String.format("%s.bak", DataBase.DATABASE_NAME);
                File currentDB = context.getDatabasePath(DataBase.DATABASE_NAME);
                File backupDB = new File(address, backupDBPath);

                Log.e("Addres backupDBPath", backupDBPath);
                Log.e("Addres backupDb", backupDB.getAbsolutePath());
                Log.e("Addres backupDb", backupDB.getCanonicalPath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Log.i("Backup State: ", "Backup Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}