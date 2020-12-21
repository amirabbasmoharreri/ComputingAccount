package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.persiandatepicker.PersianDatePicker;
import com.abbasmoharreri.computingaccount.persiandatepicker.util.PersianCalendar;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;

import java.util.Date;

public class InitializeObjects extends Dialog implements View.OnClickListener {

    private Context context;
    private String itemName;
    private Button personSave, worksSave, reportSave, receivedSave, accountSave, personCancel, worksCancel, reportCancel, receivedCancel, accountCancel;
    private EditText personName, workName, reportNumber, reportPreRemained, reportRemained, reportPaid, reportAttachCount, accountNumber, accountRemained;
    private PersianDatePicker reportDatePicker;
    private CheckBox workCondition, reportCondition;
    private DataBaseController dataBaseController;
    private Spinner accountName;

    public InitializeObjects(@NonNull Context context, String itemName) {
        super( context );
        this.context = context;
        this.itemName = itemName;
        this.dataBaseController = new DataBaseController( context );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (itemName.equals( "Persons" )) {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            setContentView( R.layout.initialize_persons );
            personSave = findViewById( R.id.button_save_initialize_person );
            personCancel = findViewById( R.id.button_cancel_initialize_person );
            personSave.setOnClickListener( this );
            personCancel.setOnClickListener( this );
            personName = findViewById( R.id.text_person_initialize );

        } else if (itemName.equals( "Works" )) {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            setContentView( R.layout.initialize_works );
            worksSave = findViewById( R.id.button_save_initialize_work );
            worksCancel = findViewById( R.id.button_cancel_initialize_work );
            worksSave.setOnClickListener( this );
            worksCancel.setOnClickListener( this );
            workName = findViewById( R.id.text_work_initialize );
            workCondition = findViewById( R.id.condition_initialize_work );

        } else if (itemName.equals( "Reports" )) {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            setContentView( R.layout.initialize_reports );
            reportNumber = findViewById( R.id.text_number_initialize_report );
            reportCondition = findViewById( R.id.condition_initialize_report );
            reportDatePicker = findViewById( R.id.date_picker_initialize_report );
            reportPreRemained = findViewById( R.id.text_pre_remained_initialize_report );
            reportPaid = findViewById( R.id.text_paid_initialize_report );
            reportRemained = findViewById( R.id.text_remained_initialize_report );
            reportAttachCount = findViewById( R.id.text_attach_count_initialize_report );
            reportSave = findViewById( R.id.button_save_initialize_report );
            reportSave.setOnClickListener( this );
            reportCancel = findViewById( R.id.button_cancel_initialize_report );
            reportCancel.setOnClickListener( this );

            reportPreRemained.addTextChangedListener( new NumberTextWatcherForThousand( reportPreRemained ) );
            reportPaid.addTextChangedListener( new NumberTextWatcherForThousand( reportPaid ) );
            reportRemained.addTextChangedListener( new NumberTextWatcherForThousand( reportRemained ) );

        } else if (itemName.equals( "Received" )) {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            setContentView( R.layout.initialize_received );
        } else if (itemName.equals( "Wallet" ) || itemName.equals( "Card" )) {
            requestWindowFeature( Window.FEATURE_NO_TITLE );
            setContentView( R.layout.initialize_wallet_card );

            accountCancel = findViewById( R.id.button_cancel_wallet_card_initialize );
            accountSave = findViewById( R.id.button_save_wallet_card_initialize );
            accountCancel.setOnClickListener( this );
            accountSave.setOnClickListener( this );
            accountName = findViewById( R.id.spinner_name_wallet_card_initialize );
            accountNumber = findViewById( R.id.text_number_wallet_card_initialize );
            accountRemained = findViewById( R.id.text_remained_wallet_card_initialize );

            accountRemained.addTextChangedListener( new NumberTextWatcherForThousand( accountRemained ) );

            if (itemName.equals( "Wallet" )) {
                accountName.setSelection( 0 );
            } else {
                accountName.setSelection( 1 );
            }


        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_save_initialize_person:
                dataBaseController.insertItem( new AItem().setName( personName.getText().toString() ), AItem.PERSON );
                dismiss();
                break;
            case R.id.button_save_initialize_work:
                boolean condition = false;
                if (workCondition.isChecked()) {
                    condition = true;
                }
                dataBaseController.insertItem( new AItem().setName( workName.getText().toString() ).setCondition( condition ), AItem.WORK_NAME );
                dismiss();
                break;
            case R.id.button_save_initialize_report:
                AReport aReport = new AReport();

                if (!reportCondition.isChecked()) {
                    reportDatePicker.setDisplayPersianDate( new PersianCalendar() );
                }

                Date date = reportDatePicker.getDisplayDate();

                int preRemained = 0;
                if (!reportPreRemained.getText().toString().equals( "" )) {
                    preRemained = Integer.parseInt( NumberTextWatcherForThousand.trimCommaOfString( reportPreRemained.getText().toString() ) );
                }
                int paid = 0;
                if (!reportPaid.getText().toString().equals( "" )) {
                    paid = Integer.parseInt( NumberTextWatcherForThousand.trimCommaOfString( reportPaid.getText().toString() ) );
                }
                int remained = 0;
                if (!reportRemained.getText().toString().equals( "" )) {
                    remained = Integer.parseInt( NumberTextWatcherForThousand.trimCommaOfString( reportRemained.getText().toString() ) );
                }

                aReport.setNumber( Integer.parseInt( reportNumber.getText().toString() ) )
                        .setCondition( reportCondition.isChecked() )
                        .setPreRemained( preRemained )
                        .setPaid( paid )
                        .setRemained( remained )
                        .setAttachCount( Integer.parseInt( reportAttachCount.getText().toString() ) )
                        .setGregorianDate( date );
                dataBaseController.insertReport( aReport );
                dismiss();
                break;

            case R.id.button_save_wallet_card_initialize:

                int remainedAccount = Integer.parseInt( NumberTextWatcherForThousand.trimCommaOfString( accountRemained.getText().toString() ) );
                if (itemName.equals( "Wallet" )) {
                    dataBaseController.insertWallet( new AAccount().setName( accountName.getSelectedItem().toString() )
                            .setAccountNumber( accountNumber.getText().toString() )
                            .setRemained( remainedAccount ) );
                } else if (itemName.equals( "Card" )) {
                    dataBaseController.insertCard( new AAccount().setName( accountName.getSelectedItem().toString() )
                            .setAccountNumber( accountNumber.getText().toString() )
                            .setRemained( remainedAccount ) );
                }
                dismiss();
                break;


            case R.id.button_cancel_initialize_person:
            case R.id.button_cancel_initialize_work:
            case R.id.button_cancel_initialize_report:
            case R.id.button_cancel_wallet_card_initialize:
                dismiss();
                break;
        }

    }
}
