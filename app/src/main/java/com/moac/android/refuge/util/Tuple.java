package com.moac.android.refuge.util;

/**
 * Created by amelysh on 09/02/14.
 */
public class Tuple {

    // TODO make generic data type
    Double mFirstValue;
    Double mSecondValue;

    public Tuple(Double firstValue, Double secondValue) {
        mFirstValue = firstValue;
        mSecondValue = secondValue;
    }

    public Double getFirstValue () {
        return mFirstValue;
    }

    public Double getSecondValue() {
        return mSecondValue;
    }
}
