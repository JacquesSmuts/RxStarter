package com.jacquessmuts.rxstarter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacquessmuts.rxstarter.models.ActivityIntent
import com.jacquessmuts.rxstarter.models.getName
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.viewholder_activities.view.*


/**
 * Created by jacquessmuts on 2018/10/21
 * Adapter to display list of activities and provide intent to go to that activity
 */
class ActivityAdapter(private val activityClickPublisher: PublishSubject<ActivityIntent<out AppCompatActivity>>): RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    var activities: List<ActivityIntent<out AppCompatActivity>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.newInstance(parent)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activityIntent = activities[position]
        holder.update(activityIntent)
        RxView.clicks(holder.clickView)
                .map { activityIntent }
                .subscribeWith(activityClickPublisher)
    }

    class ActivityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        companion object {
            fun createView(parentView: ViewGroup): View {
                return LayoutInflater.from(parentView.context).inflate(R.layout.viewholder_activities, parentView, false)
            }

            fun newInstance(parentView: ViewGroup): ActivityViewHolder {
                return ActivityViewHolder(createView(parentView))
            }
        }

        val clickView: CardView by lazy {
            itemView.layout_holder
        }

        fun update(activityIntent: ActivityIntent<out AppCompatActivity>) {
            itemView.textViewName.text = activityIntent.getName()
        }
    }
}