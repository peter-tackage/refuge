package com.moac.android.refuge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = RefugeeFlow.TABLE_NAME)
public class RefugeeFlow extends PersistableObject {

    public static final String TABLE_NAME =  "refugeeFlows";

    public static interface Columns extends PersistableObject.Columns {
        public static final String FROM_COUNTRY_COLUMN = "FROM_COUNTRY";
        public static final String TO_COUNTRY_COLUMN = "TO_COUNTRY";
        public static final String YEAR_COLUMN = "YEAR";
        public static final String REFUGEE_COUNT_COLUMN = "REFUGEE_COUNT";
    }

    public RefugeeFlow(Country fromCountry, Country toCountry) {
        mFromCountry =fromCountry;
        mToCountry = toCountry;
    }

    // Required by ORMLite
    public RefugeeFlow() {}

    @DatabaseField(columnName = Columns.FROM_COUNTRY_COLUMN, canBeNull = false)
    private Country mFromCountry;

    @DatabaseField(columnName = Columns.TO_COUNTRY_COLUMN, canBeNull = false)
    private Country mToCountry;

    @DatabaseField(columnName = Columns.YEAR_COLUMN, canBeNull = false)
    private int mYear;

    @DatabaseField(columnName = Columns.REFUGEE_COUNT_COLUMN, canBeNull = false)
    private long mRefugeeCount;

    public Country getFromCountry() { return mFromCountry; }
    public Country getToCountry() { return mToCountry; }

    public int getYear() { return mYear; }
    public void setYear(int _year) { mYear = _year; }

    public long getRefugeeCount() { return mRefugeeCount; }
    public void setRefugeeCount(long _refugeCount) { mRefugeeCount = _refugeCount; }

}
