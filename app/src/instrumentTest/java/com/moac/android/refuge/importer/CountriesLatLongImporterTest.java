package com.moac.android.refuge.importer;

import android.test.InstrumentationTestCase;

import com.moac.android.refuge.database.MockModelService;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by amelysh on 09/02/14.
 */
public class CountriesLatLongImporterTest extends InstrumentationTestCase {

    private static String testFile = "CountriesLatLong.csv";

//    public void testLatLongImporter () throws IOException, FileImportException {
//        CountriesLatLongImporter parser = new CountriesLatLongImporter();
//        InputStream is = getInstrumentation().getContext().getResources().getAssets().open(testFile);
//        parser.parse(is);
//    }

}
