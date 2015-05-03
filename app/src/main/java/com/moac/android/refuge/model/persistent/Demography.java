package com.moac.android.refuge.model.persistent;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Demography.TABLE_NAME)
public class Demography extends PersistableObject {

    public static final String TABLE_NAME = "demographies";

    public static interface Columns extends PersistableObject.Columns {
        public static final String COUNTRY_COLUMN = "COUNTRY";
        public static final String YEAR_COLUMN = "YEAR";
        public static final String POPULATION_COLUMN = "POPULATION";
        public static final String MIGRATION_COLUMN = "MIGRATION";
    }

    @DatabaseField(columnName = Columns.COUNTRY_COLUMN, foreign = true, canBeNull = false)
    private Country country;

    @DatabaseField(columnName = Columns.YEAR_COLUMN, canBeNull = false)
    private int year;

    @DatabaseField(columnName = Columns.POPULATION_COLUMN, canBeNull = false)
    private long population;

    @DatabaseField(columnName = Columns.MIGRATION_COLUMN, canBeNull = false)
    private long migration;

    public Demography(Country country) {
        this.country = country;
    }

    // Required by ORMLite
    public Demography() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public double getPopulation() {
        return population;
    }

}
