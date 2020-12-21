package com.abbasmoharreri.computingaccount.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.ui.popupdialog.TransferMoney;

import java.util.List;

public class WalletCardAdapter extends RecyclerView.Adapter<WalletCardAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private List<AAccount> aAccounts;
    private Context context;
    private PopupMenu.OnDismissListener listener;
    private DialogInterface.OnDismissListener listenerDialog;


    public WalletCardAdapter(Context context, List<AAccount> aAccounts) {
        this.context = context;
        this.aAccounts = aAccounts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View contentView = inflater.inflate( R.layout.card_view_wallet_card, parent, false );
        viewHolder = new ViewHolder( contentView );


        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ImageButton button = holder.button;
        TextView name = holder.name;
        TextView remained = holder.remained;
        TextView accountNumber = holder.accountNumber;

        AAccount aAccount = aAccounts.get( position );

        name.setText( aAccount.getName() );
        remained.setText( String.format( "%,d", aAccount.getRemained() ) );
        accountNumber.setText( String.valueOf( aAccount.getAccountNumber() ) );


        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup( view, position );
            }
        } );

    }


    public void showPopup(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu( context, v );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.card_view_menu_card_wallet, popupMenu.getMenu() );
        popupMenu.show();

        final DataBaseController dataBaseController = new DataBaseController( context );

        popupMenu.setOnDismissListener( listener );
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.button_delete) {

                    new AlertDialog.Builder( context )
                            .setTitle( R.string.message_title_attention )
                            .setMessage( R.string.massage_ifDeleteLostData )
                            .setNegativeButton( android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (aAccounts.get( position ).getName().equals( AAccount.ACCOUNT_WALLET )) {
                                        try {
                                            dataBaseController.deleteWalletReceive( aAccounts.get( position ) );
                                            aAccounts.remove( position );
                                            notifyDataSetChanged();
                                            Toast.makeText( context, R.string.toast_deleteItem, Toast.LENGTH_SHORT ).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (aAccounts.get( position ).getName().equals( AAccount.ACCOUNT_CARD )) {
                                        try {
                                            dataBaseController.deleteCardReceive( aAccounts.get( position ) );
                                            aAccounts.remove( position );
                                            notifyDataSetChanged();
                                            Toast.makeText( context, R.string.toast_deleteItem, Toast.LENGTH_SHORT ).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            } )
                            .setIcon( android.R.drawable.ic_dialog_alert )
                            .show();

                } else if (item.getItemId() == R.id.button_transfer) {

                    try {
                        TransferMoney transferMoney = new TransferMoney( context, aAccounts.get( position ) );
                        transferMoney.setOnDismissListener( listenerDialog );
                        transferMoney.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        } );
    }


    public void setPopUpMenuListener(PopupMenu.OnDismissListener listener) {
        this.listener = listener;
    }

    public void setDialogListener(DialogInterface.OnDismissListener listenerDialog) {
        this.listenerDialog = listenerDialog;
    }


    @Override
    public int getItemCount() {
        return aAccounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageButton button;
        TextView name, remained, accountNumber;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            button = itemView.findViewById( R.id.button_card_view_wallet_card );
            name = itemView.findViewById( R.id.text_wallet_name_card_wallet_card );
            remained = itemView.findViewById( R.id.text_remained_card_wallet_card );
            accountNumber = itemView.findViewById( R.id.text_account_number_card_wallet_card );

        }
    }
}
