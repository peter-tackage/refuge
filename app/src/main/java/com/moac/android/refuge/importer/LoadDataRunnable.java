package com.moac.android.refuge.importer;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class LoadDataRunnable implements Runnable {

    private static final String TAG = LoadDataRunnable.class.getSimpleName();

    private final DataFileImporter importer;
    private final InputStream unDataInputStream;
    private final InputStream countriesLatLongInputStream;

    public LoadDataRunnable(DataFileImporter importer, InputStream unDataInputStream, InputStream countriesLatLongInputStream) {
        this.importer = importer;
        this.unDataInputStream = unDataInputStream;
        this.countriesLatLongInputStream = countriesLatLongInputStream;
    }

    @Override
    public void run() {
        try {
            importer.parse(unDataInputStream, countriesLatLongInputStream);
        } catch (FileImportException e) {
            Log.e(TAG, "Failed to import the dataset", e);
        } finally {
            try {
                unDataInputStream.close();
                countriesLatLongInputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "Failed to close the input stream", ioe);
            }
        }
    }
}
