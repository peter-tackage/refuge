package com.moac.android.refuge.database;

import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.Demography;
import com.moac.android.refuge.model.RefugeeFlow;

import java.util.List;

public class MockModelService implements ModelService{

    /*
     * Country
     */

    @Override
    public List<Country> getAllCountries() {
        return null;
    }

    @Override
    public Country getCountry(long id) {
        return null;
    }

    @Override
    public long createCountry(Country country) {
        return 0;
    }

    @Override
    public void updateCountry(Country country) {

    }

    @Override
    public void deleteCountry(long id) {

    }

    /*
     * Demography
     */

    @Override
    public List<Demography> getAllDemographics() {
        return null;
    }

    @Override
    public Demography getDemography(long id) {
        return null;
    }

    @Override
    public long createDemography(Demography demography) {
        return 0;
    }

    @Override
    public void updateDemography(Demography demography) {

    }

    @Override
    public void deleteDemography(long id) {

    }

    /*
     * Refugee Flow
     */

    @Override
    public List<RefugeeFlow> getAllRefugeeFlows() {
        return null;
    }

    @Override
    public RefugeeFlow getRefugeeFlow(long id) {
        return null;
    }

    @Override
    public long createRefugeeFlow(RefugeeFlow refugeeFlow) {
        return 0;
    }

    @Override
    public void updateRefugeeFlow(RefugeeFlow refugeeFlow) {

    }

    @Override
    public void deleteRefugeeFlow(long id) {

    }

    @Override
    public long getTotalRefugeeFlowTo(long countryId) {
        return 0;
    }

    @Override
    public long getTotalRefugeeFlowFrom(long countryId) {
        return 0;
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsFrom(long countryId) {
//        List<RefugeeFlow> flows = new ArrayList<RefugeeFlow>();
//        RefugeeFlow af = new RefugeeFlow();
//        af.setRefugeeCount(230);
//
//
//        RefugeeFlow ir = new RefugeeFlow();
//        ir.setRefugeeCount(120);
//
//        RefugeeFlow iq = new RefugeeFlow();
//        iq.setRefugeeCount(800);
//
//        flows.add(af);
//        flows.add(ir);
//        flows.add(iq);
//
//        return flows;
        return null;
    }

    @Override
    public List<RefugeeFlow> getRefugeeFlowsTo(long countryId) {
        return null;
    }
}
