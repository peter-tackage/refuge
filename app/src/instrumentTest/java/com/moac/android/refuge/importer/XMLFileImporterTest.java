package com.moac.android.refuge.importer;

import android.test.InstrumentationTestCase;

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

        RefugeeFlow expectedResult = new RefugeeFlow();
        expectedResult.setFromCountryId(1);
        expectedResult.setToCountryId(2);
        expectedResult.setRefugeeNum(1);
        expectedResult.setYear(2012);

        assertTrue(areRefugeeFlowsEqual(expectedResult, domParser.refugeeFlow));
    }

    private Boolean areRefugeeFlowsEqual(RefugeeFlow a, RefugeeFlow b) {
        return (a.getFromCountryId() == b.getFromCountryId()) &&
                (a.getToCountryId() == b.getToCountryId()) &&
                (a.getRefugeeNum() == b.getRefugeeNum()) &&
                (a.getYear() == b.getYear());
    }
}
