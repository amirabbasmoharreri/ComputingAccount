package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.AWorkName;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class for fetch all Works name and ID's of them and conditions
 * @version 1.0
 * */
public class FetchWorkNames extends DataBaseController {
    private Context context;
    private List<AWorkName> workNames;

    public FetchWorkNames(Context context) {
        super( context );
        this.context = context;
        this.workNames = new ArrayList<>();
        fetchPersonNames();
    }


    /**
     * This method for fetch all data of works name from DB
     * and adding these items to List<AWorkName>
     * */
    private void fetchPersonNames() {



        String query = "SELECT "
                + DataBase.KEY_ID + ","
                + DataBase.KEY_NAME + ","
                + DataBase.KEY_CONDITION
                + " FROM " + DataBase.TABLE_WORK_NAME;
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            do {

                AWorkName aWorkName = new AWorkName();
                aWorkName.setId( cursor.getInt( 0 ) )
                        .setName( cursor.getString( 1 ) )
                        .setCondition( cursor.getInt( 2 ) > 0 );

                this.workNames.add( aWorkName );

            } while (cursor.moveToNext());
    }


    /**
     * This method for getting list of Works name
     * @return workNames (Type = List<AWorkName>)
     * */
    public List<AWorkName> getList() {
        return workNames;
    }

    /**
     * This method for getting List of Items by LisT<AItem> type
     * this method for using in ItemActivity
     * @return aItem (Type = List<AItem>)
     * */
    public List<AItem> getListItem() {
        List<AItem> aItems = new ArrayList<>();


        for (AWorkName workName : workNames) {

            AItem aItem = new AItem();
            aItem.setName( workName.getName() )
                    .setId( workName.getId() )
                    .setClassName( AItem.WORK_NAME )
                    .setCondition( workName.getCondition() );
            aItems.add( aItem );

        }

        return aItems;
    }
}
