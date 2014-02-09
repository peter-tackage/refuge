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
    static final String TAG = Visualizer.class.getSimpleName();

    public Visualizer() {
    }

    public static void drawCountries(ModelService modelService, GoogleMap map, List<Country> countries) {
            Log.i(TAG, "drawCountries() - Draw TO countries: " + countries);
            long maxRefugeeFlowTo = 0;
            for (Country country : countries) {
                maxRefugeeFlowTo = Math.max(maxRefugeeFlowTo,
                        modelService.getTotalRefugeeFlowTo(country.getId()));
            }

            final int[] colors =
                    {0xAA434B52, 0xAA54B395, 0xAAD6B331,
                            0xAAA465C5, 0xAA5661DE, 0xAA4AB498, 0xAAFA7B68, 0xAAFF6600,
                            0xAA669900, 0xAA66CCCC};

            // Maximum radius is defined
            for (Country toCountry : countries) {
                drawAllFromCircles(modelService, map, toCountry.getId(), colors[(int) toCountry.getId() % (colors.length)], maxRefugeeFlowTo);
                drawToCircle(modelService, map, toCountry.getId(), colors[(int) toCountry.getId() % (colors.length)], modelService.getTotalRefugeeFlowTo(toCountry.getId()), maxRefugeeFlowTo);

            }
            Log.i(TAG, "drawCountries() - Calculated maxRefugeeFlowTo: " + maxRefugeeFlowTo);
    }

    private static void drawAllFromCircles(ModelService modelService, GoogleMap map, long toCountryId, int toCountryColor, long maxCount) {
        Log.i(TAG, "drawAllFromCircles() - toCountryId: " + toCountryId + " toCountryColor: " + toCountryColor + " maxFlow: " + maxCount);
        List<RefugeeFlow> flows = modelService.getRefugeeFlowsTo(toCountryId);
        for (RefugeeFlow flow : flows) {
            Country fromCountry = modelService.getCountry(flow.getFromCountry().getId());
            Log.i(TAG, "drawAllFromCircles() - Drawing flow from: " + fromCountry.getName());
            drawScaledCircle(map, fromCountry.getLatLng(), flow.getRefugeeCount(), maxCount, toShade(toCountryColor), toCountryColor);
        }
    }

    private static void drawToCircle(ModelService modelService, GoogleMap map, long toCountryId, int toCountryColor, long countTo, long maxCountTo) {
        Country toCountry = modelService.getCountry(toCountryId);
        drawScaledCircle(map, toCountry.getLatLng(), countTo, maxCountTo, toCountryColor, toShade(toCountryColor));
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
}
