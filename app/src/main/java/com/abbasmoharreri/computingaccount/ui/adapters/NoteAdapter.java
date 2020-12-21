package com.abbasmoharreri.computingaccount.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> stringList;
    private ViewHolder viewHolder;
    private DialogInterface.OnDismissListener listener;
    private String st;

    public NoteAdapter(Context context, List<String> stringList) {
        this.stringList = new ArrayList<>();
        this.stringList = stringList;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View contentView = inflater.inflate( R.layout.card_view_note, parent, false );
        viewHolder = new ViewHolder( contentView );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView textView = viewHolder.textView;
        final ImageView delete = viewHolder.delete;

        final String st = stringList.get( position );
        this.st = st;

        textView.setText( st );

        delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder( context )
                        .setTitle( "Warning" )
                        .setMessage( "Do you want to delete this item?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem( true );
                                dialog.dismiss();
                            }
                        } )
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem( false );
                                dialog.dismiss();
                            }
                        } )
                        .setIcon( android.R.drawable.stat_sys_warning )
                        .setOnDismissListener( listener )
                        .show();

            }
        } );

    }

    private void deleteItem(boolean choose) {
        if (choose) {
            DataBaseController dataBaseController = new DataBaseController( context );
            dataBaseController.deleteNote( st );
            Toast.makeText( context, R.string.toast_deleteItem, Toast.LENGTH_SHORT ).show();
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.note_fragment_delete_button) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView delete;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            delete = itemView.findViewById( R.id.note_fragment_delete_button );
            textView = itemView.findViewById( R.id.note_fragment_text );
        }
    }
}
