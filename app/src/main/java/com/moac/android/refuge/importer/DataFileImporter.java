package com.moac.android.refuge.importer;

import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.model.persistent.Country;
import com.moac.android.refuge.model.persistent.RefugeeFlow;

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

public class DataFileImporter {

    static final String TAG = DataFileImporter.class.getSimpleName();

    static final String NAME_ATTRIBUTE_TAG = "name";
    static final String FROM_COUNTRY_TAG = "Country or territory of origin";
    static final String TO_COUNTRY_TAG = "Country or territory of asylum or residence";
    static final String YEAR_TAG = "Year";
    static final String REFUGEE_NUM_TAG = "Refugees<sup>*</sup>";

    public RefugeeFlow refugeeFlow;
    private RefugeeDataStore refugeeDataStore;
    private HashMap<String, Country> countriesMap;

    public DataFileImporter(RefugeeDataStore refugeeDataStore) {
        this.refugeeDataStore = refugeeDataStore;
        countriesMap = new HashMap<>();
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
                        if (fieldNode instanceof Element) {
                            String fieldNameValue = fieldNode.getAttributes().getNamedItem(NAME_ATTRIBUTE_TAG).getNodeValue();
                            switch (fieldNameValue) {
                                case FROM_COUNTRY_TAG: {
                                    String content = fieldNode.getLastChild().getTextContent().trim();
                                    fromCountry = getLatLong(content);
                                    break;
                                }
                                case TO_COUNTRY_TAG: {
                                    String content = fieldNode.getLastChild().getTextContent().trim();
                                    toCountry = getLatLong(content);
                                    break;
                                }
                                case YEAR_TAG: {
                                    String content = fieldNode.getLastChild().getTextContent().trim();
                                    year = Integer.parseInt(content);
                                    break;
                                }
                                case REFUGEE_NUM_TAG: {
                                    String content = fieldNode.getLastChild().getTextContent().trim();
                                    refugeeNum = Long.parseLong(content);
                                    break;
                                }
                            }
                        }
                    }
                    // we have all our data for the Record node, let's build our RefugeeFlow and insert in DB
                    refugeeFlow = new RefugeeFlow(fromCountry, toCountry);
                    refugeeFlow.setYear(year);
                    refugeeFlow.setRefugeeCount(refugeeNum);
                    refugeeDataStore.createRefugeeFlow(refugeeFlow);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
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
            refugeeDataStore.createCountry(country);
        }
    }

    private Country getLatLong(String country) throws FileImportException {
        country = country.toUpperCase();
        if (countriesMap.containsKey(country)) {
            return countriesMap.get(country);
        } else {
            throw new FileImportException(new Exception("Country not found: " + country));
        }
    }
}
