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

    private String mCountryName;
    private long mTotalIntake;
    private int mColor;

    public RxCountryViewModel(final RefugeeDataStore dataStore, final Map<Long, Integer> colorMap, final long countryId) {
        this.countryId = countryId;
        this.dataStore = dataStore;
        this.colorMap = colorMap;
    }

    public long getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public int getColor() {
        return mColor;
    }

    public Long getTotalIntake() {
        return mTotalIntake;
    }

    public void subscribeToDataStore() {
        compositeSubscription.add(createCountryNameObservable().subscribe(new Action1<String>() {
            @Override public void call(String countryName) {
                mCountryName = countryName;
            }
        }));
        compositeSubscription.add(createTotalIntakeObservable().subscribe(new Action1<Long>() {
            @Override public void call(Long totalIntake) {
                mTotalIntake = totalIntake;
            }
        }));
        compositeSubscription.add(createCountryColorObservable().subscribe(new Action1<Integer>() {
            @Override public void call(Integer color) {
                mColor = color;
            }
        }));
    }

    public void unsubscribeFromDataStore() {
        compositeSubscription.clear();
    }

    private Observable<String> createCountryNameObservable() {
        return Observable.from(dataStore.getCountry(countryId))
                .map(new Func1<Country, String>() {
                    @Override
                    public String call(Country country) {
                        return country.getName();
                    }
                });
    }

    private Observable<Long> createTotalIntakeObservable() {
        return Observable.from(dataStore.getTotalRefugeeFlowTo(countryId));
    }

    private Observable<Integer> createCountryColorObservable() {
        return Observable.from(colorMap.get(countryId));
    }
}
