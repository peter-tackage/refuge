package com.moac.android.refuge.inject;

import android.util.Log;

import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.activity.MainActivity;
import com.moac.android.refuge.database.DatabaseHelper;
import com.moac.android.refuge.database.MockModelService;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module(injects = {RefugeApplication.class, MainActivity.class})
public class DebugAppModule {
    private static final String TAG = DebugAppModule.class.getSimpleName();

    private final RefugeApplication mApplication;

    public DebugAppModule(RefugeApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    ModelService provideDatabase() {
        Log.i(TAG, "Providing debug database");
        return makeMock();
    }

    private MockModelService makeMock() {
        List<Country> countryList = new ArrayList<Country>();
        List<RefugeeFlow> refugeeFlowList = new ArrayList<RefugeeFlow>();

        Country au = new Country("Australia", -27d, 133d);
        au.setId(0);
        countryList.add(au);

        Country af = new Country("Afghanistan", 33d, 65d);
        af.setId(1);
        countryList.add(af);

        Country iq = new Country("Iraq", 33d, 45d);
        iq.setId(2);
        countryList.add(iq);

        Country sy = new Country("Syria", 30d, 40d);
        sy.setId(3);
        countryList.add(sy);

        Country sl = new Country("Sri Lanka", 15d, 80d);
        sl.setId(4);
        countryList.add(sl);

        Country sw = new Country("Sweden", 59.3294, 18.0686);
        sw.setId(5);
        countryList.add(sw);

        Country es = new Country("Spain", 40d, -5d);
        es.setId(6);
        countryList.add(es);

        Country mx = new Country("Mexico", 15d, -80d);
        mx.setId(7);
        countryList.add(mx);

        Country cn = new Country("China", 35d, 110d);
        cn.setId(8);
        countryList.add(cn);

        // AU
        {
            RefugeeFlow af2au = new RefugeeFlow(af, au);
            af2au.setRefugeeCount(1000);
            af2au.setYear(2012);
            refugeeFlowList.add(af2au);

            RefugeeFlow iq2au = new RefugeeFlow(iq, au);
            iq2au.setRefugeeCount(750);
            iq2au.setYear(2012);
            refugeeFlowList.add(iq2au);

            RefugeeFlow sy2au = new RefugeeFlow(sy, au);
            sy2au.setRefugeeCount(550);
            sy2au.setYear(2012);
            refugeeFlowList.add(sy2au);

            RefugeeFlow sl2au = new RefugeeFlow(sl, au);
            sl2au.setRefugeeCount(250);
            sl2au.setYear(2012);
            refugeeFlowList.add(sl2au);

            RefugeeFlow s22au = new RefugeeFlow(sw, au);
            s22au.setRefugeeCount(1500);
            s22au.setYear(2012);
            refugeeFlowList.add(s22au);
        }

        // ES
        {
            RefugeeFlow af2es = new RefugeeFlow(af, es);
            af2es.setRefugeeCount(1000);
            af2es.setYear(2012);
            refugeeFlowList.add(af2es);

            RefugeeFlow iqes = new RefugeeFlow(iq, es);
            iqes.setRefugeeCount(2000);
            iqes.setYear(2012);
            refugeeFlowList.add(iqes);

            RefugeeFlow sl2es = new RefugeeFlow(sl, es);
            sl2es.setRefugeeCount(250);
            sl2es.setYear(2012);
            refugeeFlowList.add(sl2es);

            RefugeeFlow cn2es = new RefugeeFlow(cn, es);
            cn2es.setRefugeeCount(800);
            cn2es.setYear(2012);
            refugeeFlowList.add(cn2es);

            RefugeeFlow mx2es = new RefugeeFlow(mx, es);
            mx2es.setRefugeeCount(1200);
            mx2es.setYear(2012);
            refugeeFlowList.add(mx2es);
        }

        return new MockModelService(countryList, refugeeFlowList);
    }

}

