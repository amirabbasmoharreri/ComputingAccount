package com.abbasmoharreri.computingaccount;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.util.List;

import static android.Manifest.permission.INTERNET;

abstract public class BaseActivity extends AppCompatActivity {

    // Storage Permissions variables
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Runnable mOnWritePermissionGranted;

    // Internet Permissions variables
    private static final int REQUEST_CODE_INTERNET = 2;
    private static final String[] PERMISSION_INTERNET = {
            Manifest.permission.INTERNET
    };
    private Runnable mOnInternetPermissionGranted;

    // Sms Permissions variables
    private static final int REQUEST_CODE_SMS = 3;
    private static final String[] PERMISSION_SMS = {
            Manifest.permission.RECEIVE_SMS
    };
    private Runnable mOnSmsPermissionGranted;





    public void verifyStoragePermissions(Runnable onPermissionGranted) {
        // Check if we have read or write permission
        mOnWritePermissionGranted = onPermissionGranted;
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE_EXTERNAL_STORAGE
            );
        } else
            callOnWritePermissionGranted();

    }


    public void verifyInternetPermissions(Runnable onPermissionGranted) {
        // Check if we have read or write permission
        mOnInternetPermissionGranted = onPermissionGranted;
        int internetPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSION_INTERNET,
                    REQUEST_CODE_INTERNET
            );
        } else
            callOnInternetPermissionGranted();

    }

    public void verifySmsPermissions(Runnable onPermissionGranted) {
        // Check if we have read or write permission
        mOnSmsPermissionGranted = onPermissionGranted;
        int smsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSION_SMS,
                    REQUEST_CODE_SMS
            );
        } else
            callOnSmsPermissionGranted();

    }

    private void callOnWritePermissionGranted() {
        if (mOnWritePermissionGranted != null) {
            mOnWritePermissionGranted.run();
            mOnWritePermissionGranted = null; //reset to prevent future calls issues...
        }
    }


    protected void callOnSmsPermissionGranted() {
        if (mOnSmsPermissionGranted != null) {
            mOnSmsPermissionGranted.run();
            mOnSmsPermissionGranted = null; //reset to prevent future calls issues...
        }
    }

    private void callOnInternetPermissionGranted() {
        if (mOnInternetPermissionGranted != null) {
            mOnInternetPermissionGranted.run();
            mOnInternetPermissionGranted = null; //reset to prevent future calls issues...
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callOnWritePermissionGranted();
            } else {
                Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_CODE_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callOnInternetPermissionGranted();
            } else {
                Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_CODE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callOnSmsPermissionGranted();
            } else {
                Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }




    public void takeCardUriPermission(String sdCardRootPath) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            File sdCard = new File(sdCardRootPath);
            StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            StorageVolume storageVolume = storageManager.getStorageVolume(sdCard);
            Intent intent = storageVolume.createAccessIntent(null);
            try {
                startActivityForResult(intent, 4010);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4010) {

            Uri uri = data.getData();

            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            getContentResolver().takePersistableUriPermission(uri, takeFlags);
        }
    }

    public Uri getUri() {
        List<UriPermission> persistedUriPermissions = getContentResolver().getPersistedUriPermissions();
        if (persistedUriPermissions.size() > 0) {
            UriPermission uriPermission = persistedUriPermissions.get(0);
            return uriPermission.getUri();
        }
        return null;
    }
}
