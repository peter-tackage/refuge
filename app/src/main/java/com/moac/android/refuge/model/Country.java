package com.moac.android.refuge.model;

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

    public Country(String name) {
        mName = name;
    }

    // Required by ORMLite
    public Country() {}

    @DatabaseField(columnName = Columns.NAME_COLUMN, unique = true, canBeNull = false)
    private String mName;

    @DatabaseField(columnName = Columns.LONGITUDE_COLUMN, canBeNull = false)
    private double mLongitude;

    @DatabaseField(columnName = Columns.LATITUDE_COLUMN, canBeNull = false)
    private double mLatitude;

    @ForeignCollectionField(eager = false)
    private java.util.Collection<RefugeeFlow> mRefugeeFlows;

    public String getName() { return mName; }
    public void setName(String _name) { mName = _name; }

    public double getLongitude() { return mLongitude;}
    public void setLongitude(double longitude) { mLongitude = mLongitude; }

    public double getLatitude() { return mLatitude; }
    public void setLatitude(double latitude) { mLatitude = mLatitude; }

    public void setLatLng(double latitude, double longitude) {
        mLongitude = longitude;
        mLatitude = latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }

}