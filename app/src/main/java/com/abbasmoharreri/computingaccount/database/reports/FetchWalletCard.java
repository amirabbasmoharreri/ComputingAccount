package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchWalletCard extends DataBaseController {

    private Context context;
    private List<AAccount> aAccounts;
    private String databaseName;

    public FetchWalletCard(Context context, String databaseName) {
        super( context );
        this.context = context;
        this.aAccounts = new ArrayList<>();
        this.databaseName = databaseName;
        fetchList();
    }

    public FetchWalletCard(Context context) {
        super( context );
        this.context = context;
        this.aAccounts = new ArrayList<>();
        fetch_all_Wallet_card();
    }

    private void fetchList() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + databaseName;
        cursor = db.rawQuery( query, null );


        if (cursor.moveToFirst())
            do {
                AAccount aAccount = new AAccount();

                aAccount.setId( cursor.getInt( 0 ) )
                        .setRemained( cursor.getInt( 1 ) )
                        .setAccountNumber( cursor.getString( 2 ) )
                        .setName( databaseName );

                this.aAccounts.add( aAccount );

            } while (cursor.moveToNext());

    }

    private void fetch_all_Wallet_card() {

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + DataBase.TABLE_CARD;
        cursor = db.rawQuery( query, null );


        if (cursor.moveToFirst())
            do {
                AAccount aAccount = new AAccount();

                aAccount.setId( cursor.getInt( 0 ) )
                        .setRemained( cursor.getInt( 1 ) )
                        .setAccountNumber( cursor.getString( 2 ) )
                        .setName( DataBase.TABLE_CARD );

                this.aAccounts.add( aAccount );

            } while (cursor.moveToNext());


        String query2 = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + DataBase.TABLE_WALLET;
        cursor = db.rawQuery( query2, null );


        if (cursor.moveToFirst())
            do {
                AAccount aAccount = new AAccount();

                aAccount.setId( cursor.getInt( 0 ) )
                        .setRemained( cursor.getInt( 1 ) )
                        .setAccountNumber( cursor.getString( 2 ) )
                        .setName( DataBase.TABLE_WALLET );

                this.aAccounts.add( aAccount );

            } while (cursor.moveToNext());
    }


    public List<AAccount> getList() {
        return aAccounts;
    }

    public List<String> getListName() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < aAccounts.size(); i++) {
            names.add( String.valueOf( aAccounts.get( i ).getAccountNumber() ) );
        }

        return names;
    }
}
