package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This Class for fetching works with Person name and period of time
 * @version 1.0
 * */
public class ReportWPersonATime extends DataBaseController {

    private Context context;
    private List<AWork> aWorks;


    public ReportWPersonATime(Context context, String person, Date startDate, Date endDate) {
        super( context );
        this.aWorks = new ArrayList<>();
        fetchWPersonATime( person, startDate, endDate );
    }



    /**
     * This method for fetching works with person name and period of time from DB and adding to aWorks (Type = List<AWork>)
     * @param person (Type  = String)
     * @param startDate (Type = Date)
     * @param endDate (Type = Date)
     * */
    private void fetchWPersonATime(String person, Date startDate, Date endDate) {

        int personId = get_Id_Person( person );

        TextProcessing textProcessing1 = new TextProcessing();
        String startDateString = textProcessing1.convertDateToString( startDate );
        String endDateString = textProcessing1.convertDateToString( endDate );


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
                + " WHERE " + DataBase.KEY_PERSON_ID + " = " + personId
                + " AND (" + DataBase.KEY_DATE + " >= '" + startDateString + "' AND " + DataBase.KEY_DATE + " <= '" + endDateString + "')";
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

        fetch_name_number( person );
    }


    /**
     * This method for completing data of the list like Work name and Report number and adding Attaches
     * @param person (Type = String)
     * */
    private void fetch_name_number(String person) {

        String workName = "";
        int reportNumber = 0;
        byte[] picture = null;
        for (AWork aWork : aWorks) {

            workName = getName_workName( aWork.getWorkNameId() );
            reportNumber = getNumber_report( aWork.getReportId() );


            aWork.setPersonName( person )
                    .setName( workName )
                    .setReportNumber( reportNumber )
                    .setAttaches( fetchPictureWorkList( aWork ) );
        }
    }


    /**
     * This method returns list of the Works
     * @return aWorks (Type = List<AWork>)
     * */
    public List<AWork> getList() {
        return this.aWorks;
    }
}
