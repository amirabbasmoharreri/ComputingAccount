package com.abbasmoharreri.computingaccount.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abbasmoharreri.computingaccount.CraveDebtActivity;
import com.abbasmoharreri.computingaccount.ItemsActivity;
import com.abbasmoharreri.computingaccount.PaidActivity;
import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.ReceiveActivity;
import com.abbasmoharreri.computingaccount.database.reports.FetchLatestInventory;
import com.abbasmoharreri.computingaccount.database.reports.ReportWMaxReport;
import com.abbasmoharreri.computingaccount.ui.adapters.WorkAdapter;
import com.abbasmoharreri.computingaccount.ui.popupdialog.CustomProgressBar;
import com.abbasmoharreri.computingaccount.ui.popupdialog.TransferMoney;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnDismissListener, PopupMenu.OnDismissListener, View.OnClickListener {


    private RecyclerView recyclerView;
    public WorkAdapter workAdapter;
    private ReportWMaxReport reportWMaxReport;
    private FetchLatestInventory fetchLatestInventory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView remained, received, crave, debt;
    private ImageView craveImage, debtImage, receiveImage;

    private FloatingActionButton fab_Main, fab_Paid, fab_Receive, fab_Debt_Crave, fab_Transfer;
    TextView text_Paid, text_Receive, text_Debt_Crave, text_Transfer;
    private boolean isFabMenuOpen = false;
    private Animation fabOpenAnimation, fabCloseAnimation, fabClock, fabAntiClock, textOpen, textClose;
    ImageView background_fab;
    Intent intent;

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


        getAnimations();


        fab_Main = root.findViewById(R.id.floating_main);
        fab_Paid = root.findViewById(R.id.floating_paid);
        fab_Receive = root.findViewById(R.id.floating_receive);
        fab_Debt_Crave = root.findViewById(R.id.floating_debt_and_crave);
        fab_Transfer = root.findViewById(R.id.floating_transfer);

        background_fab = root.findViewById(R.id.background_fab);

        text_Paid = root.findViewById(R.id.text_paid);
        text_Receive = root.findViewById(R.id.text_receive);
        text_Debt_Crave = root.findViewById(R.id.text_debt_and_crave);
        text_Transfer = root.findViewById(R.id.text_transfer);

        text_Paid.setOnClickListener(this);
        text_Receive.setOnClickListener(this);
        text_Debt_Crave.setOnClickListener(this);
        text_Transfer.setOnClickListener(this);

        background_fab.setOnClickListener(this);

        fab_Main.setOnClickListener(this);
        fab_Paid.setOnClickListener(this);
        fab_Receive.setOnClickListener(this);
        fab_Debt_Crave.setOnClickListener(this);
        fab_Transfer.setOnClickListener(this);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        collapseFabMenu();
        background_fab.setClickable(false);
        background_fab.setAlpha(0.0f);
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


    private void getAnimations() {

        fabOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_open);
        fabCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_close);
        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_rotate_clock);
        fabAntiClock = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_rotate_anticlock);

        textOpen = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_open);
        textClose = AnimationUtils.loadAnimation(getContext(), R.anim.floating_button_close);
    }


    private void expandFabMenu() {

        text_Paid.setVisibility(View.VISIBLE);
        text_Receive.setVisibility(View.VISIBLE);
        text_Debt_Crave.setVisibility(View.VISIBLE);
        text_Transfer.setVisibility(View.VISIBLE);


        text_Paid.startAnimation(textOpen);
        text_Receive.startAnimation(textOpen);
        text_Debt_Crave.startAnimation(textOpen);
        text_Transfer.startAnimation(textOpen);

        fab_Paid.startAnimation(fabOpenAnimation);
        fab_Receive.startAnimation(fabOpenAnimation);
        fab_Debt_Crave.startAnimation(fabOpenAnimation);
        fab_Transfer.startAnimation(fabOpenAnimation);
        fab_Main.startAnimation(fabClock);

        fab_Paid.setClickable(true);
        fab_Receive.setClickable(true);
        fab_Debt_Crave.setClickable(true);
        fab_Transfer.setClickable(true);


        isFabMenuOpen = true;
    }


    private void collapseFabMenu() {
        text_Paid.setVisibility(View.INVISIBLE);
        text_Receive.setVisibility(View.INVISIBLE);
        text_Debt_Crave.setVisibility(View.INVISIBLE);
        text_Transfer.setVisibility(View.INVISIBLE);


        text_Paid.startAnimation(textClose);
        text_Receive.startAnimation(textClose);
        text_Debt_Crave.startAnimation(textClose);
        text_Transfer.startAnimation(textClose);

        fab_Paid.startAnimation(fabCloseAnimation);
        fab_Receive.startAnimation(fabCloseAnimation);
        fab_Debt_Crave.startAnimation(fabCloseAnimation);
        fab_Transfer.startAnimation(fabCloseAnimation);
        fab_Main.startAnimation(fabAntiClock);

        fab_Paid.setClickable(false);
        fab_Receive.setClickable(false);
        fab_Debt_Crave.setClickable(false);
        fab_Transfer.setClickable(false);


        isFabMenuOpen = false;

    }


    private void hide_background_fab() {

        int x = background_fab.getRight();
        int y = background_fab.getBottom();

        int startRadius = (int) Math.hypot(background_fab.getWidth(), background_fab.getHeight());
        int endRadius = 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(background_fab, x, y, startRadius, endRadius);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                background_fab.setVisibility(View.INVISIBLE);
                background_fab.setClickable(false);
            }
        });

        anim.start();


    }


    private void show_background_fab() {
        int x = background_fab.getRight();
        int y = background_fab.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(background_fab.getWidth(), background_fab.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(background_fab, x, y, startRadius, endRadius);

        background_fab.setVisibility(View.VISIBLE);
        background_fab.setAlpha(0.9f);
        background_fab.setClickable(true);
        anim.start();

    }


    private void startIntents(Class activity) {
        intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

/*
    @Override
    public void onBackPressed() {
        if (isFabMenuOpen) {
            collapseFabMenu();
        } else {
            super.onBackPressed();
        }
    }*/

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

            case R.id.floating_main:
                if (isFabMenuOpen) {
                    collapseFabMenu();
                    hide_background_fab();
                } else {
                    expandFabMenu();
                    show_background_fab();
                }
                break;
            case R.id.floating_paid:
            case R.id.text_paid:
                Toast.makeText(getContext(), R.string.activity_title_paid, Toast.LENGTH_SHORT).show();
                startIntents(PaidActivity.class);
                collapseFabMenu();
                break;
            case R.id.floating_receive:
            case R.id.text_receive:
                Toast.makeText(getContext(), R.string.activity_title_received, Toast.LENGTH_SHORT).show();
                startIntents(ReceiveActivity.class);
                collapseFabMenu();
                break;
            case R.id.floating_debt_and_crave:
            case R.id.text_debt_and_crave:
                Toast.makeText(getContext(), R.string.activity_title_crDe, Toast.LENGTH_SHORT).show();
                intent = new Intent(getContext(), CraveDebtActivity.class);
                intent.putExtra("Type", "All");
                startActivity(intent);
                collapseFabMenu();
                break;
            case R.id.floating_transfer:
            case R.id.text_transfer:
                TransferMoney transferMoney = new TransferMoney(getContext());
                transferMoney.show();
                collapseFabMenu();
                break;
            case R.id.background_fab:
                collapseFabMenu();
                hide_background_fab();
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
