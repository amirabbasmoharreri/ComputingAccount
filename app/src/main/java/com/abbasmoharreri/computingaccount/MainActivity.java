package com.abbasmoharreri.computingaccount;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.abbasmoharreri.computingaccount.service.BackupService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class MainActivity extends BaseActivity {


    private SharedPreferences preferences;
    NotificationManager notifManager;
    boolean serviceStart = false;
    String offerChannelId = "Offers";

    static Configuration configuration;
    static Resources resources;
    static DisplayMetrics displayMetrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.Light_AppTheme);
        }
        setLanguage();
        setContentView(R.layout.activity_main);
        setNavigationMenu();

    }


    private void setLanguage() {
        resources = getResources();
        configuration = resources.getConfiguration();
        displayMetrics = resources.getDisplayMetrics();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString("language", "en");
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration, displayMetrics);
    }


    @Override
    protected void onResume() {
        super.onResume();

        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        speechNotification();

        if (!isMyServiceRunning(BackupService.class)) {
            final Context context = getApplicationContext();

            verifyStoragePermissions(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, BackupService.class);
                    startService(intent);
                }
            });
            serviceStart = true;
        }


        verifyInternetPermissions(new Runnable() {
            @Override
            public void run() {

            }
        });

        verifySmsPermissions(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String offerChannelName = "Speech to Text";
            String offerChannelDescription = "writing speech to text";
            int offerChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notifChannel = new NotificationChannel(offerChannelId, offerChannelName, offerChannelImportance);
            notifChannel.setDescription(offerChannelDescription);
            //notifChannel.enableVibration(true);
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.GREEN);

            notifManager.createNotificationChannel(notifChannel);
        }

    }

    private void speechNotification() {

        Intent intent = new Intent(this, SpeechActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotifChannel();

            Notification.Builder notification = new Notification.Builder(this)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .addAction(new Notification.Action(R.drawable.ic_mic_24dp, getString(R.string.button_name_speak), pendingIntent))
                    .setContentTitle(getString(R.string.notification_title_speechToText))
                    .setContentText(getString(R.string.notification_clickSpeak))
                    .setChannelId(offerChannelId);
            notifManager.notify(1, notification.build());
        } else {

            Notification.Builder notification = new Notification.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(getString(R.string.notification_title_speechToText))
                    .setContentText(getString(R.string.notification_clickSpeak))
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .addAction(new Notification.Action(R.drawable.ic_mic_24dp, getString(R.string.button_name_speak), pendingIntent))
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification.build());

        }
    }


    private void setNavigationMenu() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_charts, R.id.navigation_reports, R.id.navigation_note)
                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.button_settings_tool_bar) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.button_sms_tool_bar) {
            Intent intent = new Intent(MainActivity.this, SmsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }



}
