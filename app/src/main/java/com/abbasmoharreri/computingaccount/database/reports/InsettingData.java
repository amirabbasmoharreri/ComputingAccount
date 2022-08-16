package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AItem;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AWork;


public class InsettingData extends DataBaseController {

    private AWork aWork;
    private AMoneyReceive aMoneyReceive;
    private AItem person;
    private AItem workName;
    private String personDatabaseName = AItem.PERSON;
    private String workNameDatabaseName = AItem.WORK_NAME;
    private APicture aPicture;
    private Context context;
    private UpdateAllData updateAllData;

    public InsettingData(Context context) {
        super( context );
        this.context = context;
        this.person = new AItem();
        this.workName = new AItem();
        this.aPicture = new APicture();
        updateAllData = new UpdateAllData( context );

    }


    /**
     * This method for inserting Work into the WorkList in DB
     * Work Steps:
     *      1-Checking existence of items
     *      2-Fetching Work name's ID
     *      3-Inserting work to DB
     *      4-Adding picture to the work and inserting it to Db
     *      5-Updating Report Items
     *      6-Updating Wallet and Card
     * @param aWork (Type = AWork class)
     * */
    public void insertWorkList(AWork aWork) {

        this.aWork = aWork;
        this.person.setName( aWork.getPersonName() )
                .setClassName( personDatabaseName );
        this.workName.setName( aWork.getName() )
                .setClassName( workNameDatabaseName );

        checkExistItems();
        fetch_Id_Name();
        insertWork( this.aWork );
        addPicture( this.aWork );
        updateAllData.updateReportItems( this.aWork );
        updateAllData.updateWalletCardInsert( this.aWork );
    }


    /**
     *This method for inserting Received money into the DB
     * Work steps:
     *      1-inserting Received money to DB
     *      2-Updating Report Items
     *      3-Updating Wallet and Card
     *
     * @param aMoneyReceive (Type = AMoneyReceive Class)
     * */
    public void insertMoney(AMoneyReceive aMoneyReceive) {
        this.aMoneyReceive = aMoneyReceive;
        this.aMoneyReceive.setReportId( get_Id_Report( this.aMoneyReceive.getReportNumber() ) );
        insertReceive( this.aMoneyReceive );
        addPicture( this.aMoneyReceive );
        updateAllData.updateReportItems( this.aMoneyReceive );
        updateAllData.updateWalletCardInsertReceive( this.aMoneyReceive );

    }


    /**
     * This method for checking existence of Items
     * work Steps:
     *      1-Checking existence Person Name
     *      2-Checking existence Work Name
     *      3-Fetching IDs of these items
     * */
    private void checkExistItems() {

        if (!isExistItem( person, personDatabaseName )) {
            insertItem( person, personDatabaseName );
        }

        if (!isExistItem( workName, workNameDatabaseName )) {
            insertItem( workName, workNameDatabaseName );
        }

        fetch_work_number();
    }

    /**
     * This method for adding picture to works and inserting picture to DB
     *
     *
     * @param aWork (Type = AWork class)
     * */
    private void addPicture(AWork aWork) {
        if (aWork.getAttaches().size() != 0) {
            APicture aPicture = new APicture();
            aPicture.setNumber( aWork.getNumber() );
            aPicture.setSubNumber( aWork.getSubNumber() );
            aPicture.setReportId( aWork.getReportId() );
            for (int i = 0; i < aWork.getAttaches().size(); i++) {
                aPicture.setPicture( aWork.getAttaches().get( i ).getPicture() );
                insertPicture( aPicture );
            }

        }
    }


    /**
     * This method for adding picture to Received Money and inserting picture to DB
     *
     *
     * @param aMoneyReceive (Type = AMoneyReceive class)
     * */
    private void addPicture(AMoneyReceive aMoneyReceive) {
        if (aMoneyReceive.getAttaches().size() != 0) {
            APicture aPicture = new APicture();
            aPicture.setReceivedNumber( aMoneyReceive.getNumber() );
            aPicture.setReportId( aMoneyReceive.getReportId() );
            for (int i = 0; i < aMoneyReceive.getAttaches().size(); i++) {
                aPicture.setPicture( aMoneyReceive.getAttaches().get( i ).getPicture() );
                insertPicture( aPicture );
            }

        }
    }


    /**
     * This method for fetching IDs of Items
     * */
    private void fetch_Id_Name() {
        int id_person = get_Id_Person( person.getName() );
        person.setId( id_person );
        aWork.setPersonId( id_person );

        int id_workName = get_Id_WorkName( person.getName() );
        workName.setId( id_workName );
        aWork.setWorkNameId( id_workName );

        int id_report = get_Id_Report( aWork.getReportNumber() );
        aWork.setReportId( id_report );

    }


    /**
     * This method for fetching data of the numbering of works and set correct number for the works
     * */
    private void fetch_work_number() {
        int subNumber = 1;
        int number = 1;
        int id_report = get_Id_Report( aWork.getReportNumber() );
        int id_workName = get_Id_WorkName( aWork.getName() );

        if (checkConditionWorkName( id_workName )) {

            try {
                String query = "SELECT MAX("
                        + DataBase.KEY_SUB_NUMBER
                        + ") FROM " + DataBase.TABLE_WORK_LIST
                        + " WHERE " + DataBase.KEY_REPORT_ID
                        + " = " + id_report + " AND "
                        + DataBase.KEY_WORK_NAME_ID
                        + " = " + id_workName;

                cursor = db.rawQuery( query, null );

                if (cursor.moveToFirst()) {
                    subNumber = cursor.getInt( 0 );
                    subNumber++;
                }
                aWork.setSubNumber( subNumber );

            } catch (Exception e) {
                e.printStackTrace();
                aWork.setSubNumber( 1 );
            }


            try {

                String query = "SELECT MAX("
                        + DataBase.KEY_NUMBER
                        + ") FROM " + DataBase.TABLE_WORK_LIST
                        + " WHERE " + DataBase.KEY_REPORT_ID
                        + " = " + id_report + " AND "
                        + DataBase.KEY_WORK_NAME_ID
                        + " = " + id_workName;

                cursor = db.rawQuery( query, null );

                if (cursor.moveToFirst())
                    number = cursor.getInt( 0 );

                if (number == 0) {
                    number = max_workNumber() + 1;
                }
                aWork.setNumber( number );

            } catch (Exception e) {
                e.printStackTrace();
                aWork.setNumber( max_workNumber() + 1 );
            }


        } else {
            aWork.setSubNumber( 1 );
            aWork.setNumber( max_workNumber() + 1 );
        }


    }


}
