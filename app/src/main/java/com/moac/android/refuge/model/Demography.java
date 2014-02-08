package com.moac.android.refuge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Demography.TABLE_NAME)
public class Demography extends PersistableObject {

    public static final String TABLE_NAME = "demographies";

    static interface Columns extends PersistableObject.Columns {
        public static final String COUNTRY_COLUMN = "COUNTRY";
        public static final String YEAR_COLUMN = "YEAR";
        public static final String POPULATION_COLUMN = "POPULATION";
        public static final String MIGRATION_COLUMN = "MIGRATION";
    }

    @DatabaseField(columnName = Columns.COUNTRY_COLUMN, foreign = true, canBeNull = false)
    private Country mCountry;

    @DatabaseField(columnName = Columns.YEAR_COLUMN, canBeNull = false)
    private int mYear;

    @DatabaseField(columnName = Columns.POPULATION_COLUMN, canBeNull = false)
    private long mPopulation;

    @DatabaseField(columnName = Columns.MIGRATION_COLUMN, canBeNull = false)
    private long mMigration;

    public Country getCountry() { return mCountry;}
    public void setCountry(Country country) { mCountry = country; }

    public int getYear() { return mYear; }
    public void setYear(int _year) { mYear = _year;  }

    public double getPopulation() {
        return mPopulation;
    }
    public void setPopulation(long _population) {
        mPopulation = _population;
    }

    public double getMigration() { return mMigration; }
    public void setMigration(long _migration) {
        mMigration = _migration;
    }

}
