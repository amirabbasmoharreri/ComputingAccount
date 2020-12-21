package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AReport;

import java.util.Calendar;
import java.util.Date;

public class CLoseAccount extends DataBaseController {


    private AReport aReportPrevious;
    private AReport aReportNew;

    public CLoseAccount(Context context, AReport aReport) {
        super(context);
        this.aReportPrevious = aReport;
        this.aReportNew = new AReport();
    }


    public void commit() {
        //fetchPreReport();
        updatePreviousReport();
        initializeNewReport();
        updateDB();
    }


    private void fetchPreReport() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_NUMBER + " , "
                + DataBase.KEY_CONDITION + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PRE_REMAINED + " , "
                + DataBase.KEY_PAID + " , "
                + DataBase.KEY_RECEIVED + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ATTACH_COUNT
                + " FROM " + DataBase.TABLE_REPORT
                + " WHERE " + DataBase.KEY_ID + " = " + get_Id_Report(max_reportNumber());
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())

            do {

                this.aReportPrevious.setId(cursor.getInt(0))
                        .setNumber(cursor.getInt(1))
                        .setCondition(cursor.getInt(2) > 0)
                        .setGregorianDate(new Date(cursor.getLong(3)))
                        .setPreRemained(cursor.getInt(4))
                        .setPaid(cursor.getInt(5))
                        .setSumReceived(cursor.getInt(6))
                        .setRemained(cursor.getInt(7))
                        .setAttachCount(cursor.getInt(8));

            } while (cursor.moveToNext());
    }


    public boolean checkCondition() {
        if (this.aReportPrevious.getCondition()) {
            return true;
        } else {
            return false;
        }
    }


    private void updatePreviousReport() {
        Calendar calendar = Calendar.getInstance();
        this.aReportPrevious.setGregorianDate(calendar.getTime())
                .setCondition(AReport.isReported);
    }


    private void initializeNewReport() {
        this.aReportNew.setNumber(this.aReportPrevious.getNumber() + 1)
                .setPreRemained(this.aReportPrevious.getRemained())
                .setRemained(this.aReportPrevious.getRemained())
                .setSumReceived(0)
                .setPaid(0)
                .setAttachCount(0)
                .setCondition(AReport.isNotReported)
                .setGregorianDate(this.aReportPrevious.getGregorianDate());
    }


    private void updateDB() {
        updateReport(this.aReportPrevious);
        insertReport(this.aReportNew);
    }


}
