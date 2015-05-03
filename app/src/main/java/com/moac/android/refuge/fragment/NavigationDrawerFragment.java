package com.moac.android.refuge.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moac.android.refuge.R;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.adapter.CountryAdapter;
import com.moac.android.refuge.adapter.CountryViewBinder;
import com.moac.android.refuge.adapter.CountryViewModel;
import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.model.DisplayedCountry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NavigationDrawerFragment extends android.support.v4.app.Fragment {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private NavigationDrawerCallbacks callbacks;

    private ActionBarDrawerToggle drawerToggle;
    private View fragmentContainerView;
    private ListView drawerListView;
    private List<CountryViewModel> viewModels = Collections.emptyList();

    private FragmentContainer fragmentContainer;
    private Subscription displayCountrySubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        drawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        drawerListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callbacks.onCountryItemSelected(id, view.isSelected());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing selected
            }
        });

        return drawerListView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefugeApplication.from(this).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayCountrySubscription = fragmentContainer.getDisplayedCountries()
                .map(new Func1<List<DisplayedCountry>, List<CountryViewModel>>() {
                    @Override
                    public List<CountryViewModel> call(List<DisplayedCountry> countries) {
                        return createDataModel(countries,
                                fragmentContainer.getRefugeeDataStore());
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CountryViewModel>>() {
                    @Override
                    public void call(List<CountryViewModel> viewModels) {
                        NavigationDrawerFragment.this.viewModels = viewModels;
                        subscribeViewModels();
                        drawerListView.setAdapter(new CountryAdapter(getActivity(),
                                NavigationDrawerFragment.this.viewModels,
                                new CountryViewBinder(R.layout.country_info_row)));
                    }
                });
    }

    @Override
    public void onPause() {
        displayCountrySubscription.unsubscribe();
        unsubscribeViewModels();
        super.onPause();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        fragmentContainerView = getActivity().findViewById(fragmentId);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (NavigationDrawerCallbacks) activity;
            fragmentContainer = (FragmentContainer) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
        fragmentContainer = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onCountryItemSelected(long countryId, boolean isSelected);
    }

    public static interface FragmentContainer {
        Observable<List<DisplayedCountry>> getDisplayedCountries();

        RefugeeDataStore getRefugeeDataStore();
    }

    private static List<CountryViewModel> createDataModel(List<DisplayedCountry> countries,
                                                          RefugeeDataStore refugeeDataStore) {
        List<CountryViewModel> models = new ArrayList<>();
        for (DisplayedCountry country : countries) {
            CountryViewModel viewModel = new CountryViewModel(refugeeDataStore, country.getId(), country.getColor());
            models.add(viewModel);
        }
        return models;
    }

    private void subscribeViewModels() {
        for (CountryViewModel vm : viewModels) {
            vm.subscribeToDataStore();
        }
    }

    private void unsubscribeViewModels() {
        for (CountryViewModel vm : viewModels) {
            vm.unsubscribeFromDataStore();
        }
    }

}
