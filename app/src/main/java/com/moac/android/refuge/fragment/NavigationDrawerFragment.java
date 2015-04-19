package com.moac.android.refuge.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
public class NavigationDrawerFragment extends Fragment {


    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private View mFragmentContainerView;
    private ListView mDrawerListView;
    private List<RxCountryViewModel> mViewModels = Collections.emptyList();

    private FragmentContainer mFragmentContainer;
    private Subscription mDisplayCountrySub;

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

        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCallbacks.onCountryItemSelected(id, view.isSelected());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing selected
            }
        });

        return mDrawerListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisplayCountrySub = mFragmentContainer.getDisplayedCountries()
                .map(new Func1<List<Long>, List<RxCountryViewModel>>() {
                    @Override
                    public List<RxCountryViewModel> call(List<Long> countryIds) {
                        return createDataModel(countryIds
                                , mFragmentContainer.getColorMap()
                                , mFragmentContainer.getRefugeeDataStore());
                    }
                }).subscribe(new Action1<List<RxCountryViewModel>>() {
                    @Override
                    public void call(List<RxCountryViewModel> viewModels) {
                        mViewModels = viewModels;
                        subscribeViewModels();
                        mDrawerListView.setAdapter(new RxCountryAdapter(getActivity(),
                                mViewModels,
                                new CountryViewBinder(R.layout.country_info_row)));
                    }
                });
    }

    @Override
    public void onPause() {
        mDisplayCountrySub.unsubscribe();
        unsubscribeViewModels();
        super.onPause();
    }

    private void selectItem(int position, boolean isSelected) {
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mCallbacks != null) {
            mCallbacks.onCountryItemSelected(position, isSelected);
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
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
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
            mFragmentContainer = (FragmentContainer) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        mFragmentContainer = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
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
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
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
        for (RxCountryViewModel vm : mViewModels) {
            vm.subscribeToDataStore();
        }
    }

    private void unsubscribeViewModels() {
        for (RxCountryViewModel vm : mViewModels) {
            vm.unsubscribeFromDataStore();
        }
    }

}
