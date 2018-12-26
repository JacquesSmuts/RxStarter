package com.jacquessmuts.rxstarter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Created by jacquessmuts on 2018/10/29
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentTest {

    lateinit var appContext: Context
    @Before
    open fun setUp() {
        appContext = InstrumentationRegistry.getTargetContext()
    }
}