package com.moac.android.refuge;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.util.Log;

import com.moac.android.refuge.inject.AppModule;

import dagger.ObjectGraph;

public class RefugeApplication extends Application {
    private static final String TAG = RefugeApplication.class.getSimpleName();

    private ObjectGraph objectGraph;

    public static RefugeApplication from(Activity activity) {
        return (RefugeApplication) activity.getApplication();
    }

    public static RefugeApplication from(Fragment fragment) {
        return (RefugeApplication) fragment.getActivity().getApplication();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() - start");
        Object prodModule = new AppModule(this);
        objectGraph = ObjectGraph.create(prodModule);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
