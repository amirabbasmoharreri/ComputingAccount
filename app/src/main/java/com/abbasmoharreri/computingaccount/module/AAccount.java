package com.abbasmoharreri.computingaccount.module;

public class AAccount {

    public final static String ACCOUNT_WALLET = "Wallet";
    public final static String ACCOUNT_CARD = "Card";
    public final static String[] accountNames = {"Card", "Wallet"};

    private int id;
    private String name;
    private int remained;
    private String accountNumber;

    public int getRemained() {
        return remained;
    }

    public AAccount setRemained(int remained) {
        this.remained = remained;
        return this;
    }


    public int getId() {
        return id;
    }

    public AAccount setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AAccount setName(String name) {
        this.name = name;
        return this;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public AAccount setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
}
