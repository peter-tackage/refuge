package com.moac.android.refuge.importer;

import android.util.Log;

import com.moac.android.refuge.database.DatabaseHelper;
import com.moac.android.refuge.database.ModelService;
import com.moac.android.refuge.database.PersistentModelService;
import com.moac.android.refuge.model.Country;
import com.moac.android.refuge.model.RefugeeFlow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by amelysh on 08/02/14.
 */
public class DOMFileImporter {

    static final String TAG = DOMFileImporter.class.getSimpleName();

    static final String NAME_ATTRIBUTE_TAG = "name";
    static final String FROM_COUNTRY_TAG = "Country or territory of origin";
    static final String TO_COUNTRY_TAG = "Country or territory of asylum or residence";
    static final String YEAR_TAG = "Year";
    static final String REFUGEE_NUM_TAG = "Refugees<sup>*</sup>";

    RefugeeFlow refugeeFlow;
    private ModelService mModelService;

    public DOMFileImporter(ModelService modelService) {
        mModelService = modelService;
    }

    public void parse(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

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
                RefugeeFlow flow = new RefugeeFlow();
                NodeList childNodes = recordNode.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node fieldNode = childNodes.item(j);
                    if (fieldNode instanceof  Element) {
                        String fieldNameValue =  fieldNode.getAttributes().getNamedItem(NAME_ATTRIBUTE_TAG).getNodeValue();
                        if (fieldNameValue.equals(FROM_COUNTRY_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            fromCountry = new Country();
                            fromCountry.setName(content);
                            mModelService.create(fromCountry);
                        }
                        else if (fieldNameValue.equals(TO_COUNTRY_TAG)) {
                            String content = fieldNode.getLastChild().getTextContent().trim();
                            toCountry = new Country();
                            toCountry.setName(content);
                            mModelService.create(toCountry);
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
                mModelService.create(refugeeFlow);
            }
        }
    }
}
