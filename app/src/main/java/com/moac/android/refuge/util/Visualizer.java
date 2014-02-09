package com.moac.android.refuge.util;

import android.graphics.Color;
import android.util.Log;

import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by amelysh on 09/02/14.
 */
public class Visualizer {

    private static final long MAX_RADIUS = 1500000; // 1500 kms
    private static final double MAX_AREA = 3.14 * MAX_RADIUS * MAX_RADIUS;
    static final String TAG = Visualizer.class.getSimpleName();

    static final int[] fillColors = {0x660066cc, 0x66D6B331, 0x66663399, 0x55FF6600,
            0x66669900};

    static final int[] strokeColors = {0xDD0066cc, 0xFFD6B331, 0xDD663399, 0xFFFF6600,
            0xDD669900};

    public Visualizer() {
    }

    public static void drawCountries(ModelService modelService, GoogleMap map, List<Country> countries) {
            Log.d(TAG, "drawCountries() - Draw TO countries: " + countries);
            double maxRefugeeFlowTo = 0.0;
            for (Country country : countries) {
                maxRefugeeFlowTo = Math.max(maxRefugeeFlowTo, modelService.getTotalRefugeeFlowTo(country.getId()));
            }

            int index=0;
            // Maximum radius is defined
            for (Country toCountry : countries) {
                drawAllFromCircles(modelService, map, toCountry.getId(), index, maxRefugeeFlowTo);
                drawToCircle(modelService, map, toCountry.getId(), index, (modelService.getTotalRefugeeFlowTo(toCountry.getId())/maxRefugeeFlowTo));
                index++;
            }
            Log.d(TAG, "drawCountries() - Calculated maxRefugeeFlowTo: " + maxRefugeeFlowTo);
    }

    private static void drawAllFromCircles(ModelService modelService, GoogleMap map, long toCountryId, int index, double maxCount) {
        Log.d(TAG, "drawAllFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + strokeColors[index] + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = modelService.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = modelService.getCountry(flow.getFromCountry().getId());
            Log.d(TAG, "drawAllFromCircles() - Drawing flow from: " + fromCountry.getName() + " count: " + flow.getRefugeeCount() + " / " + maxCount);
            drawScaledCircle(map, fromCountry.getLatLng(), (flow.getRefugeeCount()/maxCount), strokeColors[index], 0);
        }
    }

    private static void drawToCircle(ModelService modelService, GoogleMap map, long toCountryId, int index, double percent) {
        Country toCountry = modelService.getCountry(toCountryId);
        drawScaledCircle(map, toCountry.getLatLng(), percent, strokeColors[index], fillColors[index]);
    }

    public static Circle drawScaledCircle(GoogleMap map,
                                          LatLng coordinates,
                                          double percent,
                                          int strokeColor, int fillColor) {
        Log.d(TAG, "drawScaledCircle() - percent: " + percent) ;
        double circleArea = percent * MAX_AREA;
        double radius = Math.sqrt (circleArea/3.14);
        Log.d(TAG, "drawScaledCircle() - radius (m): " + radius + " circleArea: " + circleArea + " percent: " + percent + " max Area: " + MAX_AREA);
        CircleOptions circleOptions = new CircleOptions()
                .center(coordinates)
                .radius(radius)
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(5);
        return map.addCircle(circleOptions);
    }
}
