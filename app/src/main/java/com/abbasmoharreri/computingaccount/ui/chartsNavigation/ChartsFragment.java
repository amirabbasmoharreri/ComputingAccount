package com.abbasmoharreri.computingaccount.ui.chartsNavigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.reports.FetchSumPriceWNameWMaxReport;
import com.abbasmoharreri.computingaccount.database.reports.ReportWNameWMaxReport;
import com.abbasmoharreri.computingaccount.module.AContainer;
import com.abbasmoharreri.computingaccount.ui.adapters.WorkAdapter;
import com.abbasmoharreri.computingaccount.ui.chartsmodel.PieChart;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.listener.DummyPieChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartsFragment extends Fragment {

    private PieChartView pieChartView;
    private PieChart pieChart;
    private TextView price, workName, priceSpecialWork;
    private RecyclerView recyclerView;
    private FetchSumPriceWNameWMaxReport fetchSumPriceWNameWMaxReport;
    private Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate( R.layout.fragment_charts, container, false );

        pieChartView = root.findViewById( R.id.chart_pie_chart );
        pieChartView.setOnValueTouchListener( new ValueSelectPieChart() );
        price = root.findViewById( R.id.text_price_charts );
        workName = root.findViewById( R.id.text_work_name_charts );
        priceSpecialWork = root.findViewById( R.id.text_price_special_work_charts );
        recyclerView = root.findViewById( R.id.recycle_view_charts );
        pieChart = new PieChart();

        return root;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onResume() {
        super.onResume();

        try {
            fetchSumPriceWNameWMaxReport = new FetchSumPriceWNameWMaxReport( getContext() );
            price.setText( String.format( "%,d", fetchSumPriceWNameWMaxReport.sumAllPrices() ) );
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                setPieChartViewData();
            }
        }, 100 );


        try {
            setRecyclerView( fetchSumPriceWNameWMaxReport.getContainer().get( 0 ).getName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setRecyclerView(String name) {

        try {

            priceSpecialWork.setText( String.format( "%,d", fetchSumPriceWNameWMaxReport.getSumPriceWithName( name ) ) );
            workName.setText( ": " + name );
            WorkAdapter workAdapter = new WorkAdapter( getContext(), fetchSumPriceWNameWMaxReport.getWorkWName( name ) );
            recyclerView.setAdapter( workAdapter );
            recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void setPieChartViewData() {


        try {

            pieChart = new PieChart( getContext(), pieChartView, fetchSumPriceWNameWMaxReport.getContainer().size(), fetchSumPriceWNameWMaxReport.getContainer() );
            pieChart.setTextSize( (int) getResources().getDimension( R.dimen.pie_chart_text1_size ), (int) getResources().getDimension( R.dimen.pie_chart_text2_size ) );

        } catch (Exception e) {
            e.printStackTrace();
        }


        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                prepareAnimation();
            }
        }, 15000 );

    }


    private void prepareAnimation() {
        try {
            pieChart.prepareDataAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class ValueSelectPieChart implements PieChartOnValueSelectListener {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {


            List<AContainer> aContainer = fetchSumPriceWNameWMaxReport.getContainer();

            setRecyclerView( aContainer.get( arcIndex ).getName() );
            Toast.makeText( getActivity(), aContainer.get( arcIndex ).getName(), Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
