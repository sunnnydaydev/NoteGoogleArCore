package com.example.notegooglearcore

import android.app.Application
import timber.log.Timber

/**
 * Create by SunnyDay /11/01 11:26:51
 */
class ArCoreApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                // format the log tag to have "TAG:Line Number"
                return super.createStackElementTag(element) + ":" + element.lineNumber
            }
        })
    }

}