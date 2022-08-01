package com.abbasmoharreri.computingaccount;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    static Context context;
    static Activity activity;
    static Configuration configuration;
    static Resources resources;
    static DisplayMetrics displayMetrics;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.Light_AppTheme);
        }
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        activity = this;
        resources = getResources();
        configuration = resources.getConfiguration();
        displayMetrics = resources.getDisplayMetrics();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SwitchPreferenceCompat switchTheme;
        private ListPreference languageList;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            switchTheme = findPreference("darkMode");
            languageList = findPreference("language");
            switchTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    if (!switchTheme.isChecked()) {
                        context.setTheme(R.style.Dark_AppTheme);
                        switchTheme.setChecked(true);
                        activity.recreate();

                        return true;
                    } else {
                        context.setTheme(R.style.Light_AppTheme);
                        switchTheme.setChecked(false);
                        activity.recreate();
                        return false;
                    }
                }
            });

            languageList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                        configuration.locale = new Locale(newValue.toString());
                        resources.updateConfiguration(configuration, displayMetrics);
                        activity.recreate();

                        return true;
                }
            });

        }
/*
        @Override
        public void onResume() {
            super.onResume();
           *//* for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initSummary(getPreferenceScreen().getPreference(i));
            }*//*

            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void initSummary(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory cat = (PreferenceCategory) p;
                for (int i = 0; i < cat.getPreferenceCount(); i++) {
                    initSummary(cat.getPreference(i));
                }
            } else {
                updatePreference(p);
            }
        }

        private void updatePreference(Preference p) {
            if (p instanceof BackUpAddress) {
                BackUpAddress backUpAddress = (BackUpAddress) p;
                p.setSummary(backUpAddress.getText());
            }
            }*/
    }
}


