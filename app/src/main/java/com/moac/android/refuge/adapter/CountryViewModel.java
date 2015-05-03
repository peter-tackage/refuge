package com.moac.android.refuge.adapter;

import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.model.persistent.Country;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class CountryViewModel {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    private final RefugeeDataStore dataStore;
    private final long countryId;

    private String countryName;
    private long totalIntake;
    private int color;

    public CountryViewModel(final RefugeeDataStore dataStore, final long countryId, int color) {
        this.dataStore = dataStore;
        this.countryId = countryId;
        this.color = color;
    }

    public long getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getColor() {
        return color;
    }

    public Long getTotalIntake() {
        return totalIntake;
    }

    public void subscribeToDataStore() {
        compositeSubscription.add(createCountryNameObservable().subscribe(new Action1<String>() {
            @Override public void call(String countryName) {
                CountryViewModel.this.countryName = countryName;
            }
        }));
        compositeSubscription.add(createTotalIntakeObservable().subscribe(new Action1<Long>() {
            @Override public void call(Long totalIntake) {
                CountryViewModel.this.totalIntake = totalIntake;
            }
        }));
        compositeSubscription.add(createCountryColorObservable().subscribe(new Action1<Integer>() {
            @Override public void call(Integer color) {
                CountryViewModel.this.color = color;
            }
        }));
    }

    public void unsubscribeFromDataStore() {
        compositeSubscription.clear();
    }

    private Observable<String> createCountryNameObservable() {
        return Observable.just(dataStore.getCountry(countryId))
                .map(new Func1<Country, String>() {
                    @Override
                    public String call(Country country) {
                        return country.getName();
                    }
                });
    }

    private Observable<Long> createTotalIntakeObservable() {
        return Observable.just(dataStore.getTotalRefugeeFlowTo(countryId));
    }

    private Observable<Integer> createCountryColorObservable() {
        return Observable.just(color);
    }
}
