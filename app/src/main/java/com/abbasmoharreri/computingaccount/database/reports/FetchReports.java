package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.text.TextProcessing;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchReports extends DataBaseController {
    private Context context;
    private List<AReport> reports;


    public FetchReports(Context context) {
        super(context);
        this.context = context;
        this.reports = new ArrayList<>();
        fetchList();
    }


    private void fetchList() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_CONDITION + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PRE_REMAINED + " , "
                + DataBase.KEY_PAID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ATTACH_COUNT + " , "
                + DataBase.KEY_NUMBER + " , "
                + DataBase.KEY_RECEIVED
                + " FROM " + DataBase.TABLE_REPORT;
        cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst())
            do {
                AReport aReport = new AReport();
                TextProcessing textProcessing = new TextProcessing();
                Date date = new Date();
                date = textProcessing.convertStringToDate(cursor.getString(2));
                Log.e("ABBAS", date.toString());
                aReport.setId(cursor.getInt(0))
                        .setCondition(cursor.getInt(1) > 0)
                        .setGregorianDate(date)
                        .setPreRemained(cursor.getInt(3))
                        .setPaid(cursor.getInt(4))
                        .setRemained(cursor.getInt(5))
                        .setAttachCount(cursor.getInt(6))
                        .setNumber(cursor.getInt(7))
                        .setSumReceived(cursor.getInt(8));

                this.reports.add(aReport);

            } while (cursor.moveToNext());

    }


    public List<AReport> getList() {
        return reports;
    }
}
