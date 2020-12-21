package com.abbasmoharreri.computingaccount.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.abbasmoharreri.computingaccount.database.reports.DeletingData;
import com.abbasmoharreri.computingaccount.module.AList;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.ui.popupdialog.ShowDetailWorks;
import com.abbasmoharreri.computingaccount.ui.popupdialog.UpdateWorkActivity;

import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private Context context;
    private List<AWork> aWorks;
    private PopupMenu.OnDismissListener listener;


    public WorkAdapter(Context context, List<AWork> aWorks) {
        this.context = context;
        this.aWorks = aWorks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View contentView = inflater.inflate( R.layout.card_view_work, parent, false );
        viewHolder = new ViewHolder( contentView );


        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;
        TextView name = holder.name;
        TextView price = holder.price;
        TextView person = holder.person;
        TextView date = holder.date;
        ImageView button = holder.button;

        AWork aWork = aWorks.get( position );
        name.setText( aWork.getName() );
        price.setText( String.format( "%,d", aWork.getPrice() ) );
        person.setText( aWork.getPersonName() );
        date.setText( aWork.getStringIranianDate() );

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup( view, position );
            }
        } );

        cardView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup2( v, position );
                return false;
            }
        } );

    }

    public void showPopup2(View v, final int position) {
        ShowDetailWorks showDetailWorks = new ShowDetailWorks( context, aWorks.get( position ) );
        showDetailWorks.show();
    }


    public void showPopup(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu( context, v );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.card_view_menu, popupMenu.getMenu() );
        popupMenu.show();


        popupMenu.setOnDismissListener( listener );
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.button_delete) {
                    try {
                        DeletingData deletingData = new DeletingData( context );
                        deletingData.deleteFromWorkList( aWorks.get( position ) );
                        aWorks.remove( position );
                        notifyDataSetChanged();
                        Toast.makeText( context, R.string.toast_deleteItem, Toast.LENGTH_SHORT ).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (item.getItemId() == R.id.button_edit) {


                    Intent intent = new Intent( context, UpdateWorkActivity.class );
                    intent.putExtra( AList.KEY_PARCELABLE_Work, aWorks.get( position ) );
                    context.startActivity( intent );
                    Toast.makeText( context, R.string.toast_updateItem, Toast.LENGTH_SHORT ).show();
                    notifyDataSetChanged();
                }
                return false;
            }
        } );
    }

    public void setPopUpMenuListener(PopupMenu.OnDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return aWorks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, date, person;
        ImageView button;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            cardView = itemView.findViewById( R.id.card_card_view_work );
            name = itemView.findViewById( R.id.text_work_name_card_work );
            price = itemView.findViewById( R.id.text_price_card_work );
            person = itemView.findViewById( R.id.text_person_card_work );
            date = itemView.findViewById( R.id.text_date_card_work );
            button = itemView.findViewById( R.id.button_card_view_work );
        }
    }
}
