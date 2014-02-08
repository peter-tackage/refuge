package com.moac.android.refuge.model;

import java.util.Collection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by amelysh on 08/02/14.
 */

@DatabaseTable(tableName = Country.TABLE_NAME)
public class Country extends PersistableObject {

    public static final String TABLE_NAME =  "countries";

    public static interface Columns extends PersistableObject.Columns {
        public static final String NAME_COLUMN = "NAME";
    }

    @DatabaseField(columnName = Columns.NAME_COLUMN, unique = true, canBeNull = false)
    private String mName;

    public String getName() { return mName; }
    public void setName(String _name) { mName = _name; }


    public static class CountryBuilder {
        Country mCountry;

        public CountryBuilder() {
            mCountry = new Country();
            // set the required field
            mCountry.setName("defaultRequiredGroupName");
        }

        // be careful this does not set the group id
        // if you insert a group created with this builder specifyin id x,
        // this will not necessarily be the group id after the insert...
        // so this withGroupId should only be used to set a known group id
        // for instance for setting a Member group property
        public CountryBuilder withId(int _id) {
            mCountry.setId(_id);
            return this;
        }

        public CountryBuilder withName(String name) {
            mCountry.setName(name);
            return this;
        }

        public Country build() { return mCountry; }
    }

}