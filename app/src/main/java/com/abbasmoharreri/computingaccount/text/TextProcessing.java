package com.abbasmoharreri.computingaccount.text;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TextProcessing {


    public TextProcessing() {
    }


    public String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm", Locale.getDefault() );

        return dateFormat.format( date );
    }


    public Date convertStringToDateWithoutTime(String dateString){
        Date date = new Date();
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
            date = format.parse( dateString );
        } catch (Exception e) {
            e.printStackTrace();
        }


        return date;
    }


    public String convertDateToStringWithoutTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );

        return dateFormat.format( date );
    }


    public String convertDateToStringWithoutDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "HH:mm", Locale.getDefault() );

        return dateFormat.format( date );
    }


    public Date convertStringToDate(String dateString) {
        Date date = new Date();
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
            date = simpleDateFormat.parse( dateString );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }


}
