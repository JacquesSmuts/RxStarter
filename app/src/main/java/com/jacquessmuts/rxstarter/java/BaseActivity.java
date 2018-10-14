package com.jacquessmuts.rxstarter.java;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by jacquessmuts on 2018/10/14
 * This activity has some of the base RxJava functionality you would want in a Base/Abstract Activity or Fragment
 */
public abstract class BaseActivity extends AppCompatActivity {

    //Add all your subscriptions as disposables to this CompositeDisposable, to avoid memory leaks
    //Ideally you want to lazy-load your compositeDisposable, but it works fine enough like this
    protected CompositeDisposable rxSubs = new CompositeDisposable();

    @Override
    protected void onPause() {
        //Clear all your subscriptions every time the activity loses focus
        rxSubs.clear();
        super.onPause();
    }
}
