package com.moac.android.refuge.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DoOnce {

    public static final String TAG = DoOnce.class.getSimpleName();

    private static final String DO_ONCE_TAG = "do_once";

    public static boolean doOnce(Context context, String taskTag, Runnable task) {
        final String prefTag = toIsDoneTag(taskTag);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDone = prefs.getBoolean(prefTag, false);
        if (!isDone) {
            task.run();
            prefs.edit().putBoolean(prefTag, true).apply();
            return true;
        }
        return false;
    }

    public static boolean isDone(Context context, String taskTag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(toIsDoneTag(taskTag), false);
    }

    private static String toIsDoneTag(String taskTag) {
        return DO_ONCE_TAG + taskTag;
    }

}
