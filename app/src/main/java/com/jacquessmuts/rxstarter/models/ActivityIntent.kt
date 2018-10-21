package com.jacquessmuts.rxstarter.models

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity

/**
 * Created by jacquessmuts on 2018/10/21
 * Just a data holder for an activity Class
 */
data class ActivityIntent<T: AppCompatActivity>(val activityClass: Class<T>)

fun <T : AppCompatActivity> ActivityIntent<T>.getName(): String {
    return this.activityClass.simpleName
}


fun <T : AppCompatActivity> ActivityIntent<T>.getIntent(context: Context): Intent {
    return Intent(context, activityClass)
}