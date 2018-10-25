package com.jacquessmuts.rxstarter.kotlin.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/*
 * Created by jacquessmuts on 2018/10/2
 *
 * This class is an abomination intended to show how extension functions can be used to modify RxJava
 */
class ExtensionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_load)
    }

    override fun onResume() {
        super.onResume()

        val button = findViewById<Button>(R.id.button)

        val textViewExplanation = findViewById<TextView>(R.id.textViewExplanation)
        textViewExplanation.setText(R.string.explanation_failing_api)

        //this is just to show the button clicks
        rxSubs.add(RxView.clicks(button)
                .map { input -> Bulge() }
                .ofType(Bulge::class.java)
                .observeOn(Schedulers.computation())
                .subscribe({ tally ->
                    doApiCall()
                }, Timber::e))

        //This is the same as the above function, but uses Kotlin Extension functions to be an abomination
        rxSubs.glomp(OwO.notice(button)
                .whatsThis(Bulge::class.java)
                .noticeOn(Schedulers.computation())
                .nuzzle { bulge ->
                    doApiCall()
                })

    }

    private fun doApiCall() {

        //do an api call here

    }

    fun CompositeDisposable.glomp(disposable: Disposable) = add(disposable)

    fun <T> Observable<T>.whatsThis(clazz: Class<T>) = ofType(clazz)

    fun <T> Observable<T>.nuzzle(onNext: (bulge: Bulge) -> Unit) = subscribe{ onNext(Bulge()) }

    object OwO {

        fun notice(bulge: View): Observable<Bulge> {
            return RxView.clicks(bulge).map { Bulge() }
        }

    }

    class Bulge(){}

    fun <T> Observable<T>.noticeOn(scheduler: Scheduler) = observeOn(scheduler)
}
