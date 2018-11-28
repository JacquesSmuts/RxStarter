package com.jacquessmuts.rxstarter.kotlin.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.kotlin.backgroundThread
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.kotlin.uiThread
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class DistinctActivity : BaseActivity() {

    private var calculationOngoing = false

    private val randomNumber: Int
        get() = (Math.random() * 20 + 1).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_load)
    }

    override fun onResume() {
        super.onResume()

        val button = findViewById<Button>(R.id.button)

        val textViewExplanation = findViewById<TextView>(R.id.textViewExplanation)
        textViewExplanation.setText(R.string.explanation_distinct)

        rxSubs.add(RxView.clicks(button)
                .subscribeAndLogE { tally ->
                    getDistinctValues()
                    textViewExplanation.visibility = View.VISIBLE
                })

    }

    private fun getDistinctValues() {

        if (calculationOngoing) {
            return
        }
        calculationOngoing = true

        // Add an observable to change the textview while the api call is loading
        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        textView.text = ""


        rxSubs.add(Observable.interval(200, 200, TimeUnit.MILLISECONDS)
                .backgroundThread() // all following parts of this chain will be on computation thread
                .map { input -> randomNumber }
                .distinct()
                .uiThread() // all following parts of this chain will be on UiThread
                .subscribeAndLogE { result ->
                    textView.append(result!!.toString() + " ") // append value to textview
                })

    }

}
