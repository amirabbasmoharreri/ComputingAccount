package com.abbasmoharreri.computingaccount.module;

import com.abbasmoharreri.computingaccount.pesiandate.DateConverter;
import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.Date;

public class ACraveAndDebt {


    public final static String CRAVE = "Crave";
    public final static String DEBT = "Debt";

    private int id;
    private int personId;
    private String personName;
    private int number;
    protected Date orgDate;
    protected Date iranianDate;
    private int price;
    private String comment;
    private String type;

    private TextProcessing textProcessing;

    public ACraveAndDebt() {
        textProcessing = new TextProcessing();
        orgDate = new Date();
        iranianDate = new Date();
    }

    public int getId() {
        return id;
    }

    public ACraveAndDebt setId(int id) {
        this.id = id;
        return this;
    }

    public int getPersonId() {
        return personId;
    }

    public ACraveAndDebt setPersonId(int personId) {
        this.personId = personId;
        return this;
    }

    public String getPersonName() {
        return personName;
    }

    public ACraveAndDebt setPersonName(String personName) {
        this.personName = personName;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public ACraveAndDebt setNumber(int number) {
        this.number = number;
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

    public ACraveAndDebt setIranianDate(Date iranianDate) {
        this.iranianDate = iranianDate;
        iranianToGregorian();
        return this;
    }

    public ACraveAndDebt setGregorianDate(Date orgDate) {
        this.orgDate = orgDate;
        gregorianToIranian();
        return this;
    }

    public int getPrice() {
        return price;
    }

    public ACraveAndDebt setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ACraveAndDebt setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getType() {
        return type;
    }

    public ACraveAndDebt setType(String type) {
        this.type = type;
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
