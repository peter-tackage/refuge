package com.moac.android.refuge.activity;

import android.app.Activity;
;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.fragment.NavigationDrawerFragment;
import com.moac.android.refuge.R;
import com.moac.android.refuge.importer.DataFileImporter;
import com.moac.android.refuge.importer.LoadDataRunnable;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.util.DoOnce;
import com.moac.android.refuge.util.Visualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOAD_DATA_TASK_TAG = "LOAD_DATA_TASK";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Inject
    ModelService mModelService;
    private GoogleMap mMap;
    private SearchView mSearchView;
    private List<Country> mDisplayedCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RefugeApplication.from(this).inject(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Get a handle to the Map Fragment
        mMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        // We are using single top mode, so this will not contain
        // search intents as the SearchView is operating on its host
        // Activity - instead see onNewIntent()
        handleIntent(getIntent());

        mDisplayedCountries = new ArrayList<Country>();

        String assetFile = "UNDataExport2012.xml";
        String countriesLatLongFile = "CountriesLatLong.csv";

        try {
            boolean attemptedToLoad = DoOnce.doOnce(this, LOAD_DATA_TASK_TAG, new LoadDataRunnable(new DataFileImporter(mModelService), getAssets().open(assetFile), getAssets().open(countriesLatLongFile)));
            Log.i(TAG, "Attempted to load data: " + attemptedToLoad);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open the data file: " + assetFile, e);
            finish();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // updateCountry the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MapFragment.newInstance())
                .commit();
        // FIXME If we do this, then we should save the reference to the Fragment/map
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconifiedByDefault(true);
            mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (v == mSearchView && !hasFocus) mSearchView.setIconified(true);
                }
            });
            // Note: I don't register callbacks to invoke the search query - use the Intents instead.

            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent _intent) {
        Log.i(TAG, "onNewIntent - received intent");
        //  Removed this as causes the fragment to reperform search on rotation.
        // setIntent(_intent);
        handleIntent(_intent);
    }

    private void handleIntent(Intent _intent) {
        if (Intent.ACTION_SEARCH.equals(_intent.getAction())) {
            String query = _intent.getStringExtra(SearchManager.QUERY).trim();
            // FIXME doSearch(query);
            Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_LONG).show();
            Country country = mModelService.getCountryByName(query);
            if(country != null) {
                showCountry(country);
            } else {
                Toast.makeText(this, "Country not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showCountry(Country country) {
        mDisplayedCountries.add(country);
        mMap.clear();
        Visualizer.drawCountries(mModelService, mMap, mDisplayedCountries);
    }

    // TODO SaveInstanceState

}
