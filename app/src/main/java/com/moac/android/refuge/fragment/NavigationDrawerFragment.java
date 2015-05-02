package com.moac.android.refuge.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moac.android.refuge.R;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.adapter.CountryViewBinder;
import com.moac.android.refuge.adapter.RxCountryAdapter;
import com.moac.android.refuge.adapter.RxCountryViewModel;
import com.moac.android.refuge.database.RefugeeDataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private NavigationDrawerCallbacks callbacks;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private View fragmentContainerView;
    private ListView drawerListView;
    private List<RxCountryViewModel> viewModels = Collections.emptyList();

    private FragmentContainer fragmentContainer;
    private Subscription displayCountrySub;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

        // Inject dependencies
        RefugeApplication.from(this).inject(this);

    }

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
    public void onResume() {
        super.onResume();
        displayCountrySub = fragmentContainer.getDisplayedCountries()
                .map(new Func1<List<Long>, List<RxCountryViewModel>>() {
                    @Override
                    public List<RxCountryViewModel> call(List<Long> countryIds) {
                        return createDataModel(countryIds
                                , fragmentContainer.getColorMap()
                                , fragmentContainer.getRefugeeDataStore());
                    }
                }).subscribe(new Action1<List<RxCountryViewModel>>() {
                    @Override
                    public void call(List<RxCountryViewModel> viewModels) {
                        NavigationDrawerFragment.this.viewModels = viewModels;
                        subscribeViewModels();
                        drawerListView.setAdapter(new RxCountryAdapter(getActivity(),
                                NavigationDrawerFragment.this.viewModels,
                                new CountryViewBinder(R.layout.country_info_row)));
                    }
                });
    }

    @Override
    public void onPause() {
        displayCountrySub.unsubscribe();
        unsubscribeViewModels();
        super.onPause();
    }

    private void selectItem(int position, boolean isSelected) {
        if (drawerListView != null) {
            drawerListView.setItemChecked(position, true);
        }
        if (callbacks != null) {
            callbacks.onCountryItemSelected(position, isSelected);
        }
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                NavigationDrawerFragment.this.drawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
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
        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        this.drawerLayout.setDrawerListener(drawerToggle);
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
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (drawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onCountryItemSelected(long countryId, boolean isSelected);
    }

    public static interface FragmentContainer {
        Observable<List<Long>> getDisplayedCountries();

        Map<Long, Integer> getColorMap();

        RefugeeDataStore getRefugeeDataStore();
    }

    private static List<RxCountryViewModel> createDataModel(List<Long> countryIds,
                                                            Map<Long, Integer> colorMap,
                                                            RefugeeDataStore refugeeDataStore) {
        List<RxCountryViewModel> models = new ArrayList<RxCountryViewModel>();
        for (Long id : countryIds) {
            RxCountryViewModel vm = new RxCountryViewModel(refugeeDataStore, colorMap, id);
            models.add(vm);
        }
        return models;
    }

    public void subscribeViewModels() {
        for (RxCountryViewModel vm : viewModels) {
            vm.subscribeToDataStore();
        }
    }

    private void unsubscribeViewModels() {
        for (RxCountryViewModel vm : viewModels) {
            vm.unsubscribeFromDataStore();
        }
    }

}
