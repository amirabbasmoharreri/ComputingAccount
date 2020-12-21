package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchReportNumbers extends DataBaseController {

    private Context context;
    private List<Integer> numbers;


    public FetchReportNumbers(Context context) {
        super( context );
        this.context = context;
        this.numbers = new ArrayList<>();
        fetchNumbers();
    }


    private void fetchNumbers() {

        String query = "SELECT "
                + DataBase.KEY_NUMBER
                + " FROM " + DataBase.TABLE_REPORT;
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            do {

                this.numbers.add( cursor.getInt( 0 ) );

            } while (cursor.moveToNext());

    }


    public List<Integer> getList() {
        return numbers;
    }
}
