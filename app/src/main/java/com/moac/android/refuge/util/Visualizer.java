package com.moac.android.refuge.util;

import android.util.Log;

import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public class Visualizer {

    private static final long MAX_RADIUS = 1500000; // 1500 kms
    private static final double MAX_AREA = 3.14 * MAX_RADIUS * MAX_RADIUS;
    static final String TAG = Visualizer.class.getSimpleName();

    static final int[] fillColors = {0x660066cc, 0x66D6B331, 0x66663399, 0x55FF6600,
            0x66669900};

    static final int[] strokeColors = {0xDD0066cc, 0xFFD6B331, 0xDD663399, 0xFFFF6600,
            0xDD669900};

    private Visualizer() { }

    public static void drawCountries(ModelService modelService, GoogleMap map, List<Country> countries, Map<Long, Integer> colorMap) {
            Log.d(TAG, "drawCountries() - Draw TO countries: " + countries);
            double maxRefugeeFlowTo = 0.0;
            for (Country country : countries) {
                maxRefugeeFlowTo = Math.max(maxRefugeeFlowTo, modelService.getTotalRefugeeFlowTo(country.getId()));
            }

            int colorIndex=0;
            // Maximum radius is defined
            for (Country toCountry : countries) {
                int strokeColor = strokeColors[colorIndex];
                int fillColor = fillColors[colorIndex];
                colorMap.put(toCountry.getId(), strokeColor);
                drawAllFromCircles(modelService, map, toCountry.getId(), strokeColor, maxRefugeeFlowTo);
                drawToCircle(modelService, map, toCountry.getId(), strokeColor, fillColor, (modelService.getTotalRefugeeFlowTo(toCountry.getId())/maxRefugeeFlowTo));
                colorIndex++;
            }
            Log.d(TAG, "drawCountries() - Calculated maxRefugeeFlowTo: " + maxRefugeeFlowTo);
    }

    private static void drawAllFromCircles(ModelService modelService, GoogleMap map, long toCountryId, int strokeColor, double maxCount) {
        Log.d(TAG, "drawAllFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + strokeColor + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = modelService.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = modelService.getCountry(flow.getFromCountry().getId());
            Log.d(TAG, "drawAllFromCircles() - Drawing flow from: " + fromCountry.getName() + " count: " + flow.getRefugeeCount() + " / " + maxCount);
            drawScaledCircle(map, fromCountry.getLatLng(), (flow.getRefugeeCount()/maxCount), strokeColor, 0);
        }
    }

    private static void drawToCircle(ModelService modelService, GoogleMap map, long toCountryId, int strokeColor, int fillColor, double percent) {
        Country toCountry = modelService.getCountry(toCountryId);
        drawScaledCircle(map, toCountry.getLatLng(), percent, strokeColor, fillColor);
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
