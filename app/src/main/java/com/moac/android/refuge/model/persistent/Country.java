package com.moac.android.refuge.model.persistent;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Country.TABLE_NAME)
public class Country extends PersistableObject {

    public static final String TABLE_NAME = "countries";

    public static interface Columns extends PersistableObject.Columns {
        public static final String NAME_COLUMN = "NAME";
        public static final String LONGITUDE_COLUMN = "LONGITUDE";
        public static final String LATITUDE_COLUMN = "LATITUDE";
    }

    @DatabaseField(columnName = Columns.NAME_COLUMN, unique = true, canBeNull = false)
    private String name;

    @DatabaseField(columnName = Columns.LONGITUDE_COLUMN, canBeNull = false)
    private double longitude;

    @DatabaseField(columnName = Columns.LATITUDE_COLUMN, canBeNull = false)
    private double latitude;

    @ForeignCollectionField(eager = false)
    private java.util.Collection<RefugeeFlow> mRefugeeFlows;

    public Country(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Required by ORMLite
    public Country() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

}