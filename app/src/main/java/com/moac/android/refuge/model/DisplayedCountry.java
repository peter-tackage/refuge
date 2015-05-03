package com.moac.android.refuge.model;

/**
 * @author Peter Tackage
 * @since 03/05/15
 */
public class DisplayedCountry {
    long id;
    int color;

    public DisplayedCountry(long id, int color) {
        this.id = id;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
}
