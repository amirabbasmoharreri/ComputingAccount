package com.abbasmoharreri.computingaccount.text;

import com.abbasmoharreri.computingaccount.module.ASms;

import java.text.BreakIterator;

public class SmsProcessing {

    private ASms aSms;
    private BreakIterator lineIterator;


    public SmsProcessing(ASms aSms) {
        this.aSms = aSms;
        lineIterator = BreakIterator.getLineInstance();
        fetch_bankName();
        fetch_account();
        fetch_price();
        fetch_type();
    }


    private void fetch_bankName() {

        String bankName = String.valueOf( lineIterator.next( 2 ) );
    }


    private void fetch_account() {


    }

    private void fetch_price() {


    }

    private void fetch_type() {


    }
}
