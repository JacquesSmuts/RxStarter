package com.jacquessmuts.rxstarter.kotlin

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.jacquessmuts.rxstarter.R
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class ASingleActivity : AppCompatActivity() {

    private var singleDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.textView)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {

            // Create a single. This is not ideal.
            Single.just(getAGreeting())
                    .subscribe { resultString -> textView.text = resultString }

            // Create a single, and make sure it's disposed of in onDestroy. This is better.
            singleDisposable = Single.just(getAGreeting())
                    .subscribe { resultString -> textView.text = resultString }
        }
    }

    override fun onDestroy() {
        // dispose of all your Subscriptions in onDestroy, to prevent memory leaks
        singleDisposable?.dispose()

        super.onDestroy()
    }

    private fun getAGreeting(): String {
        return "Hello ~World~ GDG"
    }
}
