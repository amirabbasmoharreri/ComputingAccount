package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AContainer;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchSumPriceWNameWMaxReport extends DataBaseController {

    private List<AWork> aWorks;
    private List<Integer> ids;
    private List<AContainer> container;
    private Context context;

    public FetchSumPriceWNameWMaxReport(Context context) {
        super( context );
        this.context = context;
        this.aWorks = new ArrayList<>();
        this.ids = new ArrayList<>();
        this.container = new ArrayList<>();
        fetchWorks();
        sumPrices();
    }


    private void fetchWorks() {
        int reportId = get_Id_Report( max_reportNumber() );


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
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + reportId;

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


                setListIDS( aWork.getWorkNameId() );

                this.aWorks.add( aWork );

            } while (cursor.moveToNext());

        fetch_name_number();
    }

    private void fetch_name_number() {

        String personName = "";
        int reportNumber = 0;
        String workName = "";
        byte[] picture = null;
        for (AWork aWork : aWorks) {

            personName = getName_person( aWork.getPersonId() );
            reportNumber = getNumber_report( aWork.getReportId() );
            workName = getName_workName( aWork.getWorkNameId() );

            aWork.setPersonName( personName )
                    .setName( workName )
                    .setReportNumber( reportNumber )
                    .setAttaches( fetchPictureWorkList( aWork ) );
        }
    }


    private void setListIDS(int id) {

        if (!ids.contains( id )) {
            ids.add( id );
        }

    }


    private void sumPrices() {

        List<Integer> workIds;


        for (int i = 0; i < ids.size(); i++) {

            workIds = new ArrayList<>();
            int sum = 0;
            for (int j = 0; j < aWorks.size(); j++) {

                if (ids.get( i ) == aWorks.get( j ).getWorkNameId()) {
                    if (!workIds.contains( aWorks.get( j ).getId() )) {
                        sum += aWorks.get( j ).getPrice();
                        workIds.add( aWorks.get( j ).getId() );
                    }

                }
            }

            AContainer container1 = new AContainer();
            container1.setWorkId( workIds );
            container1.setName( getName_workName( ids.get( i ) ) );
            container1.setSumPrice( sum );
            container.add( container1 );
        }

    }

    public int sumAllPrices() {
        int sum = 0;

        for (int i = 0; i < container.size(); i++) {
            sum += container.get( i ).getSumPrice();
        }
        return sum;
    }


    public List<AWork> getWorkWName(String name) {
        List<AWork> works = new ArrayList<>();
        for (AContainer c : container) {
            if (c.getName().equals( name )) {
                works = getAWorks( c.getWorkId() );
            }
        }

        return works;
    }

    public int getSumPriceWithName(String name) {
        int sum = 0;
        for (AContainer c : container) {
            if (c.getName().equals( name )) {
                for (int i = 0; i < getAWorks( c.getWorkId() ).size(); i++) {
                    sum += getAWorks( c.getWorkId() ).get( i ).getPrice();
                }

            }
        }

        return sum;
    }


    private List<AWork> getAWorks(List<Integer> id) {
        List<AWork> list = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {

            for (int j = 0; j < aWorks.size(); j++) {

                if (aWorks.get( j ).getId() == id.get( i )) {
                    list.add( aWorks.get( j ) );
                }
            }
        }

        return list;
    }


    public List<AContainer> getContainer() {
        return container;
    }
}
