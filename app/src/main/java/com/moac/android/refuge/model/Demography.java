package com.moac.android.refuge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by amelysh on 08/02/14.
 */

@DatabaseTable(tableName = Demography.TABLE_NAME)
public class Demography extends PersistableObject {

    public static final String TABLE_NAME =  "demographies";

    public static interface Columns extends PersistableObject.Columns {
        public static final String COUNTRY_ID_COLUMN = "COUNTRY_ID";
        public static final String YEAR_COLUMN = "YEAR";
        public static final String POPULATION_COLUMN = "POPULATION_NUM";
        public static final String MIGRATION_COLUMN = "MIGRATION_NUM";
    }

    @DatabaseField(columnName = Columns.COUNTRY_ID_COLUMN, unique = false, canBeNull = false)
    private int mCountryId;

    public int getCountryId() { return mCountryId; }
    public void setCountryId(int _countryId) { mCountryId = _countryId; }

    @DatabaseField(columnName = Columns.YEAR_COLUMN, unique = false, canBeNull = false)
    private int mYear;

    public int getYear() { return mYear; }
    public void setYear(int _year) { mYear = _year; }

    @DatabaseField(columnName = Columns.POPULATION_COLUMN, unique = false, canBeNull = false)
    private long mPopulation;

    public long getPopulation() { return mPopulation; }
    public void setPopulation(long _population) { mPopulation = _population; }

    @DatabaseField(columnName = Columns.MIGRATION_COLUMN, unique = false, canBeNull = false)
    private long mMigration;

    public long getMigration() { return mMigration; }
    public void setMigration(long _migration) { mMigration = _migration; }


    public static class DemographyBuilder {
        Demography mDemography;

        public DemographyBuilder() {
            mDemography = new Demography();
            mDemography.setCountryId(-1);
            mDemography.setYear(-1);
            mDemography.setPopulation(-1);
            mDemography.setMigration(-1);
        }

        public DemographyBuilder withCountryId(int countryId) {
            mDemography.setCountryId(countryId);
            return this;
        }

        public DemographyBuilder withYear(int year) {
            mDemography.setYear(year);
            return this;
        }

        public DemographyBuilder withPopuplation(long population) {
            mDemography.setPopulation(population);
            return this;
        }

        public DemographyBuilder withMigration(long migration) {
            mDemography.setMigration(migration);
            return this;
        }

        public Demography build() { return mDemography; }
    }

}
