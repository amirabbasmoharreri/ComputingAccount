package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This Class for fetch data of The Received price
 * @version 1.0
 * */

public class FetchReceived extends DataBaseController {

    private Context context;
    private List<AMoneyReceive> aMoneyReceives;

    public FetchReceived(Context context) {
        super( context );
        this.aMoneyReceives = new ArrayList<>();
        this.context = context;
        fetchList();
    }


    /**
     * This method for fetch all data about the Received price and create List<AMoneyReceived>
     * and fetch report number and number of received and completing list
     * */
    private void fetchList() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PRICE + " , "
                + DataBase.KEY_NUMBER + " , "
                + DataBase.KEY_REPORT_ID
                + " FROM " + DataBase.TABLE_RECEIVED;
        cursor = db.rawQuery( query, null );


        if (cursor.moveToFirst())
            do {
                AMoneyReceive aMoneyReceived = new AMoneyReceive();


                TextProcessing textProcessing = new TextProcessing();
                Date date = new Date();
                date = textProcessing.convertStringToDate( cursor.getString( 1 ) );
                Log.e( "ABBAS", date.toString() );

                aMoneyReceived.setId( cursor.getInt( 0 ) )
                        .setGregorianDate( date )
                        .setPrice( cursor.getInt( 2 ) )
                        .setNumber( cursor.getInt( 3 ) )
                        .setReportId( cursor.getInt( 4 ) );

                this.aMoneyReceives.add( aMoneyReceived );

            } while (cursor.moveToNext());

        fetch_name_number();

    }


    /**
     * This method for getting report number and picture linked to the received
     * */
    private void fetch_name_number() {

        int reportNumber = 0;
        for (AMoneyReceive aMoneyReceive : aMoneyReceives) {


            reportNumber = getNumber_report( aMoneyReceive.getReportId() );

            aMoneyReceive.setReportNumber( reportNumber )
                    .setAttaches( fetchPictureReceived( aMoneyReceive ) );
        }
    }


    /**
     * This method returns List of the Received price
     * @return aMoneyReceived (Type = List<AMoneyReceived>)
     * */
    public List<AMoneyReceive> getList() {
        return aMoneyReceives;
    }
}
