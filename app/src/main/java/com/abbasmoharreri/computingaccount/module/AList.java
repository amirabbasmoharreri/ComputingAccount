package com.abbasmoharreri.computingaccount.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.abbasmoharreri.computingaccount.pesiandate.DateConverter;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AList {


    public final static String KEY_PARCELABLE_Work = "AWork";

    protected int id;
    protected int reportNumber;
    protected int reportId;
    protected int workId;
    protected int workNameId;
    protected int personId;
    protected int number;
    protected int subNumber;
    protected int receivedId;
    protected int price;
    protected int preRemained;
    protected int paid;
    protected int sumReceived;
    protected int remained;
    protected int attachCount;
    protected int attachSumCount;
    protected String accountName;
    protected String accountNumber;
    protected Date orgDate;
    protected Date iranianDate;
    protected boolean condition = false;
    protected String name;
    protected String comment;
    protected String personName;
    protected ArrayList<APicture> attaches;
    protected TextProcessing textProcessing;


    public AList() {
        textProcessing = new TextProcessing();
        attaches = new ArrayList<>();
        orgDate = new Date();
        iranianDate = new Date();
    }


    public int getId() {
        return id;
    }

    public AList setId(int id) {
        this.id = id;
        return this;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public AList setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
        return this;
    }

    public int getReportId() {
        return reportId;
    }

    public AList setReportId(int reportId) {
        this.reportId = reportId;
        return this;
    }

    public int getWorkId() {
        return workId;
    }

    public AList setWorkId(int workId) {
        this.workId = workId;
        return this;
    }

    public int getWorkNameId() {
        return workNameId;
    }

    public AList setWorkNameId(int workNameId) {
        this.workNameId = workNameId;
        return this;
    }

    public int getPersonId() {
        return personId;
    }

    public AList setPersonId(int personId) {
        this.personId = personId;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public AList setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getSubNumber() {
        return subNumber;
    }

    public AList setSubNumber(int subNumber) {
        this.subNumber = subNumber;
        return this;
    }

    public int getReceivedId() {
        return receivedId;
    }

    public AList setReceivedId(int receivedId) {
        this.receivedId = receivedId;
        return this;
    }


    public int getPrice() {
        return price;
    }

    public AList setPrice(int price) {
        this.price = price;
        return this;
    }

    public int getPreRemained() {
        return preRemained;
    }

    public AList setPreRemained(int preRemained) {
        this.preRemained = preRemained;
        return this;
    }

    public int getPaid() {
        return paid;
    }

    public AList setPaid(int paid) {
        this.paid = paid;
        return this;
    }

    public int getSumReceived() {
        return sumReceived;
    }

    public AList setSumReceived(int sumReceived) {
        this.sumReceived = sumReceived;
        return this;
    }

    public int getRemained() {
        return remained;
    }

    public AList setRemained(int remained) {
        this.remained = remained;
        return this;
    }


    public int getAttachCount() {
        return attachCount;
    }

    public AList setAttachCount(int attachCount) {
        this.attachCount = attachCount;
        return this;
    }

    public int getAttachSumCount() {
        return attachSumCount;
    }

    public AList setAttachSumCount(int attachSumCount) {
        this.attachSumCount = attachSumCount;
        return this;
    }


    public String getAccountName() {
        return accountName;
    }

    public AList setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public AList setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }


    public String getStringIranianDate() {
        return textProcessing.convertDateToString( iranianDate );
    }

    public String getStringGregorianDate() {
        return textProcessing.convertDateToString( orgDate );
    }

    public Date getIranianDate() {
        return iranianDate;
    }

    public Date getGregorianDate() {
        return orgDate;
    }

    public AList setIranianDate(Date iranianDate) {
        this.iranianDate = iranianDate;
        iranianToGregorian();
        return this;
    }

    public AList setGregorianDate(Date orgDate) {
        this.orgDate = orgDate;
        gregorianToIranian();
        return this;
    }


    public boolean getCondition() {
        return condition;
    }

    public AList setCondition(boolean condition) {
        this.condition = condition;
        return this;
    }


    public ArrayList<APicture> getAttaches() {
        return attaches;
    }

    public AList setAttaches(ArrayList<APicture> attaches) {
        this.attaches = attaches;
        return this;
    }


    public String getName() {
        return name;
    }

    public AList setName(String name) {
        this.name = name;

        return this;
    }

    public String getComment() {
        return comment;
    }

    public AList setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getPersonName() {
        return personName;
    }

    public AList setPersonName(String personName) {
        this.personName = personName;
        return this;
    }


    private void gregorianToIranian() {
        DateConverter dateConverter = new DateConverter( orgDate.getYear(), orgDate.getMonth(), orgDate.getDate() );

        Date date1 = new Date( dateConverter.getIranianYear()
                , dateConverter.getIranianMonth()
                , dateConverter.getIranianDay()
                , orgDate.getHours()
                , orgDate.getMinutes() );

        iranianDate = date1;
    }


    private void iranianToGregorian() {
        DateConverter dateConverter = new DateConverter();

        dateConverter.setIranianDate( iranianDate.getYear(), iranianDate.getMonth(), iranianDate.getDate() );

        Date date1 = new Date( dateConverter.getGregorianYear()
                , dateConverter.getGregorianMonth()
                , dateConverter.getGregorianDay() - 1
                , iranianDate.getHours()
                , iranianDate.getMinutes() );

        orgDate = date1;
    }


}
