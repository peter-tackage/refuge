package com.moac.android.refuge.model;

import com.moac.android.refuge.adapter.CountryViewModel;

import java.util.List;

public class VisualEvent {

    private final List<CountryViewModel> mCountries;

    public VisualEvent(List<CountryViewModel> countries) {
        mCountries = countries;
    }

    public List<CountryViewModel> getCountries() {
        return mCountries;
    }
}
