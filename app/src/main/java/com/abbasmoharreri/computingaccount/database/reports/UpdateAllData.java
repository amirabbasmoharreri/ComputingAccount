package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;


/**
 * This Class for Updating all data in DB tables when user is changing data of works and etc.
 * @version 1.0
 * */
public class UpdateAllData extends DataBaseController {

    private Context context;
    private DataBaseController dataBaseController;
    private FetchData fetchData;

    public UpdateAllData(Context context) {
        super( context );
        this.context = context;
        fetchData = new FetchData( context );
    }

    /**
     * This method for updating all data in DB tables
     * Work Steps:
     *      1-Updating Works
     *      2-Updating Pictures
     *      3-Updating Report items
     *      4-Updating Wallet and Card
     *
     *
     * @param aWork (Type = AWork class)
     * @param differencePrice (Type = int)
     * */
    public void updateAll(AWork aWork, int differencePrice) {
        updateWork( aWork );
        updatePictureItems( aWork );
        updateReportItems( aWork );
        updateWalletCardUpdate( aWork, differencePrice );
    }

    /**
     * This method for Updating Attachments of works
     *      if deleted picture from work , deleting it from DB
     *      if added picture to works , adding it to DB
     * */
    public void updatePictureItems(AWork aWork) {
        for (int i = 0; i < aWork.getAttaches().size(); i++) {

            if (aWork.getAttaches().get( i ).getId() != 0) {
                if (aWork.getAttaches().get( i ).getPicture() == null) {
                    deletePicture( aWork.getAttaches().get( i ) );
                }
            } else {
                APicture aPicture = new APicture();
                aPicture.setNumber( aWork.getNumber() );
                aPicture.setSubNumber( aWork.getSubNumber() );
                aPicture.setReportId( aWork.getReportId() );
                aPicture.setPicture( aWork.getAttaches().get( i ).getPicture() );
                insertPicture( aPicture );
            }
        }
    }


    /**
     * This method for Updating report Items like sum of paid and count of attachments and Received money
     * when user changes data of any works
     * @param aWork (Type = AWork class)
     * */
    public void updateReportItems(AWork aWork) {

        int sumPaid = fetchData.sum_paid_work_list_with_report( aWork.getReportId() );
        int sumAttachCount = fetchData.sum_attach_count_work_list_with_report( aWork.getReportId() );
        int received = fetchData.sum_received_report( aWork.getReportId() );

        AReport aReport = fetchData.fetch_report( aWork.getReportNumber() );

        aReport.setPaid( sumPaid )
                .setRemained( received + aReport.getPreRemained() - sumPaid )
                .setAttachCount( sumAttachCount )
                .setSumReceived( received );

        updateReport( aReport );
    }


    /**
     * This method for Updating report Items like sum of paid and count of attachments and Received money
     * when user changes data of any works
     * @param aMoneyReceive (Type = AMoneyReceive class)
     * */
    public void updateReportItems(AMoneyReceive aMoneyReceive) {

        int sumPaid = fetchData.sum_paid_work_list_with_report( aMoneyReceive.getReportId() );
        int sumAttachCount = fetchData.sum_attach_count_work_list_with_report( aMoneyReceive.getReportId() );
        int received = fetchData.sum_received_report( aMoneyReceive.getReportId() );

        AReport aReport = fetchData.fetch_report( aMoneyReceive.getReportNumber() );

        aReport.setPaid( sumPaid )
                .setRemained( received + aReport.getPreRemained() - sumPaid )
                .setAttachCount( sumAttachCount )
                .setSumReceived( received );

        updateReport( aReport );
    }


    /**
     * This method for Updating data of Wallet or Card when user is adding work
     * @param aWork (Type = AWork class)
     * */
    public void updateWalletCardInsert(AWork aWork) {
        if (aWork.getAccountName().equals( AAccount.ACCOUNT_CARD )) {

            AAccount card = fetchData.fetch_card( aWork.getAccountNumber() );
            int remained = card.getRemained() - aWork.getPrice();
            card.setRemained( remained );
            updateCard( card );

        } else if (aWork.getAccountName().equals( AAccount.ACCOUNT_WALLET )) {

            AAccount wallet = fetchData.fetch_wallet( aWork.getAccountNumber() );
            int remained = wallet.getRemained() - aWork.getPrice();
            wallet.setRemained( remained );
            updateWallet( wallet );

        }
    }


    /**
     * This method for Updating data of Wallet or Card when user is changing the data of the works
     * @param aWork (Type = AWork class)
     * @param difference (Type = int)
     * */
    public void updateWalletCardUpdate(AWork aWork, int difference) {
        if (aWork.getAccountName().equals( AAccount.ACCOUNT_CARD )) {

            AAccount card = fetchData.fetch_card( aWork.getAccountNumber() );
            int remained = card.getRemained() + difference;
            card.setRemained( remained );
            updateCard( card );

        } else if (aWork.getAccountName().equals( AAccount.ACCOUNT_WALLET )) {

            AAccount wallet = fetchData.fetch_wallet( aWork.getAccountNumber() );
            int remained = wallet.getRemained() + difference;
            wallet.setRemained( remained );
            updateWallet( wallet );

        }
    }


    /**
     * This method for Updating data of Wallet or Card when user is adding Received money
     * @param aMoneyReceive (Type = AMoneyReceive class)
     * */
    public void updateWalletCardInsertReceive(AMoneyReceive aMoneyReceive) {
        if (aMoneyReceive.getAccountName().equals( AAccount.ACCOUNT_CARD )) {

            AAccount card = fetchData.fetch_card( aMoneyReceive.getAccountNumber() );
            int remained = card.getRemained() + aMoneyReceive.getPrice();
            card.setRemained( remained );
            updateCard( card );

        } else if (aMoneyReceive.getAccountName().equals( AAccount.ACCOUNT_WALLET )) {

            AAccount wallet = fetchData.fetch_wallet( aMoneyReceive.getAccountNumber() );
            int remained = wallet.getRemained() + aMoneyReceive.getPrice();
            wallet.setRemained( remained );
            updateWallet( wallet );

        }
    }


    /**
     * This method for Updating data of Wallet or Card when user is deleting work
     * @param aWork (Type = AWork class)
     * */
    public void updateWalletCardDelete(AWork aWork) {
        if (aWork.getAccountName().equals( AAccount.ACCOUNT_CARD )) {

            AAccount card = fetchData.fetch_card( aWork.getAccountNumber() );
            int remained = card.getRemained() + aWork.getPrice();
            card.setRemained( remained );
            updateCard( card );

        } else if (aWork.getAccountName().equals( AAccount.ACCOUNT_WALLET )) {

            AAccount wallet = fetchData.fetch_wallet( aWork.getAccountNumber() );
            int remained = wallet.getRemained() + aWork.getPrice();
            wallet.setRemained( remained );
            updateWallet( wallet );

        }
    }


    /**
     * This method for Updating data of Wallet or Card when user is deleting Received money
     * @param aMoneyReceive (Type = AMoneyReceive class)
     * */
    public void updateWalletCardDeleteReceive(AMoneyReceive aMoneyReceive) {
        if (aMoneyReceive.getAccountName().equals( AAccount.ACCOUNT_CARD )) {

            AAccount card = fetchData.fetch_card( aMoneyReceive.getAccountNumber() );
            int remained = card.getRemained() - aMoneyReceive.getPrice();
            card.setRemained( remained );
            updateCard( card );

        } else if (aMoneyReceive.getAccountName().equals( AAccount.ACCOUNT_WALLET )) {

            AAccount wallet = fetchData.fetch_wallet( aMoneyReceive.getAccountNumber() );
            int remained = wallet.getRemained() - aMoneyReceive.getPrice();
            wallet.setRemained( remained );
            updateWallet( wallet );

        }
    }
}
