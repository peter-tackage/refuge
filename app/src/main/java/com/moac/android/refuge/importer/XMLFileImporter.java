package com.moac.android.refuge.importer;

/**
 * Created by amelysh on 08/02/14.
 */

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.moac.android.refuge.R;
import com.moac.android.refuge.model.RefugeeFlow;

public class XMLFileImporter extends DefaultHandler {

    String tmpVal;
    RefugeeFlow refugeeFlow = new RefugeeFlow();

    public void parseFile(InputStream stream) throws FileImportException {

        DefaultHandler handler = new XMLFileImporter();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);

        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
            parser.parse(stream, handler);
        } catch (ParserConfigurationException e) {
            throw new FileImportException(e);
        } catch (SAXException e) {
            throw new FileImportException(e);
        } catch (IOException e) {
            throw new FileImportException(e);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        // get the number of attributes in the list
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String value = attributes.getValue(i);
            System.out.println("i: " + i + " value:" + value);
            if (value.equals(R.string.fromCountryNameValue)) {
                // we want the element
                // insert in db and get id back
                int countryId = 1;
                refugeeFlow.setFromCountryId(countryId);

            } else if (value.equals(R.string.toCountryNameValue)) {
                int countryId = 2;
                refugeeFlow.setToCountryId(countryId);

            } else if (value.equals(R.string.yearValue)) {
                refugeeFlow.setYear(Integer.parseInt(tmpVal));

            } else if (value.equals(R.string.refugeesNumValue)) {
                refugeeFlow.setRefugeeNum(Double.parseDouble(tmpVal));
            }

            // insert refugeeFlow in db
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tmpVal = new String(ch, start, length);
    }

}

