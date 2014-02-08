package com.moac.android.refuge.importer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by amelysh on 08/02/14.
 */
public class Record {

    public final String fromCountry;
    public final String toCountry;
    public final int year;
    public final long refugeeNum;

    private Record(String fromCountry, String toCountry, int year, long refugeeNum) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.year = year;
        this.refugeeNum = refugeeNum;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.

}
