package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;

public class UpdateAllData extends DataBaseController {

    private Context context;
    private DataBaseController dataBaseController;
    private FetchData fetchData;

    public UpdateAllData(Context context) {
        super( context );
        this.context = context;
        fetchData = new FetchData( context );
    }

    public void updateAll(AWork aWork, int differencePrice) {
        updateWork( aWork );
        updatePictureItems( aWork );
        updateReportItems( aWork );
        updateWalletCardUpdate( aWork, differencePrice );
    }

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
