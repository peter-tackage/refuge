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
    private static final int NO_FILL_COLOR = 0;

    /*
     * Draw all circles flow in/out for a specific country
     */
    public static void drawCountries(RefugeeDataStore refugeeDataStore,
                                     GoogleMap map,
                                     List<DisplayedCountry> countries,
                                     long maxFlow) {
        Log.d(TAG, "drawCountries() - Draw flows for into countries: " + countries);

        for (DisplayedCountry country : countries) {
            int strokeColor = country.getColor();
            int fillColor = toFillColor(strokeColor);

            // Draw all the circle for outgoing refugees
            drawFromCircles(refugeeDataStore, map, country.getId(), strokeColor, maxFlow);

            // Draw single circle for intake
            drawToCircle(refugeeDataStore,
                    map,
                    country.getId(),
                    strokeColor,
                    fillColor,
                    (refugeeDataStore.getTotalRefugeeFlowTo(country.getId()) / (double)maxFlow));
        }
        Log.d(TAG, "drawCountries() - Using: " + maxFlow);
    }

    /*
     * Draws all circles for outgoing refugees to a specific country
     */
    private static void drawFromCircles(RefugeeDataStore refugeeDataStore, GoogleMap map, long toCountryId, int strokeColor, long maxCount) {
        Log.d(TAG, "drawFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + strokeColor + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = refugeeDataStore.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = refugeeDataStore.getCountry(flow.getFromCountry().getId());
            Log.d(TAG, "drawFromCircles() - Drawing flow from: " + fromCountry.getName() + " count: " + flow.getRefugeeCount() + " / " + maxCount);
            drawScaledCircle(map, fromCountry.getLatLng(), (flow.getRefugeeCount() / (double)maxCount), strokeColor, NO_FILL_COLOR); // no fill
        }
    }

    /*
     * Draws a single circles from the intake to a specific country
     */
    private static void drawToCircle(RefugeeDataStore refugeeDataStore, GoogleMap map, long toCountryId, int strokeColor, int fillColor, double percent) {
        Country toCountry = refugeeDataStore.getCountry(toCountryId);
        drawScaledCircle(map, toCountry.getLatLng(), percent, strokeColor, fillColor);
    }

    /*
     * Draws a general circle shape with provided stroke & fill colors
     */
    private static Circle drawScaledCircle(GoogleMap map,
                                          LatLng coordinates,
                                          double percent,
                                          int strokeColor,
                                          int fillColor) {
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

    /*
     * Generate an appropriate fill color for a given stroke color
     */
    private static int toFillColor(int strokeColor) {
        return Color.argb(Color.alpha(strokeColor) + MAGICAL_ALPHA_OFFSET,
                Color.red(strokeColor),
                Color.green(strokeColor),
                Color.blue(strokeColor));
    }
}

