package com.jacquessmuts.rxstarter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.java.sample.*
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.models.ActivityIntent
import com.jacquessmuts.rxstarter.models.getIntent
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {


    companion object {
        val CLICKABLE_ACTIVITIES = listOf(
                ButtonRapidClickActivity::class.java,
                ThreadSwitchActivity::class.java,
                FailingApiCallActivity::class.java,
                LongApiCallActivity::class.java,
                DistinctActivity::class.java,
                PinActivity::class.java,
                ListActivity::class.java,
                ListBusActivity::class.java)
    }
    lateinit var adapter: ActivityAdapter

    private val activityClickedPublisher: PublishSubject<ActivityIntent<out AppCompatActivity>> by lazy {
        PublishSubject.create<ActivityIntent<out AppCompatActivity>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdapter()
    }

    fun setupAdapter(){

        adapter = ActivityAdapter(activityClickedPublisher)
        adapter.activities = getListOfClickableActivities()
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(activityClickedPublisher
                .subscribeAndLogE { activityIntent ->
                    startActivity(activityIntent.getIntent(this))
                })

        rxSubs.add(RxView.clicks(findViewById(R.id.button))
                .subscribeAndLogE {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/rxstarter")))
                })
    }

    fun getListOfClickableActivities(): List<ActivityIntent<out AppCompatActivity>> {

        val intentList = mutableListOf<ActivityIntent<out AppCompatActivity>>()
        CLICKABLE_ACTIVITIES.forEach {
            intentList.add(ActivityIntent(it))
        }

        return intentList
    }

}
