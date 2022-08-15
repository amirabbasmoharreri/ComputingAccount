package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This Class for take data from DB about Crave and Debt
 * @version 1.0
 * */

public class FetchCraveDebts extends DataBaseController {


    private List<ACraveAndDebt> aCraveAndDebts;
    private List<ACraveAndDebt> aCraves;
    private List<ACraveAndDebt> aDebts;

    public FetchCraveDebts(Context context) {
        super( context );
        aCraveAndDebts = new ArrayList<>();
        aCraves = new ArrayList<>();
        aDebts = new ArrayList<>();
        fetchList();
    }


    /**
     * This method for take all data from DB about Crave and Debt and create List<ACraveAndDebt>
     * and take name of persons and set to objects in List
     * */
    private void fetchList() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_PERSON_ID + " , "
                + DataBase.KEY_NUMBER + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PRICE + " , "
                + DataBase.KEY_COMMENT + " , "
                + DataBase.KEY_TYPE
                + " FROM " + DataBase.TABLE_CRAVE_DEBT;
        cursor = db.rawQuery( query, null );


        if (cursor.moveToFirst())
            do {
                ACraveAndDebt aCraveAndDebt = new ACraveAndDebt();


                TextProcessing textProcessing = new TextProcessing();
                Date date = new Date();
                date = textProcessing.convertStringToDate( cursor.getString( 3 ) );
                Log.e( "ABBAS", date.toString() );

                aCraveAndDebt.setId( cursor.getInt( 0 ) )
                        .setPersonId( cursor.getInt( 1 ) )
                        .setNumber( cursor.getInt( 2 ) )
                        .setGregorianDate( date )
                        .setPrice( cursor.getInt( 4 ) )
                        .setComment( cursor.getString( 5 ) )
                        .setType( cursor.getString( 6 ) );

                this.aCraveAndDebts.add( aCraveAndDebt );

            } while (cursor.moveToNext());


        fetch_name_number();


        for (ACraveAndDebt aCraveAndDebt : aCraveAndDebts) {
            if (aCraveAndDebt.getType().equals( ACraveAndDebt.DEBT )) {
                aDebts.add( aCraveAndDebt );
            }
        }


        for (ACraveAndDebt aCraveAndDebt : aCraveAndDebts) {
            if (aCraveAndDebt.getType().equals( ACraveAndDebt.CRAVE )) {
                aCraves.add( aCraveAndDebt );
            }
        }


    }


    /**
     * This method for take name of persons from DB with Person-ID
     * */
    private void fetch_name_number() {
        String personName = "";

        for (ACraveAndDebt aCraveAndDebt : aCraveAndDebts) {

            personName = getName_person( aCraveAndDebt.getPersonId() );
            aCraveAndDebt.setPersonName( personName );
        }

    }


    /**
     * This method to return the List of Crave And Debt both of them together
     * @return List<ACraveAndDebt>
     * */
    public List<ACraveAndDebt> getList() {
        return aCraveAndDebts;
    }

    /**
     * This method returns the List of Crave separately
     * @return List<ACraveAndDebt>
     * */
    public List<ACraveAndDebt> getCraveList() {
        return aCraves;
    }

    /**
     * This method returns the List of Debt separately
     * @return List<ACraveAndDebt>
     * */
    public List<ACraveAndDebt> getDebtList() {
        return aDebts;
    }

}
