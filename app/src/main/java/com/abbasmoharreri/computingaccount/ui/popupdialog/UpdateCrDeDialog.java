package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.FetchData;
import com.abbasmoharreri.computingaccount.database.reports.FetchPersonNames;
import com.abbasmoharreri.computingaccount.module.APerson;
import com.abbasmoharreri.computingaccount.persiandatepicker.PersianDatePicker;
import com.abbasmoharreri.computingaccount.persiandatepicker.util.PersianCalendar;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;
import com.abbasmoharreri.computingaccount.text.TextProcessing;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateCrDeDialog extends Dialog implements View.OnClickListener, PersianDatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private ACraveAndDebt aCraveAndDebt;
    private Context context;
    private AutoCompleteTextView person;
    private EditText price, comment;
    private RadioButton crave, debt;
    private List<APerson> personNames;
    private PersianDatePicker datePicker;
    private TimePicker time;
    private String dateString, timeString, type;
    private Date crDeDate;
    private Button updateAdd, cancel;
    private List<ACraveAndDebt> aCraveAndDebts;
    private boolean isUseDatePicker = false;
    private boolean isUpdateForm = false;

    public UpdateCrDeDialog(@NonNull Context context, ACraveAndDebt aCraveAndDebt) {
        super(context);
        this.context = context;
        this.aCraveAndDebt = aCraveAndDebt;
        isUpdateForm = true;
    }

    public UpdateCrDeDialog(@NonNull Context context, List<ACraveAndDebt> aCraveAndDebtList) {
        super(context);
        this.context = context;
        this.aCraveAndDebt = new ACraveAndDebt();
        this.aCraveAndDebts = aCraveAndDebtList;
        isUpdateForm = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_crde);

        person = findViewById(R.id.dialog_update_person_name_crde);
        price = findViewById(R.id.dialog_update_price_crde);
        comment = findViewById(R.id.dialog_update_comment_crde);
        crave = findViewById(R.id.dialog_update_radio_crave_crde);
        debt = findViewById(R.id.dialog_update_radio_debt_crde);
        datePicker = findViewById(R.id.dialog_update_date_crde);
        time = findViewById(R.id.dialog_update_time_crde);
        updateAdd = findViewById(R.id.dialog_update_button_update_crde);
        cancel = findViewById(R.id.dialog_update_button_cancel_crde);

        updateAdd.setOnClickListener(this);
        cancel.setOnClickListener(this);

        datePicker.setOnDateChangedListener(this);
        time.setIs24HourView(true);
        time.setOnTimeChangedListener(this);


        if (isUpdateForm) {
            updateAdd.setText(R.string.button_name_update);
            setDataView();
        } else {
            updateAdd.setText(R.string.button_name_add);
            getSystemDate();
        }


        getList();
        setAutoCompleteTextView();
        setEditTextWatcher();
    }

    private void getSystemDate() {

        Calendar calendar = Calendar.getInstance();
        isUseDatePicker = false;
        TextProcessing textProcessing = new TextProcessing();
        Date date1 = calendar.getTime();
        timeString = textProcessing.convertDateToStringWithoutDate(date1);

        PersianCalendar persianCalendar = new PersianCalendar();
        datePicker.setDisplayPersianDate(persianCalendar);
        dateString = textProcessing.convertDateToStringWithoutTime(date1);

    }


    @SuppressLint({"DefaultLocale", "NewApi"})
    private void setDataView() {

        person.setText(aCraveAndDebt.getPersonName());
        price.setText(String.format("%,d", aCraveAndDebt.getPrice()));
        comment.setText(aCraveAndDebt.getComment());

        TextProcessing textProcessing = new TextProcessing();
        this.dateString = textProcessing.convertDateToString(aCraveAndDebt.getGregorianDate());
        this.timeString = textProcessing.convertDateToStringWithoutDate(aCraveAndDebt.getGregorianDate());

        datePicker.setDisplayDate(aCraveAndDebt.getGregorianDate());
        time.setHour(aCraveAndDebt.getIranianDate().getHours());
        time.setMinute(aCraveAndDebt.getIranianDate().getMinutes());

        if (aCraveAndDebt.getType().equals(ACraveAndDebt.CRAVE)) {
            debt.setChecked(false);
            crave.setChecked(true);
        } else if (aCraveAndDebt.getType().equals(ACraveAndDebt.DEBT)) {
            debt.setChecked(true);
            crave.setChecked(false);
        }

    }


    private void getList() {

        FetchPersonNames fetchPersonNames = new FetchPersonNames(context);
        personNames = fetchPersonNames.getList();

    }

    private void setEditTextWatcher() {

        price.addTextChangedListener(new NumberTextWatcherForThousand(price));

    }

    private void setAutoCompleteTextView() {

        ArrayAdapter<String> adapter;

        String[] names = new String[personNames.size()];
        for (int i = 0; i < personNames.size(); i++) {
            names[i] = personNames.get(i).getName();
        }
        adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, names);
        person.setThreshold(1);
        person.setAdapter(adapter);

    }

    private void getDate() {
        TextProcessing textProcessing = new TextProcessing();
        crDeDate = textProcessing.convertStringToDate(this.dateString + " " + this.timeString);
    }

    private void getType() {
        if (crave.isChecked()) {
            type = ACraveAndDebt.CRAVE;
        } else if (debt.isChecked()) {
            type = ACraveAndDebt.DEBT;
        }
    }


    private boolean isExistItem(String name) {
        for (ACraveAndDebt aCraveAndDebt : aCraveAndDebts) {
            if (aCraveAndDebt.getPersonName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    private boolean showMassage() {

        if (person.getText().toString().equals("")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_personName)
                    .setMessage(R.string.massage_inputPersonName)
                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        if (price.getText().toString().equals("")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_price)
                    .setMessage(R.string.massage_inputPrice)
                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }


        if (!crave.isChecked() && !debt.isChecked() ) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_crave_and_debt)
                    .setMessage(R.string.massage_selectTypeOfCraveDebt)
                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        if (comment.getText().toString().equals("")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_comment)
                    .setMessage(R.string.massage_inputComment)
                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }


        return true;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_update_button_update_crde:

                if(showMassage()) {
                    if (isUpdateForm) {

                        FetchData fetchData = new FetchData(context);
                        getDate();
                        getType();
                        aCraveAndDebt.setPersonId(fetchData.update_ACraveDebt_PersonId(person.getText().toString()))
                                .setPersonName(person.getText().toString())
                                .setPrice(Integer.parseInt(price.getText().toString()))
                                .setComment(comment.getText().toString())
                                .setType(type);

                        if (isUseDatePicker) {
                            aCraveAndDebt.setIranianDate(crDeDate);
                        } else {
                            aCraveAndDebt.setGregorianDate(crDeDate);
                        }

                        DataBaseController dataBaseController = new DataBaseController(context);
                        dataBaseController.updateCraveDebt(aCraveAndDebt);


                    } else {

                        FetchData fetchData = new FetchData(context);
                        getDate();
                        getType();
                        aCraveAndDebt.setPersonId(fetchData.update_ACraveDebt_PersonId(person.getText().toString()))
                                .setPersonName(person.getText().toString())
                                .setPrice(Integer.parseInt(price.getText().toString()))
                                .setComment(comment.getText().toString())
                                .setType(type);

                        if (isUseDatePicker) {
                            aCraveAndDebt.setIranianDate(crDeDate);
                        } else {
                            aCraveAndDebt.setGregorianDate(crDeDate);
                        }

                        if (isExistItem(aCraveAndDebt.getPersonName())) {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.massage_title_attention)
                                    .setMessage(R.string.massage_isExistPerson)
                                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        DataBaseController dataBaseController = new DataBaseController(context);
                        dataBaseController.insertCraveDebt(aCraveAndDebt);


                    }
                    dismiss();
                }

                break;
            case R.id.dialog_update_button_cancel_crde:

                dismiss();

                break;
        }
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onDateChanged(int newYear, int newMonth, int newDay) {
        this.dateString = newYear + "-" + String.format("%02d", newMonth) + "-" + String.format("%02d", newDay);
        isUseDatePicker = true;
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        this.timeString = hourOfDay + ":" + minute;
    }


}
