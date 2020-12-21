package com.abbasmoharreri.computingaccount.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.AAccount;

import java.util.List;

public class SpinnerAccountAdapter extends ArrayAdapter<AAccount> {

    private List<AAccount> aAccounts;

    public SpinnerAccountAdapter(@NonNull Context context, List<AAccount> aAccounts) {
        super( context, 0, aAccounts );
        this.aAccounts = aAccounts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView( position, convertView, parent );
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView( position, convertView, parent );
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.popup_spinner, parent, false );
        }

        TextView name = convertView.findViewById( R.id.spinner_account_name_paid_activity );
        TextView number = convertView.findViewById( R.id.spinner_account_number_paid_activity );

        AAccount aAccount = aAccounts.get( position );

        if (aAccount != null) {
            name.setText( aAccount.getName() );
            number.setText( aAccount.getAccountNumber() );
        }

        return convertView;
    }
}
