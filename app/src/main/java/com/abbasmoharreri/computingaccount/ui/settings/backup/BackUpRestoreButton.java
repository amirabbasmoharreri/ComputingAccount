package com.abbasmoharreri.computingaccount.ui.settings.backup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.BackupAndRestore;
import com.abbasmoharreri.computingaccount.filemanager.FileManager;
import com.abbasmoharreri.computingaccount.filemanager.Storage;

import java.io.File;
import java.util.Objects;

public class BackUpRestoreButton extends Preference implements View.OnClickListener {

    private Button backUp;
    private Button restore;
    private Context context;

    public BackUpRestoreButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BackUpRestoreButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BackUpRestoreButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackUpRestoreButton(Context context) {
        super(context);
    }


    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        backUp = (Button) holder.findViewById(R.id.button_backup_back_up_settings);
        restore = (Button) holder.findViewById(R.id.button_restore_back_up_settings);
        backUp.setOnClickListener(this);
        restore.setOnClickListener(this);
        this.context = getContext();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_backup_back_up_settings) {

            BackupAndRestore backupAndRestore = new BackupAndRestore();
            backupAndRestore.exportDB(getContext());
            Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.button_restore_back_up_settings) {
            Intent intent = new Intent(context, MiddleActivity.class);
            context.startActivity(intent);
        }

    }

    public static class MiddleActivity extends AppCompatActivity {

        private final int REQUEST_GET_CONTENT = 1;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onResume() {
            super.onResume();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_GET_CONTENT);

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == REQUEST_GET_CONTENT) {
                assert data != null;
                Uri folderUri = data.getData();
                if (folderUri != null) {

                    Storage storage = new Storage(getApplicationContext(), new File(Objects.requireNonNull(folderUri.getPath())));
                    BackupAndRestore backupAndRestore = new BackupAndRestore();
                    backupAndRestore.importDB(getApplicationContext(), storage.getRealPathStorage());
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    Log.e("abbassssss: ", storage.getRealPathStorage());
                    this.finish();
                }
            }

        }

    }

}
