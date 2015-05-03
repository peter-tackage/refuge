package com.moac.android.refuge.adapter;

import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.model.Country;

import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class RxCountryViewModel {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    private final RefugeeDataStore dataStore;
    private final Map<Long, Integer> colorMap;
    private final long countryId;

    private String countryName;
    private long totalIntake;
    private int color;

    public RxCountryViewModel(final RefugeeDataStore dataStore, final Map<Long, Integer> colorMap, final long countryId) {
        this.countryId = countryId;
        this.dataStore = dataStore;
        this.colorMap = colorMap;
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
                RxCountryViewModel.this.countryName = countryName;
            }
        }));
        compositeSubscription.add(createTotalIntakeObservable().subscribe(new Action1<Long>() {
            @Override public void call(Long totalIntake) {
                RxCountryViewModel.this.totalIntake = totalIntake;
            }
        }));
        compositeSubscription.add(createCountryColorObservable().subscribe(new Action1<Integer>() {
            @Override public void call(Integer color) {
                RxCountryViewModel.this.color = color;
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
        return Observable.just(colorMap.get(countryId));
    }
}
