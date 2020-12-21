package com.abbasmoharreri.computingaccount.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.abbasmoharreri.computingaccount.MainActivity;
import com.abbasmoharreri.computingaccount.database.BackupAndRestore;
import com.abbasmoharreri.computingaccount.static_value.PreferencesKeys;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class BackupService extends Service {

    private SharedPreferences preferences;

    private void createBackup() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int time = preferences.getInt(PreferencesKeys.BACKUP_DURATION, 0);
        final BackupAndRestore backupAndRestore = new BackupAndRestore();
        final Handler handler = new Handler();
        final Context context = this;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                backupAndRestore.exportDB(context);
                Log.e("casdasdasdas", "Backup");
                handler.postDelayed(this, time * 60 * 1000);
            }
        };
        handler.post(runnable);


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
        createBackup();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
