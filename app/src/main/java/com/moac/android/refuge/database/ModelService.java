package com.moac.android.refuge.database;

import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.Demography;
import com.moac.android.refuge.model.RefugeeFlow;

import java.util.List;

public interface ModelService {

    /*
     * Country
     */

    List<Country> getAllCountries();

    Country getCountry(long id);

    long create(Country country);

    void update(Country country);

    void deleteCountry(long id);

    /*
     * Demography
     */

    List<Demography> getAllDemographics();

    Demography getDemography(long id);

    long create(Demography demography);

    void update(Demography demography);

    void deleteDemography(long id);

    /*
     * Refugee Flow
     */

    List<RefugeeFlow> getAllRefugeeFlows();

    RefugeeFlow getRefugeeFlow(long id);

    long create(RefugeeFlow refugeeFlow);

    void update(RefugeeFlow refugeeFlow);

    void deleteRefugeeFlow(long id);

    long getTotalRefugeeFlowTo(long countryId);

    long getTotalRefugeeFlowFrom(long countryId);

    List<RefugeeFlow> getRefugeeFlows(long countryId);

}
