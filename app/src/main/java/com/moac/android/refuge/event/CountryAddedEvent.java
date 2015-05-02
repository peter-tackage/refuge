package com.moac.android.refuge.event;

import com.moac.android.refuge.adapter.CountryViewModel;

public class CountryAddedEvent {

    private final CountryViewModel country;

    public CountryAddedEvent(CountryViewModel country) {
        this.country = country;
    }

    public CountryViewModel getCountry() {
        return country;
    }
}
