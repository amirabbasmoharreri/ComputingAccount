package com.abbasmoharreri.computingaccount.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.StatusHints;

import com.abbasmoharreri.computingaccount.text.TextProcessing;

import java.util.ArrayList;
import java.util.List;

public class AWork extends AList implements Parcelable {

    public AWork() {
        attaches = new ArrayList<>();

    }

    public static final Creator<AWork> CREATOR = new Creator<AWork>() {
        @Override
        public AWork createFromParcel(Parcel in) {
            return new AWork( in );
        }

        @Override
        public AWork[] newArray(int size) {
            return new AWork[size];
        }
    };


    protected AWork(Parcel in) {

        textProcessing = new TextProcessing();

        this.id = in.readInt();
        this.reportNumber = in.readInt();
        this.reportId = in.readInt();
        this.workNameId = in.readInt();
        this.personId = in.readInt();
        this.number = in.readInt();
        this.subNumber = in.readInt();
        this.price = in.readInt();
        this.attachCount = in.readInt();
        this.accountName = in.readString();
        this.accountNumber = in.readString();
        this.orgDate = textProcessing.convertStringToDate( in.readString() );
        this.iranianDate = textProcessing.convertStringToDate( in.readString() );
        this.name = in.readString();
        this.comment = in.readString();
        this.personName = in.readString();
        this.attaches = (ArrayList<APicture>) (in.readValue( AWork.class.getClassLoader() ));
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( id );
        dest.writeInt( reportNumber );
        dest.writeInt( reportId );
        dest.writeInt( workNameId );
        dest.writeInt( personId );
        dest.writeInt( number );
        dest.writeInt( subNumber );
        dest.writeInt( price );
        dest.writeInt( attachCount );
        dest.writeString( accountName );
        dest.writeString( accountNumber );
        dest.writeString( textProcessing.convertDateToString( orgDate ) );
        dest.writeString( textProcessing.convertDateToString( iranianDate ) );
        dest.writeString( name );
        dest.writeString( comment );
        dest.writeString( personName );
        //dest.writeInt( attaches.size() );
        dest.writeValue( attaches );

    }

}
