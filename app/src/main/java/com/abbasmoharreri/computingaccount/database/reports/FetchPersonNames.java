package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.APerson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchPersonNames extends DataBaseController {

    private Context context;
    private List<APerson> personNames;

    public FetchPersonNames(Context context) {
        super( context );
        this.context = context;
        this.personNames = new ArrayList<>();
        fetchPersonNames();
    }


    private void fetchPersonNames() {



        String query = "SELECT "
                + DataBase.KEY_ID + ","
                + DataBase.KEY_NAME
                + " FROM " + DataBase.TABLE_PERSON;
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            do {

                APerson aPerson = new APerson();
                aPerson.setId( cursor.getInt( 0 ) )
                        .setName( cursor.getString( 1 ) );

                this.personNames.add( aPerson );

            } while (cursor.moveToNext());
    }


    public List<APerson> getList() {
        return personNames;
    }

    public List<AItem> getListItem() {
        List<AItem> aItems = new ArrayList<>();


        for (int i = 0; i < personNames.size(); i++) {

            AItem aItem = new AItem();
            aItem.setName( personNames.get( i ).getName() )
                    .setId( personNames.get( i ).getId() )
                    .setClassName( AItem.PERSON );
            aItems.add( aItem );

        }

        return aItems;
    }
}
