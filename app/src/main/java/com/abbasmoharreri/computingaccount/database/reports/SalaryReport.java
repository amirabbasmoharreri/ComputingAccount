package com.abbasmoharreri.computingaccount.database.reports;

import android.content.Context;

import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalaryReport extends DataBaseController {

    private Context context;
    private List<AWork> workList;
    private Date startGeorgianDate;
    private Date endGeorgianDate;
    private Date startIranianDate;
    private Date endIranianDate;
    private AReport aReport;
    private ArrayList<APicture> attachList;
    private ArrayList<AWork> salaryList;


    public SalaryReport(Context context, AReport aReport) {
        super(context);
        this.context = context;
        this.workList = new ArrayList<>();
        ReportWNumber reportWNumber = new ReportWNumber(context, aReport.getNumber());
        this.workList = reportWNumber.getList();
        this.aReport = aReport;
        this.attachList = new ArrayList<>();
        this.salaryList = new ArrayList<>();
        this.startGeorgianDate = new Date();
        this.endGeorgianDate = new Date();
        this.startIranianDate = new Date();
        this.endIranianDate = new Date();

        fetchPeriodDate();
        createSalaryList();
        createPictureList();

    }


    private void fetchPeriodDate() {

        Date date = new Date();

        this.startGeorgianDate = workList.get(0).getGregorianDate();
        this.endGeorgianDate = workList.get(0).getGregorianDate();
        this.startIranianDate = workList.get(0).getIranianDate();
        this.endIranianDate = workList.get(0).getIranianDate();

        for (int i = 1; i < workList.size(); i++) {
            date = workList.get(i).getGregorianDate();
            if (date.compareTo(endGeorgianDate) > 0) {
                this.endGeorgianDate = workList.get(i).getGregorianDate();
                this.endIranianDate = workList.get(i).getIranianDate();
            }
            if (date.compareTo(startGeorgianDate) < 0) {
                this.startGeorgianDate = workList.get(i).getGregorianDate();
                this.startIranianDate = workList.get(i).getIranianDate();
            }
        }
    }


    private void createSalaryList() {

        List<Integer> workId = new ArrayList<>();

        for (int i = 0; i < workList.size(); i++) {

            if (!workId.contains(workList.get(i).getId())) {
                salaryList.add(workList.get(i));
                workId.add(workList.get(i).getId());
            }

            for (int j = 0; j < workList.size(); j++) {


                if (!workId.contains(workList.get(j).getId())) {

                    if (workList.get(i).getNumber() == workList.get(j).getNumber() && workList.get(i).getSubNumber() != workList.get(j).getSubNumber()) {

                        int price = salaryList.get(salaryList.indexOf(workList.get(i))).getPrice() + workList.get(j).getPrice();
                        String comment = salaryList.get(salaryList.indexOf(workList.get(i))).getComment() + " , " + workList.get(j).getComment();

                        salaryList.get(salaryList.indexOf(workList.get(i))).setPrice(price);
                        salaryList.get(salaryList.indexOf(workList.get(i))).setComment(comment);
                        salaryList.get(salaryList.indexOf(workList.get(i))).getAttaches().addAll(workList.get(j).getAttaches());
                        workId.add(workList.get(j).getId());
                    }
                }
            }
        }

    }


    private void createPictureList() {
        for (AWork aWork : salaryList) {
            attachList.addAll(aWork.getAttaches());
        }
    }


    public String getStartGeorgianDate() {
        TextProcessing textProcessing = new TextProcessing();

        return textProcessing.convertDateToStringWithoutTime(startGeorgianDate);
    }

    public String getEndGeorgianDate() {
        TextProcessing textProcessing = new TextProcessing();

        return textProcessing.convertDateToStringWithoutTime(endGeorgianDate);
    }

    public String getStartIranianDate() {
        TextProcessing textProcessing = new TextProcessing();

        return textProcessing.convertDateToStringWithoutTime(startIranianDate);
    }

    public String getEndIranianDate() {
        TextProcessing textProcessing = new TextProcessing();


        return textProcessing.convertDateToStringWithoutTime(endIranianDate);
    }

    public AReport getAReport() {
        return aReport;
    }

    public List<APicture> getAttachList() {
        return attachList;
    }

    public List<AWork> getSalaryList() {
        return salaryList;
    }
}
