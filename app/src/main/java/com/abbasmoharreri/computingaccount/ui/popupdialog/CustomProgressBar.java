package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.abbasmoharreri.computingaccount.R;
import com.airbnb.lottie.LottieAnimationView;

public class CustomProgressBar extends DialogFragment {

    public Fragment CustomProgressBar(){

        Bundle args = new Bundle();

        CustomProgressBar fragment = new CustomProgressBar();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_progress_bar, null);

        final Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

}
