package com.jacquessmuts.rxstarter.kotlin.sample

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.kotlin.tallyClicks
import com.jacquessmuts.rxstarter.kotlin.uiThread
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ListActivity : BaseActivity() {

    //normally you would want to lazy-load these
    internal var timePublisher = PublishSubject.create<String>() //create a publisher you can publish to
    internal var timeObservable = timePublisher.hide() //create an observer which you can NOT publish to, but you can listen from
    private var isTimerRunning = false

    private val maxNumber = 144

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val listOfNumbers = (1..10000).map { it.toString() }

        recyclerView.adapter = CounterAdapter(listOfNumbers, timeObservable, rxSubs)
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(RxView.clicks(findViewById(R.id.button))
                .subscribeAndLogE { tally -> startTimer() })
    }

    private fun startTimer() {
        if (isTimerRunning) {
            findViewById<View>(R.id.textViewExplanation).visibility = View.VISIBLE
            return
        }
        isTimerRunning = true

        rxSubs.add(Observable.interval(7, TimeUnit.MILLISECONDS) //emit at 144 frames per second
                .tallyClicks()
                .subscribeAndLogE { counter ->
                    timePublisher.onNext(numberToString(counter)) //send the string to the publisher
                })
    }

    private fun numberToString(startingNumber: Int): String {
        var number = startingNumber
        var remainder: Int
        val toReturn = StringBuilder()
        while (number > 0) {
            remainder = number % maxNumber
            toReturn.append(Character.toString((remainder + 255).toChar()))
            number /= maxNumber
        }
        return toReturn.reverse().toString()
    }
}

/**
 * Normally this adapter would be extracted into its own file, but it's kept here for demonstrative purposes
 */
internal class CounterAdapter(private val numbers: List<String>, private val timeObservable: Observable<String>, private val rxSubs: CompositeDisposable //this disposable is managed by the parent activity lifecycle.
) : RecyclerView.Adapter<CounterAdapter.CounterViewHolder>() {

    override fun getItemCount(): Int {
        return numbers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        return CounterViewHolder.newInstance(parent, timeObservable, rxSubs)
    }

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewNumber.text = numbers[position]
        holder.setTimerListener(timeObservable, rxSubs)
    }


    internal class CounterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //This disposable is used to prevent memory leaks
        private var disposable: Disposable? = null

        var textViewNumber: TextView
        val textViewTime: TextView

        init {
            textViewNumber = v.findViewById(R.id.textViewName)
            textViewTime = v.findViewById(R.id.textViewTime)
        }

        fun setTimerListener(timeObservable: Observable<String>, rxSubs: CompositeDisposable) {

            //if the disposable already exists, that means the ViewHolder is being recycled by recyclerview
            if (disposable != null && !disposable!!.isDisposed) {
                //So delete it out of the list of disposables in BaseActivity and dispose of it.
                rxSubs.delete(disposable!!)
                disposable!!.dispose()
            }

            //

            disposable = timeObservable
                    .uiThread()
                    .subscribeAndLogE {
                        textViewTime.text = it
                    }

            rxSubs.add(disposable!!)
        }

        companion object {

            fun newInstance(parent: ViewGroup, timeObservable: Observable<String>, rxSubs: CompositeDisposable): CounterViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.viewholder_timer, parent, false)
                return CounterViewHolder(view)
            }
        }


    }

}