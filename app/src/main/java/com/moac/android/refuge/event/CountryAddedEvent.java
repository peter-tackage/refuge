package com.moac.android.refuge.event;

import com.moac.android.refuge.adapter.CountryViewModel;

public class CountryAddedEvent {

    private final CountryViewModel mCountry;

    public CountryAddedEvent(CountryViewModel country) {
        mCountry = country;
    }

    public CountryViewModel getCountry() {
        return mCountry;
    }
}
