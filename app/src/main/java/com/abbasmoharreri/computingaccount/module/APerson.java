package com.abbasmoharreri.computingaccount.module;

import com.abbasmoharreri.computingaccount.database.DataBase;

public class APerson extends AItem {


    final static String DATA_BASE_NAME = DataBase.TABLE_PERSON;
    private String className = PERSON;

    @Override
    public String getClassName() {
        return className;
    }


}
