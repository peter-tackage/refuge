package com.moac.android.refuge.inject;

import android.util.Log;

import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.activity.MainActivity;
import com.moac.android.refuge.database.DatabaseHelper;
import com.moac.android.refuge.database.DatabaseService;
import com.moac.android.refuge.fragment.RefugeMapFragment;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module(injects = {RefugeApplication.class, MainActivity.class, RefugeMapFragment.class})
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();

    private final RefugeApplication mApplication;

    public AppModule(RefugeApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    DatabaseService provideDatabase() {
        Log.i(TAG, "Providing database");
        DatabaseHelper databaseHelper = new DatabaseHelper(mApplication);
        return new DatabaseService(databaseHelper);
    }
}

