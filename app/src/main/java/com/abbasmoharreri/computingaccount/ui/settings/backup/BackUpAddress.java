package com.abbasmoharreri.computingaccount.ui.settings.backup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.filemanager.Storage;

import java.io.File;
import java.util.Objects;


public class BackUpAddress extends Preference implements View.OnClickListener, Parcelable {


    public final String TAG = "BackUpAddress";
    public static String addressBackup = "";
    private static Context context;
    private TextView addressTile;
    private TextView address;
    private Button folder;


    public BackUpAddress(Context context) {
        super(context);
        this.context = context;
        @SuppressLint("RestrictedApi") PreferenceViewHolder preferenceViewHolder = PreferenceViewHolder.createInstanceForTests(View.inflate(context, R.layout.preference_back_up_address, null));
        onBindViewHolder(preferenceViewHolder);
    }

    public BackUpAddress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.layout.preference_back_up_address, defStyleRes);
    }

    public BackUpAddress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.layout.preference_back_up_address);
    }

    public BackUpAddress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getText() {
        return this.address.getText().toString();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.addressBackUp = getAddressBackup();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        persistString(myState.addressBackUp);
        notifyChanged();
    }

    public static final Creator<BackUpAddress> CREATOR = new Creator<BackUpAddress>() {
        @Override
        public BackUpAddress createFromParcel(Parcel in) {
            return new BackUpAddress(in);
        }

        @Override
        public BackUpAddress[] newArray(int size) {
            return new BackUpAddress[size];
        }
    };


    protected BackUpAddress(Parcel in) {
        super(context);

    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        String addressBackUp;

        SavedState(Parcel source) {
            super(source);
            addressBackUp = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(addressBackUp);
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        addressTile = (TextView) holder.findViewById(R.id.text_title_address_settings_backup);
        address = (TextView) holder.findViewById(R.id.text_address_settings_backup);
        folder = (Button) holder.findViewById(R.id.button_settings_backup);
        folder.setOnClickListener(this);
        context = getContext();
        if (!getPersistedString("address").equals("")) {
            address.setText(getPersistedString("address"));
        } else {
            address.setText("address");
        }

    }

    public String getAddressBackup() {
        return addressBackup;
    }

    public void refresh() {
        @SuppressLint("RestrictedApi") PreferenceViewHolder preferenceViewHolder = PreferenceViewHolder.createInstanceForTests(View.inflate(context, R.layout.preference_back_up_address, null));
        onBindViewHolder(preferenceViewHolder);
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_settings_backup) {

            Log.e("error ", " working");

            Intent popup = new Intent(context, MiddleActivity.class);
            context.startActivity(popup);
        }
    }


    public static class MiddleActivity extends AppCompatActivity {

        private final int REQUEST_OPEN_DOCUMENT_TREE = 1;
        BackUpAddress backUpAddress;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onResume() {
            super.onResume();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, REQUEST_OPEN_DOCUMENT_TREE);

        }


        /*
         * Listening to the {@Link SeekBar}
         *  */

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            //TODO fix problem when come back to this activity without choosing address


            if (requestCode == REQUEST_OPEN_DOCUMENT_TREE) {
                assert data != null;
                Uri folderUri = data.getData();
                if (folderUri != null) {

                    Log.e("Address0", Objects.requireNonNull(folderUri.getPath()));

                    Storage storage = new Storage(getApplicationContext(), new File(Objects.requireNonNull(folderUri.getPath())));

                    addressBackup = storage.getRealPathStorage();
                    Log.e("Address", addressBackup);
                    backUpAddress = new BackUpAddress(context);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor myEditor = preferences.edit();
                    myEditor.putString("addressBackUp", addressBackup);
                    myEditor.apply();
                    backUpAddress.refresh();
                    this.finish();
                }
            }

        }

    }

}
