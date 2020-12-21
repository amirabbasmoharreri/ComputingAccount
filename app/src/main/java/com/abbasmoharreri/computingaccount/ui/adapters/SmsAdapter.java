package com.abbasmoharreri.computingaccount.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.ASms;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> implements View.OnClickListener {

    private List<ASms> smsList;
    private Context context;
    private ViewHolder viewHolder;

    public SmsAdapter(Context context, List<ASms> smsList) {
        this.context = context;
        this.smsList = smsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View contentView = inflater.inflate( R.layout.card_view_work, parent, false );
        viewHolder = new SmsAdapter.ViewHolder( contentView );


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView bankName = holder.bankName;
        TextView account = holder.account;
        TextView type = holder.type;
        TextView price = holder.price;
        TextView date = holder.date;
        Button save = holder.save;
        Button delete = holder.delete;

        ASms aSms = new ASms();

        bankName.setText( aSms.getBankName() );
        account.setText( aSms.getAccount() );
        type.setText( aSms.getType() );
        price.setText( aSms.getPrice() );
        date.setText( aSms.getTime() );
        save.setOnClickListener( this );
        delete.setOnClickListener( this );

    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_card_sms:
                break;
            case R.id.button_delete_card_sms:
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bankName, account, date, price, type;
        Button save, delete;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            bankName = itemView.findViewById( R.id.text_bank_name_card_sms );
            account = itemView.findViewById( R.id.text_account_card_sms );
            date = itemView.findViewById( R.id.text_date_card_sms );
            price = itemView.findViewById( R.id.text_price_card_sms );
            type = itemView.findViewById( R.id.text_type_card_sms );
            save = itemView.findViewById( R.id.button_save_card_sms );
            delete = itemView.findViewById( R.id.button_delete_card_sms );
        }
    }
}
