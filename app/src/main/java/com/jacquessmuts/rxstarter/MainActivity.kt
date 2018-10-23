package com.jacquessmuts.rxstarter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jacquessmuts.rxstarter.java.BaseActivity
import com.jacquessmuts.rxstarter.java.sample.ButtonRapidClickActivity
import com.jacquessmuts.rxstarter.java.sample.LongLoadingActivity
import com.jacquessmuts.rxstarter.java.sample.ThreadSwitchActivity
import com.jacquessmuts.rxstarter.kotlin.subscribeAndLogE
import com.jacquessmuts.rxstarter.models.ActivityIntent
import com.jacquessmuts.rxstarter.models.getIntent
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {


    companion object {
        val CLICKABLE_ACTIVITIES = listOf(
                ButtonRapidClickActivity::class.java,
                ThreadSwitchActivity::class.java,
                LongLoadingActivity::class.java)
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
    }

    fun getListOfClickableActivities(): List<ActivityIntent<out AppCompatActivity>> {

        val intentList = mutableListOf<ActivityIntent<out AppCompatActivity>>()
        CLICKABLE_ACTIVITIES.forEach {
            intentList.add(ActivityIntent(it))
        }

        return intentList
    }

}
