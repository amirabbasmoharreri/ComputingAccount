package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.FetchWalletCard;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;
import com.abbasmoharreri.computingaccount.ui.adapters.SpinnerAccountAdapter;


public class TransferMoney extends Dialog implements View.OnClickListener {

    private Context context;
    private AAccount fromAccount, toAccount;
    private Spinner toAccounts, fromAccounts;
    private EditText price;
    private Button transfer, cancel;

    public TransferMoney(@NonNull Context context, AAccount aAccount) {
        super(context);
        this.context = context;
        this.fromAccount = aAccount;
        this.toAccount = new AAccount();
    }

    public TransferMoney(@NonNull Context context) {
        super(context);
        this.context = context;
        this.toAccount = new AAccount();
        this.fromAccount = new AAccount();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_transfer_money_account);

        fromAccounts = findViewById(R.id.dialog_transfer_from_account);
        toAccounts = findViewById(R.id.dialog_transfer_to_accounts);
        price = findViewById(R.id.dialog_transfer_price);
        transfer = findViewById(R.id.dialog_transfer_button_transfer);
        cancel = findViewById(R.id.dialog_transfer_button_cancel);

        transfer.setOnClickListener(this);
        cancel.setOnClickListener(this);

        setSpinner();
        setEditTextWatcher();
    }


    private void setSpinner() {
        FetchWalletCard fetchWalletCard = new FetchWalletCard(context);
        SpinnerAccountAdapter spinnerAccountAdapter = new SpinnerAccountAdapter(context, fetchWalletCard.getList());
        fromAccounts.setAdapter(spinnerAccountAdapter);
        fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fromAccount = (AAccount) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int index = 0;
        for (int i = 0; i < fetchWalletCard.getList().size(); i++) {
            if (fetchWalletCard.getList().get(i).getAccountNumber().equals(this.fromAccount.getAccountNumber())) {
                index = i;
            }
        }

        fromAccounts.setSelection(index);

        fromAccount = (AAccount) fromAccounts.getSelectedItem();


        toAccounts.setAdapter(spinnerAccountAdapter);
        toAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toAccount = (AAccount) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toAccount = (AAccount) toAccounts.getSelectedItem();
    }

    private void setEditTextWatcher() {

        price.addTextChangedListener(new NumberTextWatcherForThousand(price));

    }

    private boolean showMassage() {

        if (toAccount.equals(fromAccount)) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_account)
                    .setMessage(R.string.massage_orgAndDesAreSame)
                    .setNegativeButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        if (price.getText().toString().equals("")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.massage_title_price)
                    .setMessage(R.string.massage_inputPrice)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }


        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_transfer_button_transfer:

                if (showMassage()) {
                    int priceTransfer = Integer.parseInt(price.getText().toString());

                    fromAccount.setRemained(fromAccount.getRemained() - priceTransfer);
                    toAccount.setRemained(toAccount.getRemained() + priceTransfer);

                    DataBaseController dataBaseController = new DataBaseController(context);

                    if (fromAccount.getName().equals(AAccount.ACCOUNT_CARD)) {
                        dataBaseController.updateCard(fromAccount);
                    } else {
                        dataBaseController.updateWallet(fromAccount);
                    }


                    if (toAccount.getName().equals(AAccount.ACCOUNT_CARD)) {
                        dataBaseController.updateCard(toAccount);
                    } else {
                        dataBaseController.updateWallet(toAccount);
                    }

                    dismiss();
                }
                break;
            case R.id.dialog_transfer_button_cancel:
                dismiss();
                break;
        }

    }
}
