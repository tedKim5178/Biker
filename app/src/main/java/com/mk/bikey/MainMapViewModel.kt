package com.mk.bikey

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainMapViewModel @ViewModelInject constructor() : ViewModel() {
    private var lastLocation = MutableLiveData<BikerLocation>()

    private val reference by lazy { Firebase.database.reference }
    private val analytics by lazy { Firebase.analytics }

    private var routeData = mutableListOf<BikerLatLng>()

    val speedKph: LiveData<Int> = Transformations.map(lastLocation) { location ->
        location?.let {
            (it.speed * 3.6f).toInt()
        } ?: 0
    }

    fun createRoute() {
        Observable.interval(5000, TimeUnit.MILLISECONDS)
            .subscribeBy(
                onNext = {
                    lastLocation.value?.let {
                        // TODO :: dependency?
                        routeData.add(BikerLatLng(it.latitude, it.longitude))
                    }
                },
                onError = {
                    Timber.e(it)
                }
            )
    }

    fun onLocationChange(location: BikerLocation) {
        lastLocation.value = location
    }

    fun updateRouteToFirebase(name: String) {
        val key = reference.child("route").push().key
        key?.let {
            reference.child("route").child(it).setValue(
                // TODO :: dependency
                Route(name, routeData)
            )
        }
    }

    fun sendResumeEvent(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    fun sendRouteCreateEvent(routeName: String) {
        analytics.logEvent("course_created") {
            param("name", routeName)
        }
    }
}