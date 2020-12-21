package com.abbasmoharreri.computingaccount.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class AccessibilityKeyDetector extends AccessibilityService {

    private final String TAG = "AccessKeyDetector";

    @SuppressLint("ShowToast")
    @Override
    public boolean onKeyEvent(KeyEvent event) {
        Log.e( TAG, "Key pressed via accessibility is: " + event.getKeyCode() );
        Toast.makeText( getApplicationContext(), event.getKeyCode(), Toast.LENGTH_SHORT );
        //This allows the key pressed to function normally after it has been used by your app.
        return super.onKeyEvent( event );
    }


    @Override
    protected void onServiceConnected() {
        Log.i( TAG, "Service connected" );

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
}
