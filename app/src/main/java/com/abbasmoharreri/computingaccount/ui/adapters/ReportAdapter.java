package com.abbasmoharreri.computingaccount.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.CLoseAccount;
import com.abbasmoharreri.computingaccount.database.reports.SalaryReport;
import com.abbasmoharreri.computingaccount.io.PdfCreator;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.static_value.PdfLanguage;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private Context context;
    private List<AReport> aReports;
    private ViewHolder viewHolder;
    private PopupMenu.OnDismissListener listener;
    private DialogInterface.OnDismissListener dialogDismissListener;


    public ReportAdapter(Context context, List<AReport> aReports) {
        this.context = context;
        this.aReports = aReports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.card_view_report, parent, false);
        viewHolder = new ViewHolder(contentView);


        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ImageButton button = holder.button;
        TextView reportNumber = holder.reportNumber;
        TextView condition = holder.condition;
        TextView date = holder.date;
        TextView preRemained = holder.preRemained;
        TextView paid = holder.paid;
        TextView remained = holder.remained;
        TextView received = holder.received;

        AReport aReport = aReports.get(position);

        reportNumber.setText(String.valueOf(aReport.getNumber()));

        if (aReport.getCondition()) {
            condition.setText(R.string.textView_reportAdapter_condition_reported);
        } else {
            condition.setText(R.string.textView_reportAdapter_condition_notReported);
        }
        date.setText(aReport.getStringIranianDate());
        preRemained.setText(String.format("%,d", aReport.getPreRemained()));
        paid.setText(String.format("%,d", aReport.getPaid()));
        remained.setText(String.format("%,d", aReport.getRemained()));
        received.setText(String.format("%,d", aReport.getSumReceived()));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, position);
            }
        });

    }


    public void showPopup(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.card_view_menu_report, popupMenu.getMenu());
        popupMenu.show();

        final DataBaseController dataBaseController = new DataBaseController(context);

        popupMenu.setOnDismissListener(listener);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.button_delete) {

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.message_title_attention)
                            .setMessage(R.string.massage_ifDeleteLostData)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        dataBaseController.deleteReport(aReports.get(position));
                                        aReports.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, R.string.toast_deleteItem, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setOnDismissListener(dialogDismissListener)
                            .create()
                            .show();

                } else if (item.getItemId() == R.id.button_edit) {
                    try {
                      /*  UpdateWorkDialog updateWorkDialog = new UpdateWorkDialog( context, aWorks.get( position ), position );
                        updateWorkDialog.show();
                        Toast.makeText( context, "Update", Toast.LENGTH_SHORT ).show();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (item.getItemId() == R.id.button_close_account) {

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.message_title_attention)
                            .setMessage(R.string.massage_doYouWantCloseAccount)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        CLoseAccount cLoseAccount = new CLoseAccount(context, aReports.get(position));
                                        if (cLoseAccount.checkCondition()) {
                                            showMassage();
                                        } else {
                                            cLoseAccount.commit();
                                            Toast.makeText(context, R.string.toast_closeAccount, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setOnDismissListener(dialogDismissListener)
                            .create()
                            .show();
                } else if (item.getItemId() == R.id.button_create_pdf) {
                    SalaryReport salaryReport = new SalaryReport(context, aReports.get(position));
                    PdfCreator pdfCreator = new PdfCreator(context);
                    pdfCreator.createSalaryReportPdf(PdfLanguage.Farsi,salaryReport.getSalaryList(), salaryReport.getAttachList(),salaryReport.getStartIranianDate(),salaryReport.getEndIranianDate(),salaryReport.getAReport());
                    Toast.makeText(context, R.string.toast_pdf_created, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }


    private void showMassage() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.message_title_attention)
                .setMessage(R.string.massage_alreadyReported)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    public void setPopUpMenuListener(PopupMenu.OnDismissListener listener) {
        this.listener = listener;
    }


    public void setDialogDismissListener(DialogInterface.OnDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    @Override
    public int getItemCount() {
        return aReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton button;
        TextView reportNumber, condition, preRemained, paid, remained, date, received;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.button_card_view_report);
            reportNumber = itemView.findViewById(R.id.text_report_number_card_report);
            condition = itemView.findViewById(R.id.text_condition_card_report);
            preRemained = itemView.findViewById(R.id.text_pre_remained_card_report);
            paid = itemView.findViewById(R.id.text_paid_card_report);
            remained = itemView.findViewById(R.id.text_remained_card_report);
            date = itemView.findViewById(R.id.text_date_card_report);
            received = itemView.findViewById(R.id.text_receive_card_report);
        }
    }
}
