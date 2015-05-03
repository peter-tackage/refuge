package com.moac.android.refuge.util;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.model.DisplayedCountry;
import com.moac.android.refuge.model.persistent.Country;
import com.moac.android.refuge.model.persistent.RefugeeFlow;

import java.util.List;

public class Visualizer {

    private static final String TAG = Visualizer.class.getSimpleName();

    private static final long MAX_RADIUS = 1500000; // 1500 kms
    private static final double MAX_AREA = Math.PI * MAX_RADIUS * MAX_RADIUS;
    private static final int MAGICAL_ALPHA_OFFSET = 120;

    public static void drawCountries(RefugeeDataStore refugeeDataStore,
                                     GoogleMap map,
                                     List<DisplayedCountry> countries,
                                     double scaling) {
        Log.d(TAG, "drawCountries() - Draw TO countries: " + countries);

        int colorIndex = 0;
        // Maximum radius is defined
        for (DisplayedCountry country : countries) {
            int strokeColor = country.getColor();
            int fillColor = toFillColor(strokeColor);
            drawAllFromCircles(refugeeDataStore, map, country.getId(), strokeColor, scaling);
            drawToCircle(refugeeDataStore,
                    map,
                    country.getId(),
                    strokeColor, fillColor,
                    (refugeeDataStore.getTotalRefugeeFlowTo(country.getId()) / scaling));
            colorIndex++;
        }
        Log.d(TAG, "drawCountries() - Using: " + scaling);
    }

    private static void drawAllFromCircles(RefugeeDataStore refugeeDataStore, GoogleMap map, long toCountryId, int strokeColor, double maxCount) {
        Log.d(TAG, "drawAllFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + strokeColor + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = refugeeDataStore.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = refugeeDataStore.getCountry(flow.getFromCountry().getId());
            Log.d(TAG, "drawAllFromCircles() - Drawing flow from: " + fromCountry.getName() + " count: " + flow.getRefugeeCount() + " / " + maxCount);
            drawScaledCircle(map, fromCountry.getLatLng(), (flow.getRefugeeCount() / maxCount), strokeColor, 0);
        }
    }

    private static void drawToCircle(RefugeeDataStore refugeeDataStore, GoogleMap map, long toCountryId, int strokeColor, int fillColor, double percent) {
        Country toCountry = refugeeDataStore.getCountry(toCountryId);
        drawScaledCircle(map, toCountry.getLatLng(), percent, strokeColor, fillColor);
    }

    public static Circle drawScaledCircle(GoogleMap map,
                                          LatLng coordinates,
                                          double percent,
                                          int strokeColor, int fillColor) {
        Log.d(TAG, "drawScaledCircle() - percent: " + percent);
        double circleArea = percent * MAX_AREA;
        double radius = Math.sqrt(circleArea / Math.PI);
        Log.d(TAG, "drawScaledCircle() - radius (m): " + radius + " circleArea: " + circleArea + " percent: " + percent + " max Area: " + MAX_AREA);
        CircleOptions circleOptions = new CircleOptions()
                .center(coordinates)
                .radius(radius)
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(5);
        return map.addCircle(circleOptions);
    }

    private static int toFillColor(int primaryColor) {
        return Color.argb(Color.alpha(primaryColor) + MAGICAL_ALPHA_OFFSET,
                Color.red(primaryColor),
                Color.green(primaryColor),
                Color.blue(primaryColor));
    }
}

