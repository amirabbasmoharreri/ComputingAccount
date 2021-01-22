package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;

public class CustomProgressBar extends Dialog {

    private Context context;

    public CustomProgressBar(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress_bar);
    }
}
