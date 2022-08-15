package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;

/**
 * This Class for getting data from DB for Showing to HomeFragment
 * @version 1.0
 * */

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


    /**
     * This method returns the remaining fund of The Report
     * @return remainedReport (Type = int)
     * */
    public int getRemainedReport() {
        return remainedReport;
    }

    /**
     * This method returns the sum Received of The Report
     * @return sumReceivedReport (Type = int)
     * */
    public int getSumReceivedReport() {
        return sumReceivedReport;
    }

    /**
     * This method returns the sum Crave
     * @return sumCrave (Type = int)
     * */
    public int getSumCrave() {
        return sumCrave;
    }

    /**
     * This method returns the sum Debt
     * @return sumDebt (Type = int)
     * */
    public int getSumDebt() {
        return sumDebt;
    }

    /**
     * This method returns the last number of Report
     * @return max_reportNumber (Type = int)
     * */
    public int getReportNumber() {
        return max_reportNumber();
    }

    /**
     * This method computing the remaining fund of The Report
     * */
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

    /**
     * This method computing the sum Received of The Report
     * */
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

    /**
     * This method of computing the sum Crave
     * */
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

    /**
     * This method of computing the sum Debt
     * */
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
