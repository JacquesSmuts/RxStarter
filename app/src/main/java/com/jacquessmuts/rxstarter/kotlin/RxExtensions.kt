package com.jacquessmuts.rxstarter.kotlin

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by jacquessmuts on 2018/09/30
 * This is for custom rxJava extensions
 */

//adds a standard ±300ms delay for clickableObjects
fun <T> Observable<T>.filterRapidClicks() = throttleFirst(1000, TimeUnit.MILLISECONDS)

//same as subscribe, except it logs errors with Timber.e() automatically
//TODO: try to figure out how to make this work. Consumer not recognizing lambdas?
fun <T> Observable<T>.subscribeAndLogE(onNext: (it : T) -> Unit): Disposable =
        subscribe({ onNext(it) }, Timber::e)

class ErrorConsumer<T>: Consumer<T> {
    override fun accept(t: T) {
        if (t is Throwable)
            Timber.e(t)
    }
}

fun <T> Observable<T>.tallyClicks(): Observable<Int> =
        map { input -> 1 }.scan { total, nuValue -> total + nuValue }

/**
 * Puts the Observer on the Main/UI Thread using [Observable.observeOn]. Please make sure you understand
 * the difference between [Observable.subscribeOn] and [Observable.observeOn] when using this.
 */
fun <T> Observable<T>.uiThread() = observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.uiThread() = observeOn(AndroidSchedulers.mainThread())

class RunningAverage : ObservableTransformer<Int, Double> {
    override fun apply(upstream: Observable<Int>): ObservableSource<Double> {
        return upstream
                .scan(
                        AverageAcc(0, 0)
                ) { acc, v -> AverageAcc(acc.sum + v, acc.count + 1) }
                .filter { acc -> acc.count > 0 }
                .map { acc -> acc.sum / acc.count.toDouble() }

    }

    private class AverageAcc(val sum: Int, val count: Int)

}