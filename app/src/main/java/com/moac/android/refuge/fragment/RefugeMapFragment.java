package com.moac.android.refuge.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;
import com.moac.android.refuge.maps.InfoAdapter;

import java.util.HashMap;
import java.util.Map;

public class RefugeMapFragment extends MapFragment implements GoogleMap.OnMarkerClickListener {

    private static final String TAG = RefugeMapFragment.class.getSimpleName();

    private Map<Marker, Object> mMarkerMap;

    public RefugeMapFragment getInstance() {
        return new RefugeMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() - start");
        super.onCreate(savedInstanceState);
        mMarkerMap = new HashMap<Marker, Object>();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater _inflator, ViewGroup parent, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() - start");
        View view = super.onCreateView(_inflator, parent, savedInstanceState);
        getMap().setOnMarkerClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated() - start");
        getMap().setInfoWindowAdapter(new InfoAdapter(getActivity().getLayoutInflater(), mMarkerMap));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // I can't believe I have to do all this myself now...
        getMap().animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        marker.showInfoWindow();
        return true;
    }

    public void doSearch(String _query) {
        clear();
        // Search the DB?
    }

    /**
     * Generates Map Marker model objects from API derived Track objects.
     */
//    private class TracksResponseListener implements Response.Listener<Collection<Track>> {
//        @Override
//        public void onResponse(Collection<Track> tracks) {
//            Log.i(TAG, "onResponse(): got tracks: " + tracks.size());
//            clear();
//                    Marker marker = getMap().addMarker(new MarkerOptions()
//                      .position(new LatLng(loc.getLatitude(), loc.getLongitude())).snippet(track.getUser().getUsername())
//                      .title(track.getTitle()));
//                    mMarkerMap.put(marker, track);
//                }
//            }
//            String msg = String.format("Found %d sounds", mMarkerMap.size());
//    }
    private void clear() {
        getMap().clear();
        mMarkerMap.clear();
    }
}
