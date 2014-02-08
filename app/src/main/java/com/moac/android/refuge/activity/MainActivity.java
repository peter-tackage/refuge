package com.moac.android.refuge.activity;

import android.app.Activity;
;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.fragment.NavigationDrawerFragment;
import com.moac.android.refuge.R;
import com.moac.android.refuge.importer.DOMFileImporter;
import com.moac.android.refuge.importer.LoadDataRunnable;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;
import com.moac.android.refuge.util.DoOnce;

import java.io.IOException;
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
    private MapFragment mMapFragment;
    private Handler mHandler;
    private SearchView mSearchView;
    private static final long MAX_RADIUS = 2000000; // 2000 kms

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


        String assetFile = "UNDataExport2012.xml";
        try {
            boolean attemptedToLoad = DoOnce.doOnce(this, LOAD_DATA_TASK_TAG, new LoadDataRunnable(new DOMFileImporter(mModelService), getAssets().open(assetFile)));
            Log.i(TAG, "Attempted to load data: " + attemptedToLoad);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open the data file: " + assetFile, e);
            finish();
        }

        List<Country> allCountries = mModelService.getAllCountries();
        Log.i(TAG, "We loaded these countries: " + allCountries);
        for(Country c : allCountries) {
            Log.i(TAG, "Country: " + c.getName());
            List<RefugeeFlow> flows = mModelService.getRefugeeFlowsFrom(c.getId());
            Log.i(TAG, "Country has flows into: " + c.getName());

        }
      //  drawCountries(allCountries);
    }

//    private void insertTestData() {
//        List<Country> countryList = new ArrayList<Country>();
//
//        Country au = new Country("Australia");
//        au.setLatLng(-45, 90);
//        countryList.add(au);
//        long auId = mModelService.createCountry(au);
//
//        Country af = new Country("Afghanistan");
//        af.setLatLng(45, 90);
//        countryList.add(af);
//        long afId = mModelService.createCountry(af);
//
//
//        Country iq = new Country("Iraq");
//        iq.setLatLng(-45, -90);
//        countryList.add(iq);
//        long iqId = mModelService.createCountry(iq);
//
//        RefugeeFlow af2au = new RefugeeFlow(af, au);
//        af2au.setRefugeeCount(1000);
//        af2au.setYear(2012);
//        mModelService.createCountry(af2au);
//
//        RefugeeFlow iq2au = new RefugeeFlow(iq, au);
//        iq2au.setRefugeeCount(750);
//        iq2au.setYear(2012);
//        mModelService.createCountry(iq2au);
//    }

    private void drawCountries(List<Country> countries) {
        long maxRefugeeFlowTo = 0;
        for (Country country : countries) {
            maxRefugeeFlowTo = Math.max(maxRefugeeFlowTo,
                    mModelService.getTotalRefugeeFlowTo(country.getId()));
        }

        // Maximum radius is defined
        for (Country country : countries) {
            drawFromCircles(country.getId(), 0xAA4AB498, maxRefugeeFlowTo);
        }
    }

    private void drawFromCircles(long toCountryId, int toCountryColor, long maxFlow) {
        List<RefugeeFlow> flows = mModelService.getRefugeeFlowsFrom(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = mModelService.getCountry(flow.getFromCountry().getId());
            LatLng coordinates = new LatLng(fromCountry.getLatitude(), fromCountry.getLongitude());
            renderOutgoingFlowCircle(mMap, coordinates, maxFlow, flow.getRefugeeCount(), toCountryColor);
        }
    }

    public static Circle renderOutgoingFlowCircle(GoogleMap map,
                                                  LatLng coordinates,
                                                  long maxSelectedCount,
                                                  long count,
                                                  int color) {

        long radius = (count / maxSelectedCount) * MAX_RADIUS;
        CircleOptions circleOptions = new CircleOptions()
                .center(coordinates)
                .radius(radius)
                .fillColor(color)
                .strokeWidth(0); // In meters

        return map.addCircle(circleOptions);
    }

//    private class AnimateRunnable implements Runnable {
//
//        private final Circle mCircle;
//
//        public AnimateRunnable(Circle c) {
//            mCircle = c;
//        }
//        @Override
//        public void run() {
//
//          //  mCircle.setRadius(mCircle.getRadius() - 10000);
//            LatLng ll = mCircle.getCenter();
//            mCircle.setCenter(new LatLng(ll.latitude, ll.longitude + 0.5));
//            mHandler.postDelayed(this, 100);
////            mCircle.setFillColor(mCircle.getFillColor() + 1);
////                    mHandler.postDelayed(this, 1);
//        }
//    }



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
            restoreActionBar();
            return true;
        }
//        // Inflate the options menu from XML
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        mSearchView.setIconifiedByDefault(true);
//        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override public void onFocusChange(View v, boolean hasFocus) {
//                if(v == mSearchView && !hasFocus) mSearchView.setIconified(true);
//            }
//        });
//        // Note: I don't register callbacks to invoke the search query - use the Intents instead.
//
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *
     private static final String TAG = MainActivity.class.getSimpleName();
     RefugeMapFragment mMapFragment;
     SearchView mSearchView;

     @Override protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     Log.v(TAG, "onCreate()- start");
     setContentView(R.layout.activity_main);

     // Get reference to MapFragment
     mMapFragment = (RefugeMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

     // We are using single top mode, so this will not contain
     // search intents as the SearchView is operating on its host
     // Activity - instead see onNewIntent()
     handleIntent(getIntent());
     }

     @Override protected void onNewIntent(Intent _intent) {
     Log.i(TAG, "onNewIntent - received intent");
     //  Removed this as causes the fragment to reperform search on rotation.
     // setIntent(_intent);
     handleIntent(_intent);
     }

     private void handleIntent(Intent _intent) {
     if(Intent.ACTION_SEARCH.equals(_intent.getAction())) {
     String query = _intent.getStringExtra(SearchManager.QUERY);
     doSearch(query);
     }
     }

     @Override public void onSaveInstanceState(Bundle _outstate) {
     super.onSaveInstanceState(_outstate);
     }

     @Override public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the options menu from XML
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.options_menu, menu);

     // Get the SearchView and set the searchable configuration
     SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     mSearchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
     mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
     mSearchView.setIconifiedByDefault(true);
     mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
     @Override public void onFocusChange(View v, boolean hasFocus) {
     if(v == mSearchView && !hasFocus) mSearchView.setIconified(true);
     }
     });
     // Note: I don't register callbacks to invoke the search query - use the Intents instead.

     return true;
     }

     private void doSearch(String _query) {
     Log.i(TAG, "doSearch() - query: " + _query);
     mMapFragment.doSearch(_query);
     }
     */

}
