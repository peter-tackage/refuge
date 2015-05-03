package com.moac.android.refuge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

public class CountriesModel {

    private List<Long> ids = new ArrayList<>();
    private Subject<List<Long>, List<Long>> subject = BehaviorSubject.create();

    public Observable<List<Long>> getDisplayedCountries() {
        return subject;
    }

    public void add(Long id) {
        ids.add(id);
        subject.onNext(ids);
    }

    public void clear() {
        ids.clear();
        subject.onNext(Collections.<Long>emptyList());
    }

}
