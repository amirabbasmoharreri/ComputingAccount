package com.abbasmoharreri.computingaccount;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abbasmoharreri.computingaccount.io.PdfCreator;
import com.abbasmoharreri.computingaccount.service.BackupService;
import com.abbasmoharreri.computingaccount.static_value.PdfLanguage;
import com.abbasmoharreri.computingaccount.static_value.PreferencesKeys;
import com.abbasmoharreri.computingaccount.ui.chartsNavigation.ChartsFragment;
import com.abbasmoharreri.computingaccount.ui.home.HomeFragment;
import com.abbasmoharreri.computingaccount.ui.popupdialog.TransferMoney;
import com.abbasmoharreri.computingaccount.ui.reports.ReportsFragment;
import com.abbasmoharreri.computingaccount.ui.note.NoteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;


public class MainActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {


    private SharedPreferences preferences;
    private FloatingActionButton fab_Main, fab_Paid, fab_Receive, fab_Debt_Crave, fab_Transfer;
    TextView text_Paid, text_Receive, text_Debt_Crave, text_Transfer;
    private boolean isFabMenuOpen = false;
    private Animation fabOpenAnimation, fabCloseAnimation, fabClock, fabAntiClock, textOpen, textClose;
    ImageView background_fab;
    Intent intent;
    NotificationManager notifManager;
    boolean serviceStart = false;

    String offerChannelId = "Offers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationMenu();

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
                    .setSmallIcon(R.drawable.ic_luncher_forground_2)
                    .addAction(new Notification.Action(R.drawable.ic_mic_white_24dp, getString(R.string.button_name_speak), pendingIntent))
                    .setContentTitle(getString(R.string.notification_title_speechToText))
                    .setContentText(getString(R.string.notification_clickSpeak))
                    .setChannelId(offerChannelId);
            notifManager.notify(1, notification.build());
        } else {

            Notification.Builder notification = new Notification.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(getString(R.string.notification_title_speechToText))
                    .setContentText(getString(R.string.notification_clickSpeak))
                    .setSmallIcon(R.drawable.ic_luncher_forground_2)
                    .addAction(new Notification.Action(R.drawable.ic_mic_white_24dp, getString(R.string.button_name_speak), pendingIntent))
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(this);

        getAnimations();
        setFabMenu();
        collapseFabMenu();

    }


    private void setDisableFab() {

        text_Paid.setVisibility(View.INVISIBLE);
        text_Receive.setVisibility(View.INVISIBLE);
        text_Debt_Crave.setVisibility(View.INVISIBLE);
        text_Transfer.setVisibility(View.INVISIBLE);
        text_Paid.setAlpha(0.0f);
        text_Receive.setAlpha(0.0f);
        text_Debt_Crave.setAlpha(0.0f);
        text_Transfer.setAlpha(0.0f);
        background_fab.setAlpha(0.0f);
        text_Paid.setClickable(false);
        text_Receive.setClickable(false);
        text_Debt_Crave.setClickable(false);
        text_Transfer.setClickable(false);
        fab_Main.hide();
        fab_Paid.hide();
        fab_Receive.hide();
        fab_Debt_Crave.hide();
        fab_Transfer.hide();
        fab_Paid.setClickable(false);
        fab_Receive.setClickable(false);
        fab_Debt_Crave.setClickable(false);
        fab_Transfer.setClickable(false);
        fab_Main.setClickable(false);

    }


    private void setEnableFab() {
        text_Paid.setVisibility(View.VISIBLE);
        text_Receive.setVisibility(View.VISIBLE);
        text_Debt_Crave.setVisibility(View.VISIBLE);
        text_Transfer.setVisibility(View.VISIBLE);
        text_Paid.setAlpha(1.0f);
        text_Receive.setAlpha(1.0f);
        text_Debt_Crave.setAlpha(1.0f);
        text_Transfer.setAlpha(1.0f);
        text_Paid.setClickable(true);
        text_Receive.setClickable(true);
        text_Debt_Crave.setClickable(true);
        text_Transfer.setClickable(true);
        fab_Paid.setClickable(true);
        fab_Receive.setClickable(true);
        fab_Debt_Crave.setClickable(true);
        fab_Transfer.setClickable(true);
        fab_Main.setClickable(true);
        fab_Main.show();
        fab_Paid.show();
        fab_Receive.show();
        fab_Debt_Crave.show();
        fab_Transfer.show();

        collapseFabMenu();
    }

    private void setFabMenu() {
        fab_Main = findViewById(R.id.floating_main);
        fab_Paid = findViewById(R.id.floating_paid);
        fab_Receive = findViewById(R.id.floating_receive);
        fab_Debt_Crave = findViewById(R.id.floating_debt_and_crave);
        fab_Transfer = findViewById(R.id.floating_transfer);

        background_fab = findViewById(R.id.background_fab);

        text_Paid = findViewById(R.id.text_paid);
        text_Receive = findViewById(R.id.text_receive);
        text_Debt_Crave = findViewById(R.id.text_debt_and_crave);
        text_Transfer = findViewById(R.id.text_transfer);

        text_Paid.setOnClickListener(this);
        text_Receive.setOnClickListener(this);
        text_Debt_Crave.setOnClickListener(this);
        text_Transfer.setOnClickListener(this);

        background_fab.setOnClickListener(this);

        fab_Main.setOnClickListener(this);
        fab_Paid.setOnClickListener(this);
        fab_Receive.setOnClickListener(this);
        fab_Debt_Crave.setOnClickListener(this);
        fab_Transfer.setOnClickListener(this);


    }


    private void getAnimations() {

        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.floating_button_open);
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.floating_button_close);
        fabClock = AnimationUtils.loadAnimation(this, R.anim.floating_button_rotate_clock);
        fabAntiClock = AnimationUtils.loadAnimation(this, R.anim.floating_button_rotate_anticlock);

        textOpen = AnimationUtils.loadAnimation(this, R.anim.floating_button_open);
        textClose = AnimationUtils.loadAnimation(this, R.anim.floating_button_close);
    }


    private void expandFabMenu() {

        text_Paid.setVisibility(View.VISIBLE);
        text_Receive.setVisibility(View.VISIBLE);
        text_Debt_Crave.setVisibility(View.VISIBLE);
        text_Transfer.setVisibility(View.VISIBLE);

        background_fab.setAlpha(0.5f);

        text_Paid.startAnimation(textOpen);
        text_Receive.startAnimation(textOpen);
        text_Debt_Crave.startAnimation(textOpen);
        text_Transfer.startAnimation(textOpen);

        fab_Paid.startAnimation(fabOpenAnimation);
        fab_Receive.startAnimation(fabOpenAnimation);
        fab_Debt_Crave.startAnimation(fabOpenAnimation);
        fab_Transfer.startAnimation(fabOpenAnimation);
        fab_Main.startAnimation(fabClock);

        fab_Paid.setClickable(true);
        fab_Receive.setClickable(true);
        fab_Debt_Crave.setClickable(true);
        fab_Transfer.setClickable(true);

        background_fab.setClickable(true);
        isFabMenuOpen = true;
    }


    private void collapseFabMenu() {
        text_Paid.setVisibility(View.INVISIBLE);
        text_Receive.setVisibility(View.INVISIBLE);
        text_Debt_Crave.setVisibility(View.INVISIBLE);
        text_Transfer.setVisibility(View.INVISIBLE);

        background_fab.setAlpha(0.0f);

        text_Paid.startAnimation(textClose);
        text_Receive.startAnimation(textClose);
        text_Debt_Crave.startAnimation(textClose);
        text_Transfer.startAnimation(textClose);

        fab_Paid.startAnimation(fabCloseAnimation);
        fab_Receive.startAnimation(fabCloseAnimation);
        fab_Debt_Crave.startAnimation(fabCloseAnimation);
        fab_Transfer.startAnimation(fabCloseAnimation);
        fab_Main.startAnimation(fabAntiClock);

        fab_Paid.setClickable(false);
        fab_Receive.setClickable(false);
        fab_Debt_Crave.setClickable(false);
        fab_Transfer.setClickable(false);

        background_fab.setClickable(false);

        isFabMenuOpen = false;

    }


    private void startIntents(Class activity) {
        intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (isFabMenuOpen) {
            collapseFabMenu();
        } else {
            super.onBackPressed();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_main:
                if (isFabMenuOpen) {
                    collapseFabMenu();
                } else {
                    expandFabMenu();
                }
                break;
            case R.id.floating_paid:
            case R.id.text_paid:
                Toast.makeText(this, R.string.activity_title_paid, Toast.LENGTH_SHORT).show();
                startIntents(PaidActivity.class);
                collapseFabMenu();
                break;
            case R.id.floating_receive:
            case R.id.text_receive:
                Toast.makeText(this, R.string.activity_title_received, Toast.LENGTH_SHORT).show();
                startIntents(ReceiveActivity.class);
                collapseFabMenu();
                break;
            case R.id.floating_debt_and_crave:
            case R.id.text_debt_and_crave:
                Toast.makeText(this, R.string.activity_title_crDe, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CraveDebtActivity.class);
                intent.putExtra("Type", "All");
                startActivity(intent);
                collapseFabMenu();
                break;
            case R.id.floating_transfer:
            case R.id.text_transfer:
                TransferMoney transferMoney = new TransferMoney(this);
                transferMoney.show();
                collapseFabMenu();
                break;
            case R.id.background_fab:
                collapseFabMenu();
                break;
            case R.id.floating_note:
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass = null;
        int id = menuItem.getItemId();

        if (id == R.id.navigation_home) {
            fragmentClass = HomeFragment.class;
            setEnableFab();
        } else if (id == R.id.navigation_charts) {
            fragmentClass = ChartsFragment.class;
            setDisableFab();
        } else if (id == R.id.navigation_reports) {
            fragmentClass = ReportsFragment.class;
            setDisableFab();
        } else if (id == R.id.navigation_note) {
            fragmentClass = NoteFragment.class;
            setDisableFab();
        }


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

        return true;

    }

}
