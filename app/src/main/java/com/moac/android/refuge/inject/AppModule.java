package com.moac.android.refuge.inject;

import android.util.Log;

import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.activity.MainActivity;
import com.moac.android.refuge.database.IDatabaseService;
import com.moac.android.refuge.database.MockModelService;
import com.moac.android.refuge.database.ModelService;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module(injects = {RefugeApplication.class, MainActivity.class})
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();

    private final RefugeApplication mApplication;

    public AppModule(RefugeApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    ModelService provideDatabase() {
        Log.i(TAG, "Providing database");
//        DatabaseHelper databaseHelper = new DatabaseHelper(mApplication);
//        return new PersistentModelService(databaseHelper);
        return new MockModelService();
    }
}

