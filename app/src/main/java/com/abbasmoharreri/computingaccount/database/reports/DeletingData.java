package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.AWork;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class for deleting any item from anywhere in app
 * @version 1.0
 * */


public class DeletingData extends DataBaseController {

    private Context context;
    private UpdateAllData updateAllData;

    public DeletingData(Context context) {
        super( context );
        this.context = context;
        updateAllData = new UpdateAllData( context );
    }


    /**
     * This method for delete Work from WorkList and update all Values which on below:
     * - Deleting this item from WorkList
     * - Deleting Picture or pictures linked with this item
     * - updating items of the current Report like 'paid' , 'remained' , ...
     * - updating remaining of Wallets and Cards
     * - Correcting WorkList numbering
     *
     * @param aWork (Type = AWork Class) which one is you want to delete - an instance of AWork
     * */
    public void deleteFromWorkList(AWork aWork) {

        deleteWork( aWork );
        deletionPicture( aWork );
        updateAllData.updateReportItems( aWork );
        updateAllData.updateWalletCardDelete( aWork );
        changeWorkNumberAfterDelete( aWork );

    }


    /**
     * This method for delete Received from DB and update all Values which on below:
     * - Deleting this item from Received Table of DB
     * - Deleting Picture or pictures linked with this item
     * - updating items of the current Report like 'paid' , 'remained' , ...
     * - updating remaining of Wallets and Cards
     * */
    public void deleteFromReceived(AMoneyReceive aMoneyReceive) {


        deleteReceive( aMoneyReceive );
        deletionPicture( aMoneyReceive );
        updateAllData.updateReportItems( aMoneyReceive );
        updateAllData.updateWalletCardDeleteReceive( aMoneyReceive );

    }

    /**
     * This method for delete Picture from Picture Table in DB
     * This used when you want delete Work
     * @param aWork (Type = AWork Class) which one is you want to delete - an instance of AWork
     * */

    private void deletionPicture(AWork aWork) {
        for (int i = 0; i < aWork.getAttaches().size(); i++) {
            deletePicture( aWork.getAttaches().get( i ) );
        }
    }


    /**
     * This method for delete Picture from Picture Table in DB
     * This used when you want delete Received
     * @param aMoneyReceive (Type = AMoneyReceived Class) which one is you want to delete - an instance of AMoneyReceived
     * */
    private void deletionPicture(AMoneyReceive aMoneyReceive) {
        for (int i = 0; i < aMoneyReceive.getAttaches().size(); i++) {
            deletePicture( aMoneyReceive.getAttaches().get( i ) );
        }
    }


    /**
     * This method for correcting WorkList numbering when you want to delete Work
     * @param aWork (Type = AWork Class) which one is you want to delete - an instance of AWork
     * */
    private void changeWorkNumberAfterDelete(AWork aWork) {
        ReportWNumber reportWNumber = new ReportWNumber( context, aWork.getReportNumber() );
        int workNameId = aWork.getWorkNameId();
        int number = aWork.getNumber();
        int subNumber = aWork.getSubNumber();
        List<AWork> aWorks = reportWNumber.getList();


        List<Integer> workIds = new ArrayList<>();
        for (int i = 0; i < aWorks.size(); i++) {
            if (checkConditionWorkName( aWorks.get( i ).getWorkNameId() )) {
                workIds.add( aWorks.get( i ).getWorkNameId() );
            }
        }


        for (int i = 0; i < aWorks.size(); i++) {

            int sub = aWorks.get( i ).getSubNumber();
            int num = aWorks.get( i ).getNumber();
            int id = aWorks.get( i ).getWorkNameId();

            if (workIds.contains( workNameId )) {
                if (id == workNameId) {
                    if (subNumber < sub) {
                        aWorks.get( i ).setSubNumber( aWorks.get( i ).getSubNumber() - 1 );
                        updateWork( aWorks.get( i ) );
                    }
                }
            } else {
                if (number < num) {
                    aWorks.get( i ).setNumber( aWorks.get( i ).getNumber() - 1 );
                    updateWork( aWorks.get( i ) );
                }
            }
        }


    }


}
