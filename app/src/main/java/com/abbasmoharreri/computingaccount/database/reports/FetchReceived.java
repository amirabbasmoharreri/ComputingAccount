package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchReceived extends DataBaseController {

    private Context context;
    private List<AMoneyReceive> aMoneyReceives;

    public FetchReceived(Context context) {
        super( context );
        this.aMoneyReceives = new ArrayList<>();
        this.context = context;
        fetchList();
    }

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

    private void fetch_name_number() {

        int reportNumber = 0;
        for (AMoneyReceive aMoneyReceive : aMoneyReceives) {


            reportNumber = getNumber_report( aMoneyReceive.getReportId() );

            aMoneyReceive.setReportNumber( reportNumber )
                    .setAttaches( fetchPictureReceived( aMoneyReceive ) );
        }
    }


    public List<AMoneyReceive> getList() {
        return aMoneyReceives;
    }
}
