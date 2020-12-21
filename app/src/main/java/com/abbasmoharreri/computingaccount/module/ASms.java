package com.abbasmoharreri.computingaccount.module;

public class ASms {
    private String id;
    private String address;
    private String msg;
    private String readState; //"0" for have not read sms and "1" for have read sms
    private String time;
    private String folderName;
    private String price;
    private String type;
    private String account;
    private String bankName;


    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getMsg() {
        return msg;
    }

    public String getReadState() {
        return readState;
    }

    public String getTime() {
        return time;
    }

    public String getFolderName() {
        return folderName;
    }


    public ASms setId(String id) {
        this.id = id;
        return this;
    }

    public ASms setAddress(String address) {
        this.address = address;
        return this;
    }

    public ASms setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ASms setReadState(String readState) {
        this.readState = readState;
        return this;
    }

    public ASms setTime(String time) {
        this.time = time;
        return this;
    }

    public ASms setFolderName(String folderName) {
        this.folderName = folderName;
        return this;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
