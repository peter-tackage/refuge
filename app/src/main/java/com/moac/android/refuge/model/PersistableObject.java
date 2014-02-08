package com.moac.android.refuge.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * In OrmLite, the foreign id references are done using an instance of
 * the class that corresponds to the foreign table.
 * <p/>
 * This can be confusing if getters are provided for these fields as
 * the returned object will not have all its fields set; only the _id
 * field is populated.
 * *
 * http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Foreign-Objects
 */
public abstract class PersistableObject {

    public static final long UNSET_ID = -1;
    public static final long UNSET_DATE = -1;

    @DatabaseField(columnName = Columns._ID, generatedId = true, unique = true, canBeNull = false)
    private long mId = UNSET_ID;

    public interface Columns {
        public static final String _ID = "_id";
    }

    public long getId() {
        return mId;
    }

    void setId(long _id) {
        mId = _id;
    }
}