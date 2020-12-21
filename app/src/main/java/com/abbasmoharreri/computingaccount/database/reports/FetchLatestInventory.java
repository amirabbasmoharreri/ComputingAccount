package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;

public class FetchLatestInventory extends DataBaseController {

    private int remainedReport;
    private int sumReceivedReport;
    private int sumCrave;
    private int sumDebt;

    public FetchLatestInventory(Context context) {
        super( context );

        fetchRemainedReport();
        fetchReceivedReport();
        fetchSumCrave();
        fetchSumDebt();

        remainedReport -= sumCrave;
    }


    public int getRemainedReport() {
        return remainedReport;
    }

    public int getSumReceivedReport() {
        return sumReceivedReport;
    }

    public int getSumCrave() {
        return sumCrave;
    }

    public int getSumDebt() {
        return sumDebt;
    }


    public int getReportNumber() {
        return max_reportNumber();
    }


    private void fetchRemainedReport() {

        int remained = 0;

        String query = "SELECT "
                + DataBase.KEY_REMAINED
                + " FROM " + DataBase.TABLE_REPORT
                + " WHERE " + DataBase.KEY_NUMBER
                + "=" + max_reportNumber();

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            remained = cursor.getInt( 0 );

        remainedReport = remained;

    }


    private void fetchReceivedReport() {

        try {
            int received = 0;

            String query = "SELECT SUM("
                    + DataBase.KEY_PRICE
                    + ") FROM " + DataBase.TABLE_RECEIVED
                    + " WHERE " + DataBase.KEY_REPORT_ID
                    + "=" + get_Id_Report( max_reportNumber() );

            cursor = db.rawQuery( query, null );

            if (cursor.moveToFirst())
                received = cursor.getInt( 0 );

            sumReceivedReport = received;
        } catch (Exception e) {
            e.printStackTrace();
            sumReceivedReport = 0;
        }


    }


    private void fetchSumCrave() {

        try {
            int crave = 0;

            String query = "SELECT SUM("
                    + DataBase.KEY_PRICE
                    + ") FROM " + DataBase.TABLE_CRAVE_DEBT
                    + " WHERE " + DataBase.KEY_TYPE
                    + " = '" + ACraveAndDebt.CRAVE + "'";

            cursor = db.rawQuery( query, null );

            if (cursor.moveToFirst())
                crave = cursor.getInt( 0 );

            sumCrave = crave;
        } catch (Exception e) {
            e.printStackTrace();
            sumCrave = 0;
        }


    }

    private void fetchSumDebt() {

        try {

            int debt = 0;

            String query = "SELECT SUM("
                    + DataBase.KEY_PRICE
                    + ") FROM " + DataBase.TABLE_CRAVE_DEBT
                    + " WHERE " + DataBase.KEY_TYPE
                    + " = '" + ACraveAndDebt.DEBT + "'";

            cursor = db.rawQuery( query, null );

            if (cursor.moveToFirst())
                debt = cursor.getInt( 0 );

            sumDebt = debt;

        } catch (Exception e) {
            e.printStackTrace();
            sumDebt = 0;
        }


    }


}
