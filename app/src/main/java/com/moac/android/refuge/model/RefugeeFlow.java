package com.moac.android.refuge.model;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by amelysh on 08/02/14.
 */
public class RefugeeFlow extends PersistableObject {

    public static final String TAG = RefugeeFlow.class.getSimpleName();

    public static final String TABLE_NAME =  "refugeeFlows";

    public static interface Columns extends PersistableObject.Columns {
        public static final String FROM_COUNTRY_ID_COLUMN = "FROM_COUNTRY_ID";
        public static final String TO_COUNTRY_ID_COLUMN = "TO_COUNTRY_ID";
        public static final String YEAR_COLUMN = "YEAR";
        public static final String REFUGEE_NUM_COLUMN = "REFUGEE_NUM";
    }

    @DatabaseField(columnName = Columns.FROM_COUNTRY_ID_COLUMN, unique = false, canBeNull = false)
    private int mFromCountryId;

    public int getFromCountryId() { return mFromCountryId; }
    public void setFromCountryId(int _fromCountryId) { mFromCountryId = _fromCountryId; }

    @DatabaseField(columnName = Columns.TO_COUNTRY_ID_COLUMN, unique = false, canBeNull = false)
    private int mToCountryId;

    public int getToCountryId() { return mToCountryId; }
    public void setToCountryId(int _toCountryId) { mToCountryId = _toCountryId; }

    @DatabaseField(columnName = Columns.YEAR_COLUMN, unique = false, canBeNull = false)
    private int mYear;

    public int getYear() { return mYear; }
    public void setYear(int _year) { mYear = _year; }

    @DatabaseField(columnName = Columns.REFUGEE_NUM_COLUMN, unique = false, canBeNull = false)
    private long mRefugeeNum;

    public long getRefugeeNum() { return mRefugeeNum; }
    public void setRefugeeNum(long _refugeeNum) { mRefugeeNum = _refugeeNum; }

    public String getStringDescription() {
        return "Year: " + mYear + " FromCountryId: " + mFromCountryId + " ToCountryId: " + mToCountryId + " RefugeesNum: " + mRefugeeNum;
    }

    public static class RefugeeFlowBuilder {
        RefugeeFlow mRefugeeFlow;

        public RefugeeFlowBuilder() {
            mRefugeeFlow = new RefugeeFlow();
            mRefugeeFlow.setFromCountryId(-1);
            mRefugeeFlow.setToCountryId(-1);
            mRefugeeFlow.setYear(-1);
            mRefugeeFlow.setRefugeeNum(-1);
        }

        public RefugeeFlowBuilder withFromCountryId(int fromCountryId) {
            mRefugeeFlow.setFromCountryId(fromCountryId);
            return this;
        }

        public RefugeeFlowBuilder withToCountryId(int toCountryId) {
            mRefugeeFlow.setToCountryId(toCountryId);
            return this;
        }

        public RefugeeFlowBuilder withYear(int year) {
            mRefugeeFlow.setYear(year);
            return this;
        }

        public RefugeeFlowBuilder withRefugeeNum(long refugeeNum) {
            mRefugeeFlow.setRefugeeNum(refugeeNum);
            return this;
        }

        public RefugeeFlow build() { return mRefugeeFlow; }
    }
}
