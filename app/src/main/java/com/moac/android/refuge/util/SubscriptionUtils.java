package com.moac.android.refuge.util;


import android.widget.TextView;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SubscriptionUtils {

    public static Subscription subscribeTextView(Observable<String> observable, final TextView textView) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textView.setText(s);
                    }
                });
    }

}
