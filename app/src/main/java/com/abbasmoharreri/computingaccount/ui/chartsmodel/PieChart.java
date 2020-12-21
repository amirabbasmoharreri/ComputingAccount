package com.abbasmoharreri.computingaccount.ui.chartsmodel;

import android.content.Context;
import android.graphics.Typeface;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.AContainer;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChart {


    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = true;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    private int textSize1;
    private int textSize2;

    private int numValues;
    private List<AContainer> values;
    private Context context;

    public PieChart() {

    }

    public PieChart(Context context, PieChartView chart, int numValues, List<AContainer> values) {
        this.context = context;
        this.chart = chart;
        this.numValues = numValues;
        this.values = values;


        //chart.setOnValueTouchListener( new ValueTouchListener() );
        generateData();
        initializeDataForAnimation();

    }


    //resetting all values

    private void reset() {
        chart.setCircleFillRatio( 1.0f );
        hasLabels = true;
        hasLabelsOutside = false;
        hasCenterCircle = false;
        hasCenterText1 = true;
        hasCenterText2 = true;
        isExploded = false;
        hasLabelForSelected = true;
    }


    //generate data for show in chart

    private void generateData() {

        //setting values for showing

        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {


            SliceValue sliceValue = new SliceValue( 30, ChartUtils.nextColor() ).setLabel( this.values.get( i ).getName() );
            values.add( sliceValue );
        }

        //setting data in chart and abilities

        data = new PieChartData( values );
        data.setHasLabels( hasLabels );
        data.setHasLabelsOnlyForSelected( hasLabelForSelected );
        data.setHasLabelsOutside( hasLabelsOutside );
        data.setHasCenterCircle( hasCenterCircle );

        if (isExploded) {
            data.setSlicesSpacing( 24 );
        }


        //setting text for chart

        if (hasCenterText1) {
            data.setCenterText1( context.getString( R.string.Chart_name) );


            // Get roboto-italic font.
            /*Typeface tf = Typeface.createFromAsset( context.getAssets(), "fonts/bmitrabold.ttf" );
            data.setCenterText1Typeface( tf );*/

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize( ChartUtils.px2sp( context.getResources().getDisplayMetrics().scaledDensity,
                    this.textSize1 ) );
        }

        //setting text for chart

        /*if (hasCenterText2) {
            data.setCenterText2( this.values.get( 0 ).getDescription2() );

            Typeface tf = Typeface.createFromAsset( context.getAssets(), "fonts/bmitra.ttf" );
            data.setCenterText2Typeface( tf );

            data.setCenterText2FontSize( ChartUtils.px2sp( context.getResources().getDisplayMetrics().scaledDensity,
                    this.textSize2 ) );
        }*/
        chart.setPieChartData( data );
        prepareDataAnimation();
    }


    private void explodeChart() {
        isExploded = !isExploded;
        generateData();

    }

    //showing labels of chart outside or inside

    private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled( hasLabelForSelected );
        }

        if (hasLabelsOutside) {
            chart.setCircleFillRatio( 0.7f );
        } else {
            chart.setCircleFillRatio( 1.0f );
        }

        generateData();

    }

    //showing labels or hiding

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled( hasLabelForSelected );

            if (hasLabelsOutside) {
                chart.setCircleFillRatio( 0.7f );
            } else {
                chart.setCircleFillRatio( 1.0f );
            }
        }

        generateData();
    }


    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled( hasLabelForSelected );

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart.setCircleFillRatio( 0.7f );
            } else {
                chart.setCircleFillRatio( 1.0f );
            }
        }

        generateData();
    }

    public PieChart setTextSize(int textSize1, int textSize2) {
        this.textSize1 = textSize1;
        this.textSize2 = textSize2;
        generateData();
        return this;
    }


    private void initializeDataForAnimation() {
        chart.cancelDataAnimation();

        int percent = (100 / values.size());
        int i = 0;
        for (SliceValue value : data.getValues()) {
            value.setTarget( percent );
        }

        chart.startDataAnimation();
    }

    //setting animation for chart

    public void prepareDataAnimation() {

        chart.cancelDataAnimation();

        int i = 0;
        for (SliceValue value : data.getValues()) {
            value.setTarget( values.get( i ).getSumPrice() );
            i++;
        }

        chart.startDataAnimation();

    }


    //when selecting  slice of chart , calling this class

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {


        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
