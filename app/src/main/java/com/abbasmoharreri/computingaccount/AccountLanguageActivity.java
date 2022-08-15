package com.abbasmoharreri.computingaccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.DriveScopes;

import java.util.Locale;
import java.util.Objects;

public class AccountLanguageActivity extends AppCompatActivity implements View.OnClickListener {


    Button englishButton, farsiButton, googleAccountButton;
    TextView skipButton, emailGoogleAccount;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static Configuration configuration;
    static Resources resources;
    static DisplayMetrics displayMetrics;
    boolean showActivity = false;
    GoogleSignInAccount googleAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_language);

        englishButton = findViewById(R.id.button_language_english);
        englishButton.setOnClickListener(this);
        emailGoogleAccount = findViewById(R.id.text_email_google_account);
        farsiButton = findViewById(R.id.button_language_farsi);
        farsiButton.setOnClickListener(this);
        googleAccountButton = findViewById(R.id.button_google_account);
        googleAccountButton.setOnClickListener(this);
        skipButton = findViewById(R.id.button_skip);
        skipButton.setClickable(true);
        skipButton.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showActivity = sharedPreferences.getBoolean("showAccountLanguageActivity", false);

        resources = getResources();
        configuration = resources.getConfiguration();
        displayMetrics = resources.getDisplayMetrics();

        if (showActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            getLastSignInGoogleAccount();
            if (googleAccount != null) {
                intent.putExtra("GoogleAccount", googleAccount);
            }
            startActivity(intent);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        getLastSignInGoogleAccount();
        updateUI(googleAccount);
    }


    /**
    * This method for updating condition of Google Button
    * @param account if null (Button ON) - if not null (Button OFF)
    * */

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            googleAccountButton.setEnabled(false);
            googleAccountButton.setAlpha(0.5f);
            emailGoogleAccount.setText(account.getEmail());
        } else {
            googleAccountButton.setEnabled(true);
            googleAccountButton.setAlpha(1.0f);
        }
    }


    /**
     * This method get Account information sign in last time
     * */
    private void getLastSignInGoogleAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        googleAccount = account;
        try {
            Log.e("Abbas Email Last SingIn (LanguageActivity): ", account.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method for check permission of using Google Drive
     * */
    private void requestDrivePermission() {
        //TODO Requesting permission Drive Api

        Scope scope = new Scope(DriveScopes.DRIVE_FILE);
        if (!GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(this),
                scope)) {
            GoogleSignIn.requestPermissions(
                    AccountLanguageActivity.this,
                    500,
                    GoogleSignIn.getLastSignedInAccount(this),
                    scope);
        } else {
            // saveToDriveAppFolder();
        }
    }


    /**
     * This method for sending request for sing in to Google Account
     * It's show popup for send request
     * */
    private void requestSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        startActivityForResult(client.getSignInIntent(), 400);
        requestDrivePermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 400) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == 500) {
            //TODO When granted permission of drive api , do some thing

            //saveToDriveAppFolder();
        }

    }


    /**
     * This method to handle answer of requesting for sign in to Google Account
     * */
    private void handleSignInResult(Task<GoogleSignInAccount> data) {

        try {
            GoogleSignInAccount account = data.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Log.e("Abbas Email: ", Objects.requireNonNull(account.getEmail()));
            googleAccount = account;
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w("AccountlanguageActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_language_english:
                editor = sharedPreferences.edit();
                editor.putString("language", "en");
                editor.apply();
                configuration.locale = new Locale("en");
                resources.updateConfiguration(configuration, displayMetrics);
                recreate();
                break;
            case R.id.button_language_farsi:
                editor = sharedPreferences.edit();
                editor.putString("language", "fa");
                editor.apply();
                configuration.locale = new Locale("fa");
                resources.updateConfiguration(configuration, displayMetrics);
                recreate();
                break;
            case R.id.button_google_account:
                requestSignIn();
                break;
            case R.id.button_skip:
                Intent intent = new Intent(this, MainActivity.class);
                editor = sharedPreferences.edit();
                editor.putBoolean("showAccountLanguageActivity", true);
                editor.apply();
                if (googleAccount != null) {
                    intent.putExtra("GoogleAccount", googleAccount);
                }
                startActivity(intent);
                finish();
                break;
        }
    }
}