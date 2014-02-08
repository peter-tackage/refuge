package com.moac.android.refuge.importer;

import android.test.InstrumentationTestCase;

import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

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

    public void testDOMHanlder () throws IOException, ParserConfigurationException, SAXException {
        DOMFileImporter domParser = new DOMFileImporter();
        InputStream is = getInstrumentation().getContext().getResources().getAssets().open(testDataXMLFile);
        domParser.parse(is);

        Country fromCountry = new Country();
        fromCountry.setName("Algeria");
        Country toCountry = new Country();
        toCountry.setName("Albania");

        RefugeeFlow expectedResult = new RefugeeFlow(fromCountry, toCountry);
        expectedResult.setRefugeeCount(1);
        expectedResult.setYear(2012);

        assertTrue(areRefugeeFlowsEqual(expectedResult, domParser.refugeeFlow));
    }

    private Boolean areRefugeeFlowsEqual(RefugeeFlow a, RefugeeFlow b) {
        return (a.getFromCountry().getName().equals(b.getFromCountry().getName())) &&
                (a.getToCountry().getName().equals(b.getToCountry().getName())) &&
                (a.getRefugeeCount() == b.getRefugeeCount()) &&
                (a.getYear() == b.getYear());
    }
}
