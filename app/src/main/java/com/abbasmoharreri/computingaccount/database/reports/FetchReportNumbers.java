package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class for fetch Report numbers from DB
 * @version 1.0
 * */

public class FetchReportNumbers extends DataBaseController {

    private Context context;
    private List<Integer> numbers;


    public FetchReportNumbers(Context context) {
        super( context );
        this.context = context;
        this.numbers = new ArrayList<>();
        fetchNumbers();
    }


    /**
     * this method for fetch all Report numbers from DB
     * and add items to List<Integer>
     * */
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


    /**
     * This method returns List of the report numbers
     * @return numbers (Type = List<Integer>)
     * */
    public List<Integer> getList() {
        return numbers;
    }
}
