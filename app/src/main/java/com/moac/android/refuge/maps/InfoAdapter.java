package com.moac.android.refuge.maps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

public class InfoAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = InfoAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    private final Map<Marker, Object> mMarkerMap;
//
    public InfoAdapter(LayoutInflater inflater, Map<Marker, Object> markerMap) {
        mInflater = inflater;
        mMarkerMap = markerMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null; // default Window
    }

    @Override
    public View getInfoContents(final Marker marker) {
//        Log.i(TAG, "getInfoContents() started: " + marker.getId());
        final View popup = null;// = mInflater.inflate(R.layout.popup_view, null);
//
//        ImageView imageView = (ImageView) popup.findViewById(R.id.track_imageview);
//        TextView titleTextView = (TextView) popup.findViewById(R.id.track_title_textview);
//        TextView userTextView = (TextView) popup.findViewById(R.id.username_textview);
//
//        titleTextView.setText(marker.getTitle());
//        userTextView.setText(marker.getSnippet());
//
//        Track track = mMarkerMap.get(marker);
//        Bitmap bmp = track.getAvatar();
//        if(bmp != null) {
//            imageView.setImageBitmap(bmp);
//        } else {
//            Drawable myIcon = mInflater.getContext().getResources().getDrawable(R.drawable.ic_soundcloud);
//            imageView.setImageDrawable(myIcon);
//        }
//
//        // Note: It is impossible to asynchronously load the image into the
//        // the ImageView here. The contents of the returned view is rendered as an
//        // image once it is returned from this call.
//
//        // Cache hits are actually successfully rendered here as the async/network
//        // call is never actually made, hence the image contents are ready.

        return popup;
    }
}
