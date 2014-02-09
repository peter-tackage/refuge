package com.moac.android.refuge.importer;

import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by amelysh on 08/02/14.
 */
public class DataFileImporter {

    static final String TAG = DataFileImporter.class.getSimpleName();

    static final String NAME_ATTRIBUTE_TAG = "name";
    static final String FROM_COUNTRY_TAG = "Country or territory of origin";
    static final String TO_COUNTRY_TAG = "Country or territory of asylum or residence";
    static final String YEAR_TAG = "Year";
    static final String REFUGEE_NUM_TAG = "Refugees<sup>*</sup>";

    public RefugeeFlow refugeeFlow;
    private ModelService mModelService;
    private  HashMap<String, Country>countriesMap;

    public DataFileImporter(ModelService modelService) {
        mModelService = modelService;
        countriesMap = new HashMap<String, Country>();
    }

    public void parse(InputStream UNDataInputStream, InputStream countriesLatLongInputStream) throws FileImportException {

        try {

        parseCountriesLatLong(countriesLatLongInputStream);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(UNDataInputStream);

        Country fromCountry;
        Country toCountry;
        int year;
        long refugeeNum;

        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

            //We have encountered an <record> tag, let's reset everything to make sure
            fromCountry = toCountry = null;
            refugeeNum = -1;
            year = -1;

            Node recordNode = nodeList.item(i);
            if (recordNode instanceof Element) {
                NodeList childNodes = recordNode.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node fieldNode = childNodes.item(j);
                    if (fieldNode instanceof  Element) {
                        String fieldNameValue =  fieldNode.getAttributes().getNamedItem(NAME_ATTRIBUTE_TAG).getNodeValue();
                        if (fieldNameValue.equals(FROM_COUNTRY_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            fromCountry = getLatLong(content);
                        }
                        else if (fieldNameValue.equals(TO_COUNTRY_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            toCountry = getLatLong(content);
                        }
                        else if (fieldNameValue.equals(YEAR_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            year = Integer.parseInt(content);
                        }
                        else if (fieldNameValue.equals(REFUGEE_NUM_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            refugeeNum = Long.parseLong(content);
                        }
                    }
                }
                // we have all our data for the Record node, let's build our RefugeeFlow and insert in DB
                refugeeFlow = new RefugeeFlow(fromCountry, toCountry);
                refugeeFlow.setYear(year);
                refugeeFlow.setRefugeeCount(refugeeNum);
                mModelService.createRefugeeFlow(refugeeFlow);
            }
        }
        } catch (ParserConfigurationException e) {
            throw new FileImportException(e);
        } catch (SAXException e) {
            throw new FileImportException(e);
        } catch (IOException e) {
            throw new FileImportException(e);
        }
    }

    private void parseCountriesLatLong(InputStream is) throws FileImportException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] rowData = line.split(",");
            Country country = new Country(rowData[0], Double.parseDouble(rowData[1]), Double.parseDouble(rowData[2]));
            countriesMap.put(rowData[0], country);
            mModelService.createCountry(country);
        }
    }

    private Country getLatLong(String country) throws FileImportException {
        country = country.toUpperCase();
        if (countriesMap.containsKey(country)) {
            return (Country) countriesMap.get(country);
        }
        else {
            throw new FileImportException(new Exception("Country not found: " + country));
        }
    }
}
