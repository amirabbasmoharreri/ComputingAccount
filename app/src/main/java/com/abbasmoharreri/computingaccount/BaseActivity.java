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

import com.abbasmoharreri.computingaccount.static_value.PreferencesKeys;

import java.io.File;
import java.util.List;

abstract public class BaseActivity extends AppCompatActivity {

    // Storage Permissions variables
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Runnable mOnWritePermissionGranted;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

       // takeCardUriPermission(sharedPreferences.getString(PreferencesKeys.BACKUP_ADDRESS, ""));

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
    }

    private void callOnWritePermissionGranted() {
        if (mOnWritePermissionGranted != null) {
            mOnWritePermissionGranted.run();
            mOnWritePermissionGranted = null; //reset to prevent future calls issues...
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
