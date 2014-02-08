package com.moac.android.refuge.importer;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class LoadDataRunnable implements Runnable {

    private static final String TAG = LoadDataRunnable.class.getSimpleName();

    private final DOMFileImporter mImporter;
    private final InputStream mInputStream;

    public LoadDataRunnable(DOMFileImporter importer, InputStream inputStream) {
        mImporter = importer;
        mInputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            mImporter.parse(mInputStream);
        } catch (FileImportException e) {
            Log.e(TAG, "Failed to import the dataset", e);
        } finally {
            try {
                mInputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "Failed to close the input stream", ioe);
            }
        }
    }
}
