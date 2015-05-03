package com.moac.android.refuge.importer;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.moac.android.refuge.RefugeApplication;
import com.moac.android.refuge.database.RefugeeDataStore;
import com.moac.android.refuge.util.DoOnce;

import javax.inject.Inject;

import rx.Notification;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Peter Tackage
 * @since 02/05/15
 */
public class ImportService extends IntentService {

    public static final String LOAD_DATA_TASK_TAG = "LOAD_DATA_TASK";

    private static final String TAG = ImportService.class.getSimpleName();
    private static final String ASSET_FILE = "UNDataExport2012.xml";
    private static final String COUNTRIES_LAT_LONG_CSV = "CountriesLatLong.csv";

    @Inject
    RefugeeDataStore refugeeDataStore;

    private BehaviorSubject<Status> status = BehaviorSubject.create(Status.IDLE);

    public ImportService() {
        super(ImportService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RefugeApplication app = ((RefugeApplication) getApplication());
        app.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent() started - " + intent);
        status.onNext(Status.RUNNING);
        try {
            boolean attemptedToLoad = DoOnce.doOnce(this, LOAD_DATA_TASK_TAG,
                    new LoadDataRunnable(new DataFileImporter(refugeeDataStore),
                            getAssets().open(ASSET_FILE),
                            getAssets().open(COUNTRIES_LAT_LONG_CSV)));
            Log.i(TAG, "Attempted to load data: " + attemptedToLoad);
            status.onCompleted();
        } catch (Exception e) {
            Log.e(TAG, "Failed to open the data file: " + ASSET_FILE, e);
            status.onError(e);
        }
        Log.d(TAG, "onHandleIntent() finished");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ImportClient();
    }

    public class ImportClient extends Binder {
        public Observable<Notification<Status>> getStatus() {
            return status.materialize();
        }
    }

    public enum Status {
        IDLE,
        RUNNING,
        COMPLETED
    }

}
