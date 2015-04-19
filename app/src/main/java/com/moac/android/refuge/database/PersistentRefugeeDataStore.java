package com.moac.android.refuge.database;

import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.SelectArg;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.Demography;
import com.moac.android.refuge.model.PersistableObject;
import com.moac.android.refuge.model.RefugeeFlow;

import java.sql.SQLException;
import java.util.List;

public class PersistentRefugeeDataStore implements RefugeeDataStore {

    static final String TAG = PersistentRefugeeDataStore.class.getSimpleName();

    private final DatabaseHelper mDbHelper;

    public PersistentRefugeeDataStore(DatabaseHelper helper) {
        mDbHelper = helper;
    }

    private <T extends PersistableObject> List<T> queryAll(Class<T> objClass) {
        return mDbHelper.queryAll(objClass);
    }

    private <T extends PersistableObject> T queryById(long id, Class<T> objClass) {
        return mDbHelper.queryById(id, objClass);
    }

    @SuppressWarnings("unchecked")
    private <T extends PersistableObject> T queryById(T obj) {
        return (T) mDbHelper.queryById(obj.getId(), obj.getClass());
    }

    private <T extends PersistableObject> long create(T obj) {
        return mDbHelper.create(obj, obj.getClass());
    }

    private <T extends PersistableObject> void update(T obj) {
        mDbHelper.update(obj, obj.getClass());
    }

    private <T extends PersistableObject> void delete(T obj) {
        mDbHelper.deleteById(obj.getId(), obj.getClass());
    }

    private <T extends PersistableObject> void delete(long id, Class<T> objClass) {
        mDbHelper.deleteById(id, objClass);
    }

    @Override
    public List<Country> getAllCountries() {
        return queryAll(Country.class);
    }

    @Override
    public Country getCountry(long id) {
        return queryById(id, Country.class);
    }

    @Override
    public long createCountry(Country country) {
        return create(country);
    }

    @Override
    public void updateCountry(Country country) {
        update(country);
    }

    @Override
    public void deleteCountry(long id) {
        delete(id, Country.class);
    }

    @Override
    public List<Demography> getAllDemographics() {
        return queryAll(Demography.class);
    }

    @Override
    public Demography getDemography(long id) {
        return queryById(id, Demography.class);
    }

    @Override
    public long createDemography(Demography demography) {
        return create(demography);
    }

    @Override
    public void updateDemography(Demography demography) {
        update(demography);
    }

    @Override
    public void deleteDemography(long id) {
        delete(id, Demography.class);
    }

    @Override
    public List<RefugeeFlow> getAllRefugeeFlows() {
        return queryAll(RefugeeFlow.class);
    }

    @Override
    public RefugeeFlow getRefugeeFlow(long id) {
        return queryById(id, RefugeeFlow.class);
    }

    @Override
    public long createRefugeeFlow(RefugeeFlow refugeeFlow) {
        return create(refugeeFlow);
    }

    @Override
    public void updateRefugeeFlow(RefugeeFlow refugeeFlow) {
        update(refugeeFlow);
    }

    @Override
    public void deleteRefugeeFlow(long id) {
        delete(id, RefugeeFlow.class);
    }

    @Override
    public long getTotalRefugeeFlowTo(long countryId) {
        return queryTotalRefugeeFlowTo(countryId);
    }

    @Override
    public long getTotalRefugeeFlowFrom(long countryId) {
        return queryTotalRefugeeFlowFrom(countryId);
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsFrom(long countryId) {
        return queryAllRefugeeFlowsFrom(countryId);
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsTo(long countryId) {
        return queryAllRefugeeFlowsTo(countryId);
    }

    @Override
    public Country getCountry(String countryName) {
        try {
            // Use SelectArg to ensure values are properly escaped
            // Refer - http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_3.html#index-select-arguments
            SelectArg selectArg = new SelectArg();
            selectArg.setValue(countryName);
            return mDbHelper.getDaoEx(Country.class).queryBuilder().where()
                    .like(Country.Columns.NAME_COLUMN, selectArg)
                    .queryForFirst();
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    /**
     * Bespoke queries - examples
     */
    private long queryTotalRefugeeFlowTo(long countryId) {
        String query = "select sum(" + RefugeeFlow.Columns.REFUGEE_COUNT_COLUMN + ") from " + RefugeeFlow.TABLE_NAME + " where " + RefugeeFlow.Columns.TO_COUNTRY_COLUMN + " = " + countryId;
        Log.d(TAG, query);
        GenericRawResults<String[]> rawResults;
        try {
            rawResults = mDbHelper.getDaoEx(RefugeeFlow.class).queryRaw(query);
            List<String[]> results = rawResults.getResults();
            // the results array should have 1 value
            String[] resultArray = results.get(0);
            return Long.parseLong(resultArray[0]);
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    private long queryTotalRefugeeFlowFrom(long countryId) {
        String query = "select sum(" + RefugeeFlow.Columns.REFUGEE_COUNT_COLUMN + ") from " + RefugeeFlow.TABLE_NAME + " where " + RefugeeFlow.Columns.FROM_COUNTRY_COLUMN + " = " + countryId;
        Log.d(TAG, query);

        GenericRawResults<String[]> rawResults = null;
        try {
            rawResults = mDbHelper.getDaoEx(RefugeeFlow.class).queryRaw(query);

            List<String[]> results = rawResults.getResults();
            // the results array should have 1 value
            String[] resultArray = results.get(0);
            return Long.parseLong(resultArray[0]);
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    private List<RefugeeFlow> queryAllRefugeeFlowsFrom(long countryId) {
        try {
            return mDbHelper.getDaoEx(RefugeeFlow.class).queryBuilder()
                    .where().eq(RefugeeFlow.Columns.FROM_COUNTRY_COLUMN, countryId)
                    .query();
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    private List<RefugeeFlow> queryAllRefugeeFlowsTo(long countryId) {
        try {
            return mDbHelper.getDaoEx(RefugeeFlow.class).queryBuilder()
                    .where().eq(RefugeeFlow.Columns.TO_COUNTRY_COLUMN, countryId)
                    .query();
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }
}