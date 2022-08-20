package com.abbasmoharreri.computingaccount.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This Class for Creating DB
 * @version 1.0
 * */
public class DataBase extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "ComputingAccount";
    public final static int DATABASE_VERSION = 1;

    public final static String KEY_ID = "id";
    public final static String KEY_CONDITION = "Condition";
    public final static String KEY_DATE = "Date";
    public final static String KEY_PRE_REMAINED = "PreRemained";
    public final static String KEY_PAID = "Paid";
    public final static String KEY_REMAINED = "Remained";
    public final static String KEY_ATTACH_COUNT = "AttachCount";
    public final static String KEY_REPORT_ID = "ReportId";
    public final static String KEY_WORK_NAME_ID = "WorkNameId";
    public final static String KEY_SUB_NUMBER = "SubNumber";
    public final static String KEY_NUMBER = "Number";
    public final static String KEY_PRICE = "Price";
    public final static String KEY_LINK_ATTACH = "LinkAttach";
    public final static String KEY_COMMENT = "Comment";
    public final static String KEY_PERSON_ID = "PersonId";
    public final static String KEY_NAME = "Name";
    public final static String KEY_TYPE = "Type";
    public final static String KEY_IMAGE = "Image";
    public final static String KEY_ACCOUNT = "Account";
    public final static String KEY_ACCOUNT_NUMBER = "AccountNumber";
    public final static String KEY_RECEIVED_ID = "ReceivedId";
    public final static String KEY_RECEIVED_NUMBER = "ReceivedNumber";
    public final static String KEY_RECEIVED = "Received";

    public final static String TABLE_REPORT = "Reports";
    public final static String TABLE_WORK_LIST = "WorkList";
    public final static String TABLE_WORK_NAME = "WorkName";
    public final static String TABLE_CRAVE_DEBT = "CraveDebt";
    public final static String TABLE_PERSON = "Person";
    public final static String TABLE_RECEIVED = "Received";
    public final static String TABLE_PICTURE = "Picture";
    public final static String TABLE_WALLET = "Wallet";
    public final static String TABLE_CARD = "Card";
    public final static String TABLE_NOTE = "Note";


    public DataBase(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_REPORTS = "CREATE TABLE IF NOT EXISTS " + TABLE_REPORT + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NUMBER + " INTEGER ,"
                + KEY_CONDITION + " BOOLEAN DEFAULT 0, "
                + KEY_DATE + " DATE, "
                + KEY_PRE_REMAINED + " INTEGER, "
                + KEY_PAID + " INTEGER, "
                + KEY_RECEIVED + " INTEGER, "
                + KEY_REMAINED + " INTEGER,"
                + KEY_ATTACH_COUNT + " INTEGER )";


        String CREATE_TABLE_WORK_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_WORK_LIST + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REPORT_ID + " INTEGER, "
                + KEY_WORK_NAME_ID + " INTEGER, "
                + KEY_NUMBER + " INTEGER, "
                + KEY_SUB_NUMBER + " INTEGER, "
                + KEY_PRICE + " INTEGER, "
                + KEY_ATTACH_COUNT + " INTEGER, "
                + KEY_COMMENT + " TEXT, "
                + KEY_DATE + " DATE, "
                + KEY_PERSON_ID + " INTEGER, "
                + KEY_ACCOUNT + " TEXT, "
                + KEY_ACCOUNT_NUMBER + " TEXT ,"
                + "FOREIGN KEY ( " + KEY_REPORT_ID + ") REFERENCES " + TABLE_REPORT + "( " + KEY_ID + " ) , "
                + "FOREIGN KEY ( " + KEY_WORK_NAME_ID + ") REFERENCES " + TABLE_WORK_NAME + " ( " + KEY_ID + " ), "
                + "FOREIGN KEY ( " + KEY_PERSON_ID + ") REFERENCES " + TABLE_PERSON + " ( " + KEY_ID + " ))";


        String CREATE_TABLE_WORK_NAME = "CREATE TABLE IF NOT EXISTS " + TABLE_WORK_NAME + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT , "
                + KEY_CONDITION + " BOOLEAN  DEFAULT 0)";


        String CREATE_TABLE_CRAVE_DEBT = "CREATE TABLE IF NOT EXISTS " + TABLE_CRAVE_DEBT + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PERSON_ID + " INTEGER , "
                + KEY_NUMBER + " INTEGER, "
                + KEY_DATE + " DATE, "
                + KEY_PRICE + " INTEGER, "
                + KEY_COMMENT + " TEXT, "
                + KEY_TYPE + " TEXT, "
                + "FOREIGN KEY ( " + KEY_PERSON_ID + ") REFERENCES " + TABLE_PERSON + " ( " + KEY_ID + " )) ";


        String CREATE_TABLE_PERSON = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT)";


        String CREATE_TABLE_RECEIVED = "CREATE TABLE IF NOT EXISTS " + TABLE_RECEIVED + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " DATE, "
                + KEY_PRICE + " INTEGER, "
                + KEY_NUMBER + " INTEGER, "
                + KEY_REPORT_ID + " INTEGER, "
                + "FOREIGN KEY ( " + KEY_REPORT_ID + ") REFERENCES " + TABLE_REPORT + " ( " + KEY_ID + " )) ";


        String CREATE_TABLE_PICTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_PICTURE + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REPORT_ID + " INTEGER ,"
                + KEY_NUMBER + " INTEGER ,"
                + KEY_SUB_NUMBER + " INTEGER ,"
                + KEY_RECEIVED_NUMBER + " INTEGER ,"
                + KEY_IMAGE + " BLOB DEFAULT NULL,"
                + "FOREIGN KEY ( " + KEY_REPORT_ID + ") REFERENCES " + TABLE_REPORT + " ( " + KEY_ID + " )) ";


        String CREATE_TABLE_WALLET = "CREATE TABLE IF NOT EXISTS " + TABLE_WALLET + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ACCOUNT_NUMBER + " TEXT ,"
                + KEY_REMAINED + " INTEGER)";


        String CREATE_TABLE_CARD = "CREATE TABLE IF NOT EXISTS " + TABLE_CARD + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ACCOUNT_NUMBER + " TEXT ,"
                + KEY_REMAINED + " INTEGER) ";

        String CREATE_TABLE_NOTE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_COMMENT + " TEXT )";


        sqLiteDatabase.execSQL( CREATE_TABLE_REPORTS );
        sqLiteDatabase.execSQL( CREATE_TABLE_WORK_LIST );
        sqLiteDatabase.execSQL( CREATE_TABLE_WORK_NAME );
        sqLiteDatabase.execSQL( CREATE_TABLE_CRAVE_DEBT );
        sqLiteDatabase.execSQL( CREATE_TABLE_PERSON );
        sqLiteDatabase.execSQL( CREATE_TABLE_RECEIVED );
        sqLiteDatabase.execSQL( CREATE_TABLE_PICTURE );
        sqLiteDatabase.execSQL( CREATE_TABLE_WALLET );
        sqLiteDatabase.execSQL( CREATE_TABLE_CARD );
        sqLiteDatabase.execSQL( CREATE_TABLE_NOTE );


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
