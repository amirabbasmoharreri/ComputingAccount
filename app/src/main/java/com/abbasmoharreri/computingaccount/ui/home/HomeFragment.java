package com.abbasmoharreri.computingaccount.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abbasmoharreri.computingaccount.CraveDebtActivity;
import com.abbasmoharreri.computingaccount.ItemsActivity;
import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.reports.FetchLatestInventory;
import com.abbasmoharreri.computingaccount.database.reports.ReportWMaxReport;
import com.abbasmoharreri.computingaccount.ui.adapters.WorkAdapter;
import com.abbasmoharreri.computingaccount.ui.popupdialog.CustomProgressBar;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnDismissListener, PopupMenu.OnDismissListener, View.OnClickListener {


    private RecyclerView recyclerView;
    public WorkAdapter workAdapter;
    private ReportWMaxReport reportWMaxReport;
    private FetchLatestInventory fetchLatestInventory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView remained, received, crave, debt;
    private ImageView craveImage, debtImage, receiveImage;

    HomeFragment context = this;
    CustomProgressBar customProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycle_view_home);

        swipeRefreshLayout = root.findViewById(R.id.recycle_view_swipe_refresh_home);
        swipeRefreshLayout.setOnRefreshListener(this);

        remained = root.findViewById(R.id.text_remained_activity_main);
        received = root.findViewById(R.id.text_received_activity_main);
        crave = root.findViewById(R.id.text_crave_activity_main);
        debt = root.findViewById(R.id.text_debt_activity_main);
        craveImage = root.findViewById(R.id.image_crave_home);
        debtImage = root.findViewById(R.id.image_debt_home);
        receiveImage = root.findViewById(R.id.image_receive_home);
        crave.setOnClickListener(this);
        debt.setOnClickListener(this);
        debtImage.setOnClickListener(this);
        craveImage.setOnClickListener(this);
        receiveImage.setOnClickListener(this);
        received.setOnClickListener(this);
        customProgressBar = new CustomProgressBar(getContext());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        new BackgroundTask().execute("start");
    }


    @SuppressLint("DefaultLocale")
    private void setDataToolBar() {
        try {
            remained.setText(String.format("%,d", fetchLatestInventory.getRemainedReport()));
            received.setText(String.format("%,d", fetchLatestInventory.getSumReceivedReport()));
            crave.setText(String.format("%,d", fetchLatestInventory.getSumCrave()));
            debt.setText(String.format("%,d", fetchLatestInventory.getSumDebt()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setRecyclerView() {
        workAdapter.setPopUpMenuListener(context);
        recyclerView.setAdapter(workAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onRefresh() {
        new BackgroundTask().execute("start");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        workAdapter.notifyDataSetChanged();
        setDataToolBar();
    }

    @Override
    public void onDismiss(PopupMenu popupMenu) {
        new BackgroundTask().execute("start");
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.image_crave_home:
            case R.id.text_crave_activity_main:
                intent = new Intent(getContext(), CraveDebtActivity.class);
                intent.putExtra("Type", "Crave");
                startActivity(intent);
                break;
            case R.id.image_debt_home:
            case R.id.text_debt_activity_main:
                intent = new Intent(getContext(), CraveDebtActivity.class);
                intent.putExtra("Type", "Debt");
                startActivity(intent);
                break;
            case R.id.image_receive_home:
            case R.id.text_received_activity_main:
                intent = new Intent(getContext(), ItemsActivity.class);
                intent.setData(Uri.parse("Received"));
                startActivity(intent);
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            fetchLatestInventory = new FetchLatestInventory(getActivity());
            reportWMaxReport = new ReportWMaxReport(getActivity());
            workAdapter = new WorkAdapter(getActivity(), reportWMaxReport.getList());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setDataToolBar();
            setRecyclerView();
            customProgressBar.dismiss();
            this.onCancelled();
        }
    }
}
