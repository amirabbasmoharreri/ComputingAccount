package com.abbasmoharreri.computingaccount.module;

import com.abbasmoharreri.computingaccount.database.DataBase;

public class AItem {


    private int id;
    private String name;
    public final static String PERSON = DataBase.TABLE_PERSON;
    public final static String WORK_NAME = DataBase.TABLE_WORK_NAME;
    private String className;
    private boolean condition = false;


    public int getId() {
        return id;
    }

    public AItem setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public AItem setClassName(String className) {
        this.className = className;
        return this;
    }

    public boolean getCondition() {
        return condition;
    }

    public AItem setCondition(boolean condition) {
        this.condition = condition;
        return this;
    }

    public String getDataBaseName() {
        if (className.equals( PERSON )) {
            return APerson.DATA_BASE_NAME;
        } else if (className.equals( WORK_NAME )) {
            return AWorkName.DATA_BASE_NAME;
        }
        return "";
    }


}
