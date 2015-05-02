package com.moac.android.refuge.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.moac.android.refuge.R;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.fragment.NavigationDrawerFragment;
import com.moac.android.refuge.importer.DataFileImporter;
import com.moac.android.refuge.importer.LoadDataRunnable;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.DisplayedStore;
import com.moac.android.refuge.util.DoOnce;
import com.moac.android.refuge.util.Visualizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        NavigationDrawerFragment.FragmentContainer {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOAD_DATA_TASK_TAG = "LOAD_DATA_TASK";
    private static final String DISPLAYED_COUNTRIES_KEY = "DISPLAYED_COUNTRIES";

    @Inject
    RefugeeDataStore refugeeDataStore;

    private NavigationDrawerFragment navigationDrawerFragment;
    private GoogleMap mapFragment;
    private SearchView searchView;
    private DisplayedStore displayedCountriesStore;
    private Map<Long, Integer> colorMap = new HashMap<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RefugeApplication.from(this).inject(this);
        setContentView(R.layout.activity_main);

        displayedCountriesStore = new DisplayedStore();
        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer_hint, R.string.close_drawer_hint) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                searchView.setIconified(true);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        // Get a reference to the Map Fragment
        mapFragment = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        handleIntent(getIntent());

        // TODO This should be cleaned up. Import pre-populated or do in background
        String assetFile = "UNDataExport2012.xml";
        String countriesLatLongFile = "CountriesLatLong.csv";

        try {
            boolean attemptedToLoad = DoOnce.doOnce(this, LOAD_DATA_TASK_TAG, new LoadDataRunnable(new DataFileImporter(refugeeDataStore), getAssets().open(assetFile), getAssets().open(countriesLatLongFile)));
            Log.i(TAG, "Attempted to load data: " + attemptedToLoad);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open the data file: " + assetFile, e);
            finish();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayedCountriesStore.getDisplayedCountries().subscribe(new Action1<List<Long>>() {
            @Override
            public void call(List<Long> countryIds) {
                mapFragment.clear();
                double scaling = 0.0;
                // TODO This should be moved to background
                for (Long id : countryIds) {
                    // TODO Surely there's a build-in function for this
                    scaling = Math.max(scaling, refugeeDataStore.getTotalRefugeeFlowTo(id));
                }
                Visualizer.drawCountries(refugeeDataStore, mapFragment, countryIds, colorMap, scaling);
            }
        });
    }

    @Override
    public void onCountryItemSelected(long countryId, boolean isSelected) {
        // TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == searchView && !hasFocus) searchView.setIconified(true);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return true;
            case R.id.action_clear:
                displayedCountriesStore.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent - received intent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).trim();
            // TODO Enforce limits some how
//            if (displayedCountriesStore. == 5) {
//                Toast.makeText(this, "Displaying max number of countries on map.", Toast.LENGTH_SHORT).show();
//                // add more colors!
//                return;
//            }
            Country country = refugeeDataStore.getCountry(query);
            if (country != null) {
                addCountry(country);
            } else {
                Toast.makeText(this, "Country not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addCountry(Country country) {
        displayedCountriesStore.add(country.getId());
    }

    @Override
    public Observable<List<Long>> getDisplayedCountries() {
        return displayedCountriesStore.getDisplayedCountries();
    }

    @Override
    public Map<Long, Integer> getColorMap() {
        return colorMap;
    }

    @Override
    public RefugeeDataStore getRefugeeDataStore() {
        return refugeeDataStore;
    }
}
