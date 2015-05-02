package com.moac.android.refuge.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DoOnce {

    public static final String TAG = DoOnce.class.getSimpleName();

    private static final String DO_ONCE_TAG = "do_once";

    public static boolean doOnce(Context context, String taskTag, Runnable task) {
        final String prefTag = DO_ONCE_TAG + taskTag;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDone = prefs.getBoolean(prefTag, false);
        if (!isDone) {
            task.run();
            prefs.edit().putBoolean(prefTag, true).apply();
            return true;
        }
        return false;
    }


}
