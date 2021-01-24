package com.abbasmoharreri.computingaccount.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.ui.popupdialog.UpdateItemDialog;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private Context context;
    private List<AItem> aItemList;
    private PopupMenu.OnDismissListener listener;
    private DialogInterface.OnDismissListener listenerDialog;


    public ItemAdapter(Context context, List<AItem> aItemList) {
        this.context = context;
        this.aItemList = aItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.card_view_items, parent, false);
        viewHolder = new ViewHolder(contentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        TextView name = holder.name;
        ImageView button = holder.button;
        CheckBox condition = holder.condition;


        AItem aItem = aItemList.get(position);

        if (aItem.getClassName().equals(AItem.WORK_NAME)) {

            if (aItem.getCondition()) {
                condition.setChecked(true);
            } else {
                condition.setChecked(false);
            }
            condition.setEnabled(false);


        } else {
            condition.setEnabled(false);
            condition.setVisibility(View.INVISIBLE);
        }

        name.setText(aItem.getName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, position);
            }
        });
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
                                        dataBaseController.deleteItem(aItemList.get(position), aItemList.get(position).getDataBaseName());
                                        aItemList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, R.string.toast_deleteItem, Toast.LENGTH_SHORT).show();
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
                        UpdateItemDialog updateItemDialog = new UpdateItemDialog(context, aItemList.get(position));
                        updateItemDialog.setOnDismissListener(listenerDialog);
                        updateItemDialog.show();
                        Toast.makeText(context, R.string.toast_updateItem, Toast.LENGTH_SHORT).show();
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
        return aItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView button;
        CheckBox condition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            condition = itemView.findViewById(R.id.checkbox_card_view_item);
            name = itemView.findViewById(R.id.text_name_card_item);
            button = itemView.findViewById(R.id.button_card_view_item);
        }
    }
}
