package com.jacquessmuts.rxstarter.kotlin.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.kotlin.filterRapidClicks
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.kotlin.tallyClicks
import com.jakewharton.rxbinding2.view.RxView

class ButtonRapidClickActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_click)
    }

    override fun onResume() {
        super.onResume()

        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        val textViewExplanation = findViewById<TextView>(R.id.textViewExplanation)

        rxSubs.add(RxView.clicks(button) // get clicks
                .filterRapidClicks() // same as throttleFirst(1000, TimeUnit.MILLISECONDS)
                .tallyClicks()
                .subscribeAndLogE { tally -> // subscribeAndLogE is an extension function which automatically logs all errors thrown
                    // set tally to textview
                    textView.text = tally.toString()
                    if (tally > 2) {
                        textViewExplanation.visibility = View.VISIBLE
                    }
                })
    }
}
