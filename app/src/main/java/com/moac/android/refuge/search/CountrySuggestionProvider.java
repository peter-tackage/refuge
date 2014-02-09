package com.moac.android.refuge.search;

import android.app.SearchManager;
import android.content.*;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Arrays;

/*
* Calls through to the Contacts Suggestion endpoint.
* Refer SearchManager and
* https://android.googlesource.com/platform/packages/apps/GlobalSearch/+/master/src/com/android/globalsearch/
*
*/
public class CountrySuggestionProvider extends ContentProvider {

    public static String TAG = CountrySuggestionProvider.class.getSimpleName();

    /*
     * Authority must match searchable.xml and Provider in AndroidManifest.xml
     */
    public static final String AUTHORITY = "com.moac.android.refuge.country";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");

    // UriMatcher constant for search suggestions
    private static final int SEARCH_SUGGEST = 1;

    private static final UriMatcher uriMatcher;

    private static final String[] SEARCH_SUGGESTIONS_COLUMNS = {
            "_id",
            SearchManager.SUGGEST_COLUMN_TEXT_1,
    };

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }

    private static final String KEY_SEARCH_COLUMN = ContactsContract.Contacts.DISPLAY_NAME;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Log.i(TAG, "query() - uri:" + uri);

        // Use the UriMatcher to see what kind of query we have
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                Log.i(TAG, "query() - matched SEARCH_SUGGEST");

                // The search term (perhaps partial) of interest
                // See com.android.globalsearch.SuggestionProvider
                String query;
                if (uri.getPathSegments().size() > 1) {
                    query = uri.getLastPathSegment().toLowerCase();
                } else {
                    query = "";
                }

                // Get a suggestions cursor from the Contacts Content Provider
                // These calls effectively replicate the calls made by the SearchManager, without the baggage of the framework.
                Uri suggestionsBaseUri = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, SearchManager.SUGGEST_URI_PATH_QUERY);
                Uri suggestionsQueryUri = Uri.withAppendedPath(suggestionsBaseUri, query);
                Cursor countryCursor = getContext().getContentResolver().query(suggestionsQueryUri, null, null, null, null);
                Log.i(TAG, "query() - cursor length:" + countryCursor.getCount());
                Log.i(TAG, "query() - cursor column names:" + Arrays.toString(countryCursor.getColumnNames()));
                return countryCursor;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String arg1, String[] arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues arg1) {
        throw new UnsupportedOperationException();
    }

}