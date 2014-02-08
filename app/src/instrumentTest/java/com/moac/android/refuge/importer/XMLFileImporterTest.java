package com.moac.android.refuge.importer;

import android.test.InstrumentationTestCase;

import com.moac.android.refuge.model.RefugeeFlow;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by amelysh on 08/02/14.
 */
public class XMLFileImporterTest extends InstrumentationTestCase {

    private static String testDataXMLFile = "smalltestsample.xml";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParsingXML() throws IOException, FileImportException {
        XMLFileImporter xmlFileImporter = new XMLFileImporter();
        RefugeeFlow expectedResult = new RefugeeFlow();
        // Algeria
        expectedResult.setFromCountryId(1);
        //Albania
        expectedResult.setToCountryId(2);
        expectedResult.setRefugeeNum(1);

        InputStream is = getInstrumentation().getContext().getResources().getAssets().open(testDataXMLFile);
        xmlFileImporter.parseFile(is);
        assertTrue(areRefugeeFlowsEqual(expectedResult, xmlFileImporter.refugeeFlow));
    }

    private Boolean areRefugeeFlowsEqual(RefugeeFlow a, RefugeeFlow b) {
        return (a.getFromCountryId() == b.getFromCountryId()) &&
                (a.getToCountryId() == b.getToCountryId()) &&
                (a.getRefugeeNum() == b.getRefugeeNum()) &&
                (a.getYear() == b.getYear());
    }
}
