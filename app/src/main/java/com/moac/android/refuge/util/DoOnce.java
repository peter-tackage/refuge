package com.moac.android.refuge.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DoOnce {

    public static final String TAG = DoOnce.class.getSimpleName();

    private static final String DO_ONCE_TAG = "do_once";

    public static boolean doOnce(Context _context, String _taskTag, Runnable _task) {
        final String prefTag = DO_ONCE_TAG + _taskTag;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        boolean isDone = prefs.getBoolean(prefTag, false);
        if (!isDone) {
            _task.run();
            prefs.edit().putBoolean(prefTag, true).apply();
            return true;
        }
        return false;
    }


}
