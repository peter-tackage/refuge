package com.moac.android.refuge.database;

import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.Demography;
import com.moac.android.refuge.model.RefugeeFlow;

import java.util.ArrayList;
import java.util.List;

public class MockModelService implements ModelService {

    List<Country> mCountries;
    List<RefugeeFlow> mFlows;

    public MockModelService(List<Country> countrys,
                            List<RefugeeFlow> fLows) {
        mCountries = countrys;
        mFlows = fLows;
    }

    /*
     * Country
     */

    @Override
    public List<Country> getAllCountries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Country getCountry(long id) {
        for(Country c : mCountries) {
            if(c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public long createCountry(Country country) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateCountry(Country country) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCountry(long id) {
        throw new UnsupportedOperationException();
    }

    /*
     * Demography
     */

    @Override
    public List<Demography> getAllDemographics() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Demography getDemography(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long createDemography(Demography demography) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateDemography(Demography demography) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteDemography(long id) {
        throw new UnsupportedOperationException();
    }

    /*
     * Refugee Flow
     */

    @Override
    public List<RefugeeFlow> getAllRefugeeFlows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RefugeeFlow getRefugeeFlow(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long createRefugeeFlow(RefugeeFlow refugeeFlow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateRefugeeFlow(RefugeeFlow refugeeFlow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRefugeeFlow(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getTotalRefugeeFlowTo(long toCountryId) {
        long totalCount = 0;
        for (RefugeeFlow flow : mFlows) {
            if (flow.getToCountry().getId() == toCountryId) {
                totalCount += flow.getRefugeeCount();
            }
        }
        return totalCount;
    }

    @Override
    public long getTotalRefugeeFlowFrom(long countryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsFrom(long countryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsTo(long toCountryId) {
        List<RefugeeFlow> result = new ArrayList<RefugeeFlow>();
        for(RefugeeFlow flow : mFlows) {
            if(flow.getToCountry().getId() == toCountryId) {
                result.add(flow);
            }
        }
        return result;
    }
}
