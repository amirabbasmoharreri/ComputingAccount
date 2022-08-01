package com.abbasmoharreri.computingaccount;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.database.reports.FetchPersonNames;
import com.abbasmoharreri.computingaccount.database.reports.FetchReceived;
import com.abbasmoharreri.computingaccount.database.reports.FetchReports;
import com.abbasmoharreri.computingaccount.database.reports.FetchWalletCard;
import com.abbasmoharreri.computingaccount.database.reports.FetchWorkNames;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.ui.adapters.ItemAdapter;
import com.abbasmoharreri.computingaccount.ui.adapters.ReceivedAdapter;
import com.abbasmoharreri.computingaccount.ui.adapters.ReportAdapter;
import com.abbasmoharreri.computingaccount.ui.adapters.WalletCardAdapter;
import com.abbasmoharreri.computingaccount.ui.popupdialog.InitializeObjects;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ItemsActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, PopupMenu.OnDismissListener {

    private RecyclerView recyclerView;
    private String itemName;
    private FloatingActionButton addButton;
    private ItemAdapter itemAdapter;
    private ReportAdapter reportAdapter;
    private ReceivedAdapter receivedAdapter;
    private WalletCardAdapter walletCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.Light_AppTheme);
        }
        setContentView(R.layout.activity_items);

        this.itemName = getIntent().getDataString();
        recyclerView = findViewById(R.id.activity_items_recycle_view);
        this.addButton = findViewById(R.id.floating_items);

        this.addButton.setOnClickListener(this);

        try {
            getListItems();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getListItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListItems() {

        if (itemName.equals("Persons")) {
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
            FetchPersonNames fetchPersonNames = new FetchPersonNames(this);
            itemAdapter = new ItemAdapter(this, fetchPersonNames.getListItem());
            itemAdapter.setPopUpMenuListener(this);
            itemAdapter.setDialogListener(this);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (itemName.equals("Works")) {
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
            FetchWorkNames fetchWorkNames = new FetchWorkNames(this);
            itemAdapter = new ItemAdapter(this, fetchWorkNames.getListItem());
            itemAdapter.setPopUpMenuListener(this);
            itemAdapter.setDialogListener(this);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (itemName.equals("Reports")) {
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
            FetchReports fetchReports = new FetchReports(this);
            reportAdapter = new ReportAdapter(this, fetchReports.getList());
            reportAdapter.setPopUpMenuListener(this);
            reportAdapter.setDialogDismissListener(this);
            recyclerView.setAdapter(reportAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (itemName.equals("Received")) {
            addButton.setEnabled(false);
            addButton.setVisibility(View.INVISIBLE);
            FetchReceived fetchReceived = new FetchReceived(this);
            receivedAdapter = new ReceivedAdapter(this, fetchReceived.getList());
            receivedAdapter.setPopUpMenuListener(this);
            recyclerView.setAdapter(receivedAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (itemName.equals("Wallet")) {
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
            FetchWalletCard fetchWalletCard = new FetchWalletCard(this, AAccount.ACCOUNT_WALLET);
            walletCardAdapter = new WalletCardAdapter(this, fetchWalletCard.getList());
            walletCardAdapter.setPopUpMenuListener(this);
            walletCardAdapter.setDialogListener(this);
            recyclerView.setAdapter(walletCardAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (itemName.equals("Card")) {
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
            FetchWalletCard fetchWalletCard = new FetchWalletCard(this, AAccount.ACCOUNT_CARD);
            walletCardAdapter = new WalletCardAdapter(this, fetchWalletCard.getList());
            walletCardAdapter.setPopUpMenuListener(this);
            walletCardAdapter.setDialogListener(this);
            recyclerView.setAdapter(walletCardAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }


    }


    @Override
    public void onClick(View view) {
        switch (itemName) {
            case "Persons":
            case "Works":
            case "Reports":
            case "Wallet":
            case "Card":
                InitializeObjects initializeObjects = new InitializeObjects(this, itemName);
                initializeObjects.setOnDismissListener(this);
                initializeObjects.show();
                break;
            case "Received":


                break;

        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (itemName.equals("Persons")) {
            itemAdapter.notifyDataSetChanged();
        } else if (itemName.equals("Works")) {
            itemAdapter.notifyDataSetChanged();
        } else if (itemName.equals("Reports")) {
            reportAdapter.notifyDataSetChanged();
        } else if (itemName.equals("Received")) {
            receivedAdapter.notifyDataSetChanged();
        } else if (itemName.equals("Wallet")) {
            walletCardAdapter.notifyDataSetChanged();
        } else if (itemName.equals("Card")) {
            walletCardAdapter.notifyDataSetChanged();
        }
        getListItems();
    }


    @Override
    public void onDismiss(PopupMenu menu) {
        getListItems();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
