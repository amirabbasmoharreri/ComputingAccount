package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.APerson;
import com.abbasmoharreri.computingaccount.module.AWorkName;

import java.util.ArrayList;
import java.util.List;

public class FetchWorkNames extends DataBaseController {
    private Context context;
    private List<AWorkName> workNames;

    public FetchWorkNames(Context context) {
        super( context );
        this.context = context;
        this.workNames = new ArrayList<>();
        fetchPersonNames();
    }


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


    public List<AWorkName> getList() {
        return workNames;
    }

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
