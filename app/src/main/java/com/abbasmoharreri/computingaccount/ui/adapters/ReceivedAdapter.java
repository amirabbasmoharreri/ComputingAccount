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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.ui.popupdialog.ShowDetailReceives;

import java.util.List;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.ViewHolder> {

    private Context context;
    private List<AMoneyReceive> aMoneyReceives;
    private ViewHolder viewHolder;
    private PopupMenu.OnDismissListener listener;
    private DialogInterface.OnDismissListener listenerDialog;


    public ReceivedAdapter(Context context, List<AMoneyReceive> aMoneyReceives) {
        this.context = context;
        this.aMoneyReceives = aMoneyReceives;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.card_view_received, parent, false);
        viewHolder = new ViewHolder(contentView);


        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ImageButton button = holder.button;
        TextView price = holder.price;
        TextView date = holder.date;
        CardView cardView = holder.cardView;

        AMoneyReceive aMoneyReceive = aMoneyReceives.get(position);
        price.setText(String.format("%,d", aMoneyReceive.getPrice()));
        date.setText(aMoneyReceive.getStringIranianDate());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, position);
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup2(v, position);
                return false;
            }
        });

    }

    public void showPopup2(View v, final int position) {
        ShowDetailReceives showDetailReceives = new ShowDetailReceives(context, aMoneyReceives.get(position));
        showDetailReceives.show();
    }


    public void showPopup(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.card_view_menu, popupMenu.getMenu());
        popupMenu.show();

        final DataBaseController dataBaseController = new DataBaseController(context);

        popupMenu.setOnDismissListener(listener);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.button_delete) {

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.massage_title_attention)
                            .setMessage(R.string.massage_ifDeleteLostData)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                        /*DeletingData deletingData = new DeletingData( context );
                        deletingData.deleteFromReceived( aMoneyReceives.get( position ) );
                        aMoneyReceives.remove( position );
                        notifyDataSetChanged();
                        Toast.makeText( context, "Delete", Toast.LENGTH_SHORT ).show();*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else if (item.getItemId() == R.id.button_edit) {

                    try {
                        /*UpdateWorkDialog updateWorkDialog = new UpdateWorkDialog( context, aWorks.get( position ), position );
                        updateWorkDialog.show();
                        Toast.makeText( context, "Update", Toast.LENGTH_SHORT ).show();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }



    public void setPopUpMenuListener(PopupMenu.OnDismissListener listener) {
        this.listener = listener;
    }

    public void setDialogListener(DialogInterface.OnDismissListener listenerDialog) {
        this.listenerDialog = listenerDialog;
    }

    @Override
    public int getItemCount() {
        return aMoneyReceives.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageButton button;
        TextView price, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_receive_card);
            button = itemView.findViewById(R.id.button_card_view_received);
            price = itemView.findViewById(R.id.text_price_card_received);
            date = itemView.findViewById(R.id.text_date_card_received);
        }
    }
}
