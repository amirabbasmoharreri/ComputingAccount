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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.DriveScopes;

import java.util.Locale;

public class AccountLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    Button englishButton, farsiButton, googleAccountButton;
    TextView skipButton, emailGoogleAccount;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static Configuration configuration;
    static Resources resources;
    static DisplayMetrics displayMetrics;
    boolean showActivity = false;


    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

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
            startActivity(intent);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }


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

    private void request() {

       /* oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();*/
    }


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
        } else if (requestCode == 500){
            //TODO When granted permission of drive api , do ...
            //saveToDriveAppFolder();
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> data) {

        try {
            GoogleSignInAccount account = data.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("AccountlanguageActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }


        /*GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(AccountLanguageActivity.this, Collections.singleton((DriveScopes.DRIVE_FILE)));
                        credential.setSelectedAccount(googleSignInAccount.getAccount());
                        Drive googleDriveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("Computing Account")
                                .build();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/



        /*GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount account=result.getSignInAccount();
        Log.e("addsffdf", "handleSignInIntent: "+ account.getEmail() );*/

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
                /*editor.putBoolean("showAccountLanguageActivity",true);
                editor.apply();*/
                startActivity(intent);
                finish();
                break;
        }
    }
}