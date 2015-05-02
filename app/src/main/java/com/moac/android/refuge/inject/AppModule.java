package com.moac.android.refuge.inject;

import android.util.Log;

import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.activity.MainActivity;
import com.moac.android.refuge.database.DatabaseHelper;
import com.moac.android.refuge.database.PersistentRefugeeDataStore;
import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.fragment.NavigationDrawerFragment;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module(injects = {RefugeApplication.class, MainActivity.class, NavigationDrawerFragment.class})
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();

    private final RefugeApplication application;

    public AppModule(RefugeApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton RefugeeDataStore provideDatabase() {
        Log.i(TAG, "Providing database");
        DatabaseHelper databaseHelper = new DatabaseHelper(application);
        return new PersistentRefugeeDataStore(databaseHelper);
    }

}

