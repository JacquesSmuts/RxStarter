package com.jacquessmuts.rxstarter.kotlin

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by jacquessmuts on 2018/10/14
 * This activity has some of the base RxJava functionality you would want in a Base/Abstract Activity or Fragment
 */
abstract class BaseActivity : AppCompatActivity() {

    //Add all your subscriptions as disposables to this CompositeDisposable, to avoid memory leaks
    protected val rxSubs: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onPause() {
        //Clear all your subscriptions every time the activity loses focus
        rxSubs.clear()
        super.onPause()
    }
}
