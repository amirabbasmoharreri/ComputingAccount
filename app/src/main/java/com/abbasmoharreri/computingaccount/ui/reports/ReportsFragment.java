package com.abbasmoharreri.computingaccount.ui.reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.reports.FetchPersonNames;
import com.abbasmoharreri.computingaccount.database.reports.FetchReportNumbers;
import com.abbasmoharreri.computingaccount.database.reports.ReportWNameATime;
import com.abbasmoharreri.computingaccount.database.reports.ReportWNumber;
import com.abbasmoharreri.computingaccount.database.reports.ReportWPersonATime;
import com.abbasmoharreri.computingaccount.database.reports.ReportWTime;
import com.abbasmoharreri.computingaccount.database.reports.FetchWorkNames;
import com.abbasmoharreri.computingaccount.module.APerson;
import com.abbasmoharreri.computingaccount.module.AWorkName;
import com.abbasmoharreri.computingaccount.persiandatepicker.PersianDatePicker;
import com.abbasmoharreri.computingaccount.persiandatepicker.util.PersianCalendar;
import com.abbasmoharreri.computingaccount.pesiandate.DateConverter;
import com.abbasmoharreri.computingaccount.text.TextProcessing;
import com.abbasmoharreri.computingaccount.ui.adapters.WorkAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ReportsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogInterface.OnDismissListener, SwipeRefreshLayout.OnRefreshListener {

    private PersianCalendar persianCalendar;
    private PersianDatePicker startDate, endDate;
    private AutoCompleteTextView autoCompleteTextView;
    private FloatingActionButton button;
    private Spinner reportSpinner;
    private Context context;
    private List<AWorkName> workNames;
    private List<APerson> personNames;
    private List<Integer> reportNumbers;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private TextProcessing textProcessing1, textProcessing2;
    String[] reportTexts;
    public WorkAdapter workAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String startDateString, endDateString;
    private boolean isUseStartDatePicker = false;
    private boolean isUseEndDatePicker = false;
    private boolean isUseAutoCompleteEditText = false;
    private boolean isExistenceData = false;
    private boolean isNeedDatePicker = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reports, container, false);

        textProcessing1 = new TextProcessing();
        textProcessing2 = new TextProcessing();
        context = root.getContext();

        appBarLayout = root.findViewById(R.id.app_bar_layout_report);
        collapsingToolbarLayout = root.findViewById(R.id.toolbar_layout_report);

        disableScrollingCollapse();


        recyclerView = root.findViewById(R.id.recycle_view_report);

        startDate = root.findViewById(R.id.picker_start_date_report);
        endDate = root.findViewById(R.id.picker_end_date_report);
        autoCompleteTextView = root.findViewById(R.id.text_autocomplete_report);
        reportSpinner = root.findViewById(R.id.spinner_report);
        persianCalendar = new PersianCalendar();

        reportSpinner.setOnItemSelectedListener(this);
        button = root.findViewById(R.id.floating_layout_report);
        button.setOnClickListener(this);

        swipeRefreshLayout = root.findViewById(R.id.recycle_view_swipe_refresh_report);
        swipeRefreshLayout.setOnRefreshListener(this);

        reportTexts = getResources().getStringArray(R.array.Reports_Mode);

        workNames = new ArrayList<>();
        personNames = new ArrayList<>();

        getList();
        setSpinnerData();
        setAutoCompleteTextView();
        setListenerDate();


        return root;
    }


    private void disableScrollingCollapse() {

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
    }


    private void enableScrollingCollapse() {

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }


    private void setAutoCompleteTextView() {

        ArrayAdapter<String> adapter;

        if (reportTexts[0].equals(reportSpinner.getSelectedItem().toString())) {

            String[] names = new String[workNames.size()];
            for (int i = 0; i < workNames.size(); i++) {
                names[i] = workNames.get(i).getName();
            }
            adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, names);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);
        } else if (reportTexts[2].equals(reportSpinner.getSelectedItem().toString())) {
            String[] names = new String[personNames.size()];
            for (int i = 0; i < personNames.size(); i++) {
                names[i] = personNames.get(i).getName();
            }
            adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, names);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);
        } else if (reportTexts[3].equals(reportSpinner.getSelectedItem().toString())) {
            String[] names = new String[reportNumbers.size()];
            for (int i = 0; i < reportNumbers.size(); i++) {
                names[i] = reportNumbers.get(i).toString();
            }
            adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, names);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);
        }
    }


    private void setSpinnerData() {
        String[] spinnerList = getResources().getStringArray(R.array.Reports_Mode);
        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, reportTexts);
        spinnerArray.setDropDownViewResource(R.layout.popup_autocomplete);
        reportSpinner.setAdapter(spinnerArray);
    }


    private void getList() {

        FetchWorkNames fetchWorkNames = new FetchWorkNames(context);
        workNames = fetchWorkNames.getList();
        FetchPersonNames fetchPersonNames = new FetchPersonNames(context);
        personNames = fetchPersonNames.getList();
        FetchReportNumbers fetchReportNumbers = new FetchReportNumbers(context);
        reportNumbers = fetchReportNumbers.getList();
    }


    private void fetchReports() {

        Object selectedItem = reportSpinner.getSelectedItem();
        if (selectedItem.equals(reportTexts[0])) {

            ReportWNameATime reportWNameATime = new ReportWNameATime(context
                    , autoCompleteTextView.getText().toString()
                    , textProcessing1.convertStringToDateWithoutTime(startDateString)
                    , textProcessing2.convertStringToDateWithoutTime(endDateString));
            if (reportWNameATime.getList().size() != 0) {
                workAdapter = new WorkAdapter(context, reportWNameATime.getList());
                recyclerView.setAdapter(workAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                isExistenceData = true;
            } else {
                exceptionMessage();
                isExistenceData = false;
            }


        } else if (selectedItem.equals(reportTexts[1])) {


                ReportWTime reportWTime = new ReportWTime(context
                        , textProcessing1.convertStringToDateWithoutTime(startDateString)
                        , textProcessing2.convertStringToDateWithoutTime(endDateString));

                if (reportWTime.getList().size() != 0) {
                    workAdapter = new WorkAdapter(context, reportWTime.getList());
                    recyclerView.setAdapter(workAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    isExistenceData = true;
                } else {
                    exceptionMessage();
                    isExistenceData = false;
                }

        } else if (reportTexts[2].equals(selectedItem)) {

                ReportWPersonATime reportWPersonATime = new ReportWPersonATime(context
                        , autoCompleteTextView.getText().toString()
                        , textProcessing1.convertStringToDateWithoutTime(startDateString)
                        , textProcessing2.convertStringToDateWithoutTime(endDateString));
                if (reportWPersonATime.getList().size() != 0) {
                    workAdapter = new WorkAdapter(context, reportWPersonATime.getList());
                    recyclerView.setAdapter(workAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    isExistenceData = true;
                } else {
                    exceptionMessage();
                    isExistenceData = false;
                }


        } else if (selectedItem.equals(reportTexts[3])) {

            ReportWNumber reportWNumber = new ReportWNumber(context, Integer.parseInt(autoCompleteTextView.getText().toString()));

            if (reportWNumber.getList().size() != 0) {
                workAdapter = new WorkAdapter(context, reportWNumber.getList());
                recyclerView.setAdapter(workAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                isExistenceData = true;
            } else {
                exceptionMessage();
                isExistenceData = false;
            }

        }
    }

    private void setListenerDate() {
        startDate.setOnDateChangedListener(new PersianDatePicker.OnDateChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateChanged(int newYear, int newMonth, int newDay) {
                DateConverter dateConverter = new DateConverter();
                dateConverter.setIranianDate(newYear, newMonth, newDay);
                dateConverter.setHour(0);
                dateConverter.setMinute(0);
                startDateString = dateConverter.getGregorianYear() + "-" + String.format("%02d", dateConverter.getGregorianMonth()) + "-" + String.format("%02d", dateConverter.getGregorianDay()) + " " + String.format("%02d", dateConverter.getHour()) + ":" + String.format("%02d", dateConverter.getMinute());
                isUseStartDatePicker = true;
            }
        });


        endDate.setOnDateChangedListener(new PersianDatePicker.OnDateChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateChanged(int newYear, int newMonth, int newDay) {
                DateConverter dateConverter = new DateConverter();
                dateConverter.setIranianDate(newYear, newMonth, newDay + 1);
                dateConverter.setHour(23);
                dateConverter.setMinute(59);
                endDateString = dateConverter.getGregorianYear() + "-" + String.format("%02d", dateConverter.getGregorianMonth()) + "-" + String.format("%02d", dateConverter.getGregorianDay()) + " " + String.format("%02d", dateConverter.getHour()) + ":" + String.format("%02d", dateConverter.getMinute());
                isUseEndDatePicker = true;
            }
        });
    }

    private void exceptionMessage() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.message_title_attention)
                .setMessage(R.string.massage_no_data)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private boolean showMassage() {

        if ((!isUseStartDatePicker || !isUseEndDatePicker) && isNeedDatePicker) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_datePicker)
                    .setMessage(R.string.massage_inputStartEndDate)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }


        if (autoCompleteTextView.getText().toString().equals("") && isUseAutoCompleteEditText) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_name)
                    .setMessage(R.string.massage_inputName)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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

        if (view.getId() == R.id.floating_layout_report) {

            fetchReports();
            if (showMassage() && isExistenceData) {
                enableScrollingCollapse();
                appBarLayout.setExpanded(false);
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Object selectedItem = reportSpinner.getSelectedItem();
        if (selectedItem.equals(reportTexts[0])) {

            startDate.setEnabled(true);
            startDate.setAlpha(1.0f);
            endDate.setEnabled(true);
            endDate.setAlpha(1.0f);
            autoCompleteTextView.setEnabled(true);
            autoCompleteTextView.setAlpha(1.0f);
            isUseAutoCompleteEditText = true;
            isNeedDatePicker = true;

        } else if (selectedItem.equals(reportTexts[1])) {

            startDate.setEnabled(true);
            startDate.setAlpha(1.0f);
            endDate.setEnabled(true);
            endDate.setAlpha(1.0f);
            autoCompleteTextView.setEnabled(false);
            autoCompleteTextView.setAlpha(0.5f);
            isUseAutoCompleteEditText = false;
            isNeedDatePicker = true;

        } else if (selectedItem.equals(reportTexts[2])) {

            startDate.setEnabled(true);
            startDate.setAlpha(1.0f);
            endDate.setEnabled(true);
            endDate.setAlpha(1.0f);
            autoCompleteTextView.setEnabled(true);
            autoCompleteTextView.setAlpha(1.0f);
            isUseAutoCompleteEditText = true;
            isNeedDatePicker = true;

        } else if (selectedItem.equals(reportTexts[3])) {
            startDate.setEnabled(false);
            startDate.setAlpha(0.5f);
            endDate.setEnabled(false);
            endDate.setAlpha(0.5f);
            autoCompleteTextView.setEnabled(true);
            autoCompleteTextView.setAlpha(1.0f);
            isUseAutoCompleteEditText = true;
            isNeedDatePicker = false;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        workAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        workAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


}