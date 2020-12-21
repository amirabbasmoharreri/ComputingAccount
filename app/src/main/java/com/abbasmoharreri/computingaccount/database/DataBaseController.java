package com.abbasmoharreri.computingaccount.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseController {

    protected Context context;
    protected SQLiteDatabase db;
    protected Cursor cursor;


    //region opening DataBase

    public DataBaseController(Context context) {
        this.db = new DataBase( context ).getWritableDatabase();
    }

    //endregion


    //region closing DateBase

    public void close() {
        db.close();
        cursor.close();
    }

    //endregion


    //region getting Notes

    public List<String> fetchNotes() {

        List<String> stringList = new ArrayList<>();

        String query = "SELECT "
                + DataBase.KEY_COMMENT +
                " FROM " + DataBase.TABLE_NOTE;
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())

            do {
                String st = cursor.getString( 0 );

                stringList.add( st );

            } while (cursor.moveToNext());

        return stringList;
    }


    //endregion

    //region getting Lists

    protected ArrayList<APicture> fetchPictureWorkList(AWork aWork) {

        ArrayList<APicture> aPictures = new ArrayList<>();

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REPORT_ID + " , "
                + DataBase.KEY_NUMBER + " , "
                + DataBase.KEY_SUB_NUMBER + " , "
                + DataBase.KEY_IMAGE
                + " FROM " + DataBase.TABLE_PICTURE
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + aWork.getReportId() + " AND "
                + DataBase.KEY_NUMBER + " = " + aWork.getNumber() + " AND "
                + DataBase.KEY_SUB_NUMBER + " = " + aWork.getSubNumber();
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())

            do {
                APicture aPicture = new APicture();

                aPicture.setId( cursor.getInt( 0 ) )
                        .setReportId( cursor.getInt( 1 ) )
                        .setNumber( cursor.getInt( 2 ) )
                        .setSubNumber( cursor.getInt( 3 ) )
                        .setPicture( cursor.getBlob( 4 ) );

                aPictures.add( aPicture );

            } while (cursor.moveToNext());

        return aPictures;
    }


    protected ArrayList<APicture> fetchPictureReceived(AMoneyReceive aMoneyReceive) {

        ArrayList<APicture> aPictures = new ArrayList<>();

        String query = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REPORT_ID + " , "
                + DataBase.KEY_RECEIVED_NUMBER + " , "
                + DataBase.KEY_IMAGE
                + " FROM " + DataBase.TABLE_PICTURE
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + aMoneyReceive.getReportId() + " AND "
                + DataBase.KEY_RECEIVED_NUMBER + " = " + aMoneyReceive.getNumber();
        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())

            do {
                APicture aPicture = new APicture();

                aPicture.setId( cursor.getInt( 0 ) )
                        .setReportId( cursor.getInt( 1 ) )
                        .setReceivedNumber( cursor.getInt( 2 ) )
                        .setPicture( cursor.getBlob( 3 ) );

                aPictures.add( aPicture );

            } while (cursor.moveToNext());

        return aPictures;
    }


    //endregion


    //region getting MAX

    public int max_workNumber() {

        int number = 0;

        String query = "SELECT MAX("
                + DataBase.KEY_NUMBER
                + ") FROM " + DataBase.TABLE_WORK_LIST
                + " WHERE " + DataBase.KEY_REPORT_ID
                + "=(SELECT MAX(" + DataBase.KEY_ID + ") FROM " + DataBase.TABLE_REPORT + ")";

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            number = cursor.getInt( 0 );

        return number;
    }

    public int max_reportNumber() {

        int number = 0;

        String query = "SELECT MAX("
                + DataBase.KEY_NUMBER
                + ") FROM " + DataBase.TABLE_REPORT;

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            number = cursor.getInt( 0 );

        return number;
    }


    public int max_craveDebtNumber(int personId) {

        int number = 0;

        String query = "SELECT MAX("
                + DataBase.KEY_NUMBER
                + ") FROM " + DataBase.TABLE_CRAVE_DEBT
                + " WHERE " + DataBase.KEY_PERSON_ID
                + "=" + personId;

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            number = cursor.getInt( 0 );

        return number;
    }

    public int max_receiveNumber() {
        int number = 0;

        String query = "SELECT MAX("
                + DataBase.KEY_NUMBER
                + ") FROM " + DataBase.TABLE_RECEIVED;

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            number = cursor.getInt( 0 );

        return number;
    }

    //endregion


    //region getting  IDs


    public int get_Id_WorkName(String name) {

        int id = 0;
        cursor = db.query( DataBase.TABLE_WORK_NAME
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_NAME + " =? "
                , new String[]{String.valueOf( name )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }

    public int get_Id_Report(int number) {

        int id = 0;
        cursor = db.query( DataBase.TABLE_REPORT
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_NUMBER + " =? "
                , new String[]{String.valueOf( number )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }

    public int get_Id_Person(String name) {
        int id = 0;
        cursor = db.query( DataBase.TABLE_PERSON
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_NAME + " =? "
                , new String[]{String.valueOf( name )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }


    protected int get_Id_Receive(int number) {
        int id = 0;
        cursor = db.query( DataBase.TABLE_RECEIVED
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_NUMBER + " =? "
                , new String[]{String.valueOf( number )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }


    protected int get_Id_Wallet(int receivedId) {
        int id = 0;
        cursor = db.query( DataBase.TABLE_WALLET
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_RECEIVED_ID + " =? "
                , new String[]{String.valueOf( receivedId )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }


    protected int get_Id_Card(int receivedId) {
        int id = 0;
        cursor = db.query( DataBase.TABLE_CARD
                , new String[]{DataBase.KEY_ID}
                , DataBase.KEY_RECEIVED_ID + " =? "
                , new String[]{String.valueOf( receivedId )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            id = cursor.getInt( 0 );
        }
        return id;
    }


    //endregion


    //region getting name or number with Ids

    protected String getName_workName(int id) {
        String name = "";
        cursor = db.query( DataBase.TABLE_WORK_NAME
                , new String[]{DataBase.KEY_NAME}
                , DataBase.KEY_ID + " =? "
                , new String[]{String.valueOf( id )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            name = cursor.getString( 0 );
        }
        return name;
    }


    protected String getName_person(int id) {
        String name = "";
        cursor = db.query( DataBase.TABLE_PERSON
                , new String[]{DataBase.KEY_NAME}
                , DataBase.KEY_ID + " =? "
                , new String[]{String.valueOf( id )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            name = cursor.getString( 0 );
        }
        return name;
    }


    protected int getNumber_report(int id) {
        int number = 0;
        cursor = db.query( DataBase.TABLE_REPORT
                , new String[]{DataBase.KEY_NUMBER}
                , DataBase.KEY_ID + " =? "
                , new String[]{String.valueOf( id )}
                , null
                , null
                , null
                , null );

        if (cursor.moveToFirst()) {
            number = cursor.getInt( 0 );
        }
        return number;
    }


    //endregion


    //region  is Exists values


    public boolean isExistItem(AItem aItem, String dataBaseName) {

        cursor = db.query( dataBaseName
                , new String[]{DataBase.KEY_ID
                        , DataBase.KEY_NAME}
                , DataBase.KEY_NAME + "=? "
                , new String[]{String.valueOf( aItem.getName() )}
                , null
                , null
                , null
                , null );

        return cursor.moveToFirst();
    }

    public boolean isExistReport(AReport aReport) {

        cursor = db.query( DataBase.TABLE_REPORT
                , new String[]{DataBase.KEY_ID
                        , DataBase.KEY_NUMBER}
                , DataBase.KEY_NUMBER + "=? "
                , new String[]{String.valueOf( aReport.getNumber() )}
                , null
                , null
                , null
                , null );

        return cursor.moveToFirst();
    }

    public boolean isExistPicture(APicture aPicture) {

        cursor = db.query( DataBase.TABLE_PICTURE
                , new String[]{DataBase.KEY_ID
                        , DataBase.KEY_IMAGE}
                , DataBase.KEY_IMAGE + "=? "
                , new String[]{Arrays.toString( aPicture.getPicture() )}
                , null
                , null
                , null
                , null );

        return cursor.moveToFirst();
    }


    protected boolean checkConditionWorkName(int id) {

        boolean condition = false;

        String query = "SELECT "
                + DataBase.KEY_CONDITION
                + " FROM " + DataBase.TABLE_WORK_NAME
                + " WHERE " + DataBase.KEY_ID + " = " + id;

        cursor = db.rawQuery( query, null );

        if (cursor.moveToFirst())
            condition = cursor.getInt( 0 ) > 0;
        return condition;
    }

    //endregion


    //region inserting data to db

    public void insertReport(AReport aReport) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_CONDITION, aReport.getCondition() );
        contentValues.put( DataBase.KEY_DATE, aReport.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRE_REMAINED, aReport.getPreRemained() );
        contentValues.put( DataBase.KEY_PAID, aReport.getPaid() );
        contentValues.put( DataBase.KEY_RECEIVED, aReport.getSumReceived() );
        contentValues.put( DataBase.KEY_REMAINED, aReport.getRemained() );
        contentValues.put( DataBase.KEY_ATTACH_COUNT, aReport.getAttachCount() );
        contentValues.put( DataBase.KEY_NUMBER, aReport.getNumber() );

        db.insert( DataBase.TABLE_REPORT, null, contentValues );

    }

    public void insertWork(AWork aWork) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_REPORT_ID, get_Id_Report( aWork.getReportNumber() ) );
        contentValues.put( DataBase.KEY_WORK_NAME_ID, get_Id_WorkName( aWork.getName() ) );
        contentValues.put( DataBase.KEY_NUMBER, aWork.getNumber() );
        contentValues.put( DataBase.KEY_SUB_NUMBER, aWork.getSubNumber() );
        contentValues.put( DataBase.KEY_PRICE, aWork.getPrice() );
        contentValues.put( DataBase.KEY_ATTACH_COUNT, aWork.getAttachCount() );
        contentValues.put( DataBase.KEY_COMMENT, aWork.getComment() );
        contentValues.put( DataBase.KEY_DATE, aWork.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_ACCOUNT, aWork.getAccountName() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aWork.getAccountNumber() );
        contentValues.put( DataBase.KEY_PERSON_ID, aWork.getPersonId() );

        db.insert( DataBase.TABLE_WORK_LIST, null, contentValues );

    }


    public void insertCraveDebt(ACraveAndDebt aCraveAndDebt) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_PERSON_ID, get_Id_Person( aCraveAndDebt.getPersonName() ) );
        contentValues.put( DataBase.KEY_NUMBER, aCraveAndDebt.getNumber() );
        contentValues.put( DataBase.KEY_DATE, aCraveAndDebt.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRICE, aCraveAndDebt.getPrice() );
        contentValues.put( DataBase.KEY_COMMENT, aCraveAndDebt.getComment() );
        contentValues.put( DataBase.KEY_TYPE, aCraveAndDebt.getType() );

        db.insert( DataBase.TABLE_CRAVE_DEBT, null, contentValues );
    }

    public void insertItem(AItem aItem, String dataBaseName) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_NAME, aItem.getName() );
        if (dataBaseName.equals( DataBase.TABLE_WORK_NAME ))
            contentValues.put( DataBase.KEY_CONDITION, aItem.getCondition() );

        db.insert( dataBaseName, null, contentValues );
    }

    public void insertReceive(AMoneyReceive aMoneyReceive) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_DATE, aMoneyReceive.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRICE, aMoneyReceive.getPrice() );
        contentValues.put( DataBase.KEY_NUMBER, aMoneyReceive.getNumber() );
        contentValues.put( DataBase.KEY_REPORT_ID, get_Id_Report( aMoneyReceive.getReportNumber() ) );

        db.insert( DataBase.TABLE_RECEIVED, null, contentValues );
    }


    public void insertWallet(AAccount aAccount) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_REMAINED, aAccount.getRemained() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aAccount.getAccountNumber() );

        db.insert( DataBase.TABLE_WALLET, null, contentValues );
    }


    public void insertCard(AAccount aAccount) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_REMAINED, aAccount.getRemained() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aAccount.getAccountNumber() );

        db.insert( DataBase.TABLE_CARD, null, contentValues );
    }


    public void insertPicture(APicture aPicture) {
        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_IMAGE, aPicture.getPicture() );
        contentValues.put( DataBase.KEY_REPORT_ID, aPicture.getReportId() );
        contentValues.put( DataBase.KEY_NUMBER, aPicture.getNumber() );
        contentValues.put( DataBase.KEY_SUB_NUMBER, aPicture.getSubNumber() );
        contentValues.put( DataBase.KEY_RECEIVED_NUMBER, aPicture.getReceivedNumber() );

        db.insert( DataBase.TABLE_PICTURE, null, contentValues );
    }


    public void insertNote(String comment) {
        ContentValues contentValues = new ContentValues();

        contentValues.put( DataBase.KEY_COMMENT, comment );

        db.insert( DataBase.TABLE_NOTE, null, contentValues );
    }

    //endregion


    //region Updating Data to db

    public void updateReport(AReport aReport) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_CONDITION, aReport.getCondition() );
        contentValues.put( DataBase.KEY_DATE, aReport.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRE_REMAINED, aReport.getPreRemained() );
        contentValues.put( DataBase.KEY_PAID, aReport.getPaid() );
        contentValues.put( DataBase.KEY_RECEIVED, aReport.getSumReceived() );
        contentValues.put( DataBase.KEY_REMAINED, aReport.getRemained() );
        contentValues.put( DataBase.KEY_ATTACH_COUNT, aReport.getAttachCount() );
        contentValues.put( DataBase.KEY_NUMBER, aReport.getNumber() );

        db.update( DataBase.TABLE_REPORT, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aReport.getId() )} );

    }

    public void updateReceive(AMoneyReceive aMoneyReceive) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_DATE, aMoneyReceive.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRICE, aMoneyReceive.getPrice() );
        contentValues.put( DataBase.KEY_NUMBER, aMoneyReceive.getNumber() );
        contentValues.put( DataBase.KEY_REPORT_ID, aMoneyReceive.getReportId() );

        db.update( DataBase.TABLE_RECEIVED, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aMoneyReceive.getId() )} );
    }

    public void updateWork(AWork aWork) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_REPORT_ID, aWork.getReportId() );
        contentValues.put( DataBase.KEY_WORK_NAME_ID, aWork.getWorkNameId() );
        contentValues.put( DataBase.KEY_NUMBER, aWork.getNumber() );
        contentValues.put( DataBase.KEY_SUB_NUMBER, aWork.getSubNumber() );
        contentValues.put( DataBase.KEY_PRICE, aWork.getPrice() );
        contentValues.put( DataBase.KEY_ATTACH_COUNT, aWork.getAttachCount() );
        contentValues.put( DataBase.KEY_COMMENT, aWork.getComment() );
        contentValues.put( DataBase.KEY_DATE, aWork.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_ACCOUNT, aWork.getAccountName() );
        contentValues.put( DataBase.KEY_PERSON_ID, aWork.getPersonId() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aWork.getAccountNumber() );

        db.update( DataBase.TABLE_WORK_LIST, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aWork.getId() )} );
    }

    public void updateCraveDebt(ACraveAndDebt aCraveAndDebt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_PERSON_ID, aCraveAndDebt.getPersonId() );
        contentValues.put( DataBase.KEY_NUMBER, aCraveAndDebt.getNumber() );
        contentValues.put( DataBase.KEY_DATE, aCraveAndDebt.getStringGregorianDate() );
        contentValues.put( DataBase.KEY_PRICE, aCraveAndDebt.getPrice() );
        contentValues.put( DataBase.KEY_COMMENT, aCraveAndDebt.getComment() );
        contentValues.put( DataBase.KEY_TYPE, aCraveAndDebt.getType() );

        db.update( DataBase.TABLE_CRAVE_DEBT, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aCraveAndDebt.getId() )} );
    }

    public void updateItem(AItem aItem, String dataBaseName) {
        ContentValues contentValues = new ContentValues();

        if (aItem.getClassName().equals( AItem.PERSON )) {
            contentValues.put( DataBase.KEY_NAME, aItem.getName() );
        } else if (aItem.getClassName().equals( AItem.WORK_NAME )) {
            contentValues.put( DataBase.KEY_NAME, aItem.getName() );
            contentValues.put( DataBase.KEY_CONDITION, aItem.getCondition() );
        }


        db.update( dataBaseName, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aItem.getId() )} );
    }


    public void updateWallet(AAccount aAccount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_REMAINED, aAccount.getRemained() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aAccount.getAccountNumber() );

        db.update( DataBase.TABLE_WALLET, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aAccount.getId() )} );
    }


    public void updateCard(AAccount aAccount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_REMAINED, aAccount.getRemained() );
        contentValues.put( DataBase.KEY_ACCOUNT_NUMBER, aAccount.getAccountNumber() );

        db.update( DataBase.TABLE_CARD, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aAccount.getId() )} );
    }

    public void updatePicture(APicture aPicture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( DataBase.KEY_REPORT_ID, aPicture.getReportId() );
        contentValues.put( DataBase.KEY_NUMBER, aPicture.getNumber() );
        contentValues.put( DataBase.KEY_SUB_NUMBER, aPicture.getNumber() );
        contentValues.put( DataBase.KEY_IMAGE, aPicture.getPicture() );

        db.update( DataBase.TABLE_PICTURE, contentValues, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aPicture.getId() )} );
    }


    //endregion  to


    //region Deleting Data from db

    public void deleteReport(AReport aReport) {
        db.delete( DataBase.TABLE_REPORT, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aReport.getId() )} );
    }

    public void deleteWork(AWork aWork) {
        db.delete( DataBase.TABLE_WORK_LIST, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aWork.getId() )} );
    }


    public void deleteItem(AItem aItem, String dataBaseName) {
        db.delete( dataBaseName, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aItem.getId() )} );
    }

    public void deleteCraveDebt(ACraveAndDebt aCraveAndDebt) {
        db.delete( DataBase.TABLE_CRAVE_DEBT, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aCraveAndDebt.getId() )} );
    }

    public void deleteReceive(AMoneyReceive aMoneyReceive) {
        db.delete( DataBase.TABLE_RECEIVED, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aMoneyReceive.getId() )} );
    }

    public void deletePicture(APicture aPicture) {
        db.delete( DataBase.TABLE_PICTURE, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aPicture.getId() )} );
    }


    public void deleteWalletReceive(AAccount aAccount) {
        db.delete( DataBase.TABLE_WALLET, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aAccount.getId() )} );
    }


    public void deleteCardReceive(AAccount aAccount) {
        db.delete( DataBase.TABLE_CARD, DataBase.KEY_ID + " =?", new String[]{String.valueOf( aAccount.getId() )} );
    }


    public void deleteNote(String comment) {
        db.delete( DataBase.TABLE_NOTE, DataBase.KEY_COMMENT + " =?", new String[]{String.valueOf( comment )} );
    }

    //endregion

}

