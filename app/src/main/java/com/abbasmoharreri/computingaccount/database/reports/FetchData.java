package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;
import android.util.Log;

import com.abbasmoharreri.computingaccount.module.ACraveAndDebt;
import com.abbasmoharreri.computingaccount.database.DataBase;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.Date;

public class FetchData extends DataBaseController {


    public FetchData(Context context) {
        super(context);
    }


    public AWork update_Work_IDs(AWork aWork) {

        aWork.setReportId(get_Id_Report(aWork.getReportNumber()))
                .setWorkNameId(get_Id_WorkName(aWork.getName()))
                .setPersonId(get_Id_Person(aWork.getPersonName()));

        return aWork;
    }


    public int update_ACraveDebt_PersonId(String name) {

        int id = get_Id_Person(name);

        return id;
    }

    public AReport fetch_report(int reportNumber) {
        AReport aReport = new AReport();

        String query2 = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_CONDITION + " , "
                + DataBase.KEY_DATE + " , "
                + DataBase.KEY_PRE_REMAINED + " , "
                + DataBase.KEY_PAID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ATTACH_COUNT + " , "
                + DataBase.KEY_NUMBER
                + " FROM " + DataBase.TABLE_REPORT
                + " WHERE " + DataBase.KEY_NUMBER + " = " + reportNumber;

        cursor = db.rawQuery(query2, null);

        if (cursor.moveToFirst()) {

            TextProcessing textProcessing = new TextProcessing();
            Date date = new Date();
            date = textProcessing.convertStringToDate(cursor.getString(2));
            Log.e("ABBAS", date.toString());

            aReport.setId(cursor.getInt(0))
                    .setCondition(cursor.getInt(1) > 0)
                    .setGregorianDate(date)
                    .setPreRemained(cursor.getInt(3))
                    .setPaid(cursor.getInt(4))
                    .setRemained(cursor.getInt(5))
                    .setAttachCount(cursor.getInt(6))
                    .setNumber(cursor.getInt(7));
        }

        return aReport;

    }


    public int sum_paid_work_list_with_report(int id_report) {
        int sumPaid = 0;

        String query = "SELECT SUM("
                + DataBase.KEY_PRICE
                + ") FROM " + DataBase.TABLE_WORK_LIST
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + id_report;

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            sumPaid = cursor.getInt(0);
        return sumPaid;
    }

    public int sum_attach_count_work_list_with_report(int id_report) {
        int sumAttachCount = 0;

        String query4 = "SELECT SUM("
                + DataBase.KEY_ATTACH_COUNT
                + ") FROM " + DataBase.TABLE_WORK_LIST
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + id_report;

        cursor = db.rawQuery(query4, null);

        if (cursor.moveToFirst())
            sumAttachCount = cursor.getInt(0);
        return sumAttachCount;
    }


    public int sum_received_report(int id_report) {
        int received = 0;

        String query3 = "SELECT SUM("
                + DataBase.KEY_PRICE
                + ") FROM " + DataBase.TABLE_RECEIVED
                + " WHERE " + DataBase.KEY_REPORT_ID + " = " + id_report;

        cursor = db.rawQuery(query3, null);

        if (cursor.moveToFirst())
            received = cursor.getInt(0);

        return received;
    }

    public AAccount fetch_wallet(String accountNumber) {
        AAccount wallet = new AAccount();

        String query3 = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + DataBase.TABLE_WALLET
                + " WHERE " + DataBase.KEY_ACCOUNT_NUMBER + " = " + accountNumber;

        cursor = db.rawQuery(query3, null);

        if (cursor.moveToFirst())
            wallet.setId(cursor.getInt(0))
                    .setRemained(cursor.getInt(1))
                    .setAccountNumber(cursor.getString(2))
                    .setName(AAccount.ACCOUNT_WALLET);

        return wallet;
    }

    public AAccount fetch_card(String accountNumber) {
        AAccount card = new AAccount();

        String query3 = "SELECT "
                + DataBase.KEY_ID + " , "
                + DataBase.KEY_REMAINED + " , "
                + DataBase.KEY_ACCOUNT_NUMBER
                + " FROM " + DataBase.TABLE_CARD
                + " WHERE " + DataBase.KEY_ACCOUNT_NUMBER + " = " + accountNumber;

        cursor = db.rawQuery(query3, null);

        if (cursor.moveToFirst())
            card.setId(cursor.getInt(0))
                    .setRemained(cursor.getInt(1))
                    .setAccountNumber(cursor.getString(2))
                    .setName(AAccount.ACCOUNT_CARD);

        return card;
    }
}
