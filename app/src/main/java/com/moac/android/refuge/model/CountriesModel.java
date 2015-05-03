package com.moac.android.refuge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class CountriesModel {

    private static final int[] FILL_COLORS = {0x660066cc, 0x66D6B331, 0x66663399, 0x55FF6600,
            0x66669900};

    private static final int[] STROKE_COLORS = {0xDD0066cc, 0xFFD6B331, 0xDD663399, 0xFFFF6600,
            0xDD669900};

    private List<DisplayedCountry> ids = new ArrayList<>();
    private BehaviorSubject<List<DisplayedCountry>> subject = BehaviorSubject.create();

    public Observable<List<DisplayedCountry>> getDisplayedCountries() {
        return subject;
    }

    public void add(Long id) {
        DisplayedCountry country = new DisplayedCountry(id, STROKE_COLORS[ids.size()]);
        ids.add(country);
        subject.onNext(ids);
    }

    public void clear() {
        ids.clear();
        subject.onNext(Collections.<DisplayedCountry>emptyList());
    }

}
