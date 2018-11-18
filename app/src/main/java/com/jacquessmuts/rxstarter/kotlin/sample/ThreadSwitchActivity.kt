package com.jacquessmuts.rxstarter.kotlin.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.kotlin.backgroundThread
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.kotlin.uiThread
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject

class ThreadSwitchActivity : BaseActivity() {

    //A PublishSubject is a hot observable. You can send emissions to it, and it will propagate to all current observers
    private val buttonsClickedPublisher = PublishSubject.create<Int>()

    /**
     * Returns a random number from 1-1000, after a 1500 miillisecond wait.
     * Simulates a long-running calculation
     * @return a random int from 0-1000
     */
    private val randomNumber: Int
        get() {
            try {
                Thread.sleep(1500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return (Math.random() * (1000 + 1)).toInt()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_switch)
    }

    override fun onResume() {
        super.onResume()

        val textView = findViewById<TextView>(R.id.textView)
        val textViewExplanation = findViewById<TextView>(R.id.textViewExplanation)

        //this is the same as just setting the onClick normally, inside a try{}catch{]
        rxSubs.add(RxView.clicks(findViewById(R.id.buttonNoThreading))
                .subscribeAndLogE { input ->
                    textView.text = randomNumber.toString()
                    buttonsClickedPublisher.onNext(1) //This is just for tallying up the clicks
                })

        //This is the good code. It does threading well
        rxSubs.add(RxView.clicks(findViewById(R.id.buttonGoodThreading))
                .backgroundThread() //all following functions will be on computation thread
                .map { input -> randomNumber }
                .uiThread() //all following functions will be on ui thread
                .subscribeAndLogE { random ->
                    textView.text = random.toString()
                    buttonsClickedPublisher.onNext(1) //This is just for tallying up the clicks
                })

        //merge both buttons's emissions and tally the total number of clicks between them both
        rxSubs.add(buttonsClickedPublisher
                .scan { total, nuValue -> total + nuValue } //keep a running tally.
                .subscribeAndLogE { tally ->
                    if (tally > 3) {
                        textViewExplanation.visibility = View.VISIBLE
                    }
                })


    }
}
