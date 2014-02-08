package com.moac.android.refuge.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.Demography;
import com.moac.android.refuge.model.PersistableObject;
import com.moac.android.refuge.model.RefugeeFlow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "refuge.db";
    private static final int DATABASE_VERSION = 1;

    protected Class[] PERSISTABLE_OBJECTS;

    private final Map<Class<? extends PersistableObject>, Dao<? extends PersistableObject, Long>> daos =
            new HashMap<Class<? extends PersistableObject>, Dao<? extends PersistableObject, Long>>();

    private final Map<Class<? extends PersistableObject>, DatabaseTableConfig<? extends PersistableObject>> tableConfigs =
            new HashMap<Class<? extends PersistableObject>, DatabaseTableConfig<? extends PersistableObject>>();

    // FIXME Add the classes into the PERSISTABLE_OBJECTS
    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        PERSISTABLE_OBJECTS = new Class[]{Country.class, Demography.class, RefugeeFlow.class};
    }

    // Testing only?
    protected DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        createTables(db, cs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        // Do nothing
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void close() {
        super.close();
        daos.clear();
        tableConfigs.clear();
    }

    @SuppressWarnings("unchecked")
    public <T extends PersistableObject> Dao<T, Long> getDaoEx(Class<T> objClass) {
        Dao<T, Long> result;
        if (daos.containsKey(objClass)) {
            result = (Dao<T, Long>) daos.get(objClass);
        } else {
            try {
                result = getDao(objClass);
            } catch (java.sql.SQLException e) {
                throw new SQLException(e.getMessage());
            }
            daos.put(objClass, result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void createTables(SQLiteDatabase db, ConnectionSource cs) {
        for (Class<? extends PersistableObject> objClass : PERSISTABLE_OBJECTS) {
            createTable(objClass, cs);
        }
    }

    private void createTable(Class<? extends PersistableObject> objClass, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, objClass);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public <T extends PersistableObject> List<T> queryAll(Class<T> objClass) {
        List<T> entity;
        try {
            entity = getDaoEx(objClass).queryForAll();
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return entity;
    }

    public <T extends PersistableObject> T queryById(long id, Class<T> objClass) {
        T entity;
        try {
            entity = getDaoEx(objClass).queryForId(id);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return entity;
    }

    public <T extends PersistableObject> long create(PersistableObject entity, Class<T> objClass) {
        long id = PersistableObject.UNSET_ID;
        try {
            if (getDaoEx(objClass).create(objClass.cast(entity)) == 1) {
                id = entity.getId();
            }
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return id;
    }

    public <T extends PersistableObject> void update(PersistableObject entity, Class<T> objClass) {
        try {
            int count = getDaoEx(objClass).update(objClass.cast(entity));
            assert (count == 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public <T extends PersistableObject> void deleteById(long id, Class<T> objClass) {
        try {
            int count = getDaoEx(objClass).deleteById(id);
            assert (count == 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
}