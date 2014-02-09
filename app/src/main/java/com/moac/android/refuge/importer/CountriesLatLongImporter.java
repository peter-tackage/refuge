package com.moac.android.refuge.importer;

import android.util.Log;

import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.util.DoOnce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by amelysh on 08/02/14.
 */
public class CountriesLatLongImporter {


//    private static final String TAG = CountriesLatLongImporter.class.getSimpleName();
//    private static final String LOAD_DATA_TASK_TAG = "LOAD_DATA_TASK";
//
//    String assetFile = "CountriesLatLong.csv";
//
//    private ModelService mModelService;
//
//    public CountriesLatLongImporter(ModelService modelService) {
//        mModelService = modelService;
//    }
//
//    public void parse(InputStream is) {
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        String line;
//        try {
//            while ((line = reader.readLine()) != null) {
//                String[] rowData = line.split(",");
//                String country = rowData[0];
//                String latitude = rowData[1];
//                String longitude = rowData[2];
//
//                Country
//                mModelService.createCountry(Country)
//                Log.d("", country + " " + latitude + longitude);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
