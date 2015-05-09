package com.moac.android.refuge;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.util.Log;

import com.moac.android.refuge.inject.AppModule;
import com.squareup.leakcanary.LeakCanary;

import dagger.ObjectGraph;

public class RefugeApplication extends Application {
    private static final String TAG = RefugeApplication.class.getSimpleName();

    private ObjectGraph objectGraph;

    public static RefugeApplication from(Activity activity) {
        return (RefugeApplication) activity.getApplication();
    }

    public static RefugeApplication from(android.support.v4.app.Fragment fragment) {
        return (RefugeApplication) fragment.getActivity().getApplication();
    }

    public static RefugeApplication from(Service service) {
        return (RefugeApplication) service.getApplication();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() - start");
        Object prodModule = new AppModule(this);
        //Object debugAppModule = new DebugAppModule(this);
        objectGraph = ObjectGraph.create(prodModule);
        LeakCanary.install(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
