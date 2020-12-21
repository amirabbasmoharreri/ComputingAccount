package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AWork;

import java.util.ArrayList;
import java.util.List;

public class DeletingData extends DataBaseController {

    private Context context;
    private UpdateAllData updateAllData;

    public DeletingData(Context context) {
        super( context );
        this.context = context;
        updateAllData = new UpdateAllData( context );
    }


    public void deleteFromWorkList(AWork aWork) {

        deleteWork( aWork );
        deletionPicture( aWork );
        updateAllData.updateReportItems( aWork );
        updateAllData.updateWalletCardDelete( aWork );
        changeWorkNumberAfterDelete( aWork );

    }


    public void deleteFromReceived(AMoneyReceive aMoneyReceive) {


        deleteReceive( aMoneyReceive );
        deletionPicture( aMoneyReceive );
        updateAllData.updateReportItems( aMoneyReceive );
        updateAllData.updateWalletCardDeleteReceive( aMoneyReceive );

    }

    private void deletionPicture(AWork aWork) {
        for (int i = 0; i < aWork.getAttaches().size(); i++) {
            deletePicture( aWork.getAttaches().get( i ) );
        }
    }


    private void deletionPicture(AMoneyReceive aMoneyReceive) {
        for (int i = 0; i < aMoneyReceive.getAttaches().size(); i++) {
            deletePicture( aMoneyReceive.getAttaches().get( i ) );
        }
    }


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
