package com.moac.android.refuge.database;

import com.moac.android.refuge.model.persistent.Country;
import com.moac.android.refuge.model.persistent.Demography;
import com.moac.android.refuge.model.persistent.RefugeeFlow;

import java.util.List;

public interface RefugeeDataStore {

    /*
     * Country
     */

    List<Country> getAllCountries();

    Country getCountry(long id);

    long createCountry(Country country);

    void updateCountry(Country country);

    void deleteCountry(long id);

    /*
     * Demography
     */

    List<Demography> getAllDemographics();

    Demography getDemography(long id);

    long createDemography(Demography demography);

    void updateDemography(Demography demography);

    void deleteDemography(long id);

    /*
     * Refugee Flow
     */

    List<RefugeeFlow> getAllRefugeeFlows();

    RefugeeFlow getRefugeeFlow(long id);

    long createRefugeeFlow(RefugeeFlow refugeeFlow);

    void updateRefugeeFlow(RefugeeFlow refugeeFlow);

    void deleteRefugeeFlow(long id);

    long getTotalRefugeeFlowTo(long countryId);

    long getTotalRefugeeFlowFrom(long countryId);

    List<RefugeeFlow> getRefugeeFlowsFrom(long countryId);

    List<RefugeeFlow> getRefugeeFlowsTo(long countryId);

    Country getCountry(String countryName);
}
