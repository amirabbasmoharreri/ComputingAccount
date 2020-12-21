package com.abbasmoharreri.computingaccount.module;

import android.os.Parcel;
import android.os.Parcelable;


public class APicture implements Parcelable {

    private int id;
    private int reportId;
    private int number = 0;
    private int subNumber = 0;
    private int receivedNumber = 0;
    private byte[] picture;

    public APicture() {
    }

    public int getId() {
        return id;
    }

    public APicture setId(int id) {
        this.id = id;
        return this;
    }

    public int getReportId() {
        return reportId;
    }

    public APicture setReportId(int reportId) {
        this.reportId = reportId;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public APicture setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getSubNumber() {
        return subNumber;
    }

    public APicture setSubNumber(int subNumber) {
        this.subNumber = subNumber;
        return this;
    }

    public int getReceivedNumber() {
        return receivedNumber;
    }

    public APicture setReceivedNumber(int receivedNumber) {
        this.receivedNumber = receivedNumber;
        return this;
    }

    public byte[] getPicture() {
        return picture;
    }

    public APicture setPicture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public static final Creator<APicture> CREATOR = new Creator<APicture>() {
        @Override
        public APicture createFromParcel(Parcel in) {
            return new APicture( in );
        }

        @Override
        public APicture[] newArray(int size) {
            return new APicture[size];
        }
    };


    protected APicture(Parcel in) {
        this.id = in.readInt();
        this.reportId = in.readInt();
        this.number = in.readInt();
        this.subNumber = in.readInt();
        this.receivedNumber = in.readInt();
        picture = new byte[in.readInt()];
        in.readByteArray( picture );
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( id );
        dest.writeInt( reportId );
        dest.writeInt( number );
        dest.writeInt( subNumber );
        dest.writeInt( receivedNumber );
        dest.writeInt( picture.length );
        dest.writeByteArray( picture );
    }
}
