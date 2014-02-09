package com.moac.android.refuge.activity;

import android.app.Activity;
;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

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
    private static final long MAX_RADIUS = 1500000; // 1500 kms

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

//        String assetFile = "UNDataExport2012.xml";
//        try {
//            boolean attemptedToLoad = DoOnce.doOnce(this, LOAD_DATA_TASK_TAG, new LoadDataRunnable(new DOMFileImporter(mModelService), getAssets().open(assetFile)));
//            Log.i(TAG, "Attempted to load data: " + attemptedToLoad);
//        } catch (IOException e) {
//            Log.e(TAG, "Failed to open the data file: " + assetFile, e);
//            finish();
//        }

//        List<Country> allCountries = mModelService.getAllCountries();
//        Log.i(TAG, "We loaded these countries: " + allCountries);
//        for(Country c : allCountries) {
//            Log.i(TAG, "Country: " + c.getName());
//            List<RefugeeFlow> flows = mModelService.getRefugeeFlowsFrom(c.getId());
//            Log.i(TAG, "Country has flows into: " + c.getName());
//
//        }

        List<Country> selectedCountries = new ArrayList<Country>();
        selectedCountries.add(mModelService.getCountry(0));
        selectedCountries.add(mModelService.getCountry(6));
        drawCountries(selectedCountries);
    }


    private void drawCountries(List<Country> countries) {
        Log.i(TAG, "drawCountries() - Draw TO countries: " + countries);
        long maxRefugeeFlowTo = 0;
        for (Country country : countries) {
            maxRefugeeFlowTo = Math.max(maxRefugeeFlowTo,
                    mModelService.getTotalRefugeeFlowTo(country.getId()));
        }

        final int[] colors =
                {0xAA434B52, 0xAA54B395, 0xAAD6B331,
                        0xAAA465C5, 0xAA5661DE, 0xAA4AB498, 0xAAFA7B68, 0xAAFF6600,
                        0xAA669900, 0xAA66CCCC};

        // Maximum radius is defined
        for (Country toCountry : countries) {
            drawAllFromCircles(toCountry.getId(), colors[(int) toCountry.getId()], maxRefugeeFlowTo);
            drawToCircle(toCountry.getId(), colors[(int) toCountry.getId()], mModelService.getTotalRefugeeFlowTo(toCountry.getId()), maxRefugeeFlowTo);

        }
        Log.i(TAG, "drawCountries() - Calculated maxRefugeeFlowTo: " + maxRefugeeFlowTo);
    }

    private void drawAllFromCircles(long toCountryId, int toCountryColor, long maxCount) {
        Log.i(TAG, "drawAllFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + toCountryColor + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = mModelService.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = mModelService.getCountry(flow.getFromCountry().getId());
            Log.i(TAG, "drawAllFromCircles() - Drawing flow from: " + fromCountry.getName());
            drawScaledCircle(mMap, fromCountry.getLatLng(), flow.getRefugeeCount(), maxCount, toShade(toCountryColor), toCountryColor);
        }
    }

    private void drawToCircle(long toCountryId, int toCountryColor, long countTo, long maxCountTo) {
        Country toCountry = mModelService.getCountry(toCountryId);
        drawScaledCircle(mMap, toCountry.getLatLng(), countTo, maxCountTo, toCountryColor, toShade(toCountryColor));
    }

    public static Circle drawScaledCircle(GoogleMap map,
                                          LatLng coordinates,
                                          long count, long maxCount,
                                          int strokeColor, int fillColor) {
        Log.i(TAG, "drawScaledCircle() - count: " + count);
        long radius = (long) (((double) count / (double) maxCount) * MAX_RADIUS);
        Log.i(TAG, "drawScaledCircle() - radius (m): " + radius);
        CircleOptions circleOptions = new CircleOptions()
                .center(coordinates)
                .radius(radius)
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(5);
        return map.addCircle(circleOptions);
    }

    private static int toShade(int _color) {
        float[] hsv = new float[3];
        Color.colorToHSV(_color, hsv);
        hsv[1] *= 0.5;
        hsv[2] *= 1.5;
        return Color.HSVToColor(hsv);
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
            mModelService.getCountryByName(query);
        }
    }

    // TODO SaveInstanceState

}
