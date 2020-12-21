package com.abbasmoharreri.computingaccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.abbasmoharreri.computingaccount.database.DataBaseController;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 1;
    private final static int REQ_CODE_SPEECH_INPUT = 1;
    private String string = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, "fa" );
        startActivityForResult( intent, REQUEST_CODE );

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        DataBaseController dataBaseController = new DataBaseController( this );

        ArrayList<String> text = new ArrayList<>();

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    text = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                    string = text.get( 0 );

                    if (!string.equals( "" )) {
                        dataBaseController.insertNote( string );
                    }
                }
                break;
            }
        }
        Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();

        finish();
    }
}
