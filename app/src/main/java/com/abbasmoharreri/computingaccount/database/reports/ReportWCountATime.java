package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportWCountATime extends DataBaseController {

    private Context context;
    private List<AWork> aWorks;
    private HashMap<String, Integer> aWorkCount;


    public ReportWCountATime(Context context, Date startDate, Date endDate) {
        super( context );
        this.aWorks = new ArrayList<>();
        this.aWorkCount = new HashMap<>();
        fetchWTime( startDate, endDate );
    }


    private void fetchWTime(Date startDate, Date endDate) {


        String query = "SELECT "
                + DataBase.KEY_ID + ","
                + DataBase.KEY_REPORT_ID + ","
                + DataBase.KEY_WORK_NAME_ID + ","
                + DataBase.KEY_NUMBER + ","
                + DataBase.KEY_SUB_NUMBER + " , "
                + DataBase.KEY_PRICE + " , "
                + DataBase.KEY_ATTACH_COUNT + " , "
                + DataBase.KEY_COMMENT + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PERSON_ID + " , "
                + DataBase.KEY_ACCOUNT + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + DataBase.TABLE_WORK_LIST
                + " WHERE " + DataBase.KEY_DATE + " >= " + startDate + " AND " + DataBase.KEY_DATE + " <= " + endDate;
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            do {

                AWork aWork = new AWork();

                TextProcessing textProcessing = new TextProcessing();
                Date date = new Date();
                date = textProcessing.convertStringToDate( cursor.getString( 8 ) );
                Log.e( "ABBAS", date.toString() );

                aWork.setId( cursor.getInt( 0 ) )
                        .setReportId( cursor.getInt( 1 ) )
                        .setWorkNameId( cursor.getInt( 2 ) )
                        .setNumber( cursor.getInt( 3 ) )
                        .setSubNumber( cursor.getInt( 4 ) )
                        .setPrice( cursor.getInt( 5 ) )
                        .setAttachCount( cursor.getInt( 6 ) )
                        .setComment( cursor.getString( 7 ) )
                        .setGregorianDate( date )
                        .setPersonId( cursor.getInt( 9 ) )
                        .setAccountName( cursor.getString( 10 ) )
                        .setAccountNumber( cursor.getString( 11 ) );

                this.aWorks.add( aWork );

            } while (cursor.moveToNext());

        fetch_name_number();
    }


    private void fetch_name_number() {

        String personName = "";
        String workName = "";
        int reportNumber = 0;
        for (AWork aWork : aWorks) {

            personName = getName_person( aWork.getPersonId() );
            workName = getName_workName( aWork.getWorkNameId() );
            reportNumber = getNumber_report( aWork.getReportId() );

            aWork.setPersonName( personName )
                    .setName( workName )
                    .setReportNumber( reportNumber )
                    .setAttaches( fetchPictureWorkList( aWork ) );
        }

        countingWorks();
    }


    private void countingWorks() {

        String workName = "";
        int count = 0;
        for (AWork aWork : aWorks) {

            workName = aWork.getName();

            count = 0;

            for (AWork aWork1 : aWorks) {
                if (aWork1.getName().equals( workName )) {
                    count++;
                }
            }

            aWorkCount.put( workName, count );

        }
    }


    public HashMap<String, Integer> getCountList() {
        return this.aWorkCount;
    }

    public List<AWork> getList() {
        return this.aWorks;
    }
}
