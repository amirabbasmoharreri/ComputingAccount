package com.abbasmoharreri.computingaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abbasmoharreri.computingaccount.database.reports.FetchCraveDebts;
import com.abbasmoharreri.computingaccount.database.reports.FetchLatestInventory;
import com.abbasmoharreri.computingaccount.database.reports.FetchWalletCard;
import com.abbasmoharreri.computingaccount.ui.adapters.CrDeAdapter;
import com.abbasmoharreri.computingaccount.ui.popupdialog.UpdateCrDeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CraveDebtActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, PopupMenu.OnDismissListener, SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener {

    private TextView sumCrave, sumDebt;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup radioGroup;
    private RadioButton craveRadio, debtRadio, allRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.LightTheme_AppTheme);
        }
        setContentView( R.layout.activity_crave_debt );

        sumCrave = findViewById( R.id.activity_crave_debt_crave );
        sumDebt = findViewById( R.id.activity_crave_debt_debt );
        recyclerView = findViewById( R.id.recycle_view_crave_debt );
        swipeRefreshLayout = findViewById( R.id.recycle_view_swipe_refresh_crave_debt );
        swipeRefreshLayout.setOnRefreshListener( this );
        addButton = findViewById( R.id.floating_crave_debt );
        addButton.setOnClickListener( this );
        radioGroup = findViewById( R.id.radio_group_crde );
        radioGroup.setOnCheckedChangeListener( this );
        craveRadio = findViewById( R.id.radio_crave_crde );
        debtRadio = findViewById( R.id.radio_debt_crde );
        allRadio = findViewById( R.id.radio_all_crde );

        String type = getIntent().getStringExtra( "Type" );

        if (type.equals( "All" )) {
            allRadio.setChecked( true );
            setRecyclerView();
        } else if (type.equals( "Crave" )) {
            craveRadio.setChecked( true );
            setRecyclerViewCrave();
        } else if (type.equals( "Debt" )) {
            debtRadio.setChecked( true );
            setRecyclerViewDebt();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataToolBar();
        //setRecyclerView();
    }

    @SuppressLint("DefaultLocale")
    private void setDataToolBar() {
        try {
            FetchLatestInventory fetchLatestInventory = new FetchLatestInventory( this );
            sumDebt.setText( String.format( "%,d", fetchLatestInventory.getSumDebt() ) );
            sumCrave.setText( String.format( "%,d", fetchLatestInventory.getSumCrave() ) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setRecyclerView() {
        try {
            FetchCraveDebts fetchCraveDebts = new FetchCraveDebts( this );
            CrDeAdapter crDeAdapter = new CrDeAdapter( this, fetchCraveDebts.getList() );
            crDeAdapter.setPopUpMenuListener( this );
            recyclerView.setAdapter( crDeAdapter );
            recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerViewCrave() {
        try {
            FetchCraveDebts fetchCraveDebts = new FetchCraveDebts( this );
            CrDeAdapter crDeAdapter = new CrDeAdapter( this, fetchCraveDebts.getCraveList() );
            crDeAdapter.setPopUpMenuListener( this );
            recyclerView.setAdapter( crDeAdapter );
            recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setRecyclerViewDebt() {
        try {
            FetchCraveDebts fetchCraveDebts = new FetchCraveDebts( this );
            CrDeAdapter crDeAdapter = new CrDeAdapter( this, fetchCraveDebts.getDebtList() );
            crDeAdapter.setPopUpMenuListener( this );
            recyclerView.setAdapter( crDeAdapter );
            recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.floating_crave_debt) {
            UpdateCrDeDialog updateCrDeDialog = new UpdateCrDeDialog( this );
            updateCrDeDialog.setOnDismissListener( this );
            updateCrDeDialog.show();
        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        setRecyclerView();
        setDataToolBar();
    }

    @Override
    public void onDismiss(PopupMenu popupMenu) {
        setDataToolBar();
        setRecyclerView();
    }

    @Override
    public void onRefresh() {
        setDataToolBar();
        setRecyclerView();
        swipeRefreshLayout.setRefreshing( false );
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.radio_crave_crde) {
            setRecyclerViewCrave();
        } else if (checkedId == R.id.radio_debt_crde) {
            setRecyclerViewDebt();
        } else if (checkedId == R.id.radio_all_crde) {
            setRecyclerView();
        }
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
