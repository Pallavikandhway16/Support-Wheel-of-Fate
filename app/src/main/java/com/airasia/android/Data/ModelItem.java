package com.airasia.android.Data;

import java.io.Serializable;

public class ModelItem implements Serializable {
    private String mEmpId;
    private String mEmpName;
    private String mDay;
    private String mShift;


    public ModelItem(String empID, String empName) {
        mEmpId = empID;
        mEmpName = empName;
    }

    public ModelItem(String empID, String empName, String day, String shift) {
        mEmpId = empID;
        mEmpName = empName;
        mDay = day;
        mShift = shift;
    }

    public String getDay() {
        return mDay;
    }

    public String getShift() {
        return mShift;
    }

    public String getEmpId() {
        return mEmpId;
    }

    public String getEmpName() {
        return mEmpName;
    }
}
