package com.moac.android.refuge.importer;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class LoadDataRunnable implements Runnable {

    private static final String TAG = LoadDataRunnable.class.getSimpleName();

    private final DataFileImporter mImporter;
    private final InputStream mUNDataInputStream;
    private final InputStream mCountriesLatLongInputStream;

    public LoadDataRunnable(DataFileImporter importer, InputStream UNDataInputStream, InputStream countriesLatLongInputStream) {
        mImporter = importer;
        mUNDataInputStream = UNDataInputStream;
        mCountriesLatLongInputStream = countriesLatLongInputStream;
    }

    @Override
    public void run() {
        try {
            mImporter.parse(mUNDataInputStream, mCountriesLatLongInputStream);
        } catch (FileImportException e) {
            Log.e(TAG, "Failed to import the dataset", e);
        } finally {
            try {
                mUNDataInputStream.close();
                mCountriesLatLongInputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "Failed to close the input stream", ioe);
            }
        }
    }
}
