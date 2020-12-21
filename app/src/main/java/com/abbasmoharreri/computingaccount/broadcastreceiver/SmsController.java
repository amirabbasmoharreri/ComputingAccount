package com.abbasmoharreri.computingaccount.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.abbasmoharreri.computingaccount.MainActivity;
import com.abbasmoharreri.computingaccount.module.ASms;

import java.util.ArrayList;
import java.util.List;

public class SmsController extends BroadcastReceiver {


    private static final String TAG = SmsController.class.getSimpleName();
    public static final String pdu_type = "pdus";
    private Context context;
    private List<ASms> lstSms;

    public SmsController(Context context) {
        this.context = context;
        lstSms = new ArrayList<ASms>();
    }


    public List<ASms> getAllSms() {

        ASms objSms = new ASms();
        Uri message = Uri.parse( "content://sms/" );
        ContentResolver cr = ((MainActivity) context).getContentResolver();

        Cursor c = cr.query( message, null, null, null, null );
        ((MainActivity) context).startManagingCursor( c );
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new ASms();
                objSms.setId( c.getString( c.getColumnIndexOrThrow( "_id" ) ) );
                objSms.setAddress( c.getString( c
                        .getColumnIndexOrThrow( "address" ) ) );
                objSms.setMsg( c.getString( c.getColumnIndexOrThrow( "body" ) ) );
                objSms.setReadState( c.getString( c.getColumnIndex( "read" ) ) );
                objSms.setTime( c.getString( c.getColumnIndexOrThrow( "date" ) ) );
                if (c.getString( c.getColumnIndexOrThrow( "type" ) ).contains( "1" )) {
                    objSms.setFolderName( "inbox" );
                } else {
                    objSms.setFolderName( "sent" );
                }

                lstSms.add( objSms );
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {


        getAllSms();



        /*// Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }*/

    }

}
