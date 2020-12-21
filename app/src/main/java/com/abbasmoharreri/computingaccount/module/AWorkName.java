package com.abbasmoharreri.computingaccount.module;

import com.abbasmoharreri.computingaccount.database.DataBase;

public class AWorkName extends AItem {

    final static String DATA_BASE_NAME = DataBase.TABLE_WORK_NAME;
    private String className = WORK_NAME;

    @Override
    public String getClassName() {
        return className;
    }

}
