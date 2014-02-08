package com.moac.android.refuge.activity;

import android.app.Activity;
;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;
import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.database.DatabaseService;
import com.moac.android.refuge.fragment.NavigationDrawerFragment;
import com.moac.android.refuge.R;
import com.moac.android.refuge.fragment.RefugeMapFragment;

import javax.inject.Inject;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Inject
    DatabaseService mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("Address Map", "Could not initialize google play", e);
        }
        // Inject database
        RefugeApplication.from(this).inject(this);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, RefugeMapFragment.newInstance())
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

     @Override
     protected void onCreate(Bundle savedInstanceState) {
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

     @Override
     protected void onNewIntent(Intent _intent) {
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

     @Override
     public void onSaveInstanceState(Bundle _outstate) {
     super.onSaveInstanceState(_outstate);
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the options menu from XML
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.options_menu, menu);

     // Get the SearchView and set the searchable configuration
     SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     mSearchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
     mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
     mSearchView.setIconifiedByDefault(true);
     mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
     @Override
     public void onFocusChange(View v, boolean hasFocus) {
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
