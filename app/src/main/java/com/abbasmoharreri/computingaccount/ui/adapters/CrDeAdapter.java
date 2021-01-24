package com.abbasmoharreri.computingaccount.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.ui.popupdialog.UpdateCrDeDialog;

import java.util.List;

public class CrDeAdapter extends RecyclerView.Adapter<CrDeAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private List<ACraveAndDebt> aCraveAndDebts;
    private Context context;
    private PopupMenu.OnDismissListener listener;
    private DialogInterface.OnDismissListener listenerDialog;


    public CrDeAdapter(Context context, List<ACraveAndDebt> aCraveAndDebts) {
        this.context = context;
        this.aCraveAndDebts = aCraveAndDebts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View contentView = inflater.inflate( R.layout.card_view_crde, parent, false );
        viewHolder = new ViewHolder( contentView );


        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        TextView person = holder.person;
        TextView price = holder.price;
        CardView crde = holder.crde;
        ImageView button = holder.button;

        ACraveAndDebt aCraveAndDebt = aCraveAndDebts.get( position );

        person.setText( aCraveAndDebt.getPersonName() );
        price.setText( String.format( "%,d", aCraveAndDebt.getPrice() ) );
        if (aCraveAndDebt.getType().equals( ACraveAndDebt.CRAVE )) {
            //crde.setBackgroundColor( Color.rgb( 26, 139, 29 ) );
            crde.setBackground( context.getDrawable( R.drawable.card_background_crave ) );
        } else {
            //26crde.setBackgroundColor( Color.rgb( 201, 116, 64 ) );
            crde.setBackground( context.getDrawable( R.drawable.card_background_debt ) );
        }

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup( view, position );
            }
        } );

    }

    public void setPopUpMenuListener(PopupMenu.OnDismissListener listener) {
        this.listener = listener;
    }

    public void setDialogListener(DialogInterface.OnDismissListener listenerDialog) {
        this.listenerDialog = listenerDialog;
    }


    public void showPopup(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu( context, v );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.card_view_crave_debt, popupMenu.getMenu() );
        popupMenu.show();

        final DataBaseController dataBaseController = new DataBaseController( context );

        popupMenu.setOnDismissListener( listener );
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.button_checkout) {
                    try {
                        dataBaseController.deleteCraveDebt( aCraveAndDebts.get( position ) );
                        aCraveAndDebts.remove( position );
                        notifyDataSetChanged();
                        Toast.makeText( context, R.string.toast_deleteItem, Toast.LENGTH_SHORT ).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (item.getItemId() == R.id.button_edit) {

                    try {
                        UpdateCrDeDialog updateCrDeDialog = new UpdateCrDeDialog( context, aCraveAndDebts.get( position ) );
                        updateCrDeDialog.setOnDismissListener( listenerDialog );
                        updateCrDeDialog.show();
                        Toast.makeText( context, R.string.toast_updateItem, Toast.LENGTH_SHORT ).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        } );
    }


    @Override
    public int getItemCount() {
        return aCraveAndDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView person, price;
        CardView crde;
        ImageView button;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            person = itemView.findViewById( R.id.text_person_card_crde );
            price = itemView.findViewById( R.id.text_price_card_crde );
            crde = itemView.findViewById( R.id.card_crde );
            button = itemView.findViewById( R.id.button_card_view_crde );
        }
    }
}
