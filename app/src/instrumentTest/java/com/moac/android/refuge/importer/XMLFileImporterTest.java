package com.moac.android.refuge.importer;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.moac.android.refuge.database.DatabaseHelper;
import com.moac.android.refuge.database.MockModelService;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.database.PersistentModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by amelysh on 08/02/14.
 */
public class XMLFileImporterTest extends InstrumentationTestCase {

    private static String countriesLatLongFile = "CountriesLatLong.csv";
    private static String testDataXMLFile = "smalltestsample.xml";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        getInstrumentation().getContext().deleteDatabase("/data/data/com.moac.android.refuge/databases/refuge.db");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

//    public void testDOMHandler () throws IOException, FileImportException {
//        ModelService mockModelService = new MockModelService();
//        DataFileImporter domParser = new DataFileImporter(mockModelService);
//
//        InputStream countriesLatLongIs = getInstrumentation().getContext().getResources().getAssets().open(countriesLatLongFile);
//        InputStream is = getInstrumentation().getContext().getResources().getAssets().open(testDataXMLFile);
//        domParser.parse(is, countriesLatLongIs);
//
//        Country fromCountry = new Country();
//        fromCountry.setName("Algeria");
//        Country toCountry = new Country();
//        toCountry.setName("Albania");
//
//        RefugeeFlow expectedResult = new RefugeeFlow(fromCountry, toCountry);
//        expectedResult.setRefugeeCount(1);
//        expectedResult.setYear(2012);
//
//        assertTrue(areRefugeeFlowsEqual(expectedResult, domParser.refugeeFlow));
//    }

    public void testDB() {
        String assetFile = "UNDataExport2012.xml";
        String countriesLongLat = "CountriesLatLong.csv";
        DatabaseHelper databaseHelper = new DatabaseHelper(getInstrumentation().getTargetContext());
        PersistentModelService persistentModelService = new PersistentModelService(databaseHelper);
        try {
            LoadDataRunnable loadDataRunnable = new LoadDataRunnable(new DataFileImporter(persistentModelService), getInstrumentation().getTargetContext().getAssets().open(assetFile), getInstrumentation().getTargetContext().getAssets().open(countriesLongLat));
            loadDataRunnable.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(149765, persistentModelService.getTotalRefugeeFlowTo(72));

    }

    String printRefugeeFlow(RefugeeFlow refugeeFlow) {
        return "flow from: " + refugeeFlow.getFromCountry().getName() + " to:" + refugeeFlow.getToCountry().getName()
                + " year: " + refugeeFlow.getYear() + " num:" + refugeeFlow.getRefugeeCount();
    }

    private Boolean areRefugeeFlowsEqual(RefugeeFlow a, RefugeeFlow b) {
        return (a.getFromCountry().getName().equals(b.getFromCountry().getName())) &&
                (a.getToCountry().getName().equals(b.getToCountry().getName())) &&
                (a.getRefugeeCount() == b.getRefugeeCount()) &&
                (a.getYear() == b.getYear());
    }
}
