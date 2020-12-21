package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.FetchPersonNames;
import com.abbasmoharreri.computingaccount.database.reports.FetchWorkNames;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.APerson;
import com.abbasmoharreri.computingaccount.module.AWorkName;

import java.util.List;

public class UpdateItemDialog extends Dialog implements View.OnClickListener {

    private AItem aItem;
    private Context context;
    private AutoCompleteTextView name;
    private Button update, cancel;
    private CheckBox workCondition;
    private boolean condition;
    private List<AWorkName> workNames;
    private List<APerson> personNames;

    public UpdateItemDialog(@NonNull Context context, AItem aItem) {
        super( context );
        this.context = context;
        this.aItem = aItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.dialog_update_item );
        name = findViewById( R.id.dialog_update_name_item );
        workCondition = findViewById( R.id.dialog_update_condition_work );
        update = findViewById( R.id.dialog_update_button_update_item );
        update.setOnClickListener( this );
        cancel = findViewById( R.id.dialog_update_button_cancel_item );
        cancel.setOnClickListener( this );

        if (aItem.getClassName().equals( AItem.PERSON )) {
            workCondition.setVisibility( View.INVISIBLE );
            workCondition.setEnabled( false );
        } else if (aItem.getClassName().equals( AItem.WORK_NAME )) {
            workCondition.setVisibility( View.VISIBLE );
            workCondition.setEnabled( true );
        }


        getList();
        setAutoCompleteTextView();
        setData();
    }

    private void getList() {

        FetchWorkNames fetchWorkNames = new FetchWorkNames( context );
        workNames = fetchWorkNames.getList();
        FetchPersonNames fetchPersonNames = new FetchPersonNames( context );
        personNames = fetchPersonNames.getList();

    }

    private void setAutoCompleteTextView() {

        ArrayAdapter<String> adapter;

        if (aItem.getClassName().equals( AItem.WORK_NAME )) {

            String[] names = new String[workNames.size()];
            for (int i = 0; i < workNames.size(); i++) {
                names[i] = workNames.get( i ).getName();
            }
            adapter = new ArrayAdapter<String>( context, R.layout.popup_autocomplete, names );
            name.setThreshold( 1 );
            name.setAdapter( adapter );
        } else if (aItem.getClassName().equals( AItem.PERSON )) {
            String[] names = new String[personNames.size()];
            for (int i = 0; i < personNames.size(); i++) {
                names[i] = personNames.get( i ).getName();
            }
            adapter = new ArrayAdapter<String>( context, R.layout.popup_autocomplete, names );
            name.setThreshold( 1 );
            name.setAdapter( adapter );
        }
    }

    private void setData() {

        if (aItem.getClassName().equals( AItem.PERSON )) {
            name.setText( aItem.getName() );
        } else if (aItem.getClassName().equals( AItem.WORK_NAME )) {
            name.setText( aItem.getName() );
            condition = aItem.getCondition();
            workCondition.setChecked( condition );
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_update_button_update_item:
                aItem.setName( name.getText().toString() );

                if (workCondition.isChecked()) {
                    condition = true;
                } else {
                    condition = false;
                }
                aItem.setCondition( condition );
                DataBaseController dataBaseController = new DataBaseController( context );
                dataBaseController.updateItem( aItem, aItem.getClassName() );

                dismiss();

                break;
            case R.id.dialog_update_button_cancel_item:

                dismiss();

                break;
        }
    }
}
