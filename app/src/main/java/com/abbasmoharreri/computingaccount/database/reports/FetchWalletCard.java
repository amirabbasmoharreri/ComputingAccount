package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;

import java.util.ArrayList;
import java.util.List;


/**
 * This Class for fetch all data of Wallets and Cards
 * @version 1.0
 * */
public class FetchWalletCard extends DataBaseController {

    private Context context;
    private List<AAccount> aAccounts;
    private String databaseName;

    /**
     * This Constructor for Using This class for Wallets or Cards separately
     * for fetch data of one of Wallets and Cards
     * @param context (Type = Context) Context of app
     * @param databaseName (Type = String) data base name for using Wallets or Cards
     *                     for example:
     *                     databaseName = AAccount.ACCOUNT_CARD or AAccount.ACCOUNT_WALLET
     * */
    public FetchWalletCard(Context context, String databaseName) {
        super( context );
        this.context = context;
        this.aAccounts = new ArrayList<>();
        this.databaseName = databaseName;
        fetchList();
    }

    /**
     * This Constructor for Using This class for Wallets and Cards both of them together
     * for fetch all data of the Wallets and The Cards
     * @param context (Type = Context) Context of app
     * */
    public FetchWalletCard(Context context) {
        super( context );
        this.context = context;
        this.aAccounts = new ArrayList<>();
        fetch_all_Wallet_card();
    }


    /**
     * This method fetch Data from DB with databaseName and create List<AAccount> and adding these items into the List
     * */
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

    /**
     * This method for fetch all data of Wallets and Cards both of them together
     * and create List<AAccount> and adding these items into the List
     * */
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


    /**
     * This method returns List of the Wallets and the Cards
     * @return aAccounts (Type = List<AAccount>)
     *
     * for example:
     * if using FetchWalletCard(context, databaseName)
     *            databaseName = AAccount.ACCOUNT_WALLET --> returns list of Wallets
     *            databaseName = AAccount.ACCOUNT_CARD --> returns list of Cards
     *
     * if using FetchWalletCard(context)
     *            returns List of Wallets and Cards both of them together
     * */
    public List<AAccount> getList() {
        return aAccounts;
    }


    /**
     * This method return list of name of wallets or cards
     * @return names (Type = List<String>)
     * */
    public List<String> getListName() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < aAccounts.size(); i++) {
            names.add( String.valueOf( aAccounts.get( i ).getAccountNumber() ) );
        }

        return names;
    }
}
