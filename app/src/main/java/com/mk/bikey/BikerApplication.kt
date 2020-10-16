package com.mk.bikey

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BikerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(FirebaseDebugsTree())
    }
}

class FirebaseDebugsTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        when (priority) {
            Log.DEBUG, Log.INFO -> {
                FirebaseCrashlytics.getInstance().log(message)
            }
            Log.ERROR -> {
                t?.let { FirebaseCrashlytics.getInstance().recordException(it) }
            }
            else -> { DO_NOTHING }
        }
    }
}